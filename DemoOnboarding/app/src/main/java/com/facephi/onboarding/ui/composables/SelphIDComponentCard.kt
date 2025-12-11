package com.facephi.onboarding.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.facephi.onboarding.R
import com.facephi.onboarding.ui.data.UIComponentResult
import com.facephi.selphid_component.data.configuration.SelphIDDocumentSide
import com.facephi.selphid_component.data.configuration.SelphIDDocumentType
import com.facephi.selphid_component.data.configuration.SelphIDScanMode

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun SelphIDComponentCard(
    enabled: Boolean,
    title: String,
    desc: String,
    buttonText: String,
    resultValue: UIComponentResult,
    onLaunch: (
        showPreviousTip: Boolean,
        showTutorial: Boolean,
        showDiagnostic: Boolean,
        wizardMode: Boolean,
        showResultAfterCapture: Boolean,
        scanMode: SelphIDScanMode,
        specificData: String,
        fullscreen: Boolean,
        documentType: SelphIDDocumentType,
        documentSide: SelphIDDocumentSide,
        generateRawImages: Boolean,
    ) -> Unit,
) {
    var showPreviousTip by rememberSaveable { mutableStateOf(true) }
    var showTutorial by rememberSaveable { mutableStateOf(true) }
    var showDiagnostic by rememberSaveable { mutableStateOf(true) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    var wizardMode by rememberSaveable { mutableStateOf(true) }
    var showResultAfterCapture by rememberSaveable { mutableStateOf(true) }
    var scanMode by rememberSaveable { mutableStateOf(SelphIDScanMode.MODE_SEARCH) }
    var specificData by rememberSaveable { mutableStateOf("ES|<ALL>") }
    var fullscreen by rememberSaveable { mutableStateOf(true) }
    var documentType by rememberSaveable { mutableStateOf(SelphIDDocumentType.ID_CARD) }
    var documentSide by rememberSaveable { mutableStateOf(SelphIDDocumentSide.FRONT) }
    var generateRawImages by rememberSaveable { mutableStateOf(false) }

    var scanMenuExpanded by remember { mutableStateOf(false) }
    var docTypeMenuExpanded by remember { mutableStateOf(false) }

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

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { expanded = !expanded },
                tonalElevation = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.onboarding_configuration),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BaseCheckView(
                            modifier = Modifier.weight(1f),
                            checkValue = showDiagnostic,
                            text = stringResource(id = R.string.onboarding_show_diagnostic),
                        ) { showDiagnostic = it }

                        BaseCheckView(
                            modifier = Modifier.weight(1f),
                            checkValue = fullscreen,
                            text = "Fullscreen",
                        ) { fullscreen = it }
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
                            checkValue = wizardMode,
                            text = "Wizard mode",
                        ) { wizardMode = it }

                        BaseCheckView(
                            modifier = Modifier.weight(1f),
                            checkValue = showResultAfterCapture,
                            text = "Show result after capture",
                        ) { showResultAfterCapture = it }
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
                            checkValue = generateRawImages,
                            text = "Generate raw images",
                        ) { generateRawImages = it }
                    }

                    ExposedDropdownMenuBox(
                        expanded = scanMenuExpanded,
                        onExpandedChange = {
                            if (enabled) scanMenuExpanded = !scanMenuExpanded
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = scanMode.name,
                            onValueChange = {},
                            readOnly = true,
                            enabled = enabled,
                            label = { Text("Select scan mode") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = scanMenuExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = scanMenuExpanded,
                            onDismissRequest = { scanMenuExpanded = false }
                        ) {
                            SelphIDScanMode.entries.forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode.name) },
                                    onClick = {
                                        scanMode = mode
                                        scanMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = specificData,
                        onValueChange = { specificData = it },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled,
                        label = { Text("Specific data") },
                        placeholder = { Text("ES|<ALL>") },
                        singleLine = true
                    )

                    ExposedDropdownMenuBox(
                        expanded = docTypeMenuExpanded,
                        onExpandedChange = {
                            if (enabled) docTypeMenuExpanded = !docTypeMenuExpanded
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = documentType.name,
                            onValueChange = {},
                            readOnly = true,
                            enabled = enabled,
                            label = { Text("Select document type") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = docTypeMenuExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = docTypeMenuExpanded,
                            onDismissRequest = { docTypeMenuExpanded = false }
                        ) {
                            SelphIDDocumentType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name) },
                                    onClick = {
                                        documentType = type
                                        docTypeMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Text(
                        text = "Document side",
                        style = MaterialTheme.typography.labelLarge
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(if (enabled) 1f else 0.6f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SelphIDDocumentSide.entries.forEach { side ->
                            FilterChip(
                                selected = documentSide == side,
                                onClick = { if (enabled) documentSide = side },
                                label = { Text(side.name) }
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
                        showTutorial,
                        showDiagnostic,
                        wizardMode,
                        showResultAfterCapture,
                        scanMode,
                        specificData,
                        fullscreen,
                        documentType,
                        documentSide,
                        generateRawImages
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
