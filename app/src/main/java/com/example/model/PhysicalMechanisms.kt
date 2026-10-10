package com.example.model

/**
 * 5 Physical Simulation Mechanisms snapshot and metrics
 */
data class PhysicalMechanismsState(
    // 1. Resources & Production Leontief limits
    val rawMaterialThroughputPct: Double, // 0..100% (Input availability ratio)
    val productionThrottleFactor: Double, // 0..1.0 (Output limitation due to deficits)

    // 2. Transport & Logistics Capacity
    val freightNetworkCapacityTonnesDaily: Double,
    val freightDemandTonnesDaily: Double,
    val logisticsCongestionPct: Double,  // 0..100% (Bottleneck penalty)

    // 3. Energy Grid Balance
    val energyGenerationMegawatts: Double,
    val energyDemandMegawatts: Double,
    val energyGridDeficitSurplusPct: Double, // Negative indicates brownouts

    // 4. Environmental & Climate Stress
    val industrialPollutionIndex: Double, // 0..100
    val climateDroughtFloodRiskPct: Double,// 0..100
    val cropYieldDampeningFactor: Double, // 0..1.0

    // 5. Physical Capacity & Carrying Limits
    val infrastructureWearAndTearPct: Double, // 0..100%
    val factoryUtilizationRatePct: Double,    // >95% causes exponential breakdown risk
    val dailyMaintenanceWearCostBillionUsd: Double
)
