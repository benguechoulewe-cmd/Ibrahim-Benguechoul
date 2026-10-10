package com.example.model

enum class SimulationSpeed(val multiplier: Float, val label: String, val icon: String) {
    PAUSED(0f, "إيقاف مؤقت", "⏸️"),
    NORMAL(1f, "1x عادي", "▶️"),
    FAST(2f, "2x سريع", "⏩"),
    ULTRA(4f, "4x فائق", "⚡")
}

data class EventLogItem(
    val day: Int,
    val title: String,
    val description: String,
    val severity: String = "INFO", // "INFO", "WARN", "CRITICAL", "SUCCESS"
    val timestampMs: Long = System.currentTimeMillis()
)

data class MacroNationState(
    val id: String,
    val gdpBillionUsd: Double,
    val stabilityIndex: Int,
    val militaryPower: Int,
    val relationWithPlayer: Int, // -100 to +100
    val atWarWith: List<String> = emptyList(),
    val isAlliedWithPlayer: Boolean = false
)

data class GameState(
    val playerCountryId: String,
    val currentDay: Int = 1,
    val speed: SimulationSpeed = SimulationSpeed.NORMAL,
    val autoPauseOnCrisis: Boolean = true,

    // Deep simulation for player
    val economy: EconomyState,
    val military: MilitaryState,
    val ministers: List<Minister>,
    val factions: List<PoliticalFaction>,
    val constructionQueue: List<ActiveProject>,
    val completedProjects: List<ConstructionBlueprint>,
    val activeCrises: List<ActiveCrisis>,
    val physics: PhysicalMechanismsState,

    // أنظمة دفتر السيادة والعقد الأم v10.0
    val psychology: PsychologicalMatrix,
    val regimeType: RegimeType = RegimeType.SOVEREIGN_FEDERATION,
    val autocracyTools: AutocracyToolsState = AutocracyToolsState(),
    val parliament: ParliamentState = ParliamentState(),
    val laws: List<LegalAct> = emptyList(),
    val separatists: List<SeparatistMovement> = emptyList(),
    val cosmicDomains: List<CosmicDomain> = emptyList(),
    val sovereignDecrees: List<SovereignDecreeItem> = emptyList(),
    val sovereignFacilities: List<SovereignFacility50> = emptyList(),
    val financingMechanisms: List<SovereignFinancingOption> = emptyList(),

    // World states for all 200 countries
    val worldMacroMap: Map<String, MacroNationState>,

    // Event logs for reports tab
    val eventLogs: List<EventLogItem> = emptyList(),

    val isCampaignStarted: Boolean = false
)
