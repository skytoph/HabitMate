package com.github.skytoph.taski.presentation.habit.icon

import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource

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

    fun rewardedMessage(icon: IconResource): SnackbarMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.reward_unlocked),
        title = StringResource.ResId(R.string.success_title),
        icon = icon
    )
}