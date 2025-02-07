package com.facephi.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.facephi.core.data.SdkApplication
import com.facephi.onboarding.ui.composables.BaseButton
import com.facephi.onboarding.ui.composables.BaseTextButton

@Composable
fun MainScreen(
    sdkApplication: SdkApplication,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    var showPreviousTip by rememberSaveable {
        mutableStateOf(true)
    }

    var showTutorial by rememberSaveable {
        mutableStateOf(true)
    }


    val logs = viewModel.logs.collectAsState()
    var newOperationClicked by rememberSaveable { mutableStateOf(false) }

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
            contentDescription = null,
            contentScale = ContentScale.None,
            modifier = Modifier
                .padding(8.dp)
                .height(75.dp)
        )

        BaseButton(modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.onboarding_new_operation),
            onClick = {
                newOperationClicked = true
                viewModel.newOperation()
            })

        Row() {
            Checkbox(
                checked = showPreviousTip,
                onCheckedChange = {
                    showPreviousTip = it
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.sdkPrimaryColor),
                    uncheckedColor = colorResource(id = R.color.sdkPrimaryColor)
                )
            )
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = stringResource(id = R.string.onboarding_show_previous_tip),
                color = colorResource(id = R.color.sdkBodyTextColor)
            )

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
                modifier = Modifier.padding(vertical = 16.dp),
                text = stringResource(id = R.string.onboarding_show_tutorial),
                color = colorResource(id = R.color.sdkBodyTextColor)
            )
        }

        BaseButton(modifier = Modifier,
            text = stringResource(id = R.string.onboarding_launch_selphi),
            enabled = newOperationClicked,
            onClick = {
               viewModel.launchSelphi(
                   showTutorial = showTutorial,
                   showPreviousTip = showPreviousTip
               )
            })


        BaseButton(modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.onboarding_launch_template),
            enabled = newOperationClicked,
            onClick = {
                ImageData.selphiBestImage?.let {
                    viewModel.generateTemplateRawFromBitmap(it)
                }
            })

        BaseButton(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            text = stringResource(id = R.string.onboarding_launch_verifications),
            enabled = newOperationClicked,
            onClick = {
                viewModel.launchVerifications(context)
            })

        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp),
            text = BuildConfig.LIBRARY_VERSION,
            style =  TextStyle(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            )
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

        ImageData.selphiBestImage?.let {
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .height(150.dp),
                bitmap = it.asImageBitmap(),
                contentDescription = "Selphi Face",
                contentScale = ContentScale.Fit,
            )
        }

    }

}
