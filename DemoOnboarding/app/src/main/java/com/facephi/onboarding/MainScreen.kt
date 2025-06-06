package com.facephi.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.facephi.onboarding.ui.composables.BaseCheckView
import com.facephi.onboarding.ui.composables.BaseTextButton

@Composable
fun MainScreen(
    sdkApplication: SdkApplication,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    val logs = viewModel.logs.collectAsState()
    var newOperationClicked by rememberSaveable { mutableStateOf(false) }

    var showPreviousTipSelphi by rememberSaveable {
        mutableStateOf(true)
    }

    var showTutorialSelphi by rememberSaveable {
        mutableStateOf(true)
    }

    var showPreviousTipSelphId by rememberSaveable {
        mutableStateOf(true)
    }

    var showTutorialSelphId by rememberSaveable {
        mutableStateOf(true)
    }

    var showDiagnosticSelphi by rememberSaveable {
        mutableStateOf(true)
    }

    var showDiagnosticSelphId by rememberSaveable {
        mutableStateOf(true)
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
            contentDescription = null,
            contentScale = ContentScale.None,
            modifier = Modifier
                .padding(8.dp)
                .height(75.dp)
        )

        BaseButton(modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = R.string.onboarding_new_operation),
            onClick = {
                newOperationClicked = true
                viewModel.newOperation()
            })

        Spacer(Modifier.height(8.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BaseCheckView(
                modifier = Modifier.weight(1f),
                checkValue = showPreviousTipSelphi,
                text = stringResource(id = R.string.onboarding_show_previous_tip)
            ) {
                showPreviousTipSelphi = it
            }
            BaseCheckView(
                modifier = Modifier.weight(1f),
                checkValue = showTutorialSelphi,
                text = stringResource(id = R.string.onboarding_show_tutorial)
            ) {
                showTutorialSelphi = it
            }
        }

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BaseCheckView(
                checkValue = showDiagnosticSelphi,
                text = stringResource(id = R.string.onboarding_show_diagnostic)
            ) {
                showDiagnosticSelphi = it
            }
        }

        BaseButton(modifier = Modifier,
            text = stringResource(id = R.string.onboarding_launch_selphi),
            enabled = newOperationClicked,
            onClick = {
               viewModel.launchSelphi(
                   showTutorial = showTutorialSelphi,
                   showPreviousTip = showPreviousTipSelphi,
                   showDiagnostic = showDiagnosticSelphi
               )
            })

        Spacer(Modifier.height(8.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BaseCheckView(
                modifier = Modifier.weight(1f),
                checkValue = showPreviousTipSelphId,
                text = stringResource(id = R.string.onboarding_show_previous_tip)
            ) {
                showPreviousTipSelphId = it
            }
            BaseCheckView(
                modifier = Modifier.weight(1f),
                checkValue = showTutorialSelphId,
                text = stringResource(id = R.string.onboarding_show_tutorial)
            ) {
                showTutorialSelphId = it
            }
        }

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BaseCheckView(
                checkValue = showDiagnosticSelphId,
                text = stringResource(id = R.string.onboarding_show_diagnostic)
            ) {
                showDiagnosticSelphId = it
            }
        }

        BaseButton(modifier = Modifier,
            text = stringResource(id = R.string.onboarding_launch_selphid),
            enabled = newOperationClicked,
            onClick = {
                viewModel.launchSelphId(
                    showTutorial = showTutorialSelphId,
                    showPreviousTip = showPreviousTipSelphId,
                    showDiagnostic = showDiagnosticSelphId
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
