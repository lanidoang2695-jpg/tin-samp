package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servers")
data class ServerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val ip: String,
    val port: Int,
    val password: String = "",
    val gamemode: String = "Roleplay",
    val language: String = "Indonesian / English",
    val players: Int = 0,
    val maxPlayers: Int = 500,
    val ping: Int = 35,
    val isFavorite: Boolean = false,
    val isOfficial: Boolean = false,
    val hasVoiceChat: Boolean = true,
    val voiceProtocol: String = "SampVoice v3.8",
    val lastConnectedTimestamp: Long = 0L,
    val customNotes: String = ""
)

@Entity(tableName = "handling_presets")
data class HandlingPresetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val vehicleModel: String,
    val mass: Float = 1500f,
    val dragMultiplier: Float = 2.0f,
    val steeringLock: Float = 35f,
    val suspensionForce: Float = 1.4f,
    val driveType: String = "RWD", // "AWD", "RWD", "FWD"
    val engineAcceleration: Float = 0.32f,
    val maxVelocity: Float = 210f,
    val brakeBias: Float = 0.52f,
    val isCustom: Boolean = true
)

@Entity(tableName = "mods")
data class ModEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val modType: String, // "MONETLOADER_LUA", "AML_SO", "CLEO_CSA", "HANDLING"
    val fileName: String,
    val author: String,
    val description: String,
    val isEnabled: Boolean = true,
    val supports64Bit: Boolean = true,
    val version: String = "v1.0"
)
