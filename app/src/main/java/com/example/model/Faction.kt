package com.example.model

/**
 * The 6 distinct political factions
 */
enum class FactionType(val id: String, val titleEn: String, val titleAr: String) {
    NATIONALISTS("NAT", "Nationalist Sovereignists", "القوميون والسياديون"),
    CAPITALISTS("CAP", "Industrial Oligarchs & Free Marketeers", "الرأسماليون ورجال الأعمال"),
    SOCIALISTS("SOC", "Socialist Labor & Trade Unions", "الاشتراكيون ونقابات العمال"),
    SECUROCRATS("SEC", "Military Junta & Homeland Security", "المؤسسة العسكرية والأمنية"),
    TECHNOCRATS("TEC", "Technocrats & Scientific Reformists", "التكنوقراط والإصلاحيون"),
    ECOLOGISTS("ECO", "Greens & Climate Conservators", "البيئيون وحماة المناخ")
}

/**
 * Faction state representation
 */
data class PoliticalFaction(
    val type: FactionType,
    val approvalRating: Int,       // 0..100 (Satisfaction with player govt)
    val politicalLeveragePct: Double,// % of parliament/power (sum across all ~ 100%)
    val trustInState: Int,         // 0..100
    val coreAgenda: String,
    val coreAgendaArabic: String,
    val unrestRiskPct: Double,      // Strike, rebellion or coup risk
    val stanceTowardsGovernment: String // "Loyal Ally", "Supportive", "Neutral", "Critical", "Hostile Opposition"
)
