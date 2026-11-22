package com.viajacalu.mapu.feature.guides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.viajacalu.mapu.R
import com.viajacalu.mapu.core.designsystem.theme.MapuTheme
import com.viajacalu.mapu.core.model.Guide

@Composable
fun GuidesRoute(
    modifier: Modifier = Modifier,
    viewModel: GuidesViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GuidesScreen(
        uiState = uiState,
        onTryAgainClick = viewModel::loadGuides,
        modifier = modifier
    )
}

@Composable
fun GuidesScreen(
    uiState: GuidesUiState,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // The title is always shown, whatever the state
        Text(
            text = stringResource(R.string.guides_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(24.dp)
                .semantics { heading() }
        )

        when (uiState) {
            GuidesUiState.Loading -> LoadingContent()
            GuidesUiState.Error -> ErrorContent(onTryAgainClick = onTryAgainClick)
            is GuidesUiState.Success -> GuidesList(guides = uiState.guidesList)
        }
    }

}

@Composable
fun LoadingContent() {
   Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
       CircularProgressIndicator()
   }
}

@Composable
fun ErrorContent(onTryAgainClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.guides_error), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onTryAgainClick) {
            Text(text = stringResource(R.string.guides_try_again))
        }
    }
}

@Composable
fun GuidesList(guides: List<Guide>) {
    LazyColumn {
        items(items = guides, key = {it.id}){ guide ->
            GuideItem(guide = guide)
        }
    }
}

@Composable
fun GuideItem(guide: Guide) {
    ListItem(
        supportingContent = {
            Text(text = stringResource(R.string.guides_subtitle, guide.country, guide.tagline))
        },
        content = {
            Text(text = guide.name)
        },
    )
}

private val previewGuides = listOf(
    Guide("london", "London", "England", "Europe", "Markets, museums, pubs", "map1", true),
    Guide("edinburgh", "Edinburgh", "Scotland", "Europe", "Old Town, hikes, whisky", "map2", false)
)

@Preview(showBackground = true)
@Composable
private fun GuidesScreenPreview() {
    MapuTheme {
        GuidesScreen(uiState = GuidesUiState.Success(previewGuides), onTryAgainClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun GuidesScreenErrorPreview() {
    MapuTheme {
        GuidesScreen(uiState = GuidesUiState.Error, onTryAgainClick = {})
    }
}




