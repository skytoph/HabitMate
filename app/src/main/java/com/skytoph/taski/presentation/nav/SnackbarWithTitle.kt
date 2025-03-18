package com.skytoph.taski.presentation.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skytoph.taski.R
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun SnackbarWithTitle(message: SnackbarMessage, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val color = message.color?.let { Color(it) } ?: if (message.isError) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.primary
    Snackbar(
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier.padding(16.dp)
            .border(width = 1.dp, color = color, shape = MaterialTheme.shapes.medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = message.icon.vector(context = LocalContext.current),
                contentDescription = message.title.getString(context),
                modifier = Modifier
                    .size(34.dp)
                    .background(color = color, shape = CircleShape)
                    .padding(8.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = message.title.getString(context),
                    style = MaterialTheme.typography.titleSmall,
                    color = color
                )
                Text(
                    text = message.messageResource.getString(context),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
@Preview
private fun SnackbarPreview() {
    HabitMateTheme(darkTheme = true) {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarWithTitle(
                message = SnackbarMessage(
                    title = StringResource.Value("habit"),
                    messageResource = StringResource.Value("message..."),
                    icon = IconResource.Id(R.drawable.trash)
                )
            )
        }
    }
}