package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServerEntity
import com.example.ui.theme.SoftGreenBackground
import com.example.ui.theme.SoftGreenCard
import com.example.ui.theme.SoftGreenOnPrimary
import com.example.ui.theme.SoftGreenOutline
import com.example.ui.theme.SoftGreenPrimary
import com.example.ui.theme.SoftGreenSecondary
import com.example.ui.theme.SoftGreenSurfaceVariant
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.StatusYellow
import com.example.ui.viewmodel.ConnectionProgressState

@Composable
fun PingBadge(ping: Int, modifier: Modifier = Modifier) {
    val color = when {
        ping < 45 -> StatusGreen
        ping < 90 -> StatusYellow
        else -> StatusRed
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.SignalCellularAlt,
            contentDescription = "Ping",
            tint = color,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${ping}ms",
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun VoiceChatBadge(protocol: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SoftGreenPrimary.copy(alpha = 0.18f))
            .border(1.dp, SoftGreenPrimary.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Voice Chat Active",
            tint = SoftGreenPrimary,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = protocol,
            color = SoftGreenPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ServerCard(
    server: ServerEntity,
    onServerClick: () -> Unit,
    onConnectClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onServerClick() }
            .testTag("server_card_${server.id}"),
        colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = server.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${server.ip}:${server.port}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftGreenSecondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (server.isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (server.isFavorite) StatusYellow else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges row: Gamemode, Players, Voice, Ping
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gamemode
                Text(
                    text = server.gamemode,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SoftGreenSurfaceVariant)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Players count
                Text(
                    text = "${server.players}/${server.maxPlayers}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SoftGreenSurfaceVariant)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )

                if (server.hasVoiceChat) {
                    VoiceChatBadge(protocol = server.voiceProtocol)
                }

                Spacer(modifier = Modifier.weight(1f))

                PingBadge(ping = server.ping)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Connect button
            Button(
                onClick = onConnectClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .testTag("connect_btn_${server.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftGreenPrimary,
                    contentColor = SoftGreenOnPrimary
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Main",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "MAIN SEKARANG",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun ConnectingDialog(
    state: ConnectionProgressState,
    onLaunchGame: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { if (state.isComplete) onDismiss() },
        containerColor = SoftGreenCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (state.isComplete) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Ready",
                        tint = SoftGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = SoftGreenPrimary,
                        strokeWidth = 2.5.dp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (state.isComplete) "Klien Siap Dimainkan!" else "Menyiapkan TIN SAMP...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = state.server.name,
                    fontWeight = FontWeight.SemiBold,
                    color = SoftGreenSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = "${state.server.ip}:${state.server.port}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { (state.stage / 4f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = SoftGreenPrimary,
                    trackColor = SoftGreenSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.stageMessage,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (state.isComplete) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SoftGreenBackground),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "File settings.ini & Voice Buffer terkonfigurasi otomatis untuk Android 16 (64-Bit).",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (state.isComplete) {
                Button(
                    onClick = onLaunchGame,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftGreenPrimary,
                        contentColor = SoftGreenOnPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Buka Game",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Luncurkan GTA SA")
                }
            }
        },
        dismissButton = {
            Row {
                if (state.isComplete) {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("SAMP Settings", state.configText))
                            Toast.makeText(context, "Konfigurasi disalin ke clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftGreenSecondary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Salin Config")
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
                TextButton(onClick = onDismiss) {
                    Text("Tutup", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    )
}

@Composable
fun AddServerDialog(
    onDismiss: () -> Unit,
    onAddServer: (name: String, ip: String, port: Int, pass: String, mode: String, voice: Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ip by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("7777") }
    var password by remember { mutableStateOf("") }
    var gamemode by remember { mutableStateOf("Roleplay") }
    var hasVoice by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SoftGreenCard,
        title = {
            Text(
                text = "Tambah Server Kustom",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Server") },
                    placeholder = { Text("Misal: Indo Freeroam 2026") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_server_name")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = ip,
                        onValueChange = { ip = it },
                        label = { Text("IP / Domain") },
                        placeholder = { Text("play.myserver.id") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(2f)
                            .testTag("input_server_ip")
                    )

                    OutlinedTextField(
                        value = port,
                        onValueChange = { port = it.filter { c -> c.isDigit() } },
                        label = { Text("Port") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_server_port")
                    )
                }

                OutlinedTextField(
                    value = gamemode,
                    onValueChange = { gamemode = it },
                    label = { Text("Gamemode") },
                    placeholder = { Text("Roleplay / Drift") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password Server (Opsional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Universal Voice Chat",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Switch(
                        checked = hasVoice,
                        onCheckedChange = { hasVoice = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SoftGreenPrimary,
                            checkedTrackColor = SoftGreenSurfaceVariant
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (ip.isNotBlank()) {
                        val portInt = port.toIntOrNull() ?: 7777
                        onAddServer(name, ip, portInt, password, gamemode, hasVoice)
                        onDismiss()
                    }
                },
                enabled = ip.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftGreenPrimary,
                    contentColor = SoftGreenOnPrimary
                ),
                modifier = Modifier.testTag("btn_save_server")
            ) {
                Text("Simpan Server")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
