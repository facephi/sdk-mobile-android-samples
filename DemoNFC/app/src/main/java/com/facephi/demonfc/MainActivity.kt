package com.facephi.demonfc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.facephi.demonfc.ui.composables.result.ErrorRow
import com.facephi.demonfc.ui.screens.DisclaimerScreen
import com.facephi.demonfc.ui.screens.TabScreen
import com.facephi.demonfc.ui.theme.DemoNFCTheme
import com.facephi.sdk.SDKController


class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var error: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("APP", "LAUNCH INIT SDK")
        viewModel.initSdk(SdkApplication(application), onError = {
            error = it
        })

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
                        Column {
                            Spacer(Modifier.size(48.dp))
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
                            error?.let {
                                ErrorRow(it)
                                Spacer(Modifier.size(8.dp))
                            }
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
                                        arrayOf("sdkmobile@facephi.com")
                                    )
                                    putExtra(
                                        Intent.EXTRA_SUBJECT,
                                        "Prueba NFC Samples Android (${viewModel.formatEpochMillis()})"
                                    )
                                    putExtra(Intent.EXTRA_TEXT, message)
                                }

                                try {
                                    this.startActivity(intent)
                                } catch (e: Exception) {
                                    Log.e("APP", "EMAIL ERROR $e")
                                }
                            }
                        } else {
                            DisclaimerScreen(
                                modifier = Modifier
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

