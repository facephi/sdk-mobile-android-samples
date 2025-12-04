package com.facephi.demovoice

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.facephi.core.data.SdkApplication
import com.facephi.demovoice.media.AudioFileManager
import com.facephi.demovoice.ui.composables.BaseComponentCard
import com.facephi.demovoice.ui.composables.BaseTextButton
import com.facephi.demovoice.ui.composables.ButtonCard
import com.facephi.demovoice.ui.composables.MediaCard
import com.facephi.demovoice.ui.data.UIComponentResult
import com.facephi.voice_component.data.configuration.VoiceConfigurationData


@Composable
fun MainScreen(
    sdkApplication: SdkApplication,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val logs = viewModel.logs.collectAsState()
    var newOperationClicked by rememberSaveable { mutableStateOf(false) }

    val audios = remember { mutableStateListOf<String>() }
    val context = LocalContext.current
    var enrollResult by rememberSaveable { mutableStateOf(UIComponentResult.PENDING) }
    var authResult by rememberSaveable { mutableStateOf(UIComponentResult.PENDING) }

    LaunchedEffect(Unit) {
        viewModel.initSdk(sdkApplication)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val enrollPhrases = arrayOf(
            stringResource(id = R.string.onboarding_phrase_1),
            stringResource(id = R.string.onboarding_phrase_2),
            stringResource(id = R.string.onboarding_phrase_2),
        )

        val authPhrases = arrayOf(
            stringResource(id = R.string.onboarding_phrase_1)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_demo_logo),
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(16.dp)
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

        BaseComponentCard(
            title = stringResource(id = R.string.onboarding_title_voice),
            desc = stringResource(id = R.string.onboarding_desc_voice),
            enabled = newOperationClicked,
            resultFirstValue = enrollResult,
            resultSecondValue = authResult,
            onLaunchFirst = { showPreviousTip, showTutorial, showDiagnostic ->
                audios.clear()
                viewModel.launchVoiceEnroll(
                    VoiceConfigurationData(
                        showTutorial = showTutorial,
                        showPreviousTip = showPreviousTip,
                        showDiagnostic = showDiagnostic,
                        phrases = enrollPhrases,
                    ),
                    output = { audioArray ->
                        audios.clear()
                        if (audioArray.isNotEmpty()) {
                            audioArray.forEachIndexed { index, element ->
                                Log.d("APP", "SAVING AUDIO $index")
                                AudioFileManager.saveWavToInternalStorage(
                                    context,
                                    element,
                                    "audio$index"
                                )?.let { dir ->
                                    Log.d("APP", "SAVE AUDIO $dir")
                                    audios.add(dir)
                                }
                            }
                        }


                    },
                    onResult = {
                        enrollResult = it

                    }
                )
            },
            onLaunchSecond = { showPreviousTip, showTutorial, showDiagnostic ->
                viewModel.launchVoiceAuth(
                    VoiceConfigurationData(
                        phrases = authPhrases,
                        showTutorial = showTutorial,
                        showPreviousTip = showPreviousTip,
                        showDiagnostic = showDiagnostic,
                    )
                ) {
                    authResult = it
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        MediaCard(
            title = stringResource(id = R.string.onboarding_title_media),
            desc = stringResource(id = R.string.onboarding_desc_media),
            enabled = (newOperationClicked && audios.isNotEmpty()),
            audios = audios
        )

        /*
        Spacer(Modifier.height(8.dp))
        BaseButton(
             text = stringResource(id = R.string.onboarding_verifications),
             enabled = newOperationClicked,
             onClick = {
                 Log.d("APP","LAUNCH VERIFICATIONS")
                 viewModel.launchVerifications(context)
             }
         )*/


        Text(
            modifier = Modifier.padding(16.dp),
            text = BuildConfig.LIBRARY_VERSION,
            color = colorResource(id = R.color.sdkBodyTextColor)
        )

        if (logs.value.isNotEmpty()) {
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            BaseTextButton(
                enabled = true,
                text = "Clear logs",
                onClick = {
                    viewModel.clearLogs()
                    enrollResult = UIComponentResult.PENDING
                    authResult = UIComponentResult.PENDING
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
