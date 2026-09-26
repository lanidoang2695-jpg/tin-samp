package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.SpatialAudio
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.theme.SoftGreenBackground
import com.example.ui.theme.SoftGreenCard
import com.example.ui.theme.SoftGreenOnPrimary
import com.example.ui.theme.SoftGreenOutline
import com.example.ui.theme.SoftGreenPrimary
import com.example.ui.theme.SoftGreenSecondary
import com.example.ui.theme.SoftGreenSurfaceVariant
import com.example.ui.viewmodel.TinSampViewModel

@Composable
fun VoiceStudioScreen(
    viewModel: TinSampViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val visualizer = viewModel.audioVisualizer

    val isRecording by visualizer.isRecording.collectAsState()
    val liveAmplitude by visualizer.amplitude.collectAsState()

    val voiceEnabled by viewModel.voiceChatEnabled.collectAsState()
    val voiceMode by viewModel.voiceMode.collectAsState()
    val voiceDistance by viewModel.voiceDistance.collectAsState()
    val noiseSuppression by viewModel.noiseSuppression.collectAsState()
    val echoCancel by viewModel.acousticEchoCancel.collectAsState()

    var micGain by remember { mutableFloatStateOf(1.5f) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            visualizer.startListening(scope)
            Toast.makeText(context, "Izin mikrofon aktif. Silakan bicara!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Izin mikrofon diperlukan untuk tes voice chat.", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoftGreenBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice",
                        tint = SoftGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "UNIVERSAL VOICE CHAT",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Engine voice chat universal 'Wort It All Server' (SampVoice, SVF, VAG 64-bit Opus 48kHz).",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Live Mic Test & Visualizer Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tes Mikrofon Real-time",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isRecording) SoftGreenPrimary else Color(0xFF6B7280))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isRecording) "MENDENGARKAN" else "STANDBY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isRecording) SoftGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Waveform Bars Display
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SoftGreenBackground)
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val barCount = 20
                        for (i in 0 until barCount) {
                            val factor = if (isRecording) {
                                val distanceToCenter = kotlin.math.abs(i - barCount / 2f) / (barCount / 2f)
                                val wave = (liveAmplitude * (1f - distanceToCenter * 0.5f) * (0.8f + (i % 3) * 0.2f))
                                    .coerceIn(0.08f, 1f)
                                wave
                            } else {
                                0.08f
                            }
                            Box(
                                modifier = Modifier
                                    .width(6.dp)
                                    .height((50 * factor).dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(if (isRecording) SoftGreenPrimary else SoftGreenSurfaceVariant)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (isRecording) {
                                visualizer.stopListening()
                            } else {
                                val hasPerm = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasPerm) {
                                    visualizer.startListening(scope)
                                } else {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRecording) Color(0xFFEF4444) else SoftGreenPrimary,
                            contentColor = SoftGreenOnPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_mic_test")
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mic Test",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isRecording) "Hentikan Tes Mikrofon" else "Mulai Tes Suara Suara Anda",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Voice Engine Configurations
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Pengaturan Protokol Voice Chat",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Master Enable
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Aktifkan Voice Chat Dalam Game",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Inject driver audio ke server secara otomatis.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = voiceEnabled,
                            onCheckedChange = { viewModel.toggleVoiceChat(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SoftGreenPrimary,
                                checkedTrackColor = SoftGreenSurfaceVariant
                            )
                        )
                    }

                    // Voice Mode (PTT vs VAD)
                    Column {
                        Text(
                            text = "Mode Aktivasi Suara",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "PTT" to "Push-to-Talk (Tombol)",
                                "VAD" to "Voice Activity (Otomatis)"
                            ).forEach { (mode, label) ->
                                val isSel = voiceMode == mode
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) SoftGreenPrimary else SoftGreenSurfaceVariant)
                                        .clickable { viewModel.setVoiceMode(mode) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) SoftGreenOnPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    // Microphone Gain Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Sensitivitas & Penguat Mikrofon",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${(micGain * 100).toInt()}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoftGreenPrimary
                            )
                        }
                        Slider(
                            value = micGain,
                            onValueChange = { micGain = it },
                            valueRange = 1.0f..3.0f,
                            colors = SliderDefaults.colors(
                                thumbColor = SoftGreenPrimary,
                                activeTrackColor = SoftGreenPrimary,
                                inactiveTrackColor = SoftGreenSurfaceVariant
                            )
                        )
                    }

                    // 3D Spatial Audio Distance
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Jarak Dengar Spatial 3D",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${voiceDistance.toInt()} Meter",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoftGreenPrimary
                            )
                        }
                        Slider(
                            value = voiceDistance,
                            onValueChange = { viewModel.setVoiceDistance(it) },
                            valueRange = 10f..60f,
                            colors = SliderDefaults.colors(
                                thumbColor = SoftGreenPrimary,
                                activeTrackColor = SoftGreenPrimary,
                                inactiveTrackColor = SoftGreenSurfaceVariant
                            )
                        )
                    }

                    // Noise Suppression
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Peredam Bising (Noise Suppression)",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Hilangkan desis kipas, angin, dan suara latar.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = noiseSuppression,
                            onCheckedChange = { viewModel.toggleNoiseSuppression(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SoftGreenPrimary,
                                checkedTrackColor = SoftGreenSurfaceVariant
                            )
                        )
                    }

                    // Echo Cancellation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Anti Gema (Acoustic Echo Cancellation)",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Cegah suara teman keluar dan terpantul kembali.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = echoCancel,
                            onCheckedChange = { viewModel.toggleAcousticEcho(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SoftGreenPrimary,
                                checkedTrackColor = SoftGreenSurfaceVariant
                            )
                        )
                    }
                }
            }
        }

        // Supported Protocols Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Kompatibilitas Voice Chat (Wort It All Server)",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    listOf(
                        "SampVoice v3.8.2" to "Protokol standar server RP terbesar (JGRP, Revival, Valhalla)",
                        "Universal SVF 2.1" to "Buffer adaptif untuk server komunitas lokal & internasional",
                        "VAG 64-Bit Arm64" to "Driver native zero-latency codec Opus 48,000Hz stereo",
                        "GVoice & PTT Overlay" to "Overlay tombol bicara melayang di atas layar game"
                    ).forEach { (name, note) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = "Supported",
                                tint = SoftGreenPrimary,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = note,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
