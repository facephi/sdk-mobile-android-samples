package com.facephi.demonfc.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.facephi.demonfc.R
import com.facephi.demonfc.model.DocumentType
import com.facephi.demonfc.model.ShowScreen
import com.facephi.demonfc.ui.theme.DemoNFCTheme


@ExperimentalMaterial3Api
@Composable
fun DropdownScreenMenuBox(modifier : Modifier = Modifier, newSelection: (ShowScreen)-> Unit) {
    val list = listOf(ShowScreen.SHOW_TUTORIAL, ShowScreen.SHOW_DIAGNOSTIC, ShowScreen.SHOW_TUTORIAL_AND_DIAGNOSTIC)

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(ShowScreen.SHOW_TUTORIAL_AND_DIAGNOSTIC) }

    Box(modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedText.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()  // Asegura que el menú se ancle al campo
                    .clickable { expanded = true }  // Abre el menú al hacer clic
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(color = colorResource(id = R.color.sdkBackgroundColor))
            ) {
                list.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name, color = colorResource(id = R.color.sdkBodyTextColor)) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            newSelection(item)
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun DropdownDocumentMenuBox(modifier : Modifier = Modifier, newSelection: (DocumentType)-> Unit) {
    val list = listOf(DocumentType.ID_CARD, DocumentType.PASSPORT, DocumentType.FOREIGN_CARD)

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(DocumentType.ID_CARD) }

    Box(modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedText.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()  // Asegura que el menú se ancle al campo
                    .clickable { expanded = true }  // Abre el menú al hacer clic
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(color = colorResource(id = R.color.sdkBackgroundColor))
            ) {
                list.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name, color = colorResource(id = R.color.sdkBodyTextColor)) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            newSelection(item)
                        }
                    )
                }
            }
        }
    }
}


@ExperimentalMaterial3Api
@Preview
@Composable
fun NewDropdownMenuBoxPreview() {
    DemoNFCTheme {
        DropdownScreenMenuBox(){}
    }
}
