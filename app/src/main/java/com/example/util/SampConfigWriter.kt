package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.data.model.HandlingPresetEntity
import com.example.data.model.ServerEntity
import java.io.File

object SampConfigWriter {

    fun generateSettingsIni(
        nickname: String,
        server: ServerEntity,
        fpsLimit: Int,
        voiceChatEnabled: Boolean,
        monetLoaderEnabled: Boolean,
        amlEnabled: Boolean,
        widescreenFix: Boolean,
        fastConnect: Boolean
    ): String {
        return buildString {
            appendLine("# ==========================================")
            appendLine("# TIN SAMP CLIENT CONFIGURATION (OVERPOWER)")
            appendLine("# Platform: Android 16 64-Bit Optimized")
            appendLine("# ==========================================")
            appendLine("[client]")
            appendLine("client_name = TIN_SAMP_OVERPOWER")
            appendLine("version = 0.3.7-R5-64BIT")
            appendLine("name = $nickname")
            appendLine("host = ${server.ip}")
            appendLine("port = ${server.port}")
            if (server.password.isNotBlank()) {
                appendLine("password = ${server.password}")
            }
            appendLine()
            appendLine("[engine]")
            appendLine("fps_limit = $fpsLimit")
            appendLine("fps_unlock = ${if (fpsLimit > 60) 1 else 0}")
            appendLine("fast_connect = ${if (fastConnect) 1 else 0}")
            appendLine("widescreen_20_9 = ${if (widescreenFix) 1 else 0}")
            appendLine("vulkan_backend = 1")
            appendLine("arm64_optimization = 1")
            appendLine("multithreading = 1")
            appendLine()
            appendLine("[mods]")
            appendLine("aml_support = ${if (amlEnabled) 1 else 0}")
            appendLine("monetloader_support = ${if (monetLoaderEnabled) 1 else 0}")
            appendLine("cleo_support = 1")
            appendLine()
            appendLine("[voice]")
            appendLine("voice_chat = ${if (voiceChatEnabled) 1 else 0}")
            appendLine("voice_protocol = ${server.voiceProtocol}")
            appendLine("voice_quality = 48000")
            appendLine("voice_spatial = 1")
        }
    }

    fun generateHandlingCfgLine(preset: HandlingPresetEntity): String {
        // Standard GTA San Andreas handling.cfg format
        // MODELNAME  MASS  DRAG  TURNMASS  STEERLOCK  SUSP_FORCE  DRIVE  ACCEL  MAX_VEL  BRAKE_BIAS
        val driveCode = when (preset.driveType.uppercase()) {
            "FWD" -> "F"
            "AWD" -> "4"
            else -> "R"
        }
        return "${preset.vehicleModel.uppercase().padEnd(12)} " +
                "${"%.1f".format(preset.mass)} " +
                "${"%.2f".format(preset.dragMultiplier)} " +
                "${"%.1f".format(preset.mass * 1.5f)} " +
                "${"%.1f".format(preset.steeringLock)} " +
                "${"%.2f".format(preset.suspensionForce)} " +
                "$driveCode " +
                "${"%.3f".format(preset.engineAcceleration)} " +
                "${"%.1f".format(preset.maxVelocity)} " +
                "${"%.2f".format(preset.brakeBias)}"
    }

    fun saveConfigLocally(context: Context, content: String): File {
        val dir = File(context.filesDir, "SAMP")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "settings.ini")
        file.writeText(content)
        return file
    }

    fun tryLaunchGame(context: Context, server: ServerEntity, nickname: String): Boolean {
        val packageNames = listOf(
            "com.rockstargames.gtasa",
            "com.samp.mobile",
            "com.valhalla.samp",
            "com.arizona.game",
            "com.liverussia.cr"
        )

        for (pkg in packageNames) {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(pkg)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                launchIntent.putExtra("server_ip", server.ip)
                launchIntent.putExtra("server_port", server.port)
                launchIntent.putExtra("player_name", nickname)
                try {
                    context.startActivity(launchIntent)
                    return true
                } catch (_: Exception) {
                }
            }
        }
        return false
    }
}
