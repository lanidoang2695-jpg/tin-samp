package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.HandlingPresetEntity
import com.example.data.model.ModEntity
import com.example.data.model.ServerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TinSampDao {
    // --- Servers ---
    @Query("SELECT * FROM servers ORDER BY isFavorite DESC, ping ASC, players DESC")
    fun getAllServers(): Flow<List<ServerEntity>>

    @Query("SELECT * FROM servers WHERE isFavorite = 1 ORDER BY ping ASC")
    fun getFavoriteServers(): Flow<List<ServerEntity>>

    @Query("SELECT * FROM servers WHERE isOfficial = 1 ORDER BY players DESC")
    fun getOfficialServers(): Flow<List<ServerEntity>>

    @Query("SELECT * FROM servers WHERE isOfficial = 0 ORDER BY id DESC")
    fun getCustomServers(): Flow<List<ServerEntity>>

    @Query("SELECT * FROM servers WHERE lastConnectedTimestamp > 0 ORDER BY lastConnectedTimestamp DESC LIMIT 10")
    fun getRecentServers(): Flow<List<ServerEntity>>

    @Query("SELECT * FROM servers WHERE id = :id")
    suspend fun getServerById(id: Int): ServerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServer(server: ServerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServers(servers: List<ServerEntity>)

    @Update
    suspend fun updateServer(server: ServerEntity)

    @Delete
    suspend fun deleteServer(server: ServerEntity)

    @Query("UPDATE servers SET isFavorite = :isFav WHERE id = :id")
    suspend fun toggleFavorite(id: Int, isFav: Boolean)

    @Query("UPDATE servers SET lastConnectedTimestamp = :timestamp WHERE id = :id")
    suspend fun updateLastConnected(id: Int, timestamp: Long)

    @Query("SELECT COUNT(*) FROM servers")
    suspend fun getServerCount(): Int

    // --- Handling Presets ---
    @Query("SELECT * FROM handling_presets ORDER BY id ASC")
    fun getAllHandlingPresets(): Flow<List<HandlingPresetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHandlingPreset(preset: HandlingPresetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHandlingPresets(presets: List<HandlingPresetEntity>)

    @Update
    suspend fun updateHandlingPreset(preset: HandlingPresetEntity)

    @Delete
    suspend fun deleteHandlingPreset(preset: HandlingPresetEntity)

    @Query("SELECT COUNT(*) FROM handling_presets")
    suspend fun getHandlingCount(): Int

    // --- Mods (MonetLoader, AML, CLEO) ---
    @Query("SELECT * FROM mods ORDER BY modType ASC, name ASC")
    fun getAllMods(): Flow<List<ModEntity>>

    @Query("SELECT * FROM mods WHERE modType = :type")
    fun getModsByType(type: String): Flow<List<ModEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMod(mod: ModEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMods(mods: List<ModEntity>)

    @Update
    suspend fun updateMod(mod: ModEntity)

    @Query("UPDATE mods SET isEnabled = :enabled WHERE id = :id")
    suspend fun toggleModEnabled(id: Int, enabled: Boolean)

    @Delete
    suspend fun deleteMod(mod: ModEntity)

    @Query("SELECT COUNT(*) FROM mods")
    suspend fun getModCount(): Int
}
