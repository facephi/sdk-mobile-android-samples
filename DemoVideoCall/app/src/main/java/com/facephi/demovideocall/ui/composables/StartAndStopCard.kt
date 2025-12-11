package com.facephi.demovideocall.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.facephi.demovideocall.R
import com.facephi.demovideocall.ui.data.UIComponentResult
import com.facephi.demovideoid.ui.composables.BaseButton


@Composable
fun StartAndStopCard(
    title: String,
    desc: String,
    startButtonText: String,
    stopButtonText: String,
    resultValue: UIComponentResult,
    enabled: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
) {

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.sdkBackgroundColor)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Row {
                BaseButton(
                    modifier = Modifier.padding(end = 8.dp)
                        .weight(1f),
                    text = startButtonText,
                    enabled = enabled,
                    onClick = onStart
                )

                BaseButton(
                    modifier = Modifier.padding(start = 8.dp)
                        .weight(1f),
                    text = stopButtonText,
                    enabled = enabled,
                    onClick = onStop
                )
            }
            if (resultValue != UIComponentResult.PENDING) {
                ResultRow(
                    label = if (resultValue == UIComponentResult.OK) stringResource(id = R.string.demo_result_ok)
                    else stringResource(id = R.string.demo_result_error),
                    ok = resultValue == UIComponentResult.OK
                )
            }
        }
    }
}