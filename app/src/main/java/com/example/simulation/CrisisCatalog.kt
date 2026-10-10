package com.example.simulation

import com.example.model.ActiveCrisis
import com.example.model.CrisisResponseChoice
import com.example.model.CrisisSeverity

object CrisisCatalog {

    fun generateSampleCrises(playerCountryId: String, currentDay: Int): List<ActiveCrisis> {
        return listOf(
            ActiveCrisis(
                id = "CRISIS_ENERGY_1",
                titleEn = "Global Energy Supply Bottleneck & Grid Strain",
                titleAr = "اختناق سلاسل إمداد الطاقة وإجهاد الشبكة",
                severity = CrisisSeverity.MEDIUM,
                affectedCountriesIds = listOf(playerCountryId, "DEU", "IND", "FRA"),
                triggerCauseEn = "Geopolitical pipeline sanctions and surging industrial demand.",
                triggerCauseAr = "عقوبات على خطوط الأنابيب وزيادة حادة في الطلب الصناعي العالمي.",
                startDay = currentDay,
                dailyGdpPenaltyPct = 0.25,
                dailyStabilityPenalty = 1,
                responseOptions = listOf(
                    CrisisResponseChoice(
                        id = "ACT_ENGY_SUB",
                        titleEn = "Emergency Treasury Energy Subsidies",
                        titleAr = "حزم دعم مالي طارئ لأسعار الطاقة",
                        financialCostBillionUsd = 4.0,
                        mineralResourceCost = 0.0,
                        expectedStabilityGain = 3,
                        resolveChancePct = 80,
                        descriptionEn = "Absorb electricity costs with sovereign reserves to protect factories.",
                        descriptionAr = "تحمل تكاليف الكهرباء من الاحتياطي لحماية المصانع والأسر."
                    ),
                    CrisisResponseChoice(
                        id = "ACT_ENGY_RATION",
                        titleEn = "Mandatory Industrial Power Rationing",
                        titleAr = "ترشيد إلزامي للطاقة في المجمعات الصناعية",
                        financialCostBillionUsd = 0.5,
                        mineralResourceCost = 10.0,
                        expectedStabilityGain = -1,
                        resolveChancePct = 60,
                        descriptionEn = "Order non-critical manufacturing to operate during off-peak hours.",
                        descriptionAr = "إلزام المصانع غير الحيوية بالتشغيل في ساعات الليل لتقليل الحمل."
                    )
                )
            ),
            ActiveCrisis(
                id = "CRISIS_CURRENCY_1",
                titleEn = "Imported Inflation & Speculative Currency Pressure",
                titleAr = "تضخم مستورد وضغوط مضاربة على العملة الوطنية",
                severity = CrisisSeverity.LOW,
                affectedCountriesIds = listOf(playerCountryId, "GBR", "TUR"),
                triggerCauseEn = "Surging import prices on essential electronics and grain.",
                triggerCauseAr = "ارتفاع تكاليف استيراد الرقائق الإلكترونية والحبوب في الأسواق الدولية.",
                startDay = currentDay,
                dailyGdpPenaltyPct = 0.15,
                dailyStabilityPenalty = 1,
                responseOptions = listOf(
                    CrisisResponseChoice(
                        id = "ACT_RATE_HIKE",
                        titleEn = "Central Bank Emergency Rate Defense",
                        titleAr = "رفع استثنائي لأسعار الفائدة المركزية",
                        financialCostBillionUsd = 1.0,
                        mineralResourceCost = 0.0,
                        expectedStabilityGain = 2,
                        resolveChancePct = 85,
                        descriptionEn = "Hike policy rate to curb currency flight and stabilize import costs.",
                        descriptionAr = "رفع الفائدة لحماية العملة وكبح جماح التضخم المستورد."
                    ),
                    CrisisResponseChoice(
                        id = "ACT_PRICE_CAP",
                        titleEn = "Enforce Strict Sovereign Price Ceilings",
                        titleAr = "فرض سقف تسعير حكومي إلزامي على السلع الأساسية",
                        financialCostBillionUsd = 2.5,
                        mineralResourceCost = 5.0,
                        expectedStabilityGain = 4,
                        resolveChancePct = 70,
                        descriptionEn = "Mandate retail price ceilings backed by public inspection teams.",
                        descriptionAr = "مراقبة الأسواق وفرض غرامات على محتكري السلع الغذائية."
                    )
                )
            )
        )
    }
}
