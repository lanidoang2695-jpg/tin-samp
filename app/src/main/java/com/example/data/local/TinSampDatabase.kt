package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.HandlingPresetEntity
import com.example.data.model.ModEntity
import com.example.data.model.ServerEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ServerEntity::class, HandlingPresetEntity::class, ModEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TinSampDatabase : RoomDatabase() {
    abstract fun tinSampDao(): TinSampDao

    companion object {
        @Volatile
        private var INSTANCE: TinSampDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TinSampDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TinSampDatabase::class.java,
                    "tinsamp_database"
                )
                    .addCallback(TinSampDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class TinSampDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.tinSampDao())
                }
            }
        }

        suspend fun populateInitialData(dao: TinSampDao) {
            // Initial Curated SAMP Servers
            val initialServers = listOf(
                ServerEntity(
                    name = "Jogjagamers Reality Project (JGRP)",
                    ip = "jg-g.me",
                    port = 7777,
                    gamemode = "Roleplay 0.3.7",
                    language = "Indonesian",
                    players = 894,
                    maxPlayers = 1000,
                    ping = 24,
                    isFavorite = true,
                    isOfficial = true,
                    hasVoiceChat = true,
                    voiceProtocol = "SampVoice v3.8",
                    customNotes = "Server RP nomor 1 di Indonesia dengan ekosistem ekonomi realistis."
                ),
                ServerEntity(
                    name = "Indo Pride Roleplay (IPRP)",
                    ip = "play.indopride.id",
                    port = 7777,
                    gamemode = "IPRP v4.2 Modern",
                    language = "Indonesian",
                    players = 485,
                    maxPlayers = 600,
                    ping = 28,
                    isFavorite = true,
                    isOfficial = true,
                    hasVoiceChat = true,
                    voiceProtocol = "Universal SVF",
                    customNotes = "Support custom vehicle handling dan voice chat jarak dekat."
                ),
                ServerEntity(
                    name = "Valhalla Community ID",
                    ip = "samp.valhalla-id.com",
                    port = 7777,
                    gamemode = "Realistic RP 2026",
                    language = "Indonesian",
                    players = 312,
                    maxPlayers = 450,
                    ping = 31,
                    isFavorite = false,
                    isOfficial = true,
                    hasVoiceChat = true,
                    voiceProtocol = "SampVoice v3.8",
                    customNotes = "Komunitas ramah pemula, sistem job variatif dan anti lag."
                ),
                ServerEntity(
                    name = "Indonesian Drift & Stunt (IDS)",
                    ip = "drift.ids-indo.net",
                    port = 7777,
                    gamemode = "Drift / Freeroam",
                    language = "ID / EN",
                    players = 220,
                    maxPlayers = 300,
                    ping = 29,
                    isFavorite = false,
                    isOfficial = true,
                    hasVoiceChat = true,
                    voiceProtocol = "VAG 64-bit",
                    customNotes = "Arena drift multiplayer dengan custom handling injector support."
                ),
                ServerEntity(
                    name = "Cops and Robbers Indo (CnR)",
                    ip = "cnr.samp-id.org",
                    port = 7777,
                    gamemode = "CnR Action Police",
                    language = "Indonesian",
                    players = 168,
                    maxPlayers = 250,
                    ping = 36,
                    isFavorite = false,
                    isOfficial = true,
                    hasVoiceChat = true,
                    voiceProtocol = "SampVoice v3.8",
                    customNotes = "Aksi kejar-kejaran polisi vs rampok dengan sistem squad voice."
                ),
                ServerEntity(
                    name = "Revival Roleplay 2026",
                    ip = "play.revivalrp.net",
                    port = 7777,
                    gamemode = "Medium RP",
                    language = "Indonesian",
                    players = 275,
                    maxPlayers = 400,
                    ping = 34,
                    isFavorite = false,
                    isOfficial = true,
                    hasVoiceChat = true,
                    voiceProtocol = "Universal SVF",
                    customNotes = "Fitur bisnis, properti, dan turnamen balap jalanan mingguan."
                )
            )
            dao.insertServers(initialServers)

            // Initial Handling Presets
            val initialHandlings = listOf(
                HandlingPresetEntity(
                    name = "Drift King JDM (Elegy)",
                    vehicleModel = "Elegy",
                    mass = 1420f,
                    dragMultiplier = 1.6f,
                    steeringLock = 42f,
                    suspensionForce = 1.8f,
                    driveType = "RWD",
                    engineAcceleration = 0.45f,
                    maxVelocity = 230f,
                    brakeBias = 0.58f,
                    isCustom = false
                ),
                HandlingPresetEntity(
                    name = "Drag Strip Demon (Infernus)",
                    vehicleModel = "Infernus",
                    mass = 1350f,
                    dragMultiplier = 1.3f,
                    steeringLock = 34f,
                    suspensionForce = 2.1f,
                    driveType = "AWD",
                    engineAcceleration = 0.58f,
                    maxVelocity = 280f,
                    brakeBias = 0.52f,
                    isCustom = false
                ),
                HandlingPresetEntity(
                    name = "VIP Stance & Cruising (Sultan)",
                    vehicleModel = "Sultan",
                    mass = 1580f,
                    dragMultiplier = 1.8f,
                    steeringLock = 37f,
                    suspensionForce = 1.5f,
                    driveType = "AWD",
                    engineAcceleration = 0.38f,
                    maxVelocity = 220f,
                    brakeBias = 0.50f,
                    isCustom = false
                ),
                HandlingPresetEntity(
                    name = "Batu Offroad 4x4 (Sandking)",
                    vehicleModel = "Sandking",
                    mass = 2100f,
                    dragMultiplier = 2.4f,
                    steeringLock = 32f,
                    suspensionForce = 2.4f,
                    driveType = "AWD",
                    engineAcceleration = 0.40f,
                    maxVelocity = 190f,
                    brakeBias = 0.48f,
                    isCustom = false
                ),
                HandlingPresetEntity(
                    name = "Super Bike Agile (NRG-500)",
                    vehicleModel = "NRG-500",
                    mass = 220f,
                    dragMultiplier = 1.1f,
                    steeringLock = 40f,
                    suspensionForce = 1.9f,
                    driveType = "RWD",
                    engineAcceleration = 0.62f,
                    maxVelocity = 245f,
                    brakeBias = 0.55f,
                    isCustom = false
                )
            )
            dao.insertHandlingPresets(initialHandlings)

            // Initial Mods
            val initialMods = listOf(
                ModEntity(
                    name = "AML 64-Bit Android 16 Core Engine",
                    modType = "AML_SO",
                    fileName = "libAML_arm64.so",
                    author = "RusJJ / TIN Port",
                    description = "Mesin loader modifikasi native 64-bit untuk Android 15 & 16 tanpa crash.",
                    isEnabled = true,
                    supports64Bit = true,
                    version = "v1.2.7-arm64"
                ),
                ModEntity(
                    name = "SampVoice Universal 64-Bit Driver",
                    modType = "AML_SO",
                    fileName = "sampvoice_v3_arm64.so",
                    author = "CyberCats / TIN",
                    description = "Protokol voice chat spatial 3D 48kHz Opus kompatibel semua server SAMP.",
                    isEnabled = true,
                    supports64Bit = true,
                    version = "v3.8.2-all"
                ),
                ModEntity(
                    name = "MonetLoader v3.2.0 Lua Runtime",
                    modType = "MONETLOADER_LUA",
                    fileName = "monetloader.lua",
                    author = "MonetLoader Team",
                    description = "Runtime Lua JIT berperforma tinggi untuk menjalankan script LUA SAMP.",
                    isEnabled = true,
                    supports64Bit = true,
                    version = "v3.2.0"
                ),
                ModEntity(
                    name = "Monet Quick Map & GPS Waypoint",
                    modType = "MONETLOADER_LUA",
                    fileName = "gps_radar_modern.lua",
                    author = "TinScripts",
                    description = "Radar modern dengan visualisasi rute otomatis dan penanda lokasi penting.",
                    isEnabled = true,
                    supports64Bit = true,
                    version = "v2.1"
                ),
                ModEntity(
                    name = "CLEO Touch Gesture Menu v4",
                    modType = "CLEO_CSA",
                    fileName = "cleo_menu_touch.csa",
                    author = "Alexander Blade",
                    description = "Menu sentuh interaktif CLEO aktif dengan usap ke bawah dari atas layar.",
                    isEnabled = true,
                    supports64Bit = true,
                    version = "v4.0.1"
                ),
                ModEntity(
                    name = "64-Bit Widescreen 20:9 Punch Hole Fix",
                    modType = "AML_SO",
                    fileName = "widescreen_fix64.so",
                    author = "ThirteenAG / TIN",
                    description = "Menghilangkan black bar dan meregangkan radar HUD agar pas di rasio layar modern.",
                    isEnabled = true,
                    supports64Bit = true,
                    version = "v1.5"
                ),
                ModEntity(
                    name = "Monet Live Handling Injector",
                    modType = "MONETLOADER_LUA",
                    fileName = "handling_injector.lua",
                    author = "TinDev",
                    description = "Menginjeksi pengaturan handling mobil secara real-time tanpa restart game.",
                    isEnabled = true,
                    supports64Bit = true,
                    version = "v2.0"
                )
            )
            dao.insertMods(initialMods)
        }
    }
}
