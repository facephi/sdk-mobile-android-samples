package com.facephi.demovideocall

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.facephi.core.data.SdkApplication
import com.facephi.demovideocall.ui.composables.ButtonCard
import com.facephi.demovideocall.ui.composables.StartAndStopCard
import com.facephi.demovideocall.ui.data.UIComponentResult
import com.facephi.demovideoid.ui.composables.BaseButton
import com.facephi.demovideoid.ui.composables.BaseTextButton

@Composable
fun MainScreen(
    sdkApplication: SdkApplication,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {

    val logs = viewModel.logs.collectAsState()
    var newOperationClicked by rememberSaveable { mutableStateOf(false) }
    var videoResult by rememberSaveable { mutableStateOf(UIComponentResult.PENDING) }

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

        ButtonCard(
            title = stringResource(id = R.string.demo_title_operation),
            desc = stringResource(id = R.string.demo_desc_operation),
            enabled = true,
            buttonText = stringResource(id = R.string.demo_init_operation),
            onClick = {
                viewModel.newOperation {
                    newOperationClicked = true
                }
            },
        )

        Spacer(Modifier.height(8.dp))

        StartAndStopCard(
            title = stringResource(id = R.string.demo_title_video),
            desc = stringResource(id = R.string.demo_desc_video),
            enabled = newOperationClicked,
            startButtonText = stringResource(id = R.string.demo_launch_video),
            stopButtonText = stringResource(id = R.string.demo_stop_video),
            resultValue = videoResult,
            onStart = {
                viewModel.launchVideoCall {
                    videoResult = it
                }
            },
            onStop = {
                viewModel.stopScreenSharing()
            },
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = BuildConfig.LIBRARY_VERSION,
            style =  TextStyle(
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
                    videoResult = UIComponentResult.PENDING
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
