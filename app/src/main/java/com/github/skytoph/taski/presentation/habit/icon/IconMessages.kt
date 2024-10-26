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
}