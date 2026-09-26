package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ModEntity
import com.example.ui.theme.SoftGreenBackground
import com.example.ui.theme.SoftGreenCard
import com.example.ui.theme.SoftGreenOnPrimary
import com.example.ui.theme.SoftGreenOutline
import com.example.ui.theme.SoftGreenPrimary
import com.example.ui.theme.SoftGreenSecondary
import com.example.ui.theme.SoftGreenSurfaceVariant
import com.example.ui.viewmodel.TinSampViewModel

@Composable
fun ModsScreen(
    viewModel: TinSampViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allMods by viewModel.mods.collectAsState()
    var selectedFilter by remember { mutableStateOf("ALL") }
    var showAddModDialog by remember { mutableStateOf(false) }

    val filteredMods = when (selectedFilter) {
        "AML" -> allMods.filter { it.modType == "AML_SO" }
        "MONET" -> allMods.filter { it.modType == "MONETLOADER_LUA" }
        "CLEO" -> allMods.filter { it.modType == "CLEO_CSA" }
        else -> allMods
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SoftGreenBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddModDialog = true },
                containerColor = SoftGreenPrimary,
                contentColor = SoftGreenOnPrimary,
                modifier = Modifier.testTag("fab_add_mod")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Mod")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Info
            item {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Extension,
                            contentDescription = "Mods",
                            tint = SoftGreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "MOD & ENGINE LOADER",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Dukungan penuh MonetLoader (Lua), Android Mod Loader (AML 64-bit), dan CLEO Touch.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Engine status cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EngineBadge(
                        title = "AML 64-Bit",
                        version = "v1.2.7",
                        status = "Aktif",
                        modifier = Modifier.weight(1f)
                    )
                    EngineBadge(
                        title = "MonetLoader",
                        version = "v3.2.0",
                        status = "Aktif",
                        modifier = Modifier.weight(1f)
                    )
                    EngineBadge(
                        title = "CLEO Android",
                        version = "v4.0.1",
                        status = "Aktif",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Filter Chips
            item {
                val filters = listOf(
                    "ALL" to "Semua Mod (${allMods.size})",
                    "AML" to "AML (.so)",
                    "MONET" to "MonetLoader (.lua)",
                    "CLEO" to "CLEO (.csa)"
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filters) { (key, label) ->
                        val isSelected = selectedFilter == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) SoftGreenPrimary else SoftGreenCard)
                                .border(
                                    1.dp,
                                    if (isSelected) SoftGreenPrimary else SoftGreenOutline,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedFilter = key }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) SoftGreenOnPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Mods List
            items(filteredMods, key = { it.id }) { mod ->
                ModCardItem(
                    mod = mod,
                    onToggle = { viewModel.toggleModEnabled(mod) }
                )
            }
        }
    }

    if (showAddModDialog) {
        var modName by remember { mutableStateOf("") }
        var modType by remember { mutableStateOf("MONETLOADER_LUA") }
        var fileName by remember { mutableStateOf("") }
        var author by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddModDialog = false },
            containerColor = SoftGreenCard,
            title = {
                Text(
                    text = "Daftarkan Mod Baru",
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
                        value = modName,
                        onValueChange = { modName = it },
                        label = { Text("Nama Mod") },
                        placeholder = { Text("Anti Crash & Fast Map") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Mod Type Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "MONETLOADER_LUA" to "Monet (.lua)",
                            "AML_SO" to "AML (.so)",
                            "CLEO_CSA" to "CLEO (.csa)"
                        ).forEach { (type, label) ->
                            val isSel = modType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) SoftGreenPrimary else SoftGreenSurfaceVariant)
                                    .clickable { modType = type }
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

                    OutlinedTextField(
                        value = fileName,
                        onValueChange = { fileName = it },
                        label = { Text("Nama File Script / Lib") },
                        placeholder = { Text("custom_script.lua") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Author / Pembuat") },
                        placeholder = { Text("TinDev") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Deskripsi Mod") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (modName.isNotBlank() && fileName.isNotBlank()) {
                            viewModel.addNewMod(modName, modType, fileName, author, desc)
                            showAddModDialog = false
                            Toast.makeText(context, "Mod berhasil didaftarkan ke TIN SAMP!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftGreenPrimary,
                        contentColor = SoftGreenOnPrimary
                    )
                ) {
                    Text("Simpan Mod")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddModDialog = false }) {
                    Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

@Composable
fun EngineBadge(
    title: String,
    version: String,
    status: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = SoftGreenPrimary
            )
            Text(
                text = version,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = status,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = SoftGreenOnPrimary,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(SoftGreenPrimary)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun ModCardItem(
    mod: ModEntity,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SoftGreenCard),
        shape = RoundedCornerShape(14.dp)
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
                            text = mod.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        if (mod.supports64Bit) {
                            Text(
                                text = "64-BIT",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoftGreenPrimary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SoftGreenPrimary.copy(alpha = 0.15f))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${mod.fileName} • ${mod.author} • ${mod.version}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = SoftGreenSecondary
                    )
                }

                Switch(
                    checked = mod.isEnabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SoftGreenPrimary,
                        checkedTrackColor = SoftGreenSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = mod.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
