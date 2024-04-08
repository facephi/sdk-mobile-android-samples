package com.facephi.demonfc.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.facephi.demonfc.MainViewModel
import com.facephi.demonfc.R


@Composable
fun TabScreen(viewModel: MainViewModel,
              modifier: Modifier = Modifier,
              onSendEmail: (String) -> Unit
) {
    var tabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf(
        stringResource(id = R.string.nfc_read_with_capture),
        stringResource(id = R.string.nfc_read_with_data)
        )

    Column(modifier = modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = tabIndex,
            backgroundColor = colorResource(
                id = R.color.sdkPrimaryColor
            ).copy(alpha = 0.1f),
            contentColor = colorResource(
                id = R.color.sdkPrimaryColor
            )
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> MainScreen(viewModel = viewModel, onSendEmail = onSendEmail)
            1 -> DataScreen(viewModel = viewModel, onSendEmail = onSendEmail)
        }
    }
}