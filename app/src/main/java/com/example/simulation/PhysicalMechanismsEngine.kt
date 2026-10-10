package com.example.simulation

import com.example.model.Country
import com.example.model.EconomyState
import com.example.model.PhysicalMechanismsState
import kotlin.math.max
import kotlin.math.min

object PhysicalMechanismsEngine {

    /**
     * Compute updated physical state based on current economy, infrastructure, and country baseline.
     */
    fun computePhysicalState(
        country: Country,
        economy: EconomyState,
        infrastructureBonus: Double
    ): PhysicalMechanismsState {
        // 1. Resources & Production Leontief limits
        val rawInputRatio = min(1.0, max(0.2, (economy.mineralsStockpile + 50.0) / 100.0))
        val energyInputRatio = min(1.0, max(0.2, (economy.energyStockpile + 50.0) / 100.0))
        val rawThroughputPct = ((rawInputRatio * 0.5 + energyInputRatio * 0.5) * 100.0)
        val productionThrottle = min(1.0, min(rawInputRatio, energyInputRatio))

        // 2. Transport & Logistics Capacity
        val baseCapacity = (country.gdpBillionUsd * 15.0) + (infrastructureBonus * 100.0)
        val demand = country.populationMillions * 10.0 + (economy.dailyExportsBillionUsd + economy.dailyImportsBillionUsd) * 50.0
        val congestionPct = min(100.0, max(0.0, ((demand - baseCapacity) / max(1.0, baseCapacity)) * 100.0))

        // 3. Energy Grid Balance
        val baseGeneration = (country.resources.energyOilGas * 2500.0) + (country.gdpBillionUsd * 20.0) + (infrastructureBonus * 500.0)
        val energyDemand = (country.populationMillions * 180.0) + (country.gdpBillionUsd * 30.0)
        val deficitSurplusPct = ((baseGeneration - energyDemand) / max(1.0, energyDemand)) * 100.0

        // 4. Environment & Climate Stress
        val pollution = min(100.0, max(10.0, (country.resources.industrialCapacity * 0.6) + (economy.gdpGrowthAnnualPct * 3.0)))
        val droughtFloodRisk = min(100.0, max(5.0, (pollution * 0.7) + (100.0 - country.resources.agricultureFood) * 0.3))
        val cropYieldFactor = max(0.4, 1.0 - (droughtFloodRisk / 250.0))

        // 5. Physical Capacity & Carrying Limits
        val factoryUtilization = min(100.0, max(40.0, 75.0 + (economy.gdpGrowthAnnualPct * 5.0)))
        val wearRate = if (factoryUtilization > 90.0) (factoryUtilization - 90.0) * 0.8 else 0.1
        val dailyWearCost = (country.gdpBillionUsd * 0.0001) * (1.0 + wearRate / 10.0)

        return PhysicalMechanismsState(
            rawMaterialThroughputPct = rawThroughputPct,
            productionThrottleFactor = productionThrottle,
            freightNetworkCapacityTonnesDaily = baseCapacity,
            freightDemandTonnesDaily = demand,
            logisticsCongestionPct = congestionPct,
            energyGenerationMegawatts = baseGeneration,
            energyDemandMegawatts = energyDemand,
            energyGridDeficitSurplusPct = deficitSurplusPct,
            industrialPollutionIndex = pollution,
            climateDroughtFloodRiskPct = droughtFloodRisk,
            cropYieldDampeningFactor = cropYieldFactor,
            infrastructureWearAndTearPct = min(100.0, 15.0 + wearRate * 2.0),
            factoryUtilizationRatePct = factoryUtilization,
            dailyMaintenanceWearCostBillionUsd = dailyWearCost
        )
    }
}
