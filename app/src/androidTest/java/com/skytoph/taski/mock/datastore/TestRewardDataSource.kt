package com.skytoph.taski.mock.datastore

import android.app.Activity
import com.skytoph.taski.presentation.habit.icon.RewardDataSource
import com.skytoph.taski.presentation.habit.icon.RewardFailResult

class TestRewardDataSource(private val error: RewardFailResult? = null) : RewardDataSource {
    override suspend fun initialize(activity: Activity) = Unit

    override fun isPrivacyOptionsRequired(activity: Activity): Boolean = false

    override fun requestPermissionAndShow(
        activity: Activity, success: RewardDataSource.Success, fail: RewardDataSource.Fail
    ) = show(activity = activity, success = success, fail = fail)

    override fun showPrivacyOptions(
        activity: Activity, fail: RewardDataSource.Fail, success: RewardDataSource.Success
    ) = Unit

    override fun load(activity: Activity, fail: RewardDataSource.Fail) = Unit

    override fun show(activity: Activity, success: RewardDataSource.Success, fail: RewardDataSource.Fail) {
        if (error == null)
            success.handle()
        else
            fail.handle(error)
    }

    override fun cancel() = Unit
}