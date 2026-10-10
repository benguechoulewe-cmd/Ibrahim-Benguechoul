package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ConstructionBlueprint
import com.example.model.GameState
import com.example.model.SimulationSpeed
import com.example.ui.components.TopCommandBar
import com.example.ui.screens.tabs.*
import com.example.ui.theme.*

enum class GameTab(val titleAr: String) {
    ECONOMY("الاقتصاد"),
    MILITARY("العسكري"),
    MINISTERS("الوزراء"),
    SOVEREIGNTY("السيادة"),
    CONSTRUCTION("البناء"),
    CRISES("الأزمات"),
    REPORTS("التقارير")
}

@Composable
fun MainGameScreen(
    gameState: GameState,
    onSpeedChange: (SimulationSpeed) -> Unit,
    onReturnToCountrySelection: () -> Unit,
    onUpdateTaxRates: (Double, Double, Double) -> Unit,
    onToggleMissileDefense: () -> Unit,
    onToggleDroneSwarms: () -> Unit,
    onSetAlertLevel: (String) -> Unit,
    onApproveProposal: (String, String) -> Unit,
    onFireMinister: (String) -> Unit,
    onStartConstruction: (ConstructionBlueprint) -> Unit,
    onResolveCrisisOption: (String, String) -> Unit,
    onExecuteDecree: (String) -> Unit,
    onToggleAutocracyTool: (String) -> Unit,
    onToggleCosmicDomain: (String) -> Unit,
    onToggleLaw: (String) -> Unit,
    onOfferAutonomy: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(GameTab.ECONOMY) }

    // الرجوع لقائمة الدول دون إتلاف تقدم الحملة
    BackHandler {
        onReturnToCountrySelection()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("main_game_screen"),
        topBar = {
            TopCommandBar(
                gameState = gameState,
                onSpeedChange = onSpeedChange,
                onReturnToCountrySelection = onReturnToCountrySelection
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CommandSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("game_bottom_navigation")
            ) {
                GameTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    val icon = when (tab) {
                        GameTab.ECONOMY -> Icons.Default.TrendingUp
                        GameTab.MILITARY -> Icons.Default.Shield
                        GameTab.MINISTERS -> Icons.Default.Group
                        GameTab.SOVEREIGNTY -> Icons.Default.Gavel
                        GameTab.CONSTRUCTION -> Icons.Default.AccountBalance
                        GameTab.CRISES -> Icons.Default.Warning
                        GameTab.REPORTS -> Icons.Default.Assessment
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = { Icon(icon, contentDescription = tab.titleAr, modifier = Modifier.size(20.dp)) },
                        label = { Text(tab.titleAr, fontSize = 9.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = SovereignGold,
                            indicatorColor = SovereignGold,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        },
        containerColor = DeepNavy
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                GameTab.ECONOMY -> EconomyTab(
                    gameState = gameState,
                    onUpdateTaxRates = onUpdateTaxRates
                )
                GameTab.MILITARY -> MilitaryTab(
                    gameState = gameState,
                    onToggleMissileDefense = onToggleMissileDefense,
                    onToggleDroneSwarms = onToggleDroneSwarms,
                    onSetAlertLevel = onSetAlertLevel
                )
                GameTab.MINISTERS -> MinistersTab(
                    gameState = gameState,
                    onApproveProposal = onApproveProposal,
                    onFireMinister = onFireMinister
                )
                GameTab.SOVEREIGNTY -> SovereigntyTab(
                    gameState = gameState,
                    onExecuteDecree = onExecuteDecree,
                    onToggleAutocracyTool = onToggleAutocracyTool,
                    onToggleCosmicDomain = onToggleCosmicDomain,
                    onToggleLaw = onToggleLaw,
                    onOfferAutonomy = onOfferAutonomy
                )
                GameTab.CONSTRUCTION -> ConstructionTab(
                    gameState = gameState,
                    onStartConstruction = onStartConstruction
                )
                GameTab.CRISES -> CrisesTab(
                    gameState = gameState,
                    onResolveOption = onResolveCrisisOption
                )
                GameTab.REPORTS -> ReportsTab(
                    gameState = gameState
                )
            }
        }
    }
}
