package com.facephi.demophingers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.demophingers.ui.theme.DemoPhingersTheme
import com.facephi.sdk.SDKController
import io.github.aakira.napier.Napier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if (BuildConfig.DEBUG) {
            SDKController.enableDebugMode()
        //}

        setContent {
            DemoPhingersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(sdkApplication = SdkApplication(application), modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

    override fun onDestroy() {
        SDKController.closeSession()
        super.onDestroy()
    }
}
