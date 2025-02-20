package com.facephi.demophingers.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.facephi.demophingers.R
import com.facephi.demophingers.ui.theme.DemoPhingersTheme
import com.facephi.phingers_component.data.configuration.CaptureOrientation
import com.facephi.phingers_component.data.configuration.FingerFilter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownCaptureOrientationMenu(
    modifier: Modifier = Modifier,
    newSelection: (CaptureOrientation) -> Unit
) {
    val list = CaptureOrientation.entries

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(CaptureOrientation.LEFT) }

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
                        text = {
                            Text(
                                text = item.name,
                                color = colorResource(id = R.color.sdkBodyTextColor)
                            )
                        },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownFingerFilterMenu(
    modifier: Modifier = Modifier,
    newSelection: (FingerFilter) -> Unit
) {
    val list = FingerFilter.entries

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(FingerFilter.SLAP) }

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
                        text = {
                            Text(
                                text = item.name,
                                color = colorResource(id = R.color.sdkBodyTextColor)
                            )
                        },
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


@Preview
@Composable
fun NewDropdownMenuBoxPreview() {
    DemoPhingersTheme {
        DropdownCaptureOrientationMenu() {}
    }
}
