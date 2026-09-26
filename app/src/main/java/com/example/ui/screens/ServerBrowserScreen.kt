package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ServerEntity
import com.example.ui.components.AddServerDialog
import com.example.ui.components.ConnectingDialog
import com.example.ui.components.ServerCard
import com.example.ui.theme.SoftGreenBackground
import com.example.ui.theme.SoftGreenCard
import com.example.ui.theme.SoftGreenOnPrimary
import com.example.ui.theme.SoftGreenOutline
import com.example.ui.theme.SoftGreenPrimary
import com.example.ui.theme.SoftGreenSecondary
import com.example.ui.theme.SoftGreenSurfaceVariant
import com.example.ui.viewmodel.ServerTab
import com.example.ui.viewmodel.TinSampViewModel

@Composable
fun ServerBrowserScreen(
    viewModel: TinSampViewModel,
    modifier: Modifier = Modifier
) {
    val servers by viewModel.filteredServers.collectAsState()
    val allServers by viewModel.allServers.collectAsState()
    val selectedTab by viewModel.selectedServerTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val nickname by viewModel.nickname.collectAsState()
    val connectingState by viewModel.connectingState.collectAsState()
    val showAddDialog by viewModel.showAddServerDialog.collectAsState()
    val selectedServerForDetail by viewModel.selectedServerForDetail.collectAsState()

    var showEditNicknameDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SoftGreenBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setShowAddServerDialog(true) },
                containerColor = SoftGreenPrimary,
                contentColor = SoftGreenOnPrimary,
                modifier = Modifier.testTag("fab_add_server")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Server")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Hero Banner Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner),
                        contentDescription = "GTA San Andreas Multiplayer Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient overlay to soft green background
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        SoftGreenBackground.copy(alpha = 0.7f),
                                        SoftGreenBackground
                                    )
                                )
                            )
                    )

                    // Header Info Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "TIN SAMP",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                        color = SoftGreenPrimary,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "OVERPOWER",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SoftGreenOnPrimary,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(SoftGreenPrimary)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = "64-Bit Android 16 • MonetLoader • AML • CLEO",
                                    fontSize = 11.sp,
                                    color = SoftGreenSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // Nickname Chip
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(SoftGreenCard)
                                    .border(1.dp, SoftGreenOutline, RoundedCornerShape(20.dp))
                                    .clickable { showEditNicknameDialog = true }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Player Name",
                                    tint = SoftGreenPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = nickname,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Search Bar & Stats
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("Cari server, IP, mode roleplay...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = SoftGreenPrimary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_server_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SoftGreenCard,
                            unfocusedContainerColor = SoftGreenCard,
                            focusedBorderColor = SoftGreenPrimary,
                            unfocusedBorderColor = SoftGreenOutline
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Stats summary bar
                    val totalPlayers = allServers.sumOf { it.players }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${servers.size} Server Tersedia",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SoftGreenPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$totalPlayers Pemain Online",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SoftGreenSecondary
                            )
                        }
                    }
                }
            }

            // Category Tab Row
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = Color.Transparent,
                    contentColor = SoftGreenPrimary,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                            color = SoftGreenPrimary,
                            height = 3.dp
                        )
                    },
                    divider = {}
                ) {
                    ServerTab.entries.forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { viewModel.setServerTab(tab) },
                            text = {
                                Text(
                                    text = tab.label,
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == tab) SoftGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Server Card List or Empty State
            if (servers.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Tidak ada server",
                                modifier = Modifier.size(48.dp),
                                tint = SoftGreenOutline
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Tidak ada server ditemukan",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Coba ubah kata kunci atau tambahkan server kustom via tombol +.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(servers, key = { it.id }) { server ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        ServerCard(
                            server = server,
                            onServerClick = { viewModel.openServerDetail(server) },
                            onConnectClick = { viewModel.startConnectSequence(server) },
                            onFavoriteToggle = { viewModel.toggleFavorite(server) }
                        )
                    }
                }
            }
        }
    }

    // Edit Nickname Dialog
    if (showEditNicknameDialog) {
        var tempName by remember { mutableStateOf(nickname) }
        AlertDialog(
            onDismissRequest = { showEditNicknameDialog = false },
            containerColor = SoftGreenCard,
            title = {
                Text(
                    text = "Ganti Nickname Pemain",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column {
                    Text(
                        text = "Format nama roleplay SAMP: Nama_Belakang (tanpa spasi).",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it.replace(" ", "_") },
                        label = { Text("Nickname") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (tempName.isNotBlank()) {
                            viewModel.setNickname(tempName)
                            showEditNicknameDialog = false
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
                TextButton(onClick = { showEditNicknameDialog = false }) {
                    Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    // Add Server Dialog
    if (showAddDialog) {
        AddServerDialog(
            onDismiss = { viewModel.setShowAddServerDialog(false) },
            onAddServer = { name, ip, port, pass, mode, voice ->
                viewModel.addCustomServer(name, ip, port, pass, mode, voice)
            }
        )
    }

    // Selected Server Detail Dialog
    selectedServerForDetail?.let { server ->
        AlertDialog(
            onDismissRequest = { viewModel.closeServerDetail() },
            containerColor = SoftGreenCard,
            title = {
                Text(
                    text = server.name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Alamat IP: ${server.ip}:${server.port}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = SoftGreenSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mode Game: ${server.gamemode}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Protokol Voice: ${if (server.hasVoiceChat) server.voiceProtocol else "Tidak Aktif"}",
                        fontSize = 13.sp,
                        color = SoftGreenPrimary
                    )
                    Text(
                        text = "Pemain: ${server.players} / ${server.maxPlayers} Online",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Ping Latency: ${server.ping} ms",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (server.customNotes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = server.customNotes,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (!server.isOfficial) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {
                                viewModel.deleteServer(server)
                                viewModel.closeServerDetail()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                        ) {
                            Text("Hapus Server Ini")
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.closeServerDetail()
                        viewModel.startConnectSequence(server)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftGreenPrimary,
                        contentColor = SoftGreenOnPrimary
                    )
                ) {
                    Text("Konek & Mainkan")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeServerDetail() }) {
                    Text("Tutup", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    // Connecting Sequence Dialog
    connectingState?.let { state ->
        ConnectingDialog(
            state = state,
            onLaunchGame = { viewModel.launchGameDirectly(state.server) },
            onDismiss = { viewModel.cancelConnection() }
        )
    }
}
