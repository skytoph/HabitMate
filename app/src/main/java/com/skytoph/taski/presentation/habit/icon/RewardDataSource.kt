package com.skytoph.taski.presentation.habit.icon

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.skytoph.taski.BuildConfig
import com.skytoph.taski.presentation.core.Logger
import com.skytoph.taski.presentation.habit.icon.RewardDataSource.Fail
import com.skytoph.taski.presentation.habit.icon.RewardDataSource.Success
import java.util.concurrent.atomic.AtomicBoolean

interface RewardDataSource {
    suspend fun initialize(activity: Activity)
    fun isPrivacyOptionsRequired(activity: Activity): Boolean
    fun requestPermissionAndShow(activity: Activity, success: Success, fail: Fail)
    fun showPrivacyOptions(activity: Activity, fail: Fail = Fail {}, success: Success = Success {})
    fun load(activity: Activity, fail: Fail = Fail {})
    fun show(activity: Activity, success: Success, fail: Fail)
    fun cancel()

    fun interface Success {
        fun handle()
    }

    fun interface Fail {
        fun handle(result: RewardFailResult)
    }

    class Base(
        private var rewardedAd: RewardedAd? = null,
        private var showIfIntended: ((activity: Activity) -> Unit)? = null,
        private var isLoading: Boolean = false,
        private val isMobileAdsInitializeCalled: AtomicBoolean = AtomicBoolean(false),
        private val log: Logger
    ) : RewardDataSource {

        private fun consentInfo(context: Context): ConsentInformation =
            UserMessagingPlatform.getConsentInformation(context)

        private val fullscreenCallback: FullScreenContentCallback = BaseFullScreenContentCallback { rewardedAd = null }

        override fun isPrivacyOptionsRequired(activity: Activity) =
            consentInfo(activity).privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

        override suspend fun initialize(activity: Activity) {
            if (consentInfo(activity).canRequestAds()) {
                initializeMobileAdsSdk(activity)
                if (rewardedAd == null) load(activity)
            }
        }

        private fun initializeMobileAdsSdk(activity: Activity) {
            if (isMobileAdsInitializeCalled.getAndSet(true)) return
            MobileAds.initialize(activity)
        }

        override fun showPrivacyOptions(activity: Activity, fail: Fail, success: Success) {
            consentInfo(activity).requestConsentInfoUpdate(
                activity,
                params(activity),
                /* information is successfully updated */ {
                    UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
                        if (formError != null) {
                            fail.handle(RewardFailResult.ConsentError)
                            log.log(String.format("%s: %s", formError.errorCode, formError.message), TAG)
                        } else success.handle()
                    }
                },
                /* there's an error updating consent information */ { requestConsentError ->
                    fail.handle(RewardFailResult.ConsentError)
                    log.log(String.format("%s: %s", requestConsentError.errorCode, requestConsentError.message), TAG)
                })
        }

        override fun requestPermissionAndShow(activity: Activity, success: Success, fail: Fail) {
            val consentInfo = consentInfo(activity)
            if (consentInfo.canRequestAds())
                show(activity, success, fail)
            else consentInfo.requestConsentInfoUpdate(
                activity,
                params(activity),
                /* information is successfully updated */ {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                        if (formError != null) {
                            fail.handle(RewardFailResult.ConsentError)
                            log.log(String.format("%s: %s", formError.errorCode, formError.message), TAG)
                        } else if (consentInfo.canRequestAds()) {
                            initializeMobileAdsSdk(activity)
                            show(activity, success, fail)
                        }
                    }
                },
                /* there's an error updating consent information */ { requestConsentError ->
                    fail.handle(RewardFailResult.ConsentError)
                    log.log(String.format("%s: %s", requestConsentError.errorCode, requestConsentError.message), TAG)
                })
        }

        private fun params(context: Context): ConsentRequestParameters {
            val params = ConsentRequestParameters.Builder()
                .setAdMobAppId(BuildConfig.ADMOB_APP_ID)
                .apply {
                    if (BuildConfig.DEBUG) {
                        val debugSettings = ConsentDebugSettings.Builder(context)
                            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                            .build()
                        setConsentDebugSettings(debugSettings)
                    }
                }.build()
            return params
        }

        override fun load(activity: Activity, fail: Fail) {
            log.logDebug("Preparing to load the ad...", TAG)
            isLoading = true
            val request = AdRequest.Builder().build()
            RewardedAd.load(activity, ADMOB_UNIT_ID, request, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoading = false
                    rewardedAd = null
                    fail.handle(mapErrorCode(error.code, activity))
                    log.logDebug("Ad failed to load. Error code: " + error.code + "\n" + error.message, TAG)
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    isLoading = false
                    rewardedAd = ad
                    showIfIntended?.invoke(activity)
                    log.logDebug(
                        "Ad loaded. " + if (showIfIntended == null) "No intention to show" else "Starting to show", TAG
                    )
                }
            })
        }

        private fun mapErrorCode(code: Int, activity: Activity): RewardFailResult = when (code) {
            3 -> RewardFailResult.NoAdsAvailable(isPrivacyOptionsRequired(activity))
            2 -> RewardFailResult.NoConnection
            else -> RewardFailResult.General
        }

        override fun show(activity: Activity, success: Success, fail: Fail) {
            log.logDebug("Preparing to show the ad", TAG)
            showIfIntended = { activity: Activity ->
                rewardedAd?.let { ad ->
                    ad.fullScreenContentCallback = fullscreenCallback
                    ad.show(activity) {
                        success.handle()
                        showIfIntended = null
                        rewardedAd = null
                        load(activity)
                        log.logDebug("User earned the reward.", TAG)
                    }
                } ?: run {
                    if (!isLoading) load(activity, fail)
                    log.logDebug("The rewarded ad wasn't ready yet. is loading: $isLoading", TAG)
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (isLoading) {
                            log.logDebug("Reloading the ad", TAG)
                            load(activity, fail)
                        }
                    }, AD_LOADING_TIMEOUT)
                }
            }
            showIfIntended?.invoke(activity)
        }

        override fun cancel() {
            isLoading = false
            showIfIntended = null
        }

        private companion object {
            const val ADMOB_UNIT_ID = BuildConfig.ADMOB_UNIT_ID
            const val AD_LOADING_TIMEOUT = 6000L
            val TAG: String = RewardDataSource::class.simpleName.toString()
        }
    }
}

class BaseFullScreenContentCallback(private val onError: OnError) : FullScreenContentCallback() {

    override fun onAdDismissedFullScreenContent() {
        onError.handle()
    }

    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
        onError.handle()
    }

    fun interface OnError {
        fun handle()
    }
}