package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.model.HandlingPresetEntity
import com.example.ui.theme.SoftGreenBackground
import com.example.ui.theme.SoftGreenCard
import com.example.ui.theme.SoftGreenOnPrimary
import com.example.ui.theme.SoftGreenOutline
import com.example.ui.theme.SoftGreenPrimary
import com.example.ui.theme.SoftGreenSecondary
import com.example.ui.theme.SoftGreenSurfaceVariant
import com.example.util.SampConfigWriter
import com.example.ui.viewmodel.TinSampViewModel

@Composable
fun HandlingScreen(
    viewModel: TinSampViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val presets by viewModel.handlingPresets.collectAsState()

    var selectedVehicleName by remember { mutableStateOf("Elegy") }
    var mass by remember { mutableFloatStateOf(1420f) }
    var drag by remember { mutableFloatStateOf(1.6f) }
    var steerLock by remember { mutableFloatStateOf(42f) }
    var suspension by remember { mutableFloatStateOf(1.8f) }
    var driveType by remember { mutableStateOf("RWD") }
    var acceleration by remember { mutableFloatStateOf(0.45f) }
    var maxVelocity by remember { mutableFloatStateOf(230f) }
    var brakeBias by remember { mutableFloatStateOf(0.58f) }

    var showSaveDialog by remember { mutableStateOf(false) }

    val currentPresetObj = HandlingPresetEntity(
        name = "Live Custom",
        vehicleModel = selectedVehicleName,
        mass = mass,
        dragMultiplier = drag,
        steeringLock = steerLock,
        suspensionForce = suspension,
        driveType = driveType,
        engineAcceleration = acceleration,
        maxVelocity = maxVelocity,
        brakeBias = brakeBias
    )
    val rawHandlingLine = SampConfigWriter.generateHandlingCfgLine(currentPresetObj)

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
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Handling",
                        tint = SoftGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIVE HANDLING EDITOR",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Kustomisasi fisika kendaraan real-time, drift angle, akselerasi, & suspensi GTA SA.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Preset Selector Chips
        item {
            Column {
                Text(
                    text = "Pilih Preset Kendaraan:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(presets) { p ->
                        val isSelected = selectedVehicleName == p.vehicleModel
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) SoftGreenPrimary else SoftGreenCard)
                                .border(
                                    1.dp,
                                    if (isSelected) SoftGreenPrimary else SoftGreenOutline,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedVehicleName = p.vehicleModel
                                    mass = p.mass
                                    drag = p.dragMultiplier
                                    steerLock = p.steeringLock
                                    suspension = p.suspensionForce
                                    driveType = p.driveType
                                    acceleration = p.engineAcceleration
                                    maxVelocity = p.maxVelocity
                                    brakeBias = p.brakeBias
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Column {
                                Text(
                                    text = p.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) SoftGreenOnPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${p.vehicleModel} • ${p.driveType}",
                                    fontSize = 10.sp,
                                    color = if (isSelected) SoftGreenOnPrimary.copy(alpha = 0.8f) else SoftGreenSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Generated handling.cfg Line Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Baris handling.cfg (Real-time):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SoftGreenPrimary
                        )
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Handling CFG", rawHandlingLine))
                                Toast.makeText(context, "Kode handling disalin!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = SoftGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SoftGreenBackground)
                            .horizontalScroll(rememberScrollState())
                            .padding(10.dp)
                    ) {
                        Text(
                            text = rawHandlingLine,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = SoftGreenSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Handling $selectedVehicleName diinjeksi via MonetLoader & AML!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SoftGreenPrimary,
                                contentColor = SoftGreenOnPrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Upload,
                                contentDescription = "Inject",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Injeksi Langsung", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { showSaveDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftGreenSecondary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Save",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Simpan", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Sliders & Tuning Controls
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Pengaturan Parameter Fisika",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Drive Type Selector
                    Column {
                        Text(
                            text = "Tipe Penggerak Roda (Drive Type)",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "RWD" to "RWD (Drift Belakang)",
                                "AWD" to "AWD (4 Roda Grip)",
                                "FWD" to "FWD (Roda Depan)"
                            ).forEach { (type, label) ->
                                val isSel = driveType == type
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) SoftGreenPrimary else SoftGreenSurfaceVariant)
                                        .clickable { driveType = type }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) SoftGreenOnPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    // Steering Lock Angle
                    HandlingSliderItem(
                        label = "Sudut Belok (Steering Lock)",
                        valueText = "${steerLock.toInt()}°",
                        value = steerLock,
                        onValueChange = { steerLock = it },
                        valueRange = 25f..55f
                    )

                    // Acceleration
                    HandlingSliderItem(
                        label = "Akselerasi Mesin",
                        valueText = "%.2f".format(acceleration),
                        value = acceleration,
                        onValueChange = { acceleration = it },
                        valueRange = 0.20f..0.80f
                    )

                    // Max Velocity
                    HandlingSliderItem(
                        label = "Kecepatan Maksimal (Max Velocity)",
                        valueText = "${maxVelocity.toInt()} km/h",
                        value = maxVelocity,
                        onValueChange = { maxVelocity = it },
                        valueRange = 150f..320f
                    )

                    // Suspension Force
                    HandlingSliderItem(
                        label = "Kekakuan Suspensi (Suspension Force)",
                        valueText = "%.2f".format(suspension),
                        value = suspension,
                        onValueChange = { suspension = it },
                        valueRange = 0.8f..3.0f
                    )

                    // Drag Multiplier
                    HandlingSliderItem(
                        label = "Hambatan Udara (Drag Multiplier)",
                        valueText = "%.2f".format(drag),
                        value = drag,
                        onValueChange = { drag = it },
                        valueRange = 1.0f..3.0f
                    )

                    // Mass
                    HandlingSliderItem(
                        label = "Bobot Kendaraan (Mass)",
                        valueText = "${mass.toInt()} kg",
                        value = mass,
                        onValueChange = { mass = it },
                        valueRange = 600f..3000f
                    )
                }
            }
        }
    }

    if (showSaveDialog) {
        var presetName by remember { mutableStateOf("Custom $selectedVehicleName") }
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            containerColor = SoftGreenCard,
            title = {
                Text(
                    text = "Simpan Preset Handling",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column {
                    Text(
                        text = "Simpan setelan handling saat ini ke database lokal TIN SAMP.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = presetName,
                        onValueChange = { presetName = it },
                        label = { Text("Nama Preset") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (presetName.isNotBlank()) {
                            viewModel.addHandlingPreset(
                                name = presetName,
                                model = selectedVehicleName,
                                mass = mass,
                                drag = drag,
                                steerLock = steerLock,
                                suspension = suspension,
                                driveType = driveType,
                                acceleration = acceleration,
                                maxVelocity = maxVelocity,
                                brakeBias = brakeBias
                            )
                            showSaveDialog = false
                            Toast.makeText(context, "Preset $presetName tersimpan!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftGreenPrimary,
                        contentColor = SoftGreenOnPrimary
                    )
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

@Composable
fun HandlingSliderItem(
    label: String,
    valueText: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = valueText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SoftGreenPrimary
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = SoftGreenPrimary,
                activeTrackColor = SoftGreenPrimary,
                inactiveTrackColor = SoftGreenSurfaceVariant
            )
        )
    }
}
