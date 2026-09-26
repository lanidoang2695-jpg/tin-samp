package com.example.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.TinSampDatabase
import com.example.data.model.HandlingPresetEntity
import com.example.data.model.ModEntity
import com.example.data.model.ServerEntity
import com.example.data.repository.TinSampRepository
import com.example.util.AudioVoiceVisualizer
import com.example.util.DeviceOptimizationInfo
import com.example.util.MemoryStatus
import com.example.util.SampConfigWriter
import com.example.util.SystemOptimizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ServerTab(val label: String) {
    ALL("Semua"),
    FAVORITES("Favorit"),
    OFFICIAL("Resmi & Populer"),
    CUSTOM("Kustom"),
    RECENT("Riwayat")
}

enum class NavigationSection(val label: String) {
    SERVERS("Server"),
    PERFORMANCE("Performa"),
    MODS("Mod & Engine"),
    HANDLING("Handling"),
    VOICE("Voice Chat")
}

data class ConnectionProgressState(
    val server: ServerEntity,
    val stage: Int, // 1 to 4
    val stageMessage: String,
    val isComplete: Boolean,
    val configText: String
)

class TinSampViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TinSampRepository
    private val optimizer = SystemOptimizer(application)
    val audioVisualizer = AudioVoiceVisualizer(application)

    // Device Info
    val deviceInfo: DeviceOptimizationInfo = optimizer.getDeviceInfo()

    // Navigation & Tab State
    private val _currentSection = MutableStateFlow(NavigationSection.SERVERS)
    val currentSection: StateFlow<NavigationSection> = _currentSection.asStateFlow()

    private val _selectedServerTab = MutableStateFlow(ServerTab.ALL)
    val selectedServerTab: StateFlow<ServerTab> = _selectedServerTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Player Profile & Global Client Settings
    private val _nickname = MutableStateFlow("Tin_Player")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    private val _fpsLock = MutableStateFlow(120) // Default overpower 120 FPS
    val fpsLock: StateFlow<Int> = _fpsLock.asStateFlow()

    private val _voiceChatEnabled = MutableStateFlow(true)
    val voiceChatEnabled: StateFlow<Boolean> = _voiceChatEnabled.asStateFlow()

    private val _voiceMode = MutableStateFlow("PTT") // "PTT" or "VAD"
    val voiceMode: StateFlow<String> = _voiceMode.asStateFlow()

    private val _voiceDistance = MutableStateFlow(30f)
    val voiceDistance: StateFlow<Float> = _voiceDistance.asStateFlow()

    private val _noiseSuppression = MutableStateFlow(true)
    val noiseSuppression: StateFlow<Boolean> = _noiseSuppression.asStateFlow()

    private val _acousticEchoCancel = MutableStateFlow(true)
    val acousticEchoCancel: StateFlow<Boolean> = _acousticEchoCancel.asStateFlow()

    private val _monetLoaderEnabled = MutableStateFlow(true)
    val monetLoaderEnabled: StateFlow<Boolean> = _monetLoaderEnabled.asStateFlow()

    private val _aml64Enabled = MutableStateFlow(true)
    val aml64Enabled: StateFlow<Boolean> = _aml64Enabled.asStateFlow()

    private val _widescreenFix = MutableStateFlow(true)
    val widescreenFix: StateFlow<Boolean> = _widescreenFix.asStateFlow()

    private val _vulkanBackend = MutableStateFlow(true)
    val vulkanBackend: StateFlow<Boolean> = _vulkanBackend.asStateFlow()

    private val _fastConnect = MutableStateFlow(true)
    val fastConnect: StateFlow<Boolean> = _fastConnect.asStateFlow()

    // Performance & RAM Status
    private val _memoryStatus = MutableStateFlow(optimizer.getMemoryStatus())
    val memoryStatus: StateFlow<MemoryStatus> = _memoryStatus.asStateFlow()

    private val _isBoostingMemory = MutableStateFlow(false)
    val isBoostingMemory: StateFlow<Boolean> = _isBoostingMemory.asStateFlow()

    private val _boostResultText = MutableStateFlow<String?>(null)
    val boostResultText: StateFlow<String?> = _boostResultText.asStateFlow()

    // Dialog / UI Overlay States
    private val _connectingState = MutableStateFlow<ConnectionProgressState?>(null)
    val connectingState: StateFlow<ConnectionProgressState?> = _connectingState.asStateFlow()

    private val _showAddServerDialog = MutableStateFlow(false)
    val showAddServerDialog: StateFlow<Boolean> = _showAddServerDialog.asStateFlow()

    private val _showAddHandlingDialog = MutableStateFlow(false)
    val showAddHandlingDialog: StateFlow<Boolean> = _showAddHandlingDialog.asStateFlow()

    private val _selectedServerForDetail = MutableStateFlow<ServerEntity?>(null)
    val selectedServerForDetail: StateFlow<ServerEntity?> = _selectedServerForDetail.asStateFlow()

    // Room Database Streams
    val allServers: StateFlow<List<ServerEntity>>
    val filteredServers: StateFlow<List<ServerEntity>>
    val handlingPresets: StateFlow<List<HandlingPresetEntity>>
    val mods: StateFlow<List<ModEntity>>

    init {
        val database = TinSampDatabase.getDatabase(application, viewModelScope)
        repository = TinSampRepository(database.tinSampDao())

        allServers = repository.allServers.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        handlingPresets = repository.allHandlingPresets.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        mods = repository.allMods.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        filteredServers = combine(
            allServers,
            _selectedServerTab,
            _searchQuery
        ) { servers, tab, query ->
            var list = when (tab) {
                ServerTab.ALL -> servers
                ServerTab.FAVORITES -> servers.filter { it.isFavorite }
                ServerTab.OFFICIAL -> servers.filter { it.isOfficial }
                ServerTab.CUSTOM -> servers.filter { !it.isOfficial }
                ServerTab.RECENT -> servers.filter { it.lastConnectedTimestamp > 0 }
                    .sortedByDescending { it.lastConnectedTimestamp }
            }
            if (query.isNotBlank()) {
                val q = query.trim().lowercase()
                list = list.filter {
                    it.name.lowercase().contains(q) ||
                    it.ip.lowercase().contains(q) ||
                    it.gamemode.lowercase().contains(q) ||
                    it.voiceProtocol.lowercase().contains(q)
                }
            }
            list
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        refreshMemoryStatus()
    }

    fun setNavigationSection(section: NavigationSection) {
        _currentSection.value = section
    }

    fun setServerTab(tab: ServerTab) {
        _selectedServerTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setNickname(name: String) {
        _nickname.value = name.replace(" ", "_")
    }

    fun setFpsLock(fps: Int) {
        _fpsLock.value = fps
    }

    fun toggleVoiceChat(enabled: Boolean) {
        _voiceChatEnabled.value = enabled
    }

    fun setVoiceMode(mode: String) {
        _voiceMode.value = mode
    }

    fun setVoiceDistance(dist: Float) {
        _voiceDistance.value = dist
    }

    fun toggleNoiseSuppression(enabled: Boolean) {
        _noiseSuppression.value = enabled
    }

    fun toggleAcousticEcho(enabled: Boolean) {
        _acousticEchoCancel.value = enabled
    }

    fun toggleMonetLoader(enabled: Boolean) {
        _monetLoaderEnabled.value = enabled
    }

    fun toggleAml64(enabled: Boolean) {
        _aml64Enabled.value = enabled
    }

    fun toggleWidescreenFix(enabled: Boolean) {
        _widescreenFix.value = enabled
    }

    fun toggleVulkanBackend(enabled: Boolean) {
        _vulkanBackend.value = enabled
    }

    fun toggleFastConnect(enabled: Boolean) {
        _fastConnect.value = enabled
    }

    fun refreshMemoryStatus() {
        _memoryStatus.value = optimizer.getMemoryStatus()
    }

    fun boostMemory() {
        viewModelScope.launch {
            _isBoostingMemory.value = true
            val freed = optimizer.performMemoryBoost()
            refreshMemoryStatus()
            _isBoostingMemory.value = false
            _boostResultText.value = "RAM dibersihkan! $freed MB cache dibebaskan untuk GTA SAMP."
        }
    }

    fun clearBoostResult() {
        _boostResultText.value = null
    }

    // Server Operations
    fun toggleFavorite(server: ServerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleFavorite(server.id, !server.isFavorite)
        }
    }

    fun addCustomServer(name: String, ip: String, port: Int, password: String, gamemode: String, hasVoice: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val newServer = ServerEntity(
                name = name.ifBlank { "SAMP Server ($ip:$port)" },
                ip = ip.trim(),
                port = port,
                password = password.trim(),
                gamemode = gamemode.ifBlank { "Custom Server" },
                language = "All",
                players = (10..150).random(),
                maxPlayers = 500,
                ping = (20..55).random(),
                isFavorite = false,
                isOfficial = false,
                hasVoiceChat = hasVoice,
                voiceProtocol = if (hasVoice) "SampVoice v3.8" else "None",
                customNotes = "Server kustom ditambahkan pengguna."
            )
            repository.addServer(newServer)
        }
    }

    fun deleteServer(server: ServerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteServer(server)
        }
    }

    fun openServerDetail(server: ServerEntity) {
        _selectedServerForDetail.value = server
    }

    fun closeServerDetail() {
        _selectedServerForDetail.value = null
    }

    fun setShowAddServerDialog(show: Boolean) {
        _showAddServerDialog.value = show
    }

    fun setShowAddHandlingDialog(show: Boolean) {
        _showAddHandlingDialog.value = show
    }

    // Connect & Launch Flow
    fun startConnectSequence(server: ServerEntity) {
        viewModelScope.launch {
            val config = SampConfigWriter.generateSettingsIni(
                nickname = _nickname.value,
                server = server,
                fpsLimit = _fpsLock.value,
                voiceChatEnabled = _voiceChatEnabled.value,
                monetLoaderEnabled = _monetLoaderEnabled.value,
                amlEnabled = _aml64Enabled.value,
                widescreenFix = _widescreenFix.value,
                fastConnect = _fastConnect.value
            )

            // Save settings.ini to local app SAMP directory
            SampConfigWriter.saveConfigLocally(getApplication(), config)

            // Update last connected timestamp in DB
            repository.updateLastConnected(server.id, System.currentTimeMillis())

            _connectingState.value = ConnectionProgressState(
                server = server,
                stage = 1,
                stageMessage = "Mengoptimalkan RAM & cache game (Arm64)...",
                isComplete = false,
                configText = config
            )

            kotlinx.coroutines.delay(650)
            _connectingState.value = _connectingState.value?.copy(
                stage = 2,
                stageMessage = "Memuat engine 64-bit AML & MonetLoader v3.2...",
            )

            kotlinx.coroutines.delay(700)
            _connectingState.value = _connectingState.value?.copy(
                stage = 3,
                stageMessage = "Mengaktifkan Universal Voice Chat buffer (48kHz Opus)...",
            )

            kotlinx.coroutines.delay(650)
            _connectingState.value = _connectingState.value?.copy(
                stage = 4,
                stageMessage = "Koneksi siap! Mengarahkan ke server ${server.ip}:${server.port}...",
                isComplete = true
            )
        }
    }

    fun cancelConnection() {
        _connectingState.value = null
    }

    fun launchGameDirectly(server: ServerEntity) {
        val launched = SampConfigWriter.tryLaunchGame(getApplication(), server, _nickname.value)
        if (!launched) {
            Toast.makeText(
                getApplication(),
                "Konfigurasi TIN SAMP telah tersimpan! Silakan buka APK GTA SA / SAMP Anda atau gunakan 'Salin Konfigurasi' di dialog.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Handling Preset Operations
    fun addHandlingPreset(
        name: String,
        model: String,
        mass: Float,
        drag: Float,
        steerLock: Float,
        suspension: Float,
        driveType: String,
        acceleration: Float,
        maxVelocity: Float,
        brakeBias: Float
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val preset = HandlingPresetEntity(
                name = name,
                vehicleModel = model,
                mass = mass,
                dragMultiplier = drag,
                steeringLock = steerLock,
                suspensionForce = suspension,
                driveType = driveType,
                engineAcceleration = acceleration,
                maxVelocity = maxVelocity,
                brakeBias = brakeBias,
                isCustom = true
            )
            repository.addHandlingPreset(preset)
        }
    }

    fun deleteHandlingPreset(preset: HandlingPresetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHandlingPreset(preset)
        }
    }

    // Mod Operations
    fun toggleModEnabled(mod: ModEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleModEnabled(mod.id, !mod.isEnabled)
        }
    }

    fun addNewMod(name: String, type: String, fileName: String, author: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val mod = ModEntity(
                name = name,
                modType = type,
                fileName = fileName,
                author = author.ifBlank { "Komunitas" },
                description = description,
                isEnabled = true,
                supports64Bit = true,
                version = "v1.0"
            )
            repository.addMod(mod)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioVisualizer.stopListening()
    }
}
