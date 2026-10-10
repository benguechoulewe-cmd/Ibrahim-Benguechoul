package com.example.model

/**
 * Economy State for the active simulation
 */
data class EconomyState(
    val gdpBillionUsd: Double,
    val gdpGrowthAnnualPct: Double,
    val treasuryCashBillionUsd: Double,
    val nationalDebtBillionUsd: Double,
    val creditRating: String, // "AAA", "AA", "A", "BBB", "BB", "B", "CCC"

    // Taxation policy (percentages)
    val corporateTaxPct: Double,
    val personalIncomeTaxPct: Double,
    val tradeTariffPct: Double,
    val resourceRoyaltyPct: Double,

    // Public finances (Daily rates in Billions)
    val dailyRevenue: Double,
    val dailyExpense: Double,
    val dailySurplusDeficit: Double,

    // Macroeconomic indicators
    val inflationRatePct: Double,
    val interestRatePct: Double,
    val unemploymentRatePct: Double,
    val averageMonthlyWageUsd: Double,
    val consumerConfidence: Double, // 0..100

    // Trade & Balance of Payments
    val dailyExportsBillionUsd: Double,
    val dailyImportsBillionUsd: Double,
    val tradeBalanceBillionUsd: Double,

    // Resource Stockpiles & Daily Consumption/Production
    val energyStockpile: Double,    // Megawatt-hours equivalent index
    val energyDailyBalance: Double,
    val mineralsStockpile: Double,
    val mineralsDailyBalance: Double,
    val foodStockpile: Double,
    val foodDailyBalance: Double,
    val industrialStockpile: Double,
    val industrialDailyBalance: Double,
    val chipsStockpile: Double,
    val chipsDailyBalance: Double,

    // Historical 30-day GDP trend for analytics
    val gdpHistory30Days: List<Double> = emptyList()
)
