package com.facephi.demonfc.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.colorResource
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
import com.facephi.demonfc.model.ShowScreen
import com.facephi.demonfc.ui.composables.BaseButton
import com.facephi.demonfc.ui.composables.BaseTextButton
import com.facephi.demonfc.ui.composables.DropdownScreenMenuBox
import com.facephi.demonfc.utils.validNfcDate
import com.facephi.nfc_component.data.configuration.NfcConfigurationData
import io.github.aakira.napier.Napier


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
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

    var showScreen by rememberSaveable {
        mutableStateOf(ShowScreen.SHOW_TUTORIAL_AND_DIAGNOSTIC)
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
                onDone = {keyboardController?.hide()})
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
                onDone = {keyboardController?.hide()})
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
                onDone = {keyboardController?.hide()})
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp, bottom = 8.dp),
            text = stringResource(id = R.string.nfc_show_screen),
            color = colorResource(id = R.color.sdkBodyTextColor)
        )

        DropdownScreenMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
        ) {
            showScreen = it
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
                        support, birthDate, expirationDate, showScreen, false
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
                        support, birthDate, expirationDate, showScreen, true
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
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp),
            text = "Version 1.5.3",
            style =  TextStyle(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            ),
            color = colorResource(id = R.color.sdkBodyTextColor)
        )

        if (!logs.isEmpty()) {
            Divider(color = Color.LightGray, thickness = 1.dp)
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
