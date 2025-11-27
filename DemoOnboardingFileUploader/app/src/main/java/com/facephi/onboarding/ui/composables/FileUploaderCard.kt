package com.facephi.onboarding.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.facephi.onboarding.R

@Composable
fun FileUploaderCard(
    enabled: Boolean,
    buttonText: String,
    onLaunch: (
        showPreviousTip: Boolean,
        allowGallery: Boolean,
        showDiagnostic: Boolean,
        maxDocuments: Int
    ) -> Unit,
) {
    var showPreviousTip by rememberSaveable {
        mutableStateOf(true)
    }

    var allowGallery by rememberSaveable {
        mutableStateOf(true)
    }
    var showDiagnostic by rememberSaveable {
        mutableStateOf(true)
    }

    var maxDocuments by rememberSaveable {
        mutableStateOf(2)
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.sdkBackgroundColor)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column() {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                BaseCheckView(
                    modifier = Modifier.weight(1f),
                    checkValue = showPreviousTip,
                    text = stringResource(id = R.string.onboarding_show_previous_tip)
                ) {
                    showPreviousTip = it
                }
                BaseCheckView(
                    modifier = Modifier.weight(1f),
                    checkValue = allowGallery,
                    text = stringResource(id = R.string.onboarding_allow_gallery)
                ) {
                    allowGallery = it
                }
            }

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                BaseCheckView(
                    checkValue = showDiagnostic,
                    text = stringResource(id = R.string.onboarding_show_diagnostic)
                ) {
                    showDiagnostic = it
                }
            }

            // --- Límite de documentos
            SettingStepperRow(
                text = "Documentos a capturar",
                value = maxDocuments.coerceAtLeast(1),
                onValueChange = {
                    maxDocuments = it.coerceAtLeast(1)
                },
                min = 1,
                max = 20,
                //icon = { Icon(Icons.Rounded.Description, null) }
            )

            BaseButton(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = buttonText,
                enabled = enabled,
                onClick = {
                    onLaunch(
                        showPreviousTip,
                        allowGallery,
                        showDiagnostic,
                        maxDocuments
                    )
                })
        }

    }
}