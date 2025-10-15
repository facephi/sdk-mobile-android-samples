package com.facephi.onboarding.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.facephi.onboarding.R
import com.facephi.onboarding.ui.theme.appFontFamily


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
    loading: Boolean = false,
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
        if (loading){
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .size(32.dp)
            )
        } else {
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

