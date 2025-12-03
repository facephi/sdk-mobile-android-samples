package com.facephi.onboarding.ui.composables

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.facephi.onboarding.R
import com.facephi.onboarding.ui.data.UIComponentResult

@Composable
fun FileUploaderCard(
    enabled: Boolean,
    title: String,
    desc: String,
    buttonText: String,
    resultValue: UIComponentResult,
    onLaunch: (
        showPreviousTip: Boolean,
        allowGallery: Boolean,
        showDiagnostic: Boolean,
        maxDocuments: Int
    ) -> Unit,
) {
    var showPreviousTip by rememberSaveable { mutableStateOf(true) }
    var allowGallery by rememberSaveable { mutableStateOf(true) }
    var showDiagnostic by rememberSaveable { mutableStateOf(true) }
    var maxDocuments by rememberSaveable { mutableStateOf(2) }

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (enabled) 1f else 0.6f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BaseCheckView(
                    modifier = Modifier.weight(1f),
                    checkValue = showPreviousTip,
                    text = stringResource(id = R.string.onboarding_show_previous_tip)
                ) { showPreviousTip = it }

                BaseCheckView(
                    modifier = Modifier.weight(1f),
                    checkValue = allowGallery,
                    text = stringResource(id = R.string.onboarding_allow_gallery)
                ) { allowGallery = it }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (enabled) 1f else 0.6f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                BaseCheckView(
                    checkValue = showDiagnostic,
                    text = stringResource(id = R.string.onboarding_show_diagnostic)
                ) { showDiagnostic = it }
            }

            SettingStepperRow(
                text = stringResource(id = R.string.onboarding_max_file_uploader),
                value = maxDocuments.coerceAtLeast(1),
                onValueChange = { maxDocuments = it.coerceAtLeast(1) },
                min = 1,
                max = 20
            )

            BaseButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = buttonText,
                enabled = enabled,
                onClick = {
                    onLaunch(
                        showPreviousTip,
                        allowGallery,
                        showDiagnostic,
                        maxDocuments
                    )
                }
            )

            if (resultValue != UIComponentResult.PENDING) {
                ResultRow(
                    label = if (resultValue == UIComponentResult.OK) stringResource(id = R.string.onboarding_result_ok)
                    else stringResource(id = R.string.onboarding_result_error),
                    ok = resultValue == UIComponentResult.OK
                )
            }
        }
    }
}

