package com.github.skytoph.taski.presentation.settings.credits.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.settings.credits.CreditItemUi
import com.github.skytoph.taski.presentation.settings.credits.CreditsViewModel
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun CreditsScreen(
    viewModel: CreditsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.settings_credits)
    }

    CreditsList(credits = CreditItemUi.credits(LocalContext.current.resources))
}

@Composable
fun CreditsList(credits: List<CreditItemUi> = emptyList()) {
    LazyColumn {
        itemsIndexed(credits) { index, item ->
            CreditItem(item)
            if (index != credits.lastIndex)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
fun CreditItem(item: CreditItemUi) {
    val uriHandler = LocalUriHandler.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                item.url?.let { url -> uriHandler.openUri(url) }
            })
            .padding(vertical = 16.dp, horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
            item.author?.let { author ->
                Text(
                    text = stringResource(R.string.credit_author, author),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.extraLarge
            )
        ) {
            Text(
                text = item.license,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun CreditsScreenPreview(
    credits: List<CreditItemUi> = listOf(
        CreditItemUi(title = "Lucide Icons", license = "ISC License"),
        CreditItemUi(title = "Material Icons", license = "Apache License 2.0"),
        CreditItemUi(title = "Reorderable", license = "Apache License 2.0", author = "Calvin Liang"),
    )
) {
    HabitMateTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            CreditsList(credits = credits)
        }
    }
}