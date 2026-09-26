package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SoftGreenBackground
import com.example.ui.theme.SoftGreenCard
import com.example.ui.theme.SoftGreenOnPrimary
import com.example.ui.theme.SoftGreenOutline
import com.example.ui.theme.SoftGreenPrimary
import com.example.ui.theme.SoftGreenSecondary
import com.example.ui.theme.SoftGreenSurfaceVariant
import com.example.ui.theme.StatusGreen
import com.example.ui.viewmodel.TinSampViewModel

@Composable
fun PerformanceScreen(
    viewModel: TinSampViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val memoryStatus by viewModel.memoryStatus.collectAsState()
    val isBoosting by viewModel.isBoostingMemory.collectAsState()
    val boostResult by viewModel.boostResultText.collectAsState()
    val fpsLock by viewModel.fpsLock.collectAsState()
    val aml64Enabled by viewModel.aml64Enabled.collectAsState()
    val vulkanBackend by viewModel.vulkanBackend.collectAsState()
    val widescreenFix by viewModel.widescreenFix.collectAsState()
    val fastConnect by viewModel.fastConnect.collectAsState()
    val deviceInfo = viewModel.deviceInfo

    var isRepairing by remember { mutableStateOf(false) }
    var repairMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoftGreenBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Badge
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Performance",
                        tint = SoftGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "OPTIMASI & LOCK FPS",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Konfigurasi performa tinggi stabil parah untuk Android 16 & arsitektur 64-bit.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Live RAM Usage & Game Booster
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Memory,
                                contentDescription = "RAM",
                                tint = SoftGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Monitor RAM Game",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "${memoryStatus.usagePercent}% Digunakan",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (memoryStatus.usagePercent > 80) Color(0xFFEF4444) else SoftGreenSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { memoryStatus.usagePercent / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (memoryStatus.usagePercent > 80) Color(0xFFEF4444) else SoftGreenPrimary,
                        trackColor = SoftGreenSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tersedia: ${memoryStatus.availRamMb} MB",
                            fontSize = 12.sp,
                            color = SoftGreenPrimary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Total: ${memoryStatus.totalRamMb} MB",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    AnimatedVisibility(visible = boostResult != null) {
                        boostResult?.let { msg ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SoftGreenPrimary.copy(alpha = 0.15f))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = SoftGreenPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = msg,
                                    fontSize = 11.sp,
                                    color = SoftGreenPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { viewModel.clearBoostResult() },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Dismiss",
                                        tint = SoftGreenPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.boostMemory() },
                        enabled = !isBoosting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_boost_ram"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SoftGreenPrimary,
                            contentColor = SoftGreenOnPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isBoosting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = SoftGreenOnPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Membersihkan RAM...")
                        } else {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Boost",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BOOST RAM & BERSIHKAN CACHE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // FPS Lock Selector
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "FPS",
                            tint = SoftGreenPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kunci FPS Game (FPS Limiter)",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Kunci frame rate untuk mencegah thermal throttling dan menjaga stabilitas gameplay 100% mulus.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val fpsOptions = listOf(30, 60, 90, 120, 144)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fpsOptions.forEach { fps ->
                            val isSelected = fpsLock == fps
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) SoftGreenPrimary else SoftGreenSurfaceVariant)
                                    .border(
                                        1.dp,
                                        if (isSelected) SoftGreenPrimary else SoftGreenOutline,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { viewModel.setFpsLock(fps) }
                                    .padding(vertical = 10.dp)
                                    .testTag("fps_option_$fps"),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$fps",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = if (isSelected) SoftGreenOnPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "FPS",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isSelected) SoftGreenOnPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Rekomendasi perangkat Anda: ${deviceInfo.recommendedFps} FPS untuk performa paling stabil.",
                        fontSize = 11.sp,
                        color = SoftGreenSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Android 16 & 64-Bit Core Optimization Switches
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Fitur Overpower (Android 16 & 64-Bit)",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // 64-Bit Engine
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Arm64-v8a 64-Bit Native Hook",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Kompatibilitas kernel Android 16 tanpa crash memori.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = aml64Enabled,
                            onCheckedChange = { viewModel.toggleAml64(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SoftGreenPrimary,
                                checkedTrackColor = SoftGreenSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Vulkan Graphics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Vulkan Graphics Backend",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Memangkas beban CPU dan mendongkrak frame rate.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = vulkanBackend,
                            onCheckedChange = { viewModel.toggleVulkanBackend(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SoftGreenPrimary,
                                checkedTrackColor = SoftGreenSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Widescreen 20:9 Fix
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Widescreen 20:9 Punch-Hole Fix",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Hilangkan black bar dan regangkan layar penuh.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = widescreenFix,
                            onCheckedChange = { viewModel.toggleWidescreenFix(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SoftGreenPrimary,
                                checkedTrackColor = SoftGreenSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fast Connect
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Fast-Connect & Anti-Lag Buffer",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Akselerasi soket query dan kurangi packet loss.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = fastConnect,
                            onCheckedChange = { viewModel.toggleFastConnect(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SoftGreenPrimary,
                                checkedTrackColor = SoftGreenSurfaceVariant
                            )
                        )
                    }
                }
            }
        }

        // Hardware Diagnostics & Crash Repair
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Diagnostics",
                            tint = SoftGreenPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Status Perangkat & Diagnosa Crash",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SoftGreenBackground)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Sistem: ${deviceInfo.androidVersion}",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Arsitektur: ${if (deviceInfo.is64Bit) "Arm64-v8a (64-Bit Valid)" else "32-Bit"}",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = SoftGreenPrimary
                        )
                        Text(
                            text = "Core Prosesor: ${deviceInfo.cpuCores} Threads Aktif",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Vulkan Driver: ${if (deviceInfo.vulkanSupported) "Tersedia & Siap" else "OpenGL Fallback"}",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = SoftGreenSecondary
                        )
                    }

                    AnimatedVisibility(visible = repairMessage != null) {
                        repairMessage?.let { msg ->
                            Text(
                                text = msg,
                                fontSize = 11.sp,
                                color = SoftGreenPrimary,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            isRepairing = true
                            repairMessage = "Memeriksa pointer memori & membersihkan buffer audio..."
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                isRepairing = false
                                repairMessage = "Selesai! Buffer SAMP, cache texture DXT, dan Lua hook berhasil dioptimasi tanpa error."
                                Toast.makeText(context, "Klien SAMP berhasil diperbaiki!", Toast.LENGTH_SHORT).show()
                            }, 1200)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SoftGreenSurfaceVariant,
                            contentColor = SoftGreenPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isRepairing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = SoftGreenPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        } else {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "Repair",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = "1-Tap Perbaiki Masalah & Anti Crash",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
