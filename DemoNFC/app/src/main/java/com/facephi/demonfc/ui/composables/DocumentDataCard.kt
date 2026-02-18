package com.facephi.demonfc.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.facephi.demonfc.R
import com.facephi.demonfc.model.DocumentType
import com.facephi.demonfc.ui.composables.base.DropdownDocumentMenuBox
import com.facephi.demonfc.utils.validNfcDate

@Composable
fun DocumentDataCard(
    support: String,
    onSupportChange: (String) -> Unit,
    birthDate: String,
    onBirthDateChange: (String) -> Unit,
    expirationDate: String,
    onExpirationDateChange: (String) -> Unit,
    documentType: DocumentType,
    onDocumentTypeChange: (DocumentType) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    // Para no mostrar errores al arrancar: solo después de que el usuario toque el campo
    var supportTouched by rememberSaveable { mutableStateOf(false) }
    var birthTouched by rememberSaveable { mutableStateOf(false) }
    var expiryTouched by rememberSaveable { mutableStateOf(false) }

    val supportValid = support.isNotEmpty()
    val birthValid = birthDate.validNfcDate()
    val expiryValid = expirationDate.validNfcDate()

    val supportError = supportTouched && !supportValid
    val birthError = birthTouched && !birthValid
    val expiryError = expiryTouched && !expiryValid


    Column(
        modifier = Modifier,
    ) {
        Text(
            text = stringResource(id = R.string.nfc_data_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.size(4.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = support,
            onValueChange = {
                if (!supportTouched) supportTouched = true
                onSupportChange(it)
            },
            label = { Text(stringResource(id = R.string.nfc_num_support)) },
            singleLine = true,
            isError = supportError,
            supportingText = {
                if (supportError) {
                    Text(stringResource(id = R.string.nfc_data_mandatory))
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(Modifier.size(4.dp))

        val birthTfvSaver = TextFieldValue.Saver

        var birthTfv by rememberSaveable(stateSaver = birthTfvSaver) {
            mutableStateOf(TextFieldValue(birthDate, TextRange(birthDate.length)))
        }

        LaunchedEffect(birthDate) {
            if (birthDate != birthTfv.text) {
                birthTfv = TextFieldValue(birthDate, TextRange(birthDate.length))
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = birthTfv,
            onValueChange = { incoming ->
                if (!birthTouched) birthTouched = true

                val digits = incoming.text.filter(Char::isDigit).take(8)
                val formatted = digits.formatAsDate()

                val newValue = TextFieldValue(
                    text = formatted,
                    selection = TextRange(formatted.length) // cursor al final
                )
                birthTfv = newValue
                onBirthDateChange(formatted) // <- String con “/”
            },
            label = { Text(stringResource(id = R.string.nfc_birth_date)) },
            singleLine = true,
            isError = birthError,
            supportingText = { if (birthError) Text(stringResource(id = R.string.nfc_data_date_error)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.size(4.dp))

        var expiryTfv by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(expirationDate, TextRange(expirationDate.length)))
        }

        LaunchedEffect(expirationDate) {
            if (expirationDate != expiryTfv.text) {
                expiryTfv = TextFieldValue(expirationDate, TextRange(expirationDate.length))
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = expiryTfv,
            onValueChange = { incoming ->
                if (!expiryTouched) expiryTouched = true

                val digits = incoming.text.filter(Char::isDigit).take(8)
                val formatted = digits.formatAsDate()

                val newValue = TextFieldValue(
                    text = formatted,
                    selection = TextRange(formatted.length) // cursor al final
                )
                expiryTfv = newValue
                onExpirationDateChange(formatted) // <- String con “/”
            },
            label = { Text(stringResource(id = R.string.nfc_expiry_date)) },
            singleLine = true,
            isError = expiryError,
            supportingText = { if (expiryError) Text(stringResource(id = R.string.nfc_data_date_error)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            )
        )

        Spacer(Modifier.size(4.dp))

        DropdownDocumentMenuBox(
            selected = documentType,
            onSelected = onDocumentTypeChange,
            modifier = Modifier.fillMaxWidth()
        )
    }

}

fun String.formatAsDate(): String {
    val s = this
    return when {
        s.length < 2 -> s
        s.length == 2 -> "${s}/"
        s.length < 4 -> "${s.take(2)}/${s.substring(2)}"
        s.length == 4 -> "${s.take(2)}/${s.substring(2)}/"
        s.length <= 8 -> "${s.take(2)}/${s.substring(2, 4)}/${s.substring(4)}"
        else -> s
    }
}


