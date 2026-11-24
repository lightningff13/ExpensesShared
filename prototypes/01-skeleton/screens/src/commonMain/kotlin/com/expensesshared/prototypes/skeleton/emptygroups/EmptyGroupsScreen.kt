package com.expensesshared.prototypes.skeleton.emptygroups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.expensesshared.prototypes.skeleton.theme.PrototypeSpacing

/**
 * US-1 states.
 * Labels are PROPOSALS pending senior answer Q7.
 * [Loading] and [ServerError] are CONDITIONAL: only relevant if the F1 client calls /health (Q8).
 * [EmptyWithDisabledCreateVariant] is a VARIANT for Q7 (disabled "create group" button).
 */
sealed interface EmptyGroupsUiState {
    data object Empty : EmptyGroupsUiState
    data object EmptyWithDisabledCreateVariant : EmptyGroupsUiState
    data object Loading : EmptyGroupsUiState
    data object ServerError : EmptyGroupsUiState
}

object EmptyGroupsTexts {
    const val TITLE = "Aucun groupe"
    const val BODY = "Vous n'avez encore aucun groupe."
    const val CREATE_VARIANT = "Créer un groupe"
    const val LOADING = "Chargement en cours"
    const val ERROR_TITLE = "Une erreur est survenue"
    const val ERROR_BODY = "Impossible de charger vos groupes pour le moment. Réessayez plus tard."
    const val RETRY = "Réessayer"
}

@Composable
fun EmptyGroupsScreen(
    state: EmptyGroupsUiState,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(PrototypeSpacing.screenPadding),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.widthIn(max = PrototypeSpacing.contentMaxWidth).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(PrototypeSpacing.stackGap),
            ) {
                when (state) {
                    EmptyGroupsUiState.Empty -> EmptyContent(showDisabledCreate = false)
                    EmptyGroupsUiState.EmptyWithDisabledCreateVariant -> EmptyContent(showDisabledCreate = true)
                    EmptyGroupsUiState.Loading -> LoadingContent()
                    EmptyGroupsUiState.ServerError -> ErrorContent(onRetry)
                }
            }
        }
    }
}

@Composable
private fun EmptyContent(showDisabledCreate: Boolean) {
    Text(
        text = EmptyGroupsTexts.TITLE,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier.semantics { heading() },
    )
    Text(
        text = EmptyGroupsTexts.BODY,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
    if (showDisabledCreate) {
        // Disabled controls are exempt from WCAG contrast; still exposed (disabled) to screen readers.
        Button(
            onClick = {},
            enabled = false,
            modifier = Modifier.heightIn(min = PrototypeSpacing.minTarget),
        ) { Text(EmptyGroupsTexts.CREATE_VARIANT) }
    }
}

@Composable
private fun LoadingContent() {
    CircularProgressIndicator(
        modifier = Modifier.semantics {
            contentDescription = EmptyGroupsTexts.LOADING
            liveRegion = LiveRegionMode.Polite
        },
    )
    Text(
        text = EmptyGroupsTexts.LOADING,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun ErrorContent(onRetry: () -> Unit) {
    // Generic message: no technical detail, no status code, no host name.
    Text(
        text = EmptyGroupsTexts.ERROR_TITLE,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier.semantics { heading(); liveRegion = LiveRegionMode.Polite },
    )
    Text(
        text = EmptyGroupsTexts.ERROR_BODY,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
    Button(
        onClick = onRetry,
        modifier = Modifier.heightIn(min = PrototypeSpacing.minTarget),
    ) { Text(EmptyGroupsTexts.RETRY) }
}
