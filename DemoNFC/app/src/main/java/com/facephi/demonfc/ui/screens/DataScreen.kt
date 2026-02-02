package com.facephi.demonfc.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.facephi.demonfc.BuildConfig
import com.facephi.demonfc.Images
import com.facephi.demonfc.MainViewModel
import com.facephi.demonfc.R
import com.facephi.demonfc.ui.composables.base.BaseButton
import com.facephi.demonfc.ui.composables.base.BaseTextButton
import com.facephi.demonfc.ui.composables.NfcComponentCard
import com.facephi.demonfc.ui.composables.result.PersonalInfoCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DataScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onSendEmail: (String) -> Unit,
) {
    val context = LocalContext.current
    val logs = remember { mutableStateListOf<String>() }

    val personalData by viewModel.personalData.collectAsState()
    val nfcResult by viewModel.nfcResult.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        BaseButton(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            text = stringResource(id = R.string.nfc_new_operation), onClick = {
                Log.i("APP", "LAUNCH NEW OPERATION")
                logs.clear()

                viewModel.newOperation {
                    logs.add(it)
                }
            })

        NfcComponentCard(
            title = stringResource(id = R.string.onboarding_title_nfc),
            enabled = true,
            resultValue = nfcResult,
            onLaunch = { nfcConfigurationData ->
                viewModel.clearData()

                logs.add("Reading NFC...")

                viewModel.launchNfc(
                    nfcConfigurationData,
                    debugLogs = {
                        logs.add(it)
                    })


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

        Spacer(Modifier.height(16.dp))

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
