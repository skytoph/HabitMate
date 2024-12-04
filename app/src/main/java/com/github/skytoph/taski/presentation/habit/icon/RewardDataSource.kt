package com.github.skytoph.taski.presentation.habit.icon

import android.app.Activity
import android.content.Context
import android.util.Log
import com.github.skytoph.taski.BuildConfig
import com.github.skytoph.taski.presentation.habit.icon.RewardDataSource.Fail
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

interface RewardDataSource {
    suspend fun initialize(context: Context)
    fun load(activity: Activity, fail: Fail = Fail {})
    fun show(activity: Activity, reward: Reward, fail: Fail)

    fun interface Reward {
        fun rewarded()
    }

    fun interface Fail {
        fun failed()
    }

    class Base(
        private var rewardedAd: RewardedAd? = null,
        private var showIfIntended: ((activity: Activity) -> Unit)? = null,
        private var isLoading: Boolean = false,
    ) : RewardDataSource {

        private val fullscreenCallback: FullScreenContentCallback = BaseFullScreenContentCallback { rewardedAd = null }

        override suspend fun initialize(context: Context) {
            MobileAds.initialize(context)
        }

        override fun load(activity: Activity, fail: Fail) {
            isLoading = true
            val request = AdRequest.Builder().build()
            RewardedAd.load(activity, ADMOB_UNIT_ID, request, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    isLoading = false
                    fail.failed()
                    Log.e(TAG, "Ad failed to load.")
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    isLoading = false
                    showIfIntended?.invoke(activity)
                    Log.e(
                        TAG,
                        "Ad loaded. " + if (showIfIntended == null) "No intention to show" else "Starting to show"
                    )
                }
            })
        }

        override fun show(activity: Activity, reward: Reward, fail: Fail) {
            Log.e(TAG, "Preparing the ad")
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
                    Log.e(TAG, "The rewarded ad wasn't ready yet.")
                }
            }
            showIfIntended?.invoke(activity)
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