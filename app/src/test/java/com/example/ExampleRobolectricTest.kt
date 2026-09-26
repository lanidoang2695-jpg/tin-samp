package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.HandlingPresetEntity
import com.example.data.model.ServerEntity
import com.example.util.SampConfigWriter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("TIN SAMP", appName)
    }

    @Test
    fun `generate settings ini test`() {
        val server = ServerEntity(
            id = 1,
            name = "Test Server",
            ip = "127.0.0.1",
            port = 7777,
            voiceProtocol = "SampVoice v3.8"
        )
        val ini = SampConfigWriter.generateSettingsIni(
            nickname = "Tin_Tester",
            server = server,
            fpsLimit = 120,
            voiceChatEnabled = true,
            monetLoaderEnabled = true,
            amlEnabled = true,
            widescreenFix = true,
            fastConnect = true
        )
        assertTrue(ini.contains("name = Tin_Tester"))
        assertTrue(ini.contains("host = 127.0.0.1"))
        assertTrue(ini.contains("fps_limit = 120"))
        assertTrue(ini.contains("monetloader_support = 1"))
    }

    @Test
    fun `generate handling cfg test`() {
        val preset = HandlingPresetEntity(
            name = "Test Drift",
            vehicleModel = "Elegy",
            mass = 1400f,
            dragMultiplier = 1.5f,
            steeringLock = 40f,
            suspensionForce = 1.8f,
            driveType = "RWD",
            engineAcceleration = 0.40f,
            maxVelocity = 220f,
            brakeBias = 0.55f
        )
        val line = SampConfigWriter.generateHandlingCfgLine(preset)
        assertTrue(line.contains("ELEGY"))
        assertTrue(line.contains("R"))
    }
}
