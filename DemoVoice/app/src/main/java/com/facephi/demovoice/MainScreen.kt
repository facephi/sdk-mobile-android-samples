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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.facephi.core.data.SdkApplication
import com.facephi.demovoice.media.AppMediaPlayer
import com.facephi.demovoice.media.AudioFileManager
import com.facephi.demovoice.ui.composables.BaseButton
import com.facephi.demovoice.ui.composables.BaseTextButton
import io.github.aakira.napier.Napier


@Composable
fun MainScreen(
    sdkApplication: SdkApplication,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val logs = viewModel.logs.collectAsState()
    val audios = remember { mutableStateListOf<String>() }
    val context = LocalContext.current
    var showScreen by rememberSaveable { mutableStateOf(true) }
    var newOperationClicked by rememberSaveable { mutableStateOf(false) }

    var isPlaying by rememberSaveable {
        mutableStateOf(false)
    }

    var audioIndex by rememberSaveable {
        mutableIntStateOf(0)
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
            text = stringResource(id = R.string.new_operation),
            onClick = {
                newOperationClicked = true
                viewModel.newOperation()
            })

        BaseButton(
            text = stringResource(id = R.string.voice_enroll),
            enabled = newOperationClicked,
            onClick = {
                Napier.d("APP: LAUNCH VOICE ENROLL")

                viewModel.launchVoiceEnroll(showScreen){ audioArray ->
                    audios.clear()
                    if (audioArray.isNotEmpty()){
                        audioArray.forEachIndexed { index, element ->
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
            }
        )

        BaseButton(
            text = stringResource(id = R.string.voice_auth),
            enabled = newOperationClicked,
            onClick = {
                Napier.d("APP: LAUNCH VOICE AUTH")
                viewModel.launchVoiceAuth(showScreen)
            }
        )

        BaseButton(
            enabled = newOperationClicked,
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
                    Napier.d("APP: Voice result is empty")
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
