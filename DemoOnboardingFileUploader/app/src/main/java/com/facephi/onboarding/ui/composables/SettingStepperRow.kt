package com.facephi.onboarding.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.ui.res.colorResource
import com.facephi.onboarding.R

@Composable
fun SettingStepperRow(
    text: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    min: Int,
    max: Int,
    icon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) { icon() }
            Spacer(Modifier.width(12.dp))
        }
        Column(Modifier
            .weight(1f)
            .padding(horizontal = 16.dp)) {
            Text(text = text, color = colorResource(id = R.color.sdkBodyTextColor))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = { onValueChange((value - 1).coerceAtLeast(min)) },
                enabled = value > min
            ) { Icon(Icons.Rounded.Remove, contentDescription = "Menos") }

            Text(
                value.toString(),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.widthIn(min = 32.dp),
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = { onValueChange((value + 1).coerceAtMost(max)) },
                enabled = value < max
            ) { Icon(Icons.Rounded.Add, contentDescription = "Más") }
        }
    }
}
