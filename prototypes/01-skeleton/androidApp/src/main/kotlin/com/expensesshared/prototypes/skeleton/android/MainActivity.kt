package com.expensesshared.prototypes.skeleton.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.expensesshared.prototypes.skeleton.gallery.PrototypeGallery
import com.expensesshared.prototypes.skeleton.gallery.GallerySelection

/**
 * Prototype host. Selection through intent extras (used for captures):
 *   adb shell am start -n <pkg>/.MainActivity --es screen sign-in --es state filled --es theme dark
 * No extra = empty groups screen, system theme.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val selection = GallerySelection.parse { key -> intent.getStringExtra(key) }
        setContent { PrototypeGallery(selection) }
    }
}
