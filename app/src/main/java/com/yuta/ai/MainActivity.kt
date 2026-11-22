package com.yuta.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yuta.ai.data.ChatViewModel
import com.yuta.ai.ui.screens.ChatScreen
import com.yuta.ai.ui.screens.HistoryScreen
import com.yuta.ai.ui.screens.HomeScreen
import com.yuta.ai.ui.screens.ImageScreen
import com.yuta.ai.ui.screens.ProfileScreen
import com.yuta.ai.ui.screens.ToolsScreen
import com.yuta.ai.ui.screens.VoiceScreen
import com.yuta.ai.ui.theme.YutaAccentViolet
import com.yuta.ai.ui.theme.YutaBackground
import com.yuta.ai.ui.theme.YutaBrandGradient
import com.yuta.ai.ui.theme.YutaSurface
import com.yuta.ai.ui.theme.YutaTextMuted
import com.yuta.ai.ui.theme.YutaTheme

private object Routes {
    const val HOME = "home"
    const val TOOLS = "tools"
    const val HISTORY = "history"
    const val PROFILE = "profile"
    const val CHAT = "chat"
    const val CREATE = "create"
    const val VOICE = "voice"
}

// Routes that show the bottom navigation bar.
private val rootRoutes = setOf(Routes.HOME, Routes.TOOLS, Routes.HISTORY, Routes.PROFILE)

class MainActivity : ComponentActivity() {
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YutaTheme {
                YutaRoot(chatViewModel)
            }
        }
    }
}

@Composable
private fun YutaRoot(viewModel: ChatViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute == null || currentRoute in rootRoutes

    Scaffold(
        containerColor = YutaBackground,
        bottomBar = {
            if (showBottomBar) {
                YutaBottomBar(
                    currentRoute = currentRoute ?: Routes.HOME,
                    onHome = { navController.navigateToRoot(Routes.HOME) },
                    onTools = { navController.navigateToRoot(Routes.TOOLS) },
                    onCenter = { navController.navigate(Routes.CHAT) },
                    onHistory = { navController.navigateToRoot(Routes.HISTORY) },
                    onProfile = { navController.navigateToRoot(Routes.PROFILE) },
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(bottom = if (showBottomBar) padding.calculateBottomPadding() else 0.dp)) {
            NavHost(navController = navController, startDestination = Routes.HOME) {
                composable(Routes.HOME) {
                    HomeScreen(
                        viewModel = viewModel,
                        onOpenChat = { navController.navigate(Routes.CHAT) },
                        onOpenCreate = { navController.navigate(Routes.CREATE) },
                        onOpenVoice = { navController.navigate(Routes.VOICE) },
                        onOpenTools = { navController.navigateToRoot(Routes.TOOLS) },
                        onOpenHistory = { navController.navigateToRoot(Routes.HISTORY) },
                        onOpenProfile = { navController.navigateToRoot(Routes.PROFILE) },
                    )
                }
                composable(Routes.TOOLS) {
                    ToolsScreen(
                        viewModel = viewModel,
                        onOpenChat = { navController.navigate(Routes.CHAT) },
                        onOpenCreate = { navController.navigate(Routes.CREATE) },
                        onOpenVoice = { navController.navigate(Routes.VOICE) },
                    )
                }
                composable(Routes.HISTORY) {
                    HistoryScreen(viewModel = viewModel, onOpenChat = { navController.navigate(Routes.CHAT) })
                }
                composable(Routes.PROFILE) {
                    ProfileScreen(viewModel = viewModel)
                }
                composable(Routes.CHAT) {
                    ChatScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                }
                composable(Routes.CREATE) {
                    ImageScreen(onBack = { navController.popBackStack() })
                }
                composable(Routes.VOICE) {
                    VoiceScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                }
            }
        }
    }
}

private fun NavHostController.navigateToRoot(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun YutaBottomBar(
    currentRoute: String,
    onHome: () -> Unit,
    onTools: () -> Unit,
    onCenter: () -> Unit,
    onHistory: () -> Unit,
    onProfile: () -> Unit,
) {
    NavigationBar(containerColor = YutaSurface, tonalElevation = 0.dp) {
        NavigationBarItem(
            selected = currentRoute == Routes.HOME,
            onClick = onHome,
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = yutaNavColors()
        )
        NavigationBarItem(
            selected = currentRoute == Routes.TOOLS,
            onClick = onTools,
            icon = { Icon(Icons.Filled.Widgets, contentDescription = "Tools") },
            label = { Text("Tools") },
            colors = yutaNavColors()
        )
        NavigationBarItem(
            selected = false,
            onClick = onCenter,
            icon = {
                Box(
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .background(YutaBrandGradient, shape = androidx.compose.foundation.shape.CircleShape)
                        .padding(10.dp)
                ) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = "New chat", tint = androidx.compose.ui.graphics.Color.White)
                }
            },
            label = { Text("") },
            colors = yutaNavColors()
        )
        NavigationBarItem(
            selected = currentRoute == Routes.HISTORY,
            onClick = onHistory,
            icon = { Icon(Icons.Filled.History, contentDescription = "History") },
            label = { Text("History") },
            colors = yutaNavColors()
        )
        NavigationBarItem(
            selected = currentRoute == Routes.PROFILE,
            onClick = onProfile,
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = yutaNavColors()
        )
    }
}

@Composable
private fun yutaNavColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = androidx.compose.ui.graphics.Color.White,
    selectedTextColor = androidx.compose.ui.graphics.Color.White,
    unselectedIconColor = YutaTextMuted,
    unselectedTextColor = YutaTextMuted,
    indicatorColor = YutaAccentViolet
)
