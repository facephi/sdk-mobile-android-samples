package com.facephi.demonfc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.demonfc.ui.screens.DisclaimerScreen
import com.facephi.demonfc.ui.screens.TabScreen
import com.facephi.demonfc.ui.theme.DemoNFCTheme
import com.facephi.sdk.SDKController
import com.facephi.sdk_composables.R
import io.github.aakira.napier.Napier

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if (BuildConfig.DEBUG) {
        SDKController.enableDebugMode()
        //}

        Napier.d("APP: LAUNCH INIT SDK")

        if (SdkData.LICENSE_ONLINE) {
            SDKController.initSdk(
                sdkApplication = SdkApplication(application),
                environmentLicensingData = SdkData.environmentLicensingData,
            ) { result ->
                when (result) {
                    is SdkResult.Success -> Napier.d("APP: INIT SDK: OK")
                    is SdkResult.Error -> Napier.d("APP: INIT SDK: KO - ${result.error}")
                }
            }

        } else {
            SDKController.initSdk(
                sdkApplication = SdkApplication(application),
                license = SdkData.LICENSE,
            ) { result ->
                when (result) {
                    is SdkResult.Success -> Napier.d("APP: INIT SDK: OK")
                    is SdkResult.Error -> Napier.d("APP: INIT SDK: KO - ${result.error}")
                }
            }
        }

        setContent {
            var showTabs by rememberSaveable {
                mutableStateOf(false)
            }

            DemoNFCTheme {
                Scaffold(
                    backgroundColor = colorResource(
                        id = R.color.sdkBackgroundColor
                    ),
                    topBar = {
                        Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                            Image(
                                painter = painterResource(id = com.facephi.demonfc.R.drawable.ic_demo_logo),
                                contentDescription = "Logo",
                                contentScale = ContentScale.None,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .height(75.dp)
                            )
                        }
                    },
                    content = {

                        if (showTabs){
                            TabScreen(
                                viewModel = viewModel,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(it)
                            ) { message ->

                                val currentTimestamp = System.currentTimeMillis()

                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:")
                                    putExtra(
                                        Intent.EXTRA_EMAIL,
                                        arrayOf("lmartin@facephi.com", "abueno@facephi.com")
                                    )
                                    putExtra(Intent.EXTRA_SUBJECT, "Prueba: $currentTimestamp")
                                    putExtra(Intent.EXTRA_TEXT, message)
                                }

                                try {
                                    this.startActivity(intent)
                                } catch (e: Exception) {
                                    Napier.d("APP: EMAIL ERROR $e")
                                }
                            }
                        } else {
                            DisclaimerScreen(modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                                onCancel = {
                                    this@MainActivity.finish()

                                }, onAgree = {
                                    showTabs = true

                                })
                        }


                    })
            }
        }
    }

    override fun onDestroy() {
        SDKController.closeSession()
        super.onDestroy()
    }
}

