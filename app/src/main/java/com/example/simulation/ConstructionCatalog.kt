package com.example.simulation

import com.example.model.ConstructionBlueprint
import com.example.model.ProjectCategory

object ConstructionCatalog {

    val allBlueprints: List<ConstructionBlueprint> = listOf(
        ConstructionBlueprint(
            id = "PRJ_NUCLEAR",
            nameEn = "Advanced Nuclear SMR Facility",
            nameAr = "محطة طاقة نووية معيارية متقدمة",
            category = ProjectCategory.ENERGY,
            costBillionUsd = 12.0,
            energyCostPerDay = -1500.0, // Produces 1500 MW net
            mineralsRequired = 45.0,
            buildDays = 90,
            maintenanceBillionUsdPerDay = 0.02,
            gdpBonusPct = 0.8,
            capacityBonus = 2000.0,
            stabilityBonus = 5,
            descriptionEn = "Provides massive clean baseload electricity, eliminating blackouts.",
            descriptionAr = "توفر طاقة كهربائية قاعدية نظيفة وهائلة، وتنهي أزمات انقطاع الكهرباء."
        ),
        ConstructionBlueprint(
            id = "PRJ_SOLAR_MEGA",
            nameEn = "Desert Solar Mega-Park",
            nameAr = "مجمع الطاقة الشمسية الصحراوي العملاق",
            category = ProjectCategory.ENERGY,
            costBillionUsd = 6.5,
            energyCostPerDay = -800.0,
            mineralsRequired = 25.0,
            buildDays = 45,
            maintenanceBillionUsdPerDay = 0.008,
            gdpBonusPct = 0.4,
            capacityBonus = 900.0,
            stabilityBonus = 3,
            descriptionEn = "Deploys vast photovoltaic arrays to power heavy industries cleanly.",
            descriptionAr = "نشر مصفوفات شمسية واسعة لتغذية الصناعات الثقيلة بطاقة متجددة."
        ),
        ConstructionBlueprint(
            id = "PRJ_DEEP_PORT",
            nameEn = "Automated Deepwater Cargo Port",
            nameAr = "ميناء بحري للمياه العميقة مؤتمت",
            category = ProjectCategory.TRANSPORT,
            costBillionUsd = 8.5,
            energyCostPerDay = 150.0,
            mineralsRequired = 35.0,
            buildDays = 60,
            maintenanceBillionUsdPerDay = 0.015,
            gdpBonusPct = 1.2,
            capacityBonus = 4500.0,
            stabilityBonus = 4,
            descriptionEn = "Triples container freight throughput and drastically reduces trade congestion.",
            descriptionAr = "يضاعف حركة الشحن بالحاويات ثلاث مرات ويقضي على الاختناقات اللوجستية."
        ),
        ConstructionBlueprint(
            id = "PRJ_HIGH_SPEED_RAIL",
            nameEn = "Trans-National High-Speed Freight Rail",
            nameAr = "شبكة القطارات فائقة السرعة للشحن والركاب",
            category = ProjectCategory.TRANSPORT,
            costBillionUsd = 14.0,
            energyCostPerDay = 250.0,
            mineralsRequired = 55.0,
            buildDays = 75,
            maintenanceBillionUsdPerDay = 0.025,
            gdpBonusPct = 1.4,
            capacityBonus = 6000.0,
            stabilityBonus = 6,
            descriptionEn = "Interlinks regional production hubs with capital markets in record time.",
            descriptionAr = "يربط مراكز الإنتاج الصناعي بالأسواق والموانئ في أوقات قياسية."
        ),
        ConstructionBlueprint(
            id = "PRJ_CHIP_FAB",
            nameEn = "3nm Advanced Semiconductor Foundry",
            nameAr = "مجمع مسابك أشباه الموصلات المتقدمة 3nm",
            category = ProjectCategory.INDUSTRY,
            costBillionUsd = 20.0,
            energyCostPerDay = 400.0,
            mineralsRequired = 40.0,
            buildDays = 120,
            maintenanceBillionUsdPerDay = 0.04,
            gdpBonusPct = 2.5,
            capacityBonus = 1200.0,
            stabilityBonus = 8,
            descriptionEn = "Secures sovereign tech autonomy, high-tech export dominance, and superchips.",
            descriptionAr = "يضمن السيادة التكنولوجية وهيمنة الصادرات التقنية وصناعة الرقائق الفائقة."
        ),
        ConstructionBlueprint(
            id = "PRJ_SMART_HOSPITAL_NETWORK",
            nameEn = "National AI Healthcare & Medical Centers",
            nameAr = "شبكة المستشفيات والمراكز الطبية الذكية بالذكاء الاصطناعي",
            category = ProjectCategory.PUBLIC_WORKS,
            costBillionUsd = 5.0,
            energyCostPerDay = 80.0,
            mineralsRequired = 15.0,
            buildDays = 40,
            maintenanceBillionUsdPerDay = 0.012,
            gdpBonusPct = 0.5,
            capacityBonus = 800.0,
            stabilityBonus = 9,
            descriptionEn = "Provides state-of-the-art public healthcare, skyrocketing social approval.",
            descriptionAr = "يرفع جودة الرعاية الصحية العامة ويرفع الرضا الشعبي والاستقرار الاجتماعي."
        ),
        ConstructionBlueprint(
            id = "PRJ_MISSILE_SHIELD",
            nameEn = "Layered Integrated Kinetic Air Defense Shield",
            nameAr = "درع الدفاع الجوي الصاروخي المتكامل الطبقي",
            category = ProjectCategory.DEFENSE,
            costBillionUsd = 16.0,
            energyCostPerDay = 300.0,
            mineralsRequired = 50.0,
            buildDays = 80,
            maintenanceBillionUsdPerDay = 0.035,
            gdpBonusPct = 0.2,
            capacityBonus = 500.0,
            stabilityBonus = 12,
            descriptionEn = "Shields national airspace, industrial complexes, and cities against hostile strikes.",
            descriptionAr = "يحمي الأجواء الوطنية والمجمعات الصناعية والمدن من أي ضربات معادية."
        )
    )
}
