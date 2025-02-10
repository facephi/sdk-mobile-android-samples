package com.facephi.demonfc.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.facephi.demonfc.MainViewModel
import com.facephi.demonfc.R
import com.facephi.demonfc.SdkData
import com.facephi.demonfc.ui.composables.BaseButton
import com.facephi.demonfc.ui.composables.BaseTextButton
import com.facephi.demonfc.utils.validNfcDate
import com.facephi.nfc_component.data.configuration.NfcConfigurationData
import io.github.aakira.napier.Napier
import androidx.compose.ui.res.colorResource
import com.facephi.demonfc.BuildConfig
import com.facephi.demonfc.model.DocumentType
import com.facephi.demonfc.ui.composables.BaseCheckView
import com.facephi.demonfc.ui.composables.DropdownDocumentMenuBox

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DataScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onSendEmail: (String) -> Unit,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val logs = remember { mutableStateListOf<String>() }
    val focusManager = LocalFocusManager.current

    var support by rememberSaveable {
        mutableStateOf("")
    }

    var nfcConfigurationData by rememberSaveable {
        mutableStateOf<NfcConfigurationData?>(null)
    }

    var birthDate by rememberSaveable {
        mutableStateOf("")
    }

    var expirationDate by rememberSaveable {
        mutableStateOf("")
    }

    var showPreviousTip by rememberSaveable {
        mutableStateOf(true)
    }

    var showTutorial by rememberSaveable {
        mutableStateOf(true)
    }

    var showDiagnostic by rememberSaveable {
        mutableStateOf(true)
    }

    var documentType by rememberSaveable {
        mutableStateOf(DocumentType.ID_CARD)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        BaseButton(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            text = stringResource(id = R.string.nfc_new_operation), onClick = {
                Napier.d("APP: LAUNCH NEW OPERATION")
                focusManager.clearFocus()

                logs.clear()

                viewModel.newOperation {
                    logs.add(it)
                }
            })

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            value = support,
            onValueChange = { support = it },
            label = { Text(stringResource(id = R.string.nfc_num_support)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() })
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text(stringResource(id = R.string.nfc_birth_date)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() })
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            value = expirationDate,
            onValueChange = { expirationDate = it },
            label = { Text(stringResource(id = R.string.nfc_expiry_date)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() })
        )

        Spacer(Modifier.height(8.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BaseCheckView(
                modifier = Modifier.weight(1f),
                checkValue = showPreviousTip,
                text = stringResource(id = R.string.nfc_show_previous_tip)
            ) {
                showPreviousTip = it
            }
            BaseCheckView(
                modifier = Modifier.weight(1f),
                checkValue = showTutorial,
                text = stringResource(id = R.string.nfc_show_tutorial)
            ) {
                showTutorial = it
            }

        }

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BaseCheckView(
                checkValue = showDiagnostic,
                text = stringResource(id = R.string.nfc_show_diagnostic)
            ) {
                showDiagnostic = it
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp, bottom = 8.dp),
            text = stringResource(id = R.string.nfc_select_document),
            color = colorResource(id = R.color.sdkBodyTextColor)
        )

        DropdownDocumentMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
        ) {
            documentType = it
        }


        BaseButton(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            text = stringResource(id = R.string.nfc_complex_launch),
            onClick = {
                if (support.isNotEmpty() && birthDate.validNfcDate() && expirationDate.validNfcDate()) {
                    Napier.d(
                        "APP: LAUNCH NFC \n Support: $support \n birthDate: $birthDate \n" +
                                " expirationDate: $expirationDate"
                    )
                    nfcConfigurationData = SdkData.getNfcConfig(
                        support = support,
                        birthDate = birthDate,
                        expirationDate = expirationDate,
                        skipPACE = false,
                        docType = documentType,
                        showPreviousTip = showPreviousTip,
                        showTutorial = showTutorial,
                        showDiagnostic = showDiagnostic
                    )

                    logs.clear()

                    logs.add("Reading NFC...")

                    viewModel.launchNfc(
                        nfcConfigurationData!!,
                        debugLogs = {
                            logs.add(it)
                        })


                } else {
                    Napier.d("APP: NFC INVALID DATA")
                    logs.add("NFC: INVALID DATA")
                }
            }
        )

        BaseButton(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(id = R.string.nfc_simple_launch),
            onClick = {
                if (support.isNotEmpty() && birthDate.validNfcDate() && expirationDate.validNfcDate()) {
                    Napier.d(
                        "APP: LAUNCH NFC \n Support: $support \n birthDate: $birthDate \n" +
                                " expirationDate: $expirationDate"
                    )
                    nfcConfigurationData = SdkData.getNfcConfig(
                        support = support,
                        birthDate = birthDate,
                        expirationDate = expirationDate,
                        skipPACE = true,
                        docType = documentType,
                        showPreviousTip = showPreviousTip,
                        showTutorial = showTutorial,
                        showDiagnostic = showDiagnostic
                    )
                    logs.clear()

                    logs.add("Reading NFC...")

                    viewModel.launchNfc(nfcConfigurationData!!,
                        debugLogs = {
                            logs.add(it)
                        })

                } else {
                    Napier.d("APP: NFC INVALID DATA")
                    logs.add("NFC: INVALID DATA")
                }
            }
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = BuildConfig.LIBRARY_VERSION,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            ),
            color = colorResource(id = R.color.sdkBodyTextColor)
        )

        if (!logs.isEmpty()) {
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            BaseTextButton(
                enabled = true,
                text = "Clear logs",
                onClick = {
                    logs.clear()
                })
        }

        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .clickable {
                    copyToClipboard(context, logs.joinToString(separator = "\n"))
                    onSendEmail(logs.joinToString(separator = "\n"))
                },
            text = logs.joinToString(separator = "\n"),
            color = colorResource(id = R.color.sdkBodyTextColor),
        )
    }

}
