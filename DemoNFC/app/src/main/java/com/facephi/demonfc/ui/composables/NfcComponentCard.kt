package com.facephi.demonfc.ui.composables

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.facephi.demonfc.model.UIComponentResult
import com.facephi.demonfc.R
import com.facephi.demonfc.SdkData
import com.facephi.demonfc.model.DocumentType
import com.facephi.demonfc.ui.composables.base.BaseButton
import com.facephi.demonfc.ui.composables.base.BaseCheckView
import com.facephi.demonfc.ui.composables.base.DropdownReadingProgress
import com.facephi.demonfc.ui.composables.result.ResultRow
import com.facephi.demonfc.utils.validNfcDate
import com.facephi.nfc_component.data.configuration.NfcConfigurationData
import com.facephi.nfc_component.data.configuration.ReadingProgressStyle
import kotlin.text.isNotEmpty


@Composable
fun NfcComponentCard(
    enabled: Boolean,
    title: String,
    resultValue: UIComponentResult,
    onLaunch: (
        config: NfcConfigurationData
    ) -> Unit
) {
    var showPreviousTip by rememberSaveable { mutableStateOf(true) }
    var showTutorial by rememberSaveable { mutableStateOf(true) }
    var showDiagnostic by rememberSaveable { mutableStateOf(true) }
    var extractFace by rememberSaveable { mutableStateOf(true) }
    var extractSignature by rememberSaveable { mutableStateOf(true) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var support by rememberSaveable {
        mutableStateOf("")
    }

    var birthDate by rememberSaveable {
        mutableStateOf("")
    }

    var expirationDate by rememberSaveable {
        mutableStateOf("")
    }

    var documentType by rememberSaveable {
        mutableStateOf(DocumentType.ID_CARD)
    }

    var readingProgressStyle by rememberSaveable {
        mutableStateOf(ReadingProgressStyle.DOTS)
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
            Text(text = title, style = MaterialTheme.typography.titleMedium)

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            DocumentDataCard(
                support = support,
                onSupportChange = {
                    support = it
                },
                birthDate = birthDate,
                onBirthDateChange = {
                    birthDate = it
                },
                expirationDate = expirationDate,
                onExpirationDateChange = {
                    expirationDate = it
                },
                documentType = documentType,
                onDocumentTypeChange = {
                    documentType = it
                },
            )

            Spacer(Modifier.size(8.dp))

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
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(if (enabled) 1f else 0.6f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BaseCheckView(
                            modifier = Modifier.weight(1f),
                            checkValue = extractFace,
                            text = stringResource(id = R.string.nfc_extract_face),
                        ) { extractFace = it }

                        BaseCheckView(
                            modifier = Modifier.weight(1f),
                            checkValue = extractSignature,
                            text = stringResource(id = R.string.nfc_extract_signature),
                        ) { extractSignature = it }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(if (enabled) 1f else 0.6f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BaseCheckView(
                            modifier = Modifier.weight(1f),
                            checkValue = showDiagnostic,
                            text = stringResource(id = R.string.onboarding_show_diagnostic),
                        ) { showDiagnostic = it }

                        DropdownReadingProgress(
                            selected = readingProgressStyle,
                            onSelected = {
                                readingProgressStyle = it
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(Modifier.size(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BaseButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.nfc_simple_launch),
                    enabled = support.isNotEmpty() && birthDate.validNfcDate() && expirationDate.validNfcDate(),
                    onClick = {
                        Log.d(
                            "APP",
                            "LAUNCH NFC \n Support: $support \n birthDate: $birthDate \n" +
                                    " expirationDate: $expirationDate"
                        )
                        val nfcConfigurationData = SdkData.getNfcConfig(
                            support = support,
                            birthDate = birthDate,
                            expirationDate = expirationDate,
                            showPreviousTip = showPreviousTip,
                            showTutorial = showTutorial,
                            showDiagnostic = showDiagnostic,
                            skipPACE = true,
                            extractFace = extractFace,
                            extractSignature = extractSignature,
                            docType = documentType,
                            readingProgressStyle = readingProgressStyle

                        )
                        onLaunch.invoke(nfcConfigurationData)
                    }

                )
                Spacer(Modifier.size(8.dp))
                BaseButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.nfc_complex_launch),
                    enabled = support.isNotEmpty() && birthDate.validNfcDate() && expirationDate.validNfcDate(),
                    onClick = {

                        Log.d(
                            "APP",
                            "LAUNCH NFC \n Support: $support \n birthDate: $birthDate \n" +
                                    " expirationDate: $expirationDate"
                        )
                        val nfcConfigurationData = SdkData.getNfcConfig(
                            support = support,
                            birthDate = birthDate,
                            expirationDate = expirationDate,
                            showPreviousTip = showPreviousTip,
                            showTutorial = showTutorial,
                            showDiagnostic = showDiagnostic,
                            skipPACE = false,
                            extractFace = extractFace,
                            extractSignature = extractSignature,
                            docType = documentType,
                            readingProgressStyle = readingProgressStyle
                        )
                        onLaunch.invoke(nfcConfigurationData)


                    }
                )
            }

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
