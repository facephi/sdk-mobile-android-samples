package com.facephi.demophingers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.facephi.core.data.SdkApplication
import com.facephi.demophingers.ui.theme.DemoPhingersTheme
import com.facephi.sdk.SDKController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
