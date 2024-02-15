package com.facephi.demovoice

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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.demovoice.media.AppMediaPlayer
import com.facephi.demovoice.media.AudioFileManager
import com.facephi.demovoice.ui.composables.BaseButton
import com.facephi.demovoice.ui.composables.BaseTextButton
import com.facephi.demovoice.ui.theme.DemovoiceTheme
import com.facephi.sdk.SDKController
import com.facephi.tracking_component.TrackingController
import com.facephi.tracking_component.TrackingErrorController
import com.facephi.voice_component.VoiceController
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MainScreen(sdkApplication: SdkApplication, modifier: Modifier = Modifier) {
    val logs = remember { mutableStateListOf<String>() }
    val audios = remember { mutableStateListOf<String>() }
    val context = LocalContext.current
    var showScreen by rememberSaveable { mutableStateOf(true) }

    var isPlaying by rememberSaveable {
        mutableStateOf(false)
    }

    var audioIndex by rememberSaveable {
        mutableIntStateOf(0)
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

        BaseButton(text = stringResource(id = R.string.new_operation), onClick = {
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
                        logs.add("NEW OPERATION: ERROR - ${it.error}")
                    }
                }
            }
        })


        BaseButton(
            text = stringResource(id = R.string.voice_enroll),
            onClick = {
                Napier.d("APP: LAUNCH VOICE")
                val data = SdkData.voiceConfigurationData
                data.showTutorial = showScreen

                SDKController.launch(
                    VoiceController(data) {
                        when (it) {
                            is SdkResult.Success -> {
                                logs.add("VOICE: Finish OK. ${it.data.audios.size} Audios")
                                if (it.data.audios.isNotEmpty()) {
                                    Napier.d("First Audio Size: ${it.data.audios[0].size}")
                                    VerificationManager.voiceEnroll(it.data.tokenizedAudios) { template ->
                                        // SAVE template
                                    }
                                    audios.clear()
                                    it.data.audios.forEachIndexed { index, element ->
                                        Napier.d("APP SAVING AUDIO $index")
                                        AudioFileManager.saveWavToInternalStorage(
                                            context,
                                            element,
                                            "audio$index"
                                        )?.let { dir ->
                                            Napier.d("APP SAVE AUDIO $dir")
                                            audios.add(dir)
                                        }
                                    }
                                }
                            }

                            is SdkResult.Error -> logs.add("VOICE: Error - ${it.error}")
                        }
                    }
                )
            }
        )

        BaseButton(
            text = stringResource(id = R.string.voice_auth),
            onClick = {
                Napier.d("APP: LAUNCH VOICE AUTH")

                val data = SdkData.voiceAuthConfigurationData

                SDKController.launch(
                    VoiceController(data) {
                        when (it) {
                            is SdkResult.Success -> {
                                logs.add("APP: VOICE: OK. Audios: ${it.data.audios.size}")
                                if (it.data.audios.isNotEmpty()) {
                                    logs.add("APP: VOICE: OK. Audio 0 Size: ${it.data.audios[0].size}")
                                }

                                /* With template

                                VerificationManager.voiceAuthentication(
                                    it.data.tokenizedAudios.first(),
                                    template
                                ) { result ->
                                    logs.add("APP: VOICE: AUTH: $result")
                                }
                               */
                            }
                            is SdkResult.Error -> logs.add("APP: VOICE: Error - ${it.error}")
                        }
                    }
                )
            }
        )

        BaseButton(
            text = stringResource(id = R.string.voice_play),
            onClick = {
                if (!audios.isEmpty()) {
                    audioIndex = 1
                    // AppMediaPlayer.stop()
                    AppMediaPlayer.init(
                        audios = audios.toList(),
                        indexOutput = {
                            audioIndex = it + 1
                        },
                        onStop = {
                            isPlaying = false
                        }
                    )
                    isPlaying = true
                    AppMediaPlayer.playAudios()
                } else {
                    logs.add("Voice result is empty")
                }
            }
        )

        if (isPlaying) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .weight(1f),
                    text = "AUDIO $audioIndex",
                    color = colorResource(
                        id = R.color.sdkPrimaryColor
                    ),
                    textAlign = TextAlign.Center,
                )

                BaseButton(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .weight(1f),
                    text = "STOP",
                    onClick = {
                        AppMediaPlayer.stop()
                        isPlaying = false
                    }
                )
            }
        }

        Row() {
            Checkbox(
                checked = showScreen,
                onCheckedChange = { showScreen = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.sdkPrimaryColor),
                    uncheckedColor = colorResource(id = R.color.sdkPrimaryColor)
                )
            )
            Text(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = stringResource(id = R.string.voice_tutorial),
            )
        }

        if (!logs.isEmpty()) {
            Divider(color = Color.LightGray, thickness = 1.dp)
            BaseTextButton(
                enabled = true,
                text = stringResource(id = R.string.clear_logs),
                onClick = {
                    logs.clear()
                })
        }

        Text(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            text = logs.joinToString(separator = "\n")
        )

    }
}
