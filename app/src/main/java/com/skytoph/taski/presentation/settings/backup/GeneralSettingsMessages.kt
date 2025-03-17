package com.skytoph.taski.presentation.settings.backup

import com.skytoph.taski.R
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource

object GeneralSettingsMessages {
    val idCopiedMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.message_user_id_copied),
        title = StringResource.ResId(R.string.success_title),
        icon = IconResource.Id(id = R.drawable.copy_check)
    )
}