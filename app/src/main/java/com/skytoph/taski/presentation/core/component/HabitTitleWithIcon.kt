package com.skytoph.taski.presentation.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skytoph.taski.R
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun HabitTitleWithIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: Color,
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = "habit icon",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.habit_icon_size))
                    .background(
                        color = color,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(6.dp),
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.inverseSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HabitTitleWithIconPreview() {
    HabitMateTheme {
        HabitTitleWithIcon(icon = Icons.Default.Check, color = Color.Blue, title = "title")
    }
}