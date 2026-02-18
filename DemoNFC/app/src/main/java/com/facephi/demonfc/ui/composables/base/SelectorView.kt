package com.facephi.demonfc.ui.composables.base


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.facephi.demonfc.R
import com.facephi.demonfc.model.DocumentType
import com.facephi.nfc_component.data.configuration.ReadingProgressStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownDocumentMenuBox(
    selected: DocumentType,
    onSelected: (DocumentType) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = remember { DocumentType.entries.toList() } // Kotlin 1.9+ (si no, usa values().toList())
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected.displayName(),
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text(stringResource(id = R.string.nfc_select_document)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.displayName()) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DocumentType.displayName(): String = when (this) {
    DocumentType.ID_CARD -> stringResource(id = R.string.nfc_id_card)
    DocumentType.PASSPORT -> stringResource(id = R.string.nfc_passport)
    DocumentType.FOREIGN_CARD -> stringResource(id = R.string.nfc_foreign_card)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownReadingProgress(
    selected: ReadingProgressStyle,
    onSelected: (ReadingProgressStyle) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = remember { ReadingProgressStyle.entries.toList() } // Kotlin 1.9+ (si no, usa values().toList())
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected.displayName(),
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text(stringResource(id = R.string.nfc_select_progress)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.displayName()) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ReadingProgressStyle.displayName(): String = when (this) {
    ReadingProgressStyle.DOTS -> stringResource(id = R.string.nfc_dots)
    ReadingProgressStyle.PERCENTAGE -> stringResource(id = R.string.nfc_percentage)
}
