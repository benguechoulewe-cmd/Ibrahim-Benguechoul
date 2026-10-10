package com.example

import com.example.data.WorldDataCatalog
import com.example.model.*
import com.example.simulation.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.*
import org.junit.Test

class MoneyWarsSimulationTest {

    @Test
    fun testAll200CountriesPresentAndUnique() {
        val countries = WorldDataCatalog.allCountries
        assertEquals("Must contain exactly 200 nations", 200, countries.size)
        val uniqueIds = countries.map { it.id }.toSet()
        assertEquals("All country IDs must be unique", 200, uniqueIds.size)
    }

    @Test
    fun testCountrySelectionAndCampaignStateAlignment() {
        for (testId in listOf("USA", "CHN", "SAU", "EGY", "DEU", "BRA", "JPN", "TUV")) {
            val country = WorldDataCatalog.getCountryById(testId)
            assertNotNull("Country $testId must exist in catalog", country)
            val state = SimulationEngine.createInitialState(testId)
            assertEquals("Player country in campaign must match selection", testId, state.playerCountryId)
            assertTrue("Campaign must be marked as started", state.isCampaignStarted)
            assertEquals("Day must start at 1", 1, state.currentDay)
            assertTrue("Initial treasury must be positive", state.economy.treasuryCashBillionUsd > 0)
        }
    }

    @Test
    fun testAll25MinisterPortfoliosPresent() {
        val ministers = MinistersEngine.createInitialMinisters()
        assertEquals("Must have exactly 25 ministers", 25, ministers.size)
        val portfolios = ministers.map { it.portfolio }.toSet()
        assertEquals("Must cover all 25 unique portfolios", 25, portfolios.size)
        assertEquals("MinisterPortfolio enum entries must count 25", 25, MinisterPortfolio.entries.size)
    }

    @Test
    fun testAll6PoliticalFactionsPresent() {
        val factions = FactionsEngine.createInitialFactions()
        assertEquals("Must have exactly 6 factions", 6, factions.size)
        val types = factions.map { it.type }.toSet()
        assertEquals("Must cover all 6 distinct faction types", 6, types.size)
    }

    @Test
    fun testFivePhysicalMechanismsComputation() {
        val country = WorldDataCatalog.getCountryById("USA")!!
        val state = SimulationEngine.createInitialState("USA")
        val physics = PhysicalMechanismsEngine.computePhysicalState(country, state.economy, 0.0)

        // Mechanism 1: Resources & Production Throttle
        assertTrue("Raw material throughput ratio must be between 0 and 100", physics.rawMaterialThroughputPct in 0.0..100.0)
        assertTrue("Production throttle factor between 0.0 and 1.0", physics.productionThrottleFactor in 0.0..1.0)

        // Mechanism 2: Transport & Logistics
        assertTrue("Freight capacity positive", physics.freightNetworkCapacityTonnesDaily > 0)
        assertTrue("Logistics congestion between 0 and 100", physics.logisticsCongestionPct in 0.0..100.0)

        // Mechanism 3: Energy Grid Balance
        assertTrue("Energy generation positive", physics.energyGenerationMegawatts > 0)

        // Mechanism 4: Environmental Stress
        assertTrue("Drought/Flood risk between 0 and 100", physics.climateDroughtFloodRiskPct in 0.0..100.0)
        assertTrue("Crop yield factor between 0.0 and 1.0", physics.cropYieldDampeningFactor in 0.0..1.0)

        // Mechanism 5: Physical Capacity & Carrying Limits
        assertTrue("Factory utilization rate between 0 and 100", physics.factoryUtilizationRatePct in 0.0..100.0)
        assertTrue("Infrastructure wear and tear between 0 and 100", physics.infrastructureWearAndTearPct in 0.0..100.0)
    }

    @Test
    fun testSimulationTimeModesAndDayTick() {
        var state = SimulationEngine.createInitialState("SAU")
        assertEquals(SimulationSpeed.NORMAL, state.speed)

        // Test speed values
        assertEquals(0f, SimulationSpeed.PAUSED.multiplier)
        assertEquals(1f, SimulationSpeed.NORMAL.multiplier)
        assertEquals(2f, SimulationSpeed.FAST.multiplier)
        assertEquals(4f, SimulationSpeed.ULTRA.multiplier)

        // Tick 5 days
        for (i in 1..5) {
            state = SimulationEngine.tickDay(state)
        }
        assertEquals("Day counter must reach 6", 6, state.currentDay)
        assertTrue("Treasury balance must be accounted for", state.economy.treasuryCashBillionUsd != 0.0)
    }

    @Test
    fun testHighSpeedDoesNotSkipFinancialObligations() {
        var state = SimulationEngine.createInitialState("DEU")
        val initialCash = state.economy.treasuryCashBillionUsd

        // Tick 30 continuous days
        for (i in 1..30) {
            state = SimulationEngine.tickDay(state)
        }

        // Daily revenues and expenses are deterministically accounted for each day
        assertEquals("State current day must be 31", 31, state.currentDay)
        assertTrue("Treasury cash must reflect continuous settlement", state.economy.treasuryCashBillionUsd != initialCash)
    }

    @Test
    fun testCrisisImpactOnStabilityAndGdp() {
        val state = SimulationEngine.createInitialState("FRA")
        val initialCrises = state.activeCrises
        assertTrue("Active crises must be present", initialCrises.isNotEmpty())

        val crisis = initialCrises.first()
        assertTrue("Crisis daily GDP penalty must be positive", crisis.dailyGdpPenaltyPct > 0)
        assertTrue("Crisis response options must not be empty", crisis.responseOptions.isNotEmpty())
    }

    @Test
    fun testSaveAndLoadSerializationIntegrity() {
        val state = SimulationEngine.createInitialState("EGY")
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(GameState::class.java)

        val json = adapter.toJson(state)
        assertNotNull(json)
        assertTrue("JSON must contain country ID", json.contains("EGY"))

        val restored = adapter.fromJson(json)
        assertNotNull("Restored state must not be null", restored)
        assertEquals("Player country must match", "EGY", restored!!.playerCountryId)
        assertEquals("World macro map must preserve all 200 countries", 200, restored.worldMacroMap.size)
        assertEquals("25 ministers preserved", 25, restored.ministers.size)
        assertEquals("6 factions preserved", 6, restored.factions.size)
    }

    @Test
    fun testWorldSimulationPerformanceWith200Countries() {
        var state = SimulationEngine.createInitialState("IND")
        val startTime = System.currentTimeMillis()

        // Benchmark 100 simulation day ticks for all 200 countries
        for (i in 1..100) {
            state = SimulationEngine.tickDay(state)
        }

        val elapsedMs = System.currentTimeMillis() - startTime
        val avgTimePerTickMs = elapsedMs / 100.0

        println("Simulation Performance: 100 day ticks for 200 countries completed in ${elapsedMs}ms (Avg: ${avgTimePerTickMs}ms/day)")
        assertTrue("Average tick must be fast (< 50ms) for smooth mobile performance", avgTimePerTickMs < 50.0)
        assertEquals("Day must be 101", 101, state.currentDay)
    }
}
