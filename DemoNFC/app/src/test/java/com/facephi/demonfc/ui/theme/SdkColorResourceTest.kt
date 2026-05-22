package com.facephi.demonfc.ui.theme

import android.content.Context
import android.util.TypedValue
import androidx.compose.ui.graphics.Color
import androidx.test.core.app.ApplicationProvider
import com.facephi.demonfc.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class SdkColorResourceTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.setTheme(R.style.Theme_DemoNFC)
    }

    @Test
    fun `sdk primary color is configured as theme attribute reference`() {
        val typedValue = TypedValue()

        context.resources.getValue(R.color.sdkPrimaryColor, typedValue, true)

        assertEquals(TypedValue.TYPE_ATTRIBUTE, typedValue.type)
        assertEquals("demoNfcPrimaryColor", context.resources.getResourceEntryName(typedValue.data))
    }

    @Test
    fun `sdk primary color resolves theme attribute value`() {
        val color = context.resolveSdkColorResource(R.color.sdkPrimaryColor)

        assertEquals(Color(0xFF245BDB), color)
    }
}
