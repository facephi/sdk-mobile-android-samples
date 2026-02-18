package com.facephi.demonfc.ui.composables.disclaimer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.facephi.demonfc.model.AlertDialogData
import com.facephi.demonfc.ui.composables.base.BaseTextButton


@Composable
fun AlertDialog(
    show: Boolean,
    dialogData: AlertDialogData,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            title = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = dialogData.title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

            },
            text = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = dialogData.description,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                )
            },
            confirmButton = {
                BaseTextButton(
                    enabled = true,
                    text = dialogData.confirmText,
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = onConfirm
                )
            },
            dismissButton = {
                BaseTextButton(
                    enabled = true,
                    text = dialogData.dismissText,
                    modifier = Modifier,
                    onClick = onDismiss
                )
            },
            modifier = Modifier.testTag("dialog")
        )
    }
}