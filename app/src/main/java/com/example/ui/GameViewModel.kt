package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SaveManager
import com.example.data.WorldDataCatalog
import com.example.model.*
import com.example.simulation.MinistersEngine
import com.example.simulation.SimulationEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _selectedCountry = MutableStateFlow<Country>(WorldDataCatalog.allCountries.first())
    val selectedCountry: StateFlow<Country> = _selectedCountry.asStateFlow()

    private val _isInCampaign = MutableStateFlow(false)
    val isInCampaign: StateFlow<Boolean> = _isInCampaign.asStateFlow()

    init {
        // Attempt restoring last valid auto-save
        viewModelScope.launch(Dispatchers.IO) {
            val savedState = SaveManager.loadGame(getApplication())
            if (savedState != null) {
                _gameState.value = savedState
                val c = WorldDataCatalog.getCountryById(savedState.playerCountryId)
                if (c != null) {
                    _selectedCountry.value = c
                }
            }
        }

        // Background simulation loop
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                val current = _gameState.value
                if (current != null && current.isCampaignStarted && current.speed != SimulationSpeed.PAUSED) {
                    val delayMs = when (current.speed) {
                        SimulationSpeed.NORMAL -> 1500L
                        SimulationSpeed.FAST -> 750L
                        SimulationSpeed.ULTRA -> 250L
                        SimulationSpeed.PAUSED -> 1500L
                    }
                    delay(delayMs)

                    // Execute day tick
                    val updated = SimulationEngine.tickDay(current)
                    _gameState.value = updated

                    // Periodic auto-save every 15 simulation days
                    if (updated.currentDay % 15 == 0) {
                        launch(Dispatchers.IO) {
                            SaveManager.saveGame(getApplication(), updated)
                        }
                    }
                } else {
                    delay(500L)
                }
            }
        }
    }

    fun selectCountry(country: Country) {
        _selectedCountry.value = country
    }

    fun startCampaign(country: Country) {
        viewModelScope.launch(Dispatchers.Default) {
            val freshState = SimulationEngine.createInitialState(country.id)
            _gameState.value = freshState
            _selectedCountry.value = country
            _isInCampaign.value = true

            // Immediate initial save
            launch(Dispatchers.IO) {
                SaveManager.saveGame(getApplication(), freshState)
            }
        }
    }

    fun resumeCampaign() {
        if (_gameState.value?.isCampaignStarted == true) {
            _isInCampaign.value = true
        }
    }

    fun returnToCountrySelection() {
        _isInCampaign.value = false
        // Persist when exiting campaign screen
        val current = _gameState.value
        if (current != null) {
            viewModelScope.launch(Dispatchers.IO) {
                SaveManager.saveGame(getApplication(), current)
            }
        }
    }

    fun setSpeed(speed: SimulationSpeed) {
        _gameState.update { it?.copy(speed = speed) }
    }

    fun updateTaxRates(corp: Double, income: Double, tariff: Double) {
        _gameState.update { current ->
            if (current == null) null
            else {
                val updatedEcon = current.economy.copy(
                    corporateTaxPct = corp,
                    personalIncomeTaxPct = income,
                    tradeTariffPct = tariff
                )
                current.copy(economy = updatedEcon)
            }
        }
    }

    fun toggleMissileDefense() {
        _gameState.update { current ->
            if (current == null) null
            else {
                val updatedMil = current.military.copy(
                    missileDefenseActive = !current.military.missileDefenseActive
                )
                current.copy(military = updatedMil)
            }
        }
    }

    fun toggleDroneSwarms() {
        _gameState.update { current ->
            if (current == null) null
            else {
                val updatedMil = current.military.copy(
                    droneSwarmsActive = !current.military.droneSwarmsActive
                )
                current.copy(military = updatedMil)
            }
        }
    }

    fun setAlertLevel(level: String) {
        _gameState.update { current ->
            if (current == null) null
            else {
                val updatedMil = current.military.copy(warReadinessAlert = level)
                current.copy(military = updatedMil)
            }
        }
    }

    fun approveMinisterProposal(ministerId: String, proposalId: String) {
        _gameState.update { current ->
            if (current == null) null
            else {
                val minister = current.ministers.find { it.id == ministerId }
                val proposal = minister?.activeProposal
                if (proposal != null && !proposal.isApproved && current.economy.treasuryCashBillionUsd >= proposal.costBillionUsd) {
                    val updatedMinisters = current.ministers.map { min ->
                        if (min.id == ministerId) min.copy(activeProposal = proposal.copy(isApproved = true), loyalty = kotlin.math.min(100, min.loyalty + 5))
                        else min
                    }
                    val updatedEcon = current.economy.copy(
                        treasuryCashBillionUsd = current.economy.treasuryCashBillionUsd - proposal.costBillionUsd,
                        gdpGrowthAnnualPct = current.economy.gdpGrowthAnnualPct + proposal.gdpGrowthImpact
                    )
                    current.copy(
                        economy = updatedEcon,
                        ministers = updatedMinisters
                    )
                } else current
            }
        }
    }

    fun fireMinister(ministerId: String) {
        _gameState.update { current ->
            if (current == null) null
            else {
                val targetMinister = current.ministers.find { it.id == ministerId } ?: return@update current
                val newCandidate = MinistersEngine.generateCandidate(targetMinister.portfolio)
                val updatedMinisters = current.ministers.map { min ->
                    if (min.id == ministerId) newCandidate else min
                }
                current.copy(ministers = updatedMinisters)
            }
        }
    }

    fun startConstruction(blueprint: ConstructionBlueprint) {
        _gameState.update { current ->
            if (current == null) null
            else {
                if (current.economy.treasuryCashBillionUsd >= blueprint.costBillionUsd &&
                    current.economy.mineralsStockpile >= blueprint.mineralsRequired
                ) {
                    val newActive = com.example.model.ActiveProject(
                        blueprint = blueprint,
                        daysRemaining = blueprint.buildDays,
                        progressPct = 0.0f
                    )
                    val updatedQueue = current.constructionQueue + newActive
                    val updatedEcon = current.economy.copy(
                        treasuryCashBillionUsd = current.economy.treasuryCashBillionUsd - blueprint.costBillionUsd,
                        mineralsStockpile = current.economy.mineralsStockpile - blueprint.mineralsRequired
                    )
                    current.copy(
                        constructionQueue = updatedQueue,
                        economy = updatedEcon
                    )
                } else current
            }
        }
    }

    fun resolveCrisisOption(crisisId: String, choiceId: String) {
        _gameState.update { current ->
            if (current == null) null
            else {
                val crisis = current.activeCrises.find { it.id == crisisId } ?: return@update current
                val choice = crisis.responseOptions.find { it.id == choiceId } ?: return@update current

                if (current.economy.treasuryCashBillionUsd >= choice.financialCostBillionUsd) {
                    val updatedCrises = current.activeCrises.filter { it.id != crisisId }
                    val updatedEcon = current.economy.copy(
                        treasuryCashBillionUsd = current.economy.treasuryCashBillionUsd - choice.financialCostBillionUsd
                    )
                    current.copy(
                        activeCrises = updatedCrises,
                        economy = updatedEcon
                    )
                } else current
            }
        }
    }

    fun executeDecree(decreeId: String) {
        _gameState.update { current ->
            if (current == null) null
            else {
                val targetDecree = current.sovereignDecrees.find { it.id == decreeId } ?: return@update current
                if (current.economy.treasuryCashBillionUsd >= targetDecree.costBillionUsd) {
                    val updatedDecrees = current.sovereignDecrees.map { d ->
                        if (d.id == decreeId) d.copy(isExecuted = true) else d
                    }
                    val updatedEcon = current.economy.copy(
                        treasuryCashBillionUsd = current.economy.treasuryCashBillionUsd - targetDecree.costBillionUsd
                    )
                    current.copy(
                        sovereignDecrees = updatedDecrees,
                        economy = updatedEcon
                    )
                } else current
            }
        }
    }

    fun toggleAutocracyTool(toolId: String) {
        _gameState.update { current ->
            if (current == null) null
            else {
                val tools = current.autocracyTools
                val updatedTools = when (toolId) {
                    "elections" -> tools.copy(electionsAbolished = !tools.electionsAbolished)
                    "parties" -> tools.copy(partiesDissolved = !tools.partiesDissolved)
                    "censorship" -> tools.copy(totalCensorshipActive = !tools.totalCensorshipActive)
                    "constitution" -> tools.copy(constitutionFrozen = !tools.constitutionFrozen)
                    "life_term" -> tools.copy(lifeTermDeclared = !tools.lifeTermDeclared)
                    else -> tools
                }
                current.copy(autocracyTools = updatedTools)
            }
        }
    }

    fun toggleCosmicDomain(domainId: String) {
        _gameState.update { current ->
            if (current == null) null
            else {
                val updatedDomains = current.cosmicDomains.map { dom ->
                    if (dom.id == domainId) dom.copy(isActive = !dom.isActive) else dom
                }
                current.copy(cosmicDomains = updatedDomains)
            }
        }
    }

    fun toggleLaw(lawId: String) {
        _gameState.update { current ->
            if (current == null) null
            else {
                val updatedLaws = current.laws.map { law ->
                    if (law.id == lawId) law.copy(isEnacted = !law.isEnacted) else law
                }
                current.copy(laws = updatedLaws)
            }
        }
    }

    fun offerAutonomy(movementId: String) {
        _gameState.update { current ->
            if (current == null) null
            else {
                val updatedSeps = current.separatists.map { sep ->
                    if (sep.id == movementId) sep.copy(autonomyOffered = true, popularSupportPct = (sep.popularSupportPct - 15.0).coerceAtLeast(0.0))
                    else sep
                }
                current.copy(separatists = updatedSeps)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        val current = _gameState.value
        if (current != null) {
            SaveManager.saveGame(getApplication(), current)
        }
    }
}
