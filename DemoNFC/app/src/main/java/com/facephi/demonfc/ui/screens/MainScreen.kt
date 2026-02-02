package com.facephi.demonfc.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.facephi.demonfc.BuildConfig
import com.facephi.demonfc.Images
import com.facephi.demonfc.MainViewModel
import com.facephi.demonfc.R
import com.facephi.demonfc.model.DocumentType
import com.facephi.demonfc.ui.composables.SelphIDNfcComponentCard
import com.facephi.demonfc.ui.composables.base.BaseButton
import com.facephi.demonfc.ui.composables.base.BaseTextButton
import com.facephi.demonfc.ui.composables.result.PersonalInfoCard
import com.facephi.demonfc.ui.theme.DemoNFCTheme
import com.facephi.nfc_component.data.configuration.ReadingProgressStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onSendEmail: (String) -> Unit,
) {

    val context = LocalContext.current
    val logs = remember { mutableStateListOf<String>() }
    val nfcResult by viewModel.nfcResult.collectAsState()
    val personalData by viewModel.personalData.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        BaseButton(modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.nfc_new_operation), onClick = {
                Log.i ( "APP", "LAUNCH NEW OPERATION")
                logs.clear()
                viewModel.clearData()

                viewModel.newOperation {
                    logs.add(it)
                }
            })

        Spacer(modifier = Modifier.size(16.dp))

        SelphIDNfcComponentCard(
            enabled = true,
            title = "SELPHID + NFC",
            resultValue = nfcResult,
            onLaunch = { showPreviousTip: Boolean,
                         showTutorial: Boolean,
                         showDiagnostic: Boolean,
                         extractFace: Boolean,
                         extractSignature: Boolean,
                         documentType: DocumentType,
                         skipPace: Boolean,
                         readingProgressStyle : ReadingProgressStyle ->

                Images.clear()
                viewModel.clearData()

                viewModel.launchSelphidAndNfc(
                    skipPACE = skipPace,
                    docType = documentType,
                    extractFace = extractFace,
                    extractSignature = extractSignature,
                    showPreviousTip = showPreviousTip,
                    showDiagnostic = showDiagnostic,
                    showTutorial = showTutorial,
                    readingProgressStyle = readingProgressStyle
                ) {
                    logs.add(it)
                }
            }
        )

        Spacer(Modifier.size(8.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = BuildConfig.LIBRARY_VERSION,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            )
        )

        Spacer(Modifier.size(16.dp))

        personalData?.let {
            PersonalInfoCard(it)
        }


        if (!logs.isEmpty()) {
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            BaseTextButton(
                enabled = true,
                text = "Clear logs",
                onClick = {
                    logs.clear()
                    Images.clear()
                    viewModel.clearData()
                })
        }

        Images.faceImage?.bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Face",
                contentScale = ContentScale.Fit,
            )
        }

        Images.signatureImage?.bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Signature",
                contentScale = ContentScale.Fit,
            )
        }

        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .clickable {
                    copyToClipboard(context, logs.joinToString(separator = "\n"))
                    onSendEmail(logs.joinToString(separator = "\n"))
                },
            text = logs.joinToString(separator = "\n")
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
        MainScreen(MainViewModel()) {}
    }
}