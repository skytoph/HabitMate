package com.skytoph.taski.presentation.habit.icon

import com.skytoph.taski.R
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource

object IconMessages {
    val noConnectionMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.error_no_connection),
        title = StringResource.ResId(R.string.fail_title),
        icon = IconResource.Id(id = R.drawable.cloud_off)
    )
    val failedToLoadRewardMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.error_failed_to_load_reward),
        title = StringResource.ResId(R.string.fail_title),
        icon = IconResource.Id(id = R.drawable.cloud_off)
    )
    val failedToUpdateAdPreferences = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.error_failed_to_get_ad_permission),
        title = StringResource.ResId(R.string.fail_title),
        icon = IconResource.Id(id = R.drawable.settings)
    )
    fun rewardedMessage(icon: IconResource): SnackbarMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.reward_unlocked),
        title = StringResource.ResId(R.string.success_title),
        icon = icon
    )
}