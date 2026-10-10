package com.example.simulation

import com.example.model.EconomyState
import com.example.model.FactionType
import com.example.model.MilitaryState
import com.example.model.PoliticalFaction
import kotlin.math.max
import kotlin.math.min

object FactionsEngine {

    fun createInitialFactions(): List<PoliticalFaction> {
        return listOf(
            PoliticalFaction(
                type = FactionType.NATIONALISTS,
                approvalRating = 72,
                politicalLeveragePct = 22.0,
                trustInState = 75,
                coreAgenda = "National sovereignty, border integrity, and strategic industrial self-reliance.",
                coreAgendaArabic = "السيادة الوطنية، أمن الحدود، والاكتفاء الذاتي الصناعي الاستراتيجي.",
                unrestRiskPct = 12.0,
                stanceTowardsGovernment = "Supportive"
            ),
            PoliticalFaction(
                type = FactionType.CAPITALISTS,
                approvalRating = 68,
                politicalLeveragePct = 25.0,
                trustInState = 70,
                coreAgenda = "Free enterprise, low corporate taxes, foreign direct investment, and trade freedom.",
                coreAgendaArabic = "حرية الأعمال، خفض الضرائب على الشركات، جذب الاستثمارات، وحرية التجارة.",
                unrestRiskPct = 8.0,
                stanceTowardsGovernment = "Supportive"
            ),
            PoliticalFaction(
                type = FactionType.SOCIALISTS,
                approvalRating = 60,
                politicalLeveragePct = 20.0,
                trustInState = 62,
                coreAgenda = "Labor wage guarantees, public healthcare, progressive taxation, and social safety nets.",
                coreAgendaArabic = "ضمان أجور العمال، الرعاية الصحية العامة، الضرائب التصاعدية، وشبكات الأمان.",
                unrestRiskPct = 15.0,
                stanceTowardsGovernment = "Neutral"
            ),
            PoliticalFaction(
                type = FactionType.SECUROCRATS,
                approvalRating = 75,
                politicalLeveragePct = 15.0,
                trustInState = 80,
                coreAgenda = "High defense budgets, intelligence modernization, and civil law & order.",
                coreAgendaArabic = "زيادة ميزانية الدفاع، تطوير الاستخبارات، وفرض الأمن والنظام العام.",
                unrestRiskPct = 6.0,
                stanceTowardsGovernment = "Loyal Ally"
            ),
            PoliticalFaction(
                type = FactionType.TECHNOCRATS,
                approvalRating = 70,
                politicalLeveragePct = 10.0,
                trustInState = 78,
                coreAgenda = "R&D funding, semiconductor autonomy, educational reform, and administrative digitization.",
                coreAgendaArabic = "تمويل البحث والتطوير، استقلالية الرقائق، التحول الرقمي وإصلاح التعليم.",
                unrestRiskPct = 5.0,
                stanceTowardsGovernment = "Supportive"
            ),
            PoliticalFaction(
                type = FactionType.ECOLOGISTS,
                approvalRating = 55,
                politicalLeveragePct = 8.0,
                trustInState = 58,
                coreAgenda = "Clean energy grid, zero emissions, environmental preservation, and resource conservation.",
                coreAgendaArabic = "شبكة طاقة نظيفة، تقليل الانبعاثات، حماية البيئة واستدامة الموارد.",
                unrestRiskPct = 18.0,
                stanceTowardsGovernment = "Critical"
            )
        )
    }

    /**
     * Updates faction approval ratings based on economic and military indicators.
     */
    fun updateFactionsDaily(
        currentFactions: List<PoliticalFaction>,
        economy: EconomyState,
        military: MilitaryState,
        stabilityIndex: Int
    ): List<PoliticalFaction> {
        return currentFactions.map { faction ->
            var approvalDelta = 0

            when (faction.type) {
                FactionType.NATIONALISTS -> {
                    if (military.readinessPercentage > 75.0) approvalDelta += 1
                    if (economy.dailyExportsBillionUsd > economy.dailyImportsBillionUsd) approvalDelta += 1
                    if (stabilityIndex < 50) approvalDelta -= 2
                }
                FactionType.CAPITALISTS -> {
                    if (economy.corporateTaxPct <= 20.0) approvalDelta += 1
                    if (economy.gdpGrowthAnnualPct > 2.5) approvalDelta += 1
                    if (economy.inflationRatePct > 7.0) approvalDelta -= 2
                }
                FactionType.SOCIALISTS -> {
                    if (economy.unemploymentRatePct < 5.0) approvalDelta += 1
                    if (economy.averageMonthlyWageUsd > 2000.0) approvalDelta += 1
                    if (economy.inflationRatePct > 6.0) approvalDelta -= 2
                    if (economy.personalIncomeTaxPct > 35.0) approvalDelta -= 1
                }
                FactionType.SECUROCRATS -> {
                    if (military.dailyDefenseBudgetBillionUsd > 0.5) approvalDelta += 1
                    if (military.overallCombatPower > 70) approvalDelta += 1
                    if (stabilityIndex < 60) approvalDelta -= 2
                }
                FactionType.TECHNOCRATS -> {
                    if (economy.chipsStockpile > 50.0) approvalDelta += 1
                    if (economy.gdpGrowthAnnualPct > 2.0) approvalDelta += 1
                }
                FactionType.ECOLOGISTS -> {
                    if (economy.energyStockpile > 60.0) approvalDelta += 1
                    if (economy.resourceRoyaltyPct > 15.0) approvalDelta += 1
                }
            }

            val newApproval = min(100, max(5, faction.approvalRating + approvalDelta))
            val unrest = min(100.0, max(0.0, (100 - newApproval) * 0.4))
            val stance = when {
                newApproval >= 80 -> "Loyal Ally"
                newApproval >= 65 -> "Supportive"
                newApproval >= 45 -> "Neutral"
                newApproval >= 25 -> "Critical"
                else -> "Hostile Opposition"
            }

            faction.copy(
                approvalRating = newApproval,
                unrestRiskPct = unrest,
                stanceTowardsGovernment = stance
            )
        }
    }
}
