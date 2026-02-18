package com.facephi.demophingers.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.facephi.demophingers.R
import com.facephi.demophingers.ui.data.UIComponentResult
import com.facephi.phingers_tf_component.data.configuration.CaptureOrientation
import com.facephi.phingers_tf_component.data.configuration.FingerFilter
import com.facephi.phingers_tf_component.data.configuration.ReticleOrientation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseComponentCard(
    enabled: Boolean,
    title: String,
    desc: String,
    resultValue: UIComponentResult,
    buttonText: String,
    onLaunch: (
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
        liveness: Boolean,
        captureOrientation: ReticleOrientation,
        fingerFilter: FingerFilter,
        showPreviousFingerSelector: Boolean
    ) -> Unit,
) {
    var showPreviousTip by rememberSaveable { mutableStateOf(true) }
    var showDiagnostic by rememberSaveable { mutableStateOf(true) }
    var showPreviousFingerSelector by rememberSaveable { mutableStateOf(false) }
    var liveness by rememberSaveable { mutableStateOf(true) }

    var captureOrientation by rememberSaveable {
        mutableStateOf(ReticleOrientation.LEFT)
    }

    var fingerFilter by rememberSaveable {
        mutableStateOf(FingerFilter.SLAP)
    }

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
                    checkValue = showDiagnostic,
                    text = stringResource(id = R.string.onboarding_show_diagnostic),
                ) { showDiagnostic = it }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (enabled) 1f else 0.6f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BaseCheckView(
                    modifier = Modifier.weight(1f),
                    checkValue = liveness,
                    text = "Liveness",
                ) { liveness = it }

                BaseCheckView(
                    modifier = Modifier.weight(1f),
                    checkValue = showPreviousFingerSelector,
                    text = stringResource(id = R.string.onboarding_show_selector_screen),
                ) { showPreviousFingerSelector = it }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (enabled) 1f else 0.6f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_choose_hand),
                    style = MaterialTheme.typography.labelLarge
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = captureOrientation == ReticleOrientation.LEFT,
                        onClick = { if (enabled) captureOrientation = ReticleOrientation.LEFT },
                        label = { Text(stringResource(id = R.string.onboarding_left)) }
                    )
                    FilterChip(
                        selected = captureOrientation == ReticleOrientation.RIGHT,
                        onClick = { if (enabled) captureOrientation = ReticleOrientation.RIGHT },
                        label = { Text(stringResource(id = R.string.onboarding_right)) }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (enabled) 1f else 0.6f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_choose_filter),
                    style = MaterialTheme.typography.labelLarge
                )

                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { if (enabled) expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = fingerFilterLabel(fingerFilter),
                        onValueChange = {},
                        readOnly = true,
                        enabled = enabled,
                        label = { Text(stringResource(id = R.string.onboarding_choose_filter)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        FingerFilter.values().forEach { option ->
                            DropdownMenuItem(
                                text = { Text(fingerFilterLabel(option)) },
                                onClick = {
                                    fingerFilter = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            BaseButton(
                modifier = Modifier.fillMaxWidth(),
                text = buttonText,
                enabled = enabled,
                onClick = {
                    onLaunch(
                        showPreviousTip,
                        showDiagnostic,
                        liveness,
                        captureOrientation,
                        fingerFilter,
                        showPreviousFingerSelector
                    )
                }
            )

            if (resultValue != UIComponentResult.PENDING) {
                ResultRow(
                    label = if (resultValue == UIComponentResult.OK)
                        stringResource(id = R.string.onboarding_result_ok)
                    else
                        stringResource(id = R.string.onboarding_result_error),
                    ok = resultValue == UIComponentResult.OK
                )
            }
        }
    }
}


@Composable
private fun fingerFilterLabel(filter: FingerFilter): String {
    return when (filter) {
        FingerFilter.SLAP ->
            stringResource(R.string.onboarding_fingerfilter_slap)

        FingerFilter.ALL_4_FINGERS_ONE_BY_ONE ->
            stringResource(R.string.onboarding_fingerfilter_all_4_fingers_one_by_one)

        FingerFilter.ALL_5_FINGERS_ONE_BY_ONE ->
            stringResource(R.string.onboarding_fingerfilter_all_5_fingers_one_by_one)

        FingerFilter.INDEX_FINGER ->
            stringResource(R.string.onboarding_fingerfilter_index_finger)

        FingerFilter.MIDDLE_FINGER ->
            stringResource(R.string.onboarding_fingerfilter_middle_finger)

        FingerFilter.RING_FINGER ->
            stringResource(R.string.onboarding_fingerfilter_ring_finger)

        FingerFilter.LITTLE_FINGER ->
            stringResource(R.string.onboarding_fingerfilter_little_finger)

        FingerFilter.THUMB_FINGER ->
            stringResource(R.string.onboarding_fingerfilter_thumb_finger)
    }
}

