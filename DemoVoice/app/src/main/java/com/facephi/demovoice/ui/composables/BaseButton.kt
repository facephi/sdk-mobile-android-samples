package com.facephi.demovoice.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.facephi.demovoice.R
import com.facephi.demovoice.ui.theme.appFontFamily

@Composable
fun BaseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = colorResource(
            id = R.color.sdkPrimaryColor
        )
    ),
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        modifier = modifier.fillMaxWidth(),
        colors = colors
    ) {
        Text(
            text = text,
            color = Color.White
        )
    }
}

@Composable
fun BaseTextButton(
    enabled: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = { onClick() },
        enabled = enabled,
        modifier = modifier,
    ) {
        Text(
            text = text,
            fontFamily = appFontFamily,
            fontSize = 18.sp,
            color = colorResource(
                id = R.color.sdkPrimaryColor
            ),
            fontWeight = FontWeight.Bold,
            style = TextStyle(textDecoration = TextDecoration.Underline)
        )
    }
}
