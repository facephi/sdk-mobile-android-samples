package com.facephi.demophingers.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.facephi.demophingers.R
import com.facephi.demophingers.ui.theme.appFontFamily


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
    image: Int? = null,
    shape: Shape = RoundedCornerShape(dimensionResource(R.dimen.sdk_buttons_corner_dimen)),
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        modifier = modifier.fillMaxWidth(),
        colors = colors
    ) {
        Text(
            text = text.uppercase(),
            color = Color.White,
            fontFamily = appFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )
        if (image != null){
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
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

@Composable
fun OutlinedButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(dimensionResource(R.dimen.sdk_buttons_corner_dimen)),
    onClick: () -> Unit,
) {
    androidx.compose.material.OutlinedButton(
        onClick = { onClick() },
        enabled = enabled,
        shape = shape,
        modifier = modifier,
    ) {
        Text(
            text = text.uppercase(),
            fontFamily = appFontFamily,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
