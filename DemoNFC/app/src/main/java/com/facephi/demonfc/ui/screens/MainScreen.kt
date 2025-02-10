package com.facephi.demonfc.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.facephi.demonfc.BuildConfig
import com.facephi.demonfc.MainViewModel
import com.facephi.demonfc.R
import com.facephi.demonfc.model.DocumentType
import com.facephi.demonfc.ui.composables.BaseButton
import com.facephi.demonfc.ui.composables.BaseTextButton
import com.facephi.demonfc.ui.composables.DropdownDocumentMenuBox
import com.facephi.demonfc.ui.theme.DemoNFCTheme
import io.github.aakira.napier.Napier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onSendEmail: (String) -> Unit,
) {

    val context = LocalContext.current
    val logs = remember { mutableStateListOf<String>() }
    val focusManager = LocalFocusManager.current

    var documentType by rememberSaveable {
        mutableStateOf(DocumentType.ID_CARD)
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        BaseButton(modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.nfc_new_operation), onClick = {
                Napier.d("APP: LAUNCH NEW OPERATION")
                focusManager.clearFocus()

                logs.clear()

                viewModel.newOperation {
                    logs.add(it)
                }
            })

        Spacer(Modifier.height(8.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = showPreviousTip,
                onCheckedChange = {
                    showPreviousTip = it
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.sdkPrimaryColor),
                    uncheckedColor = colorResource(id = R.color.sdkPrimaryColor)
                )
            )
            Text(
                text = stringResource(id = R.string.nfc_show_previous_tip),
                color = colorResource(id = R.color.sdkBodyTextColor)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Checkbox(
                checked = showTutorial,
                onCheckedChange = {
                    showTutorial = it
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.sdkPrimaryColor),
                    uncheckedColor = colorResource(id = R.color.sdkPrimaryColor)
                )
            )
            Text(
                text = stringResource(id = R.string.nfc_show_tutorial),
                color = colorResource(id = R.color.sdkBodyTextColor)
            )

        }

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = showDiagnostic,
                onCheckedChange = {
                    showDiagnostic = it
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.sdkPrimaryColor),
                    uncheckedColor = colorResource(id = R.color.sdkPrimaryColor)
                )
            )
            Text(
                text = stringResource(id = R.string.nfc_show_diagnostic),
                color = colorResource(id = R.color.sdkBodyTextColor)
            )
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

        BaseButton(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            text = stringResource(id = R.string.nfc_complex_launch),
            image = R.drawable.ic_nfc_id_back_id,
            onClick = {
                logs.clear()

                viewModel.launchSelphidAndNfc(
                    skipPACE = false,
                    docType = documentType,
                    showDiagnostic = showDiagnostic,
                    showPreviousTip = showPreviousTip,
                    showTutorial = showTutorial
                    ){
                    logs.add(it)
                }
            }
        )

        BaseButton(modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(id = R.string.nfc_simple_launch),
            image = R.drawable.ic_nfc_id_back_id,
            onClick = {
                logs.clear()

                viewModel.launchSelphidAndNfc(skipPACE = true,
                    docType = documentType,
                    showDiagnostic = showDiagnostic,
                    showPreviousTip = showPreviousTip,
                    showTutorial = showTutorial
                    ){
                    logs.add(it)
                }
            }
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = BuildConfig.LIBRARY_VERSION,
            style =  TextStyle(
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

fun copyToClipboard(context: Context, text: String) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("logs", text)
    clipboardManager.setPrimaryClip(clip)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DemoNFCTheme {
        MainScreen(MainViewModel()){}
    }
}