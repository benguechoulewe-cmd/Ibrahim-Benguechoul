package com.example.model

enum class ProjectCategory(val titleEn: String, val titleAr: String) {
    ENERGY("Energy & Power", "الطاقة والكهرباء"),
    TRANSPORT("Transport & Logistics", "النقل واللوجستيات"),
    INDUSTRY("Industrial & Manufacturing", "الصناعة والإنتاج"),
    PUBLIC_WORKS("Public Facilities & Healthcare", "المرافق العامة والصحة"),
    DEFENSE("Military Infrastructure", "المنشآت العسكرية")
}

data class ConstructionBlueprint(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val category: ProjectCategory,
    val costBillionUsd: Double,
    val energyCostPerDay: Double,
    val mineralsRequired: Double,
    val buildDays: Int,
    val maintenanceBillionUsdPerDay: Double,
    val gdpBonusPct: Double,
    val capacityBonus: Double,
    val stabilityBonus: Int,
    val descriptionEn: String,
    val descriptionAr: String
)

data class ActiveProject(
    val blueprint: ConstructionBlueprint,
    val daysRemaining: Int,
    val progressPct: Float,
    val isPaused: Boolean = false
)
