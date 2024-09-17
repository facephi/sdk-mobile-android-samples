package com.facephi.demophingers.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownCaptureOrientationMenu(modifier : Modifier = Modifier, newSelection: (CaptureOrientation)-> Unit) {
    val list = listOf(CaptureOrientation.LEFT, CaptureOrientation.RIGHT, CaptureOrientation.THUMB_PORTRAIT)

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(CaptureOrientation.LEFT) }

    Box(modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxSize()
            )

            ExposedDropdownMenu(
                modifier = Modifier.background(color = colorResource(
                    id = R.color.sdkBackgroundColor
                )),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                list.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(text = item.name, color = colorResource(id = R.color.sdkBodyTextColor))
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
        DropdownCaptureOrientationMenu(){}
    }
}
