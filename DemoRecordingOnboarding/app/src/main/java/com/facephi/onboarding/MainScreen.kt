package com.facephi.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.facephi.onboarding.ui.composables.BaseCheckView
import com.facephi.onboarding.ui.composables.BaseComponentCard
import com.facephi.onboarding.ui.composables.BaseTextButton
import com.facephi.onboarding.ui.composables.ButtonCard
import com.facephi.onboarding.ui.composables.StartAndStopCard
import com.facephi.onboarding.ui.data.UIComponentResult

@Composable
fun MainScreen(
    sdkApplication: SdkApplication,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {

    val logs = viewModel.logs.collectAsState()
    var newOperationClicked by rememberSaveable { mutableStateOf(false) }

    var selphiResult by rememberSaveable { mutableStateOf(UIComponentResult.PENDING) }
    var selphIdResult by rememberSaveable { mutableStateOf(UIComponentResult.PENDING) }

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
                .height(75.dp)
        )

        ButtonCard(
            title = stringResource(id = R.string.onboarding_title_operation),
            desc = stringResource(id = R.string.onboarding_desc_operation),
            enabled = true,
            buttonText = stringResource(id = R.string.onboarding_init_operation),
            onClick = {
                viewModel.newOperation {
                    newOperationClicked = true
                }
            },
        )

        Spacer(Modifier.height(8.dp))

        // Video Recording
        StartAndStopCard(
            title = stringResource(id = R.string.onboarding_title_videorecording),
            desc = stringResource(id = R.string.onboarding_desc_videorecording),
            enabled = newOperationClicked,
            startButtonText = stringResource(id = R.string.onboarding_launch_videorecording),
            stopButtonText = stringResource(id = R.string.onboarding_launch_stop_videorecording),
            onStart = {
                viewModel.launchVideoRecording()
            },
            onStop = {
                viewModel.stopVideoRecording()
            },
        )

        Spacer(Modifier.height(8.dp))

        BaseComponentCard(
            title = stringResource(id = R.string.onboarding_title_selphi),
            desc = stringResource(id = R.string.onboarding_desc_selphi),
            buttonText = stringResource(id = R.string.onboarding_launch_selphi),
            enabled = newOperationClicked,
            resultValue = selphiResult,
            onLaunch = { showPreviousTip, showTutorial, showDiagnostic ->
                viewModel.launchSelphi(
                    showTutorial = showTutorial,
                    showPreviousTip = showPreviousTip,
                    showDiagnostic = showDiagnostic
                ){
                    selphiResult = it
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        BaseComponentCard(
            buttonText = stringResource(id = R.string.onboarding_launch_selphid),
            title = stringResource(id = R.string.onboarding_title_selphid),
            desc = stringResource(id = R.string.onboarding_desc_selphid),
            enabled = newOperationClicked,
            resultValue = selphIdResult,
            onLaunch = { showPreviousTip, showTutorial, showDiagnostic ->
                viewModel.launchSelphId(
                    showTutorial = showTutorial,
                    showPreviousTip = showPreviousTip,
                    showDiagnostic = showDiagnostic
                ){
                    selphIdResult = it
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = BuildConfig.LIBRARY_VERSION,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            )
        )

        if (logs.value.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            BaseTextButton(
                enabled = true,
                text = "Clear logs",
                onClick = {
                    viewModel.clearLogs()
                    selphiResult = UIComponentResult.PENDING
                    selphIdResult = UIComponentResult.PENDING
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

        ImageData.documentFace?.let {
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .height(150.dp),
                bitmap = it.asImageBitmap(),
                contentDescription = "Document face Face",
                contentScale = ContentScale.Fit,
            )
        }

        ImageData.documentFront?.let {
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .height(150.dp),
                bitmap = it.asImageBitmap(),
                contentDescription = "Document front",
                contentScale = ContentScale.Fit,
            )
        }

        ImageData.documentBack?.let {
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .height(150.dp),
                bitmap = it.asImageBitmap(),
                contentDescription = "Document back",
                contentScale = ContentScale.Fit,
            )
        }
    }

}
