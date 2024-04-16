package com.facephi.demonfc.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Divider
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
import com.facephi.demonfc.MainViewModel
import com.facephi.demonfc.R
import com.facephi.demonfc.model.DocumentType
import com.facephi.demonfc.ui.composables.BaseButton
import com.facephi.demonfc.ui.composables.BaseTextButton
import com.facephi.demonfc.ui.composables.DropdownDocumentMenuBox
import com.facephi.demonfc.ui.theme.DemoNFCTheme
import io.github.aakira.napier.Napier


@OptIn(ExperimentalMaterialApi::class)
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

                viewModel.launchSelphidAndNfc(false, documentType){
                    logs.add(it)
                }
            }
        )

        BaseButton(modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(id = R.string.nfc_simple_launch),
            image = R.drawable.ic_nfc_id_back_id,
            onClick = {
                logs.clear()

                viewModel.launchSelphidAndNfc(true, documentType){
                    logs.add(it)
                }
            }
        )

        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp),
            text = "Version 2.0.0",
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