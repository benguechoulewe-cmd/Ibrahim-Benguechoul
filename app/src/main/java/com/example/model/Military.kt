package com.example.model

/**
 * Military branch breakdown and operational metrics
 */
data class MilitaryBranch(
    val name: String,
    val nameArabic: String,
    val personnel: Int,
    val heavyEquipmentUnits: Int,
    val readinessPct: Double,      // 0..100%
    val techTier: Int,             // 1..5
    val dailyUpkeepBillionUsd: Double
)

/**
 * Military state of the nation
 */
data class MilitaryState(
    val totalActivePersonnel: Int,
    val totalReserves: Int,
    val overallCombatPower: Int, // 1..100
    val readinessPercentage: Double, // 0..100%
    val dailyDefenseBudgetBillionUsd: Double,
    val doctrineTrainingLevel: Int, // 1..5

    // Branches
    val army: MilitaryBranch,
    val navy: MilitaryBranch,
    val airForce: MilitaryBranch,

    // R&D defense projects
    val missileDefenseActive: Boolean = false,
    val cyberWarfareTier: Int = 1,
    val droneSwarmsActive: Boolean = false,

    // Geopolitical posture
    val militaryAlliances: List<String> = emptyList(), // ISO ids of allies
    val activeTensions: List<String> = emptyList(),   // ISO ids of rivals in friction
    val warReadinessAlert: String = "DEFCON 4 (Peace / Guard)" // DEFCON 5, 4, 3, 2, 1
)
