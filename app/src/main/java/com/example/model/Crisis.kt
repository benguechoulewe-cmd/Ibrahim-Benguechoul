package com.example.model

enum class CrisisSeverity(val labelEn: String, val labelAr: String) {
    LOW("Moderate Strain", "توتر طفيف"),
    MEDIUM("Elevated Alert", "إنذار متوسط"),
    CRITICAL("Severe Crisis", "أزمة حادة"),
    CATASTROPHIC("Systemic Collapse Risk", "خطر انهيار شامل")
}

data class CrisisResponseChoice(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val financialCostBillionUsd: Double,
    val mineralResourceCost: Double,
    val expectedStabilityGain: Int,
    val resolveChancePct: Int,
    val descriptionEn: String,
    val descriptionAr: String
)

data class ActiveCrisis(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val severity: CrisisSeverity,
    val affectedCountriesIds: List<String>,
    val triggerCauseEn: String,
    val triggerCauseAr: String,
    val startDay: Int,
    val dailyGdpPenaltyPct: Double,
    val dailyStabilityPenalty: Int,
    val responseOptions: List<CrisisResponseChoice>,
    val isResolved: Boolean = false
)
