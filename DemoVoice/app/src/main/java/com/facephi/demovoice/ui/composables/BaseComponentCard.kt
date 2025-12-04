package com.facephi.demovoice.ui.composables

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
import com.facephi.demovoice.ui.data.UIComponentResult
import com.facephi.demovoice.R

@Composable
fun BaseComponentCard(
    enabled: Boolean,
    title: String,
    desc: String,
    resultFirstValue: UIComponentResult,
    resultSecondValue: UIComponentResult,
    onLaunchFirst: (
        showPreviousTip: Boolean,
        showTutorial: Boolean,
        showDiagnostic: Boolean
    ) -> Unit,
    onLaunchSecond: (
        showPreviousTip: Boolean,
        showTutorial: Boolean,
        showDiagnostic: Boolean
    ) -> Unit,
) {
    var showPreviousTip by rememberSaveable { mutableStateOf(true) }
    var showTutorial by rememberSaveable { mutableStateOf(true) }
    var showDiagnostic by rememberSaveable { mutableStateOf(true) }

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
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

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
                    text = stringResource(id = R.string.onboarding_show_previous_tip),
                ) { showPreviousTip = it }

                BaseCheckView(
                    modifier = Modifier.weight(1f),
                    checkValue = showTutorial,
                    text = stringResource(id = R.string.onboarding_show_tutorial),
                ) { showTutorial = it }
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
                    text = stringResource(id = R.string.onboarding_show_diagnostic),
                ) { showDiagnostic = it }
            }

            BaseButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.onboarding_launch_enroll),
                enabled = enabled,
                onClick = {
                    onLaunchFirst(showPreviousTip, showTutorial, showDiagnostic)
                }
            )

            if (resultFirstValue != UIComponentResult.PENDING) {
                ResultRow(
                    label = if (resultFirstValue == UIComponentResult.OK) stringResource(id = R.string.onboarding_result_ok)
                    else stringResource(id = R.string.onboarding_result_error),
                    ok = resultFirstValue == UIComponentResult.OK
                )
            }

            BaseButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.onboarding_launch_auth),
                enabled = enabled,
                onClick = {
                    onLaunchSecond(showPreviousTip, showTutorial, showDiagnostic)
                }
            )

            if (resultSecondValue != UIComponentResult.PENDING) {
                ResultRow(
                    label = if (resultSecondValue == UIComponentResult.OK) stringResource(id = R.string.onboarding_result_ok)
                    else stringResource(id = R.string.onboarding_result_error),
                    ok = resultSecondValue == UIComponentResult.OK
                )
            }
        }
    }
}
