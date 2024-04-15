package com.facephi.demophingers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.facephi.core.data.SdkApplication
import com.facephi.demophingers.ui.composables.BaseButton
import com.facephi.demophingers.ui.composables.BaseTextButton
import com.facephi.demophingers.ui.composables.DropdownCaptureOrientationMenu
import com.facephi.phingers_component.data.configuration.CaptureOrientation

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    sdkApplication: SdkApplication,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {

    val logs = viewModel.logs.collectAsState()
    var newOperationClicked by rememberSaveable { mutableStateOf(false) }

    var showTutorial by rememberSaveable {
        mutableStateOf(true)
    }

    var captureOrientation by rememberSaveable {
        mutableStateOf(CaptureOrientation.LEFT)
    }

    LaunchedEffect(Unit) {
        viewModel.initSdk(sdkApplication)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_demo_logo),
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(16.dp)
                .height(75.dp)
        )

        BaseButton(modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.phingers_demo_new_operation),
            onClick = {
                newOperationClicked = true
                viewModel.newOperation()
            })


        BaseButton(
            text = stringResource(id = R.string.phingers_demo_launch_capture),
            enabled = newOperationClicked,
            onClick = {
                viewModel.launchPhingers(showTutorial, captureOrientation)
            }
        )

        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.phingers_demo_capture_orientation),
            color = colorResource(id = R.color.sdkBodyTextColor)
        )

        DropdownCaptureOrientationMenu(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
        ) {
            captureOrientation = it
        }

        Row() {
            Checkbox(
                checked = showTutorial,
                onCheckedChange = {
                    showTutorial = it
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.sdkPrimaryColor),
                    uncheckedColor = colorResource(id = R.color.sdkPrimaryColor)
                )
            )
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.phingers_demo_show_tutorial),
                color = colorResource(id = R.color.sdkBodyTextColor)
            )
        }

        Text(
            modifier = Modifier.padding(16.dp),
            text = "Version 2.0.0",
            color = colorResource(id = R.color.sdkBodyTextColor)
        )

        if (logs.value.isNotEmpty()) {
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            BaseTextButton(
                enabled = true,
                text = "Clear logs",
                onClick = {
                    viewModel.clearLogs()
                })

            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp),
                text = logs.value,
                color = colorResource(id = R.color.sdkBodyTextColor),
            )
        }
    }

}
