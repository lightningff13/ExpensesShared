package com.expensesshared.prototypes.skeleton.gallery

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Every screen x state x theme x size. Two sizes per function (small 360 dp phone, large 1280 dp
 * desktop/tablet), two functions (light, dark) per state. Dark is forced through the theme
 * parameter because the common @Preview annotation has no uiMode.
 * Focus states depend on requestFocus, which a static preview does not always render:
 * use the running app for the real focus indicator.
 */
@Preview(name = "small 360dp", widthDp = 360, heightDp = 640, showBackground = true)
@Preview(name = "large 1280dp", widthDp = 1280, heightDp = 800, showBackground = true)
annotation class SizesPreview

// ---- US-1 EmptyGroupsScreen ----
@SizesPreview @Composable fun EmptyGroups_Empty_Light() = PrototypeGallery(GallerySelection("empty-groups", "empty", "light"))
@SizesPreview @Composable fun EmptyGroups_Empty_Dark() = PrototypeGallery(GallerySelection("empty-groups", "empty", "dark"))

@SizesPreview @Composable fun EmptyGroups_DisabledCreateVariant_Light() = PrototypeGallery(GallerySelection("empty-groups", "empty-disabled-create", "light"))
@SizesPreview @Composable fun EmptyGroups_DisabledCreateVariant_Dark() = PrototypeGallery(GallerySelection("empty-groups", "empty-disabled-create", "dark"))

// Conditional (Q8): only if the F1 client calls /health.
@SizesPreview @Composable fun EmptyGroups_Loading_Light() = PrototypeGallery(GallerySelection("empty-groups", "loading", "light"))
@SizesPreview @Composable fun EmptyGroups_Loading_Dark() = PrototypeGallery(GallerySelection("empty-groups", "loading", "dark"))
@SizesPreview @Composable fun EmptyGroups_ServerError_Light() = PrototypeGallery(GallerySelection("empty-groups", "server-error", "light"))
@SizesPreview @Composable fun EmptyGroups_ServerError_Dark() = PrototypeGallery(GallerySelection("empty-groups", "server-error", "dark"))

// ---- US-2 DummySignInForm ----
@SizesPreview @Composable fun SignIn_Empty_Light() = PrototypeGallery(GallerySelection("sign-in", "empty", "light"))
@SizesPreview @Composable fun SignIn_Empty_Dark() = PrototypeGallery(GallerySelection("sign-in", "empty", "dark"))
@SizesPreview @Composable fun SignIn_Filled_Light() = PrototypeGallery(GallerySelection("sign-in", "filled", "light"))
@SizesPreview @Composable fun SignIn_Filled_Dark() = PrototypeGallery(GallerySelection("sign-in", "filled", "dark"))
@SizesPreview @Composable fun SignIn_FieldError_Light() = PrototypeGallery(GallerySelection("sign-in", "field-error", "light"))
@SizesPreview @Composable fun SignIn_FieldError_Dark() = PrototypeGallery(GallerySelection("sign-in", "field-error", "dark"))
@SizesPreview @Composable fun SignIn_FocusEmail_Light() = PrototypeGallery(GallerySelection("sign-in", "focus-email", "light"))
@SizesPreview @Composable fun SignIn_FocusEmail_Dark() = PrototypeGallery(GallerySelection("sign-in", "focus-email", "dark"))
@SizesPreview @Composable fun SignIn_FocusSubmit_Light() = PrototypeGallery(GallerySelection("sign-in", "focus-submit", "light"))
@SizesPreview @Composable fun SignIn_FocusSubmit_Dark() = PrototypeGallery(GallerySelection("sign-in", "focus-submit", "dark"))
@SizesPreview @Composable fun SignIn_Submitted_Light() = PrototypeGallery(GallerySelection("sign-in", "submitted", "light"))
@SizesPreview @Composable fun SignIn_Submitted_Dark() = PrototypeGallery(GallerySelection("sign-in", "submitted", "dark"))
