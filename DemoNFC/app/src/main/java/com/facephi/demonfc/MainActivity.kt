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
import androidx.compose.material3.Scaffold
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
import com.facephi.demonfc.ui.screens.DisclaimerScreen
import com.facephi.demonfc.ui.screens.TabScreen
import com.facephi.demonfc.ui.theme.DemoNFCTheme
import com.facephi.sdk.SDKController
import io.github.aakira.napier.Napier

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Napier.d("APP: LAUNCH INIT SDK")
        viewModel.initSdk(SdkApplication(application))

        setContent {
            var showTabs by rememberSaveable {
                mutableStateOf(false)
            }

            DemoNFCTheme {
                Scaffold(
                    containerColor = colorResource(
                        id = R.color.sdkBackgroundColor
                    ),
                    topBar = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_demo_logo),
                                contentDescription = "Logo",
                                contentScale = ContentScale.None,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .height(75.dp)
                            )
                        }
                    },
                    content = {

                        if (showTabs) {
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

