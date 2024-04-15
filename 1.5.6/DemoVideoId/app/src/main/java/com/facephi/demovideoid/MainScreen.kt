package com.facephi.demovideoid

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.demovideoid.ui.composables.BaseButton
import com.facephi.demovideoid.ui.composables.BaseTextButton
import com.facephi.sdk.SDKController
import com.facephi.sdk_composables.theme.Theme
import com.facephi.tracking_component.TrackingController
import com.facephi.tracking_component.TrackingErrorController
import com.facephi.video_id_component.VideoIdController
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    sdkApplication: SdkApplication,
    modifier: Modifier = Modifier
) {

    val logs = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            if (SdkData.LICENSE_ONLINE) {
                SDKController.initSdk(
                    sdkApplication = sdkApplication,
                    environmentLicensingData = SdkData.environmentLicensingData,
                    trackingController = TrackingController()
                ) { result ->
                    when (result) {
                        is SdkResult.Success -> {
                            Napier.d("APP: INIT SDK: OK")
                            logs.add("INIT SDK OK")
                        }

                        is SdkResult.Error -> {
                            Napier.d("APP: INIT SDK: KO - ${result.error}")
                            logs.add("INIT SDK ERROR: ${result.error}")
                        }
                    }
                }

            } else {
                SDKController.initSdk(
                    sdkApplication = sdkApplication,
                    license = SdkData.LICENSE,
                    trackingController = TrackingController()
                ) { result ->
                    when (result) {
                        is SdkResult.Success -> {
                            Napier.d("APP: INIT SDK: OK")
                            logs.add("INIT SDK OK")
                        }

                        is SdkResult.Error -> {
                            Napier.d("APP: INIT SDK ERROR - ${result.error}")
                            logs.add("INIT SDK ERROR: ${result.error}")
                        }
                    }
                }
            }

            SDKController.launch(TrackingErrorController {
                logs.add("Tracking Error: ${it.name}")
            })

            SDKController.enableDebugMode()
        }

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
            text = stringResource(id = R.string.demo_new_operation),
            onClick = {
                SDKController.newOperation(
                    operationType = SdkData.OPERATION_TYPE,
                    customerId = SdkData.CUSTOMER_ID,
                ) {
                    when (it) {
                        is SdkResult.Error -> {
                            Napier.d("APP: NEW OPERATION ERROR ${it.error}")
                            logs.add("NEW OPERATION ERROR ${it.error}")
                        }

                        is SdkResult.Success -> {
                            Napier.d("APP: NEW OPERATION OK")
                            logs.add("NEW OPERATION OK")
                        }
                    }
                }

            })

        BaseButton(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            text = stringResource(id = R.string.demo_launch_video),
            onClick = {
                SDKController.launch(
                    VideoIdController(SdkData.videoIdConfiguration) {
                        when (it) {
                            is SdkResult.Success -> {
                                Napier.d("APP: VIDEO OK")
                                logs.add("VIDEO OK")

                            }

                            is SdkResult.Error -> {
                                logs.add("VIDEO ERROR ${it.error}")
                                Napier.d("APP: VIDEO ERROR: ${it.error.name}")
                            }
                        }
                    }
                )

            })

        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp),
            text = "Version 1.5.6",
            style =  TextStyle(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            )
        )

        if (!logs.isEmpty()) {
            Divider(color = Color.LightGray, thickness = 1.dp)
            BaseTextButton(
                enabled = true,
                text = "Clear logs",
                onClick = {
                    logs.clear()
                })
        }

        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp),
            text = logs.joinToString(separator = "\n"),
            color = colorResource(id = R.color.sdkBodyTextColor),
        )
    }

}
