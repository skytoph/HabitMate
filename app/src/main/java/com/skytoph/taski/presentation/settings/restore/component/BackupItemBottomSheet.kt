@file:OptIn(ExperimentalMaterial3Api::class)

package com.skytoph.taski.presentation.settings.restore.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.component.TitleWithIconMenuItem
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun BackupItemBottomSheet(
    state: SheetState = rememberModalBottomSheetState(),
    title: String = "28.09.24 12:00",
    hideBottomSheet: () -> Unit = {},
    restore: () -> Unit = {},
    delete: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        sheetState = state,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            TitleWithIconMenuItem(
                title = stringResource(R.string.action_restore),
                icon = ImageVector.vectorResource(id = R.drawable.folder_output),
                onClick = { restore() }
            )
            HorizontalDivider()
            TitleWithIconMenuItem(
                title = stringResource(R.string.action_delete),
                icon = ImageVector.vectorResource(id = R.drawable.trash),
                onClick = { delete() },
                tint = MaterialTheme.colorScheme.error,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Preview
@Composable
private fun BottomSheetPreview() {
    HabitMateTheme(darkTheme = true) {
        BackupItemBottomSheet(state = rememberStandardBottomSheetState())
    }
}