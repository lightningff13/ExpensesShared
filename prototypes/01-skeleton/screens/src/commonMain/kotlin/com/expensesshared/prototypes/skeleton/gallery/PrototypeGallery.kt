package com.expensesshared.prototypes.skeleton.gallery

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.expensesshared.prototypes.skeleton.emptygroups.EmptyGroupsScreen
import com.expensesshared.prototypes.skeleton.emptygroups.EmptyGroupsUiState
import com.expensesshared.prototypes.skeleton.signin.DummySignInRoute
import com.expensesshared.prototypes.skeleton.signin.DummySignInUiState
import com.expensesshared.prototypes.skeleton.signin.SignInField
import com.expensesshared.prototypes.skeleton.theme.PrototypeTheme

/**
 * Selects one screen/state/theme without adding any UI chrome (so focus order and semantics
 * of the screen under test stay untouched). Parameters come from the intent extras (Android)
 * or the URL query (web): ?screen=sign-in&state=filled&theme=dark
 * These parameters are prototype-only selectors, never user data.
 */
data class GallerySelection(
    val screen: String = SCREEN_EMPTY_GROUPS,
    val state: String = "empty",
    val theme: String = "system",
) {
    companion object {
        const val SCREEN_EMPTY_GROUPS = "empty-groups"
        const val SCREEN_SIGN_IN = "sign-in"

        fun parse(lookup: (String) -> String?): GallerySelection = GallerySelection(
            screen = lookup("screen") ?: SCREEN_EMPTY_GROUPS,
            state = lookup("state") ?: "empty",
            theme = lookup("theme") ?: "system",
        )

        fun parseQuery(query: String): GallerySelection {
            val map = query.removePrefix("?").split("&").filter { it.contains("=") }
                .associate { it.substringBefore("=") to it.substringAfter("=") }
            return parse { map[it] }
        }
    }
}

// Fictitious values only.
internal const val FAKE_EMAIL = "alice@example.test"
internal const val FAKE_PASSWORD = "mot-de-passe-factice"

@Composable
fun PrototypeGallery(selection: GallerySelection) {
    val dark = when (selection.theme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    PrototypeTheme(darkTheme = dark) {
        when (selection.screen) {
            GallerySelection.SCREEN_SIGN_IN -> SignInState(selection.state)
            else -> EmptyGroupsState(selection.state)
        }
    }
}

@Composable
internal fun EmptyGroupsState(state: String) {
    EmptyGroupsScreen(
        state = when (state) {
            "empty-disabled-create" -> EmptyGroupsUiState.EmptyWithDisabledCreateVariant
            "loading" -> EmptyGroupsUiState.Loading
            "server-error" -> EmptyGroupsUiState.ServerError
            else -> EmptyGroupsUiState.Empty
        },
    )
}

@Composable
internal fun SignInState(state: String) {
    val filled = DummySignInUiState(email = FAKE_EMAIL, password = FAKE_PASSWORD)
    when (state) {
        "filled" -> DummySignInRoute(initial = filled)
        "field-error" -> DummySignInRoute(initial = DummySignInUiState(emailError = true))
        "focus-email" -> DummySignInRoute(initialFocus = SignInField.Email)
        "focus-submit" -> DummySignInRoute(initial = filled, initialFocus = SignInField.Submit)
        "submitted" -> DummySignInRoute(initial = filled.copy(submitted = true))
        else -> DummySignInRoute()
    }
}
