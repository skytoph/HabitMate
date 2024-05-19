package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R

@Composable
fun HabitTitleWithIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: Color,
    title: String
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(end = 8.dp)) {
        Box(Modifier.padding(dimensionResource(id = R.dimen.habit_icon_padding))) {
            Icon(
                imageVector = icon,
                contentDescription = "habit icon",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.habit_icon_size))
                    .background(
                        color = color,
                        shape = RoundedCornerShape(30)
                    )
                    .padding(4.dp),
                tint = Color.White
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}