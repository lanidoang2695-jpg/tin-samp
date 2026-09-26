package com.example.data.repository

import com.example.data.local.TinSampDao
import com.example.data.model.HandlingPresetEntity
import com.example.data.model.ModEntity
import com.example.data.model.ServerEntity
import kotlinx.coroutines.flow.Flow

class TinSampRepository(private val dao: TinSampDao) {

    // Servers
    val allServers: Flow<List<ServerEntity>> = dao.getAllServers()
    val favoriteServers: Flow<List<ServerEntity>> = dao.getFavoriteServers()
    val officialServers: Flow<List<ServerEntity>> = dao.getOfficialServers()
    val customServers: Flow<List<ServerEntity>> = dao.getCustomServers()
    val recentServers: Flow<List<ServerEntity>> = dao.getRecentServers()

    suspend fun getServerById(id: Int): ServerEntity? = dao.getServerById(id)

    suspend fun addServer(server: ServerEntity): Long = dao.insertServer(server)

    suspend fun updateServer(server: ServerEntity) = dao.updateServer(server)

    suspend fun deleteServer(server: ServerEntity) = dao.deleteServer(server)

    suspend fun toggleFavorite(id: Int, isFav: Boolean) = dao.toggleFavorite(id, isFav)

    suspend fun updateLastConnected(id: Int, timestamp: Long) = dao.updateLastConnected(id, timestamp)

    // Handling Presets
    val allHandlingPresets: Flow<List<HandlingPresetEntity>> = dao.getAllHandlingPresets()

    suspend fun addHandlingPreset(preset: HandlingPresetEntity): Long = dao.insertHandlingPreset(preset)

    suspend fun updateHandlingPreset(preset: HandlingPresetEntity) = dao.updateHandlingPreset(preset)

    suspend fun deleteHandlingPreset(preset: HandlingPresetEntity) = dao.deleteHandlingPreset(preset)

    // Mods (MonetLoader, AML, CLEO)
    val allMods: Flow<List<ModEntity>> = dao.getAllMods()

    fun getModsByType(type: String): Flow<List<ModEntity>> = dao.getModsByType(type)

    suspend fun toggleModEnabled(id: Int, isEnabled: Boolean) = dao.toggleModEnabled(id, isEnabled)

    suspend fun addMod(mod: ModEntity): Long = dao.insertMod(mod)

    suspend fun deleteMod(mod: ModEntity) = dao.deleteMod(mod)

    suspend fun ensureDefaultDataLoaded() {
        if (dao.getServerCount() == 0) {
            // In case callback didn't fire (e.g. existing db), populate
            // (Handled also by DB callback)
        }
    }
}
