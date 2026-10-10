package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.GameViewModel
import com.example.ui.screens.CountrySelectionScreen
import com.example.ui.screens.MainGameScreen
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.MoneyWarsTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoneyWarsTheme {
                CompositionLocalProvider(androidx.compose.ui.platform.LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Rtl) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = DeepNavy
                    ) {
                        MoneyWarsApp(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MoneyWarsApp(viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()
    val selectedCountry by viewModel.selectedCountry.collectAsState()
    val isInCampaign by viewModel.isInCampaign.collectAsState()

    val activeGame = gameState
    if (isInCampaign && activeGame != null) {
        MainGameScreen(
            gameState = activeGame,
            onSpeedChange = { viewModel.setSpeed(it) },
            onReturnToCountrySelection = { viewModel.returnToCountrySelection() },
            onUpdateTaxRates = { corp, inc, tar -> viewModel.updateTaxRates(corp, inc, tar) },
            onToggleMissileDefense = { viewModel.toggleMissileDefense() },
            onToggleDroneSwarms = { viewModel.toggleDroneSwarms() },
            onSetAlertLevel = { viewModel.setAlertLevel(it) },
            onApproveProposal = { mId, pId -> viewModel.approveMinisterProposal(mId, pId) },
            onFireMinister = { viewModel.fireMinister(it) },
            onStartConstruction = { viewModel.startConstruction(it) },
            onResolveCrisisOption = { cId, chId -> viewModel.resolveCrisisOption(cId, chId) },
            onExecuteDecree = { viewModel.executeDecree(it) },
            onToggleAutocracyTool = { viewModel.toggleAutocracyTool(it) },
            onToggleCosmicDomain = { viewModel.toggleCosmicDomain(it) },
            onToggleLaw = { viewModel.toggleLaw(it) },
            onOfferAutonomy = { viewModel.offerAutonomy(it) }
        )
    } else {
        CountrySelectionScreen(
            gameState = gameState,
            selectedCountry = selectedCountry,
            onCountrySelected = { viewModel.selectCountry(it) },
            onStartCampaign = { viewModel.startCampaign(it) },
            onResumeCampaign = if (gameState?.isCampaignStarted == true) {
                { viewModel.resumeCampaign() }
            } else null
        )
    }
}
