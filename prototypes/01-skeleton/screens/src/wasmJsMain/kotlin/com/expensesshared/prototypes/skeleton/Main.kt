package com.expensesshared.prototypes.skeleton

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.expensesshared.prototypes.skeleton.gallery.GallerySelection
import com.expensesshared.prototypes.skeleton.gallery.PrototypeGallery

@JsFun("() => window.location.search")
private external fun readQuery(): String

/** Web entry point. Selection via the URL query, e.g. ?screen=sign-in&state=filled&theme=dark */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val selection = GallerySelection.parseQuery(readQuery())
    ComposeViewport(viewportContainerId = "app") {
        App(selection)
    }
}

@Composable
private fun App(selection: GallerySelection) = PrototypeGallery(selection)
