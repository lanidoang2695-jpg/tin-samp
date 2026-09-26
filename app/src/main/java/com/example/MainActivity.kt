package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.HandlingScreen
import com.example.ui.screens.ModsScreen
import com.example.ui.screens.PerformanceScreen
import com.example.ui.screens.ServerBrowserScreen
import com.example.ui.screens.VoiceStudioScreen
import com.example.ui.theme.SoftGreenBackground
import com.example.ui.theme.SoftGreenOnPrimary
import com.example.ui.theme.SoftGreenPrimary
import com.example.ui.theme.SoftGreenSurface
import com.example.ui.theme.TinSampTheme
import com.example.ui.viewmodel.NavigationSection
import com.example.ui.viewmodel.TinSampViewModel

data class NavItem(
    val section: NavigationSection,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

class MainActivity : ComponentActivity() {

    private val viewModel: TinSampViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TinSampTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: TinSampViewModel) {
    val currentSection by viewModel.currentSection.collectAsState()

    // Handle back button to return to Server list if on another tab
    if (currentSection != NavigationSection.SERVERS) {
        BackHandler {
            viewModel.setNavigationSection(NavigationSection.SERVERS)
        }
    }

    val navItems = listOf(
        NavItem(
            section = NavigationSection.SERVERS,
            label = "Server",
            selectedIcon = Icons.Filled.Dns,
            unselectedIcon = Icons.Outlined.Dns,
            testTag = "nav_servers"
        ),
        NavItem(
            section = NavigationSection.PERFORMANCE,
            label = "Performa",
            selectedIcon = Icons.Filled.Speed,
            unselectedIcon = Icons.Outlined.Speed,
            testTag = "nav_performance"
        ),
        NavItem(
            section = NavigationSection.MODS,
            label = "Mod Engine",
            selectedIcon = Icons.Filled.Extension,
            unselectedIcon = Icons.Outlined.Extension,
            testTag = "nav_mods"
        ),
        NavItem(
            section = NavigationSection.HANDLING,
            label = "Handling",
            selectedIcon = Icons.Filled.DirectionsCar,
            unselectedIcon = Icons.Outlined.DirectionsCar,
            testTag = "nav_handling"
        ),
        NavItem(
            section = NavigationSection.VOICE,
            label = "Voice Chat",
            selectedIcon = Icons.Filled.Mic,
            unselectedIcon = Icons.Outlined.Mic,
            testTag = "nav_voice"
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = SoftGreenBackground,
        bottomBar = {
            NavigationBar(
                containerColor = SoftGreenSurface,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                navItems.forEach { item ->
                    val isSelected = currentSection == item.section
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setNavigationSection(item.section) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SoftGreenOnPrimary,
                            selectedTextColor = SoftGreenPrimary,
                            indicatorColor = SoftGreenPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentSection) {
            NavigationSection.SERVERS -> ServerBrowserScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            NavigationSection.PERFORMANCE -> PerformanceScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            NavigationSection.MODS -> ModsScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            NavigationSection.HANDLING -> HandlingScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            NavigationSection.VOICE -> VoiceStudioScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
