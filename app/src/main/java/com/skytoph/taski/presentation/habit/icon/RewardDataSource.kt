package com.skytoph.taski.presentation.habit.icon

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.Logger
import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.presentation.habit.icon.RewardDataSource.Fail
import java.util.concurrent.atomic.AtomicBoolean

interface RewardDataSource {
    suspend fun initialize(activity: Activity)
    fun isPrivacyOptionsRequired(activity: Activity): Boolean
    fun requestPermissionAndShow(activity: Activity, reward: Reward, fail: Fail)
    fun showPrivacyOptions(activity: Activity, fail: Fail = Fail {})
    fun load(activity: Activity, fail: Fail = Fail {})
    fun show(activity: Activity, reward: Reward, fail: Fail)
    fun cancel()

    fun interface Reward {
        fun rewarded()
    }

    fun interface Fail {
        fun failed(message: StringResource?)
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
            if (consentInfo(activity).canRequestAds())
                initializeMobileAdsSdk(activity)
        }

        private fun initializeMobileAdsSdk(activity: Activity) {
            if (isMobileAdsInitializeCalled.getAndSet(true)) return
            MobileAds.initialize(activity)
            load(activity)
        }

        override fun showPrivacyOptions(activity: Activity, fail: Fail) {
            consentInfo(activity).requestConsentInfoUpdate(
                activity,
                params(activity),
                /* information is successfully updated */ {
                    UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
                        if (formError != null) fail.failed(null)
                    }
                },
                /* there's an error updating consent information */ { requestConsentError ->
                    fail.failed(null)
                    log.log(String.format("%s: %s", requestConsentError.errorCode, requestConsentError.message))
                })
        }

        override fun requestPermissionAndShow(activity: Activity, reward: Reward, fail: Fail) {
            val consentInfo = consentInfo(activity)
            consentInfo.requestConsentInfoUpdate(
                activity,
                params(activity),
                /* information is successfully updated */ {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                        if (formError != null) fail.failed(null)
                        if (consentInfo.canRequestAds()) {
                            initializeMobileAdsSdk(activity)
                            show(activity, reward, fail)
                        }
                    }
                },
                /* there's an error updating consent information */ { requestConsentError ->
                    fail.failed(null)
                    log.log(String.format("%s: %s", requestConsentError.errorCode, requestConsentError.message))
                })

            if (consentInfo.canRequestAds()) show(activity, reward, fail)
        }

        private fun params(context: Context): ConsentRequestParameters {
            val debugSettings = ConsentDebugSettings.Builder(context)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .build()
            val params = ConsentRequestParameters.Builder()
                .setAdMobAppId(BuildConfig.ADMOB_APP_ID)
                .apply { if (BuildConfig.DEBUG) setConsentDebugSettings(debugSettings) }
                .build()
            return params
        }

        override fun load(activity: Activity, fail: Fail) {
            Log.e(TAG, "Preparing to load the ad...")
            isLoading = true
            val request = AdRequest.Builder().build()
            RewardedAd.load(activity, ADMOB_UNIT_ID, request, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoading = false
                    rewardedAd = null
                    fail.failed(if (error.code == 3) StringResource.ResId(R.string.error_no_ads) else null)
                    Log.e(TAG, "Ad failed to load.")
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    isLoading = false
                    rewardedAd = ad
                    showIfIntended?.invoke(activity)
                    Log.e(
                        TAG,
                        "Ad loaded. " + if (showIfIntended == null) "No intention to show" else "Starting to show"
                    )
                }
            })
        }

        override fun show(activity: Activity, reward: Reward, fail: Fail) {
            Log.e(TAG, "Preparing to show the ad")
            showIfIntended = { activity: Activity ->
                rewardedAd?.let { ad ->
                    ad.fullScreenContentCallback = fullscreenCallback
                    ad.show(activity) {
                        reward.rewarded()
                        showIfIntended = null
                        rewardedAd = null
                        load(activity)
                        Log.e(TAG, "User earned the reward.")
                    }
                } ?: run {
                    if (!isLoading) load(activity, fail)
                    Log.e(TAG, "The rewarded ad wasn't ready yet. is loading: $isLoading")
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (isLoading) {
                            Log.e(TAG, "Reloading!")
                            load(activity, fail)
                        }
                    }, 3000)
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