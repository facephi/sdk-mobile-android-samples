package com.facephi.demophingers.ui.theme

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat

private const val MAX_COLOR_RESOLUTION_DEPTH = 8

@Composable
fun sdkColorResource(@ColorRes id: Int): Color {
    val context = LocalContext.current
    return context.resolveSdkColorResource(id) ?: context.defaultSdkColor(id)
}

private fun Context.resolveSdkColorResource(@ColorRes id: Int, depth: Int = 0): Color? {
    if (depth > MAX_COLOR_RESOLUTION_DEPTH) return null

    val typedValue = TypedValue()
    return try {
        resources.getValue(id, typedValue, true)
        resolveTypedColorValue(typedValue, depth) ?: resolveColorStateList(id)
    } catch (_: Resources.NotFoundException) {
        null
    }
}

private fun Context.resolveTypedColorValue(typedValue: TypedValue, depth: Int): Color? {
    if (depth > MAX_COLOR_RESOLUTION_DEPTH) return null

    return when (typedValue.type) {
        TypedValue.TYPE_ATTRIBUTE -> resolveThemeAttributeColor(typedValue.data, depth + 1)
        TypedValue.TYPE_REFERENCE -> typedValue.resourceId.takeIf { it != 0 }?.let {
            resolveSdkColorResource(it, depth + 1)
        }
        in TypedValue.TYPE_FIRST_COLOR_INT..TypedValue.TYPE_LAST_COLOR_INT -> Color(typedValue.data)
        else -> typedValue.resourceId.takeIf { it != 0 }?.let {
            resolveSdkColorResource(it, depth + 1)
        }
    }
}

private fun Context.resolveThemeAttributeColor(attrId: Int, depth: Int): Color? {
    val typedValue = TypedValue()
    if (!theme.resolveAttribute(attrId, typedValue, true)) return null

    return resolveTypedColorValue(typedValue, depth)
        ?: typedValue.resourceId.takeIf { it != 0 }?.let(::resolveColorStateList)
}

private fun Context.resolveColorStateList(@ColorRes id: Int): Color? = try {
    ResourcesCompat.getColorStateList(resources, id, theme)?.defaultColor?.let(::Color)
} catch (_: Resources.NotFoundException) {
    null
}

private fun Context.defaultSdkColor(@ColorRes id: Int): Color = when (resources.getResourceEntryName(id)) {
    "sdkPrimaryColor" -> Color(0xFF3167FC)
    "sdkSecondaryColor" -> Color(0xFF03DAC5)
    "sdkBackgroundColor" -> Color(0xFFFFFFFF)
    "sdkErrorColor" -> Color(0xFFDD3631)
    "sdkTitleTextColor" -> Color(0xFF1D2C4D)
    "sdkBodyTextColor" -> Color(0xFF526080)
    "sdkSuccessColor" -> Color(0xFF07A13A)
    "sdkNeutralColor" -> Color(0xFF202C4B)
    "sdkAccentColor" -> Color(0xFFEA7547)
    "sdkTopIconsColor" -> Color(0xFF243760)
    "sdkButtonTextColor" -> Color(0xFFFFFFFF)
    else -> Color.Unspecified
}
