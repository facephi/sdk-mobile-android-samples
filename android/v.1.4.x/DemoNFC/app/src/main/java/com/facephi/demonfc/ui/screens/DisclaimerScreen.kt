package com.facephi.demonfc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.facephi.demonfc.R
import com.facephi.demonfc.ui.composables.AlertDialog
import com.facephi.demonfc.ui.composables.BaseButton
import com.facephi.demonfc.ui.composables.HtmlTextView
import com.facephi.demonfc.ui.composables.OutlinedButton
import com.facephi.demonfc.ui.theme.DemoNFCTheme
import com.facephi.sdk_composables.dialogs.AlertDialogData
import io.ktor.util.InternalAPI
import io.ktor.util.toUpperCasePreservingASCIIRules

@OptIn(InternalAPI::class)
@Composable
fun DisclaimerScreen(
    onCancel: () -> Unit,
    onAgree: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var enableButton by rememberSaveable {
        mutableStateOf(false)
    }

    var showAlert by rememberSaveable {
        mutableStateOf(false)
    }

    if (scrollState.value == scrollState.maxValue) {
        enableButton = true
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
            .background(color = Color.White)
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp, bottom = 8.dp),
            text = stringResource(id = com.facephi.demonfc.R.string.nfc_rgpd_title),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .weight(1f)
                .padding(8.dp)
                .background(
                    Color.White
                )
                .verticalScroll(
                    scrollState
                ),
        ) {

            HtmlTextView(
                modifier = Modifier.padding(8.dp),
                html = stringResource(id = R.string.nfc_rgpd_text)
            )

        }

        BaseButton(
            text = stringResource(id = com.facephi.demonfc.R.string.nfc_agree),
            onClick = onAgree,
            enabled = enableButton,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        )

        OutlinedButton(
            text = stringResource(id = com.facephi.demonfc.R.string.nfc_cancel),
            onClick = {
                showAlert = true
            },
            enabled = true,
            textColor = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
        )

        AlertDialog(
            show = showAlert,
            dialogData = AlertDialogData(
                stringResource(id = R.string.nfc_close_session_title),
                stringResource(id = R.string.nfc_close_session_desc),
                stringResource(id = R.string.nfc_close_session).toUpperCasePreservingASCIIRules(),
                stringResource(id = R.string.nfc_cancel).toUpperCasePreservingASCIIRules(),
            ),
            onDismiss = {
                showAlert = false
            },
            onConfirm = {
                showAlert = false
                onCancel.invoke()
            })

    }

}

@Preview(showBackground = true)
@Composable
fun DisclaimerScreenPreview() {
    DemoNFCTheme {
        DisclaimerScreen({}, {})
    }
}