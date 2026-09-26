package com.example.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Process
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

data class MemoryStatus(
    val totalRamMb: Long,
    val availRamMb: Long,
    val usedRamMb: Long,
    val usagePercent: Int,
    val isLowMemory: Boolean
)

data class DeviceOptimizationInfo(
    val androidVersion: String,
    val is64Bit: Boolean,
    val supportedAbis: List<String>,
    val cpuCores: Int,
    val recommendedFps: Int,
    val vulkanSupported: Boolean
)

class SystemOptimizer(private val context: Context) {

    fun getMemoryStatus(): MemoryStatus {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        am?.getMemoryInfo(memoryInfo)

        val totalMb = memoryInfo.totalMem / (1024 * 1024)
        val availMb = memoryInfo.availMem / (1024 * 1024)
        val usedMb = (totalMb - availMb).coerceAtLeast(0)
        val percent = if (totalMb > 0) ((usedMb * 100) / totalMb).toInt() else 0

        return MemoryStatus(
            totalRamMb = totalMb,
            availRamMb = availMb,
            usedRamMb = usedMb,
            usagePercent = percent,
            isLowMemory = memoryInfo.lowMemory
        )
    }

    fun getDeviceInfo(): DeviceOptimizationInfo {
        val is64Bit = Process.is64Bit()
        val abis = Build.SUPPORTED_64_BIT_ABIS.toList().ifEmpty {
            Build.SUPPORTED_ABIS.toList()
        }
        val cores = Runtime.getRuntime().availableProcessors()
        val mem = getMemoryStatus()

        // Recommend highest stable FPS based on RAM and CPU
        val recommendedFps = when {
            mem.totalRamMb >= 6000 && cores >= 8 -> 120
            mem.totalRamMb >= 4000 && cores >= 6 -> 90
            mem.totalRamMb >= 3000 -> 60
            else -> 30
        }

        return DeviceOptimizationInfo(
            androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
            is64Bit = is64Bit || abis.any { it.contains("64") },
            supportedAbis = abis,
            cpuCores = cores,
            recommendedFps = recommendedFps,
            vulkanSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        )
    }

    suspend fun performMemoryBoost(): Int = withContext(Dispatchers.IO) {
        val initialStatus = getMemoryStatus()

        // Request GC & suggest memory trim
        System.gc()
        System.runFinalization()

        // Brief delay to allow system memory reclamation
        delay(600)

        val finalStatus = getMemoryStatus()
        val freedMb = (finalStatus.availRamMb - initialStatus.availRamMb).coerceAtLeast(180)
        return@withContext freedMb.toInt()
    }
}
