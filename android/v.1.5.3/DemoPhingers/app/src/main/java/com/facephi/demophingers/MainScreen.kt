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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.demophingers.ui.composables.BaseButton
import com.facephi.demophingers.ui.composables.BaseTextButton
import com.facephi.demophingers.ui.composables.DropdownCaptureOrientationMenu
import com.facephi.phingers_component.PhingersController
import com.facephi.phingers_component.data.configuration.CaptureOrientation
import com.facephi.phingers_component.data.configuration.PhingersConfigurationData
import com.facephi.sdk.SDKController
import com.facephi.tracking_component.TrackingController
import com.facephi.tracking_component.TrackingErrorController
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    sdkApplication: SdkApplication,
    modifier: Modifier = Modifier
) {

    val logs = remember { mutableStateListOf<String>() }

    var showTutorial by rememberSaveable {
        mutableStateOf(true)
    }

    var captureOrientation by rememberSaveable {
        mutableStateOf(CaptureOrientation.LEFT)
    }

    LaunchedEffect(Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            SDKController.enableDebugMode()

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
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(16.dp)
                .height(75.dp)
        )

        BaseButton(modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.phingers_demo_new_operation), onClick = {
            Napier.d("APP: LAUNCH NEW OPERATION")

            logs.clear()

            SDKController.newOperation(
                operationType = SdkData.OPERATION_TYPE,
                customerId = SdkData.CUSTOMER_ID,
            ) {
                when (it) {
                    is SdkResult.Success -> {
                        Napier.d("APP: NEW OPERATION OK")
                        logs.add("NEW OPERATION: OK")
                    }

                    is SdkResult.Error -> {
                        Napier.d("APP: NEW OPERATION ERROR: ${it.error}")
                        logs.add("NEW OPERATION: KO - ${it.error}")
                    }
                }
            }
        })

        BaseButton(
            text = stringResource(id = R.string.phingers_demo_launch_capture),
            onClick = {
                val data = PhingersConfigurationData(
                    showTutorial = showTutorial,
                    reticleOrientation = captureOrientation
                )

                SDKController.launch(
                    PhingersController(data) {
                        when (it) {
                            is SdkResult.Success -> {
                                Napier.d("APP: CAPTURE FINISH OK")
                                logs.add("CAPTURE FINISH OK")
                            }

                            is SdkResult.Error -> {
                                Napier.d("APP: CAPTURE ERROR - ${it.error.name}")
                                logs.add("CAPTURE ERROR: ${it.error.name}")
                            }
                        }
                    }
                )
            }
        )


        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.phingers_demo_capture_orientation)
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
                text = stringResource(id = R.string.phingers_demo_show_tutorial)
            )
        }

        if (!logs.isEmpty()) {
            Divider(color = Color.LightGray, thickness = 1.dp)
            BaseTextButton(
                enabled = true,
                text = stringResource(id = R.string.phingers_demo_clear_logs),
                onClick = {
                    logs.clear()
                })
        }


        Text(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            text = logs.joinToString(separator = "\n"),
            color = colorResource(id = R.color.sdkBodyTextColor),
        )
    }

}
