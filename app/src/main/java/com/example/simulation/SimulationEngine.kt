package com.example.simulation

import com.example.data.SovereigntyDataCatalog
import com.example.data.WorldDataCatalog
import com.example.model.*
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object SimulationEngine {

    /**
     * تهيئة حملة اللعبة الكاملة لدولة اللاعب المحددة بجميع أنظمة دفتر السيادة
     */
    fun createInitialState(playerCountryId: String): GameState {
        val playerCountry = WorldDataCatalog.getCountryById(playerCountryId)
            ?: WorldDataCatalog.allCountries.first()

        // 1. الاقتصاد الأساسي
        val dailyGdp = playerCountry.gdpBillionUsd / 365.0
        val initialEconomy = EconomyState(
            gdpBillionUsd = playerCountry.gdpBillionUsd,
            gdpGrowthAnnualPct = 2.4,
            treasuryCashBillionUsd = playerCountry.govtBudgetBillionUsd * 0.25,
            nationalDebtBillionUsd = playerCountry.gdpBillionUsd * 0.65,
            creditRating = if (playerCountry.stabilityIndex > 80) "AAA" else if (playerCountry.stabilityIndex > 65) "AA" else "BBB",
            corporateTaxPct = 21.0,
            personalIncomeTaxPct = 28.0,
            tradeTariffPct = 4.5,
            resourceRoyaltyPct = 12.0,
            dailyRevenue = (playerCountry.govtBudgetBillionUsd / 365.0) * 0.98,
            dailyExpense = (playerCountry.govtBudgetBillionUsd / 365.0) * 1.02,
            dailySurplusDeficit = -((playerCountry.govtBudgetBillionUsd / 365.0) * 0.04),
            inflationRatePct = 3.2,
            interestRatePct = 4.0,
            unemploymentRatePct = 5.2,
            averageMonthlyWageUsd = max(350.0, (playerCountry.gdpBillionUsd * 1000.0) / (playerCountry.populationMillions * 12.0) * 0.5),
            consumerConfidence = playerCountry.stabilityIndex.toDouble(),
            dailyExportsBillionUsd = dailyGdp * 0.28,
            dailyImportsBillionUsd = dailyGdp * 0.26,
            tradeBalanceBillionUsd = dailyGdp * 0.02,
            energyStockpile = playerCountry.resources.energyOilGas,
            energyDailyBalance = (playerCountry.resources.energyOilGas - 50.0) * 0.1,
            mineralsStockpile = playerCountry.resources.mineralsMetals,
            mineralsDailyBalance = (playerCountry.resources.mineralsMetals - 50.0) * 0.1,
            foodStockpile = playerCountry.resources.agricultureFood,
            foodDailyBalance = (playerCountry.resources.agricultureFood - 50.0) * 0.1,
            industrialStockpile = playerCountry.resources.industrialCapacity,
            industrialDailyBalance = (playerCountry.resources.industrialCapacity - 50.0) * 0.1,
            chipsStockpile = playerCountry.resources.technologyChips,
            chipsDailyBalance = (playerCountry.resources.technologyChips - 50.0) * 0.1,
            gdpHistory30Days = listOf(playerCountry.gdpBillionUsd)
        )

        // 2. الجيش والأمن
        val personnel = (playerCountry.populationMillions * 0.005 * 1_000_000).toInt()
        val initialMilitary = MilitaryState(
            totalActivePersonnel = personnel,
            totalReserves = (personnel * 1.8).toInt(),
            overallCombatPower = playerCountry.militaryRating,
            readinessPercentage = 85.0,
            dailyDefenseBudgetBillionUsd = (playerCountry.govtBudgetBillionUsd * 0.14) / 365.0,
            doctrineTrainingLevel = if (playerCountry.techLevel >= 8) 4 else 3,
            army = MilitaryBranch("القوات البرية والمدرعات", "القوات البرية والمدرعات", (personnel * 0.65).toInt(), (personnel * 0.04).toInt(), 88.0, min(5, playerCountry.techLevel / 2), (playerCountry.govtBudgetBillionUsd * 0.07) / 365.0),
            navy = MilitaryBranch("الأسطول البحري والغواصات", "الأسطول البحري والغواصات", (personnel * 0.15).toInt(), (personnel * 0.005).toInt(), 82.0, min(5, playerCountry.techLevel / 2), (playerCountry.govtBudgetBillionUsd * 0.035) / 365.0),
            airForce = MilitaryBranch("القوات الجوية والمسيرات", "القوات الجوية والمسيرات", (personnel * 0.20).toInt(), (personnel * 0.015).toInt(), 89.0, min(5, playerCountry.techLevel / 2), (playerCountry.govtBudgetBillionUsd * 0.035) / 365.0),
            missileDefenseActive = playerCountry.techLevel >= 8,
            cyberWarfareTier = min(5, playerCountry.techLevel / 2),
            droneSwarmsActive = playerCountry.techLevel >= 7,
            militaryAlliances = if (playerCountry.continent == Continent.NORTH_AMERICA || playerCountry.continent == Continent.EUROPE) listOf("USA", "GBR", "FRA") else emptyList(),
            activeTensions = emptyList(),
            warReadinessAlert = "درجة التأهب 4 (جاهزية دفاعية اعتيادية)"
        )

        // 3. الوزراء الـ25
        val initialMinisters = MinistersEngine.createInitialMinisters()

        // 4. الفصائل السياسية الست
        val initialFactions = FactionsEngine.createInitialFactions()

        // 5. الآليات الفيزيائية الخمس
        val initialPhysics = PhysicalMechanismsEngine.computePhysicalState(
            country = playerCountry,
            economy = initialEconomy,
            infrastructureBonus = 0.0
        )

        // 6. المشاعر الفردية والجماعية للوعي الشعبي
        val initialPsychology = PsychologicalMatrix(
            individual = IndividualEmotions(
                fear = (100 - playerCountry.stabilityIndex).toDouble() * 0.5,
                anger = if (playerCountry.stabilityIndex < 60) 45.0 else 20.0,
                grief = 15.0,
                joy = playerCountry.stabilityIndex * 0.6,
                hope = 65.0,
                dignity = 75.0,
                belonging = 80.0,
                envy = 25.0,
                love = 70.0,
                hatred = 15.0
            ),
            collective = CollectiveEmotions(
                massHysteria = 8.0,
                nationalMourning = 5.0,
                massCelebration = 20.0,
                financialPanicBankRun = 6.0,
                warEuphoria = playerCountry.militaryRating * 0.4,
                generalAnomie = 18.0,
                societalDepression = 15.0,
                culturalShock = 12.0
            ),
            emotionalContagionRate = 0.45,
            collectiveTraumaIndex = 20.0,
            generationalTraumaActive = false
        )

        // 7. نظام الحكم
        val initialRegime = when {
            playerCountry.id in listOf("SAU", "OMN", "QAT", "KWT", "ARE", "BHR", "BRN") -> RegimeType.ABSOLUTE_MONARCHY
            playerCountry.id in listOf("PRK", "CUB", "CHN", "VNM", "LAO") -> RegimeType.ONE_PARTY
            playerCountry.id in listOf("IRN") -> RegimeType.THEOCRACY
            playerCountry.id in listOf("MMR", "SDN", "MLI", "BFA", "NER", "TCD") -> RegimeType.MILITARY_JUNTA
            playerCountry.id in listOf("USA", "DEU", "BRA", "IND", "CAN", "AUS", "RUS", "MEX") -> RegimeType.SOVEREIGN_FEDERATION
            playerCountry.id in listOf("SGP", "CHE") -> RegimeType.TECHNOCRACY
            else -> RegimeType.PLURALIST_DEMOCRACY
        }

        // 8. الخريطة الماكرو لجميع الـ200 دولة
        val macroMap = WorldDataCatalog.allCountries.associate { c ->
            c.id to MacroNationState(
                id = c.id,
                gdpBillionUsd = c.gdpBillionUsd,
                stabilityIndex = c.stabilityIndex,
                militaryPower = c.militaryRating,
                relationWithPlayer = if (c.id == playerCountry.id) 100 else if (c.continent == playerCountry.continent) 30 else 0,
                isAlliedWithPlayer = (c.id == "USA" && (playerCountry.id == "GBR" || playerCountry.id == "CAN" || playerCountry.id == "JPN"))
            )
        }

        // 9. الأزمات الميدانية
        val initialCrises = CrisisCatalog.generateSampleCrises(playerCountry.id, 1)

        val initialLogs = listOf(
            EventLogItem(1, "بدء الحملة السيادية", "تم تولي القيادة السيادية العليا لدولة ${playerCountry.nameArabic} (${playerCountry.name}). تفعيل محاكاة الـ 200 دولة بالكامل.", "SUCCESS"),
            EventLogItem(1, "تشكيل الحكومة الوطنية", "اكتمال اعتماد الحقائب الوزارية الـ 25 بكامل اختصاصاتها وصلاحياتها.", "INFO"),
            EventLogItem(1, "انعقاد البرلمان والفصائل", "تسجيل التكتلات البرلمانية للفصائل السياسية الست تحت مظلة الدستور.", "INFO"),
            EventLogItem(1, "مصفوفة الوعي والمشاعر", "تفعيل مصفوفة المشاعر الفردية والجماعية وربطها بمؤشرات المعيشة اليومية.", "INFO")
        )

        return GameState(
            playerCountryId = playerCountry.id,
            currentDay = 1,
            speed = SimulationSpeed.NORMAL,
            economy = initialEconomy,
            military = initialMilitary,
            ministers = initialMinisters,
            factions = initialFactions,
            constructionQueue = emptyList(),
            completedProjects = emptyList(),
            activeCrises = initialCrises,
            physics = initialPhysics,
            psychology = initialPsychology,
            regimeType = initialRegime,
            autocracyTools = AutocracyToolsState(),
            parliament = ParliamentState(),
            laws = SovereigntyDataCatalog.sampleLegalActs,
            separatists = listOf(
                SeparatistMovement("SEP_1", "إقليم الأطراف الحدودي", 32.0, 8500, null, 38)
            ),
            cosmicDomains = SovereigntyDataCatalog.cosmicDomains17,
            sovereignDecrees = SovereigntyDataCatalog.sovereignDecrees25,
            sovereignFacilities = SovereigntyDataCatalog.sovereignFacilities50,
            financingMechanisms = SovereigntyDataCatalog.sovereignFinancing10,
            worldMacroMap = macroMap,
            eventLogs = initialLogs,
            isCampaignStarted = true
        )
    }

    /**
     * تنفيذ دورة اليوم السببية الـ19 المترابطة مع دمج المشاعر والبرلمان والانفصال
     */
    fun tickDay(currentState: GameState): GameState {
        val playerCountry = WorldDataCatalog.getCountryById(currentState.playerCountryId)
            ?: WorldDataCatalog.allCountries.first()

        val day = currentState.currentDay + 1
        val newLogs = currentState.eventLogs.toMutableList()

        // 1. معالجة طابور البناء والمشاريع الكبرى
        val updatedQueue = mutableListOf<ActiveProject>()
        val updatedCompletedProjects = currentState.completedProjects.toMutableList()
        var infraCapacityBonus = 0.0
        var infraGdpBonus = 0.0
        var infraDailyMaintCost = 0.0

        for (active in currentState.constructionQueue) {
            val remaining = active.daysRemaining - 1
            if (remaining <= 0) {
                updatedCompletedProjects.add(active.blueprint)
                newLogs.add(0, EventLogItem(day, "اكتمال إنجاز المشروع", "تم بنجاح تسليم '${active.blueprint.nameAr}' وإدخاله إلى الخدمة في المنظومة الوطنية.", "SUCCESS"))
            } else {
                val progress = 1.0f - (remaining.toFloat() / active.blueprint.buildDays.toFloat())
                updatedQueue.add(active.copy(daysRemaining = remaining, progressPct = progress))
            }
        }

        for (completed in updatedCompletedProjects) {
            infraCapacityBonus += completed.capacityBonus
            infraGdpBonus += completed.gdpBonusPct
            infraDailyMaintCost += completed.maintenanceBillionUsdPerDay
        }

        // 2. آثار الأزمات النشطة
        var dailyCrisisGdpPenalty = 0.0
        var dailyCrisisStabilityPenalty = 0
        for (crisis in currentState.activeCrises) {
            if (!crisis.isResolved) {
                dailyCrisisGdpPenalty += crisis.dailyGdpPenaltyPct
                dailyCrisisStabilityPenalty += crisis.dailyStabilityPenalty
            }
        }

        // 3. الآليات الفيزيائية الخمس
        val updatedPhysics = PhysicalMechanismsEngine.computePhysicalState(
            country = playerCountry,
            economy = currentState.economy,
            infrastructureBonus = infraCapacityBonus
        )

        // 4. الإنتاج والمخزونات وسلاسل الإمداد
        val productionMult = updatedPhysics.productionThrottleFactor * (1.0 + infraGdpBonus * 0.01)
        val newEnergy = min(150.0, max(0.0, currentState.economy.energyStockpile + (playerCountry.resources.energyOilGas * 0.02 * productionMult) - 0.9))
        val newMinerals = min(150.0, max(0.0, currentState.economy.mineralsStockpile + (playerCountry.resources.mineralsMetals * 0.02 * productionMult) - 0.8))
        val newFood = min(150.0, max(0.0, currentState.economy.foodStockpile + (playerCountry.resources.agricultureFood * 0.02 * updatedPhysics.cropYieldDampeningFactor) - 0.95))
        val newIndustrial = min(150.0, max(0.0, currentState.economy.industrialStockpile + (playerCountry.resources.industrialCapacity * 0.02 * productionMult) - 0.75))
        val newChips = min(150.0, max(0.0, currentState.economy.chipsStockpile + (playerCountry.resources.technologyChips * 0.015 * productionMult) - 0.5))

        // 5. التوظيف والأجور والاستهلاك
        val unemployment = min(25.0, max(2.5, 5.0 - (currentState.economy.gdpGrowthAnnualPct * 0.4) + (dailyCrisisGdpPenalty * 1.5)))
        val avgWage = max(200.0, currentState.economy.averageMonthlyWageUsd * (1.0 + (currentState.economy.gdpGrowthAnnualPct * 0.0002)))
        val consumerConf = min(100.0, max(10.0, playerCountry.stabilityIndex.toDouble() - (unemployment * 1.2) - (currentState.economy.inflationRatePct * 0.8)))

        // 6. التجارة والأسواق وميزان المدفوعات
        val logisticsPenalty = (100.0 - updatedPhysics.logisticsCongestionPct) / 100.0
        val dailyExports = (currentState.economy.gdpBillionUsd / 365.0) * 0.28 * logisticsPenalty
        val dailyImports = (currentState.economy.gdpBillionUsd / 365.0) * 0.26
        val tradeBal = dailyExports - dailyImports

        // 7. الضرائب والنفقات والتسوية المالية اليومية
        val dailyGdpNominal = currentState.economy.gdpBillionUsd / 365.0
        val taxRateEffective = (currentState.economy.corporateTaxPct * 0.4 + currentState.economy.personalIncomeTaxPct * 0.5 + currentState.economy.tradeTariffPct * 0.1) / 100.0
        val dailyRevenue = (dailyGdpNominal * taxRateEffective) + (newEnergy * 0.001)
        val dailyDefenseCost = currentState.military.dailyDefenseBudgetBillionUsd
        val dailyExpense = (playerCountry.govtBudgetBillionUsd / 365.0) * 0.85 + dailyDefenseCost + infraDailyMaintCost + updatedPhysics.dailyMaintenanceWearCostBillionUsd
        val netSurplusDeficit = dailyRevenue - dailyExpense
        val newTreasury = currentState.economy.treasuryCashBillionUsd + netSurplusDeficit
        val newNationalDebt = max(0.0, currentState.economy.nationalDebtBillionUsd + (if (netSurplusDeficit < 0) -netSurplusDeficit else -netSurplusDeficit * 0.2))

        // 8. التضخم والنمو الاقتصادي الحقيقي
        val energyShortageInflation = if (updatedPhysics.energyGridDeficitSurplusPct < 0) -updatedPhysics.energyGridDeficitSurplusPct * 0.05 else 0.0
        val newInflation = min(25.0, max(0.5, 2.5 + energyShortageInflation + (if (netSurplusDeficit < -2.0) 0.8 else 0.0)))
        val newAnnualGdpGrowth = max(-8.0, min(12.0, 2.8 + (infraGdpBonus * 0.2) - (dailyCrisisGdpPenalty * 2.0) - (unemployment * 0.1) - (newInflation * 0.1)))
        val newGdpNominal = max(1.0, currentState.economy.gdpBillionUsd * (1.0 + (newAnnualGdpGrowth / 36500.0)))

        val updatedGdpHistory = (currentState.economy.gdpHistory30Days + newGdpNominal).takeLast(30)

        val updatedEconomy = currentState.economy.copy(
            gdpBillionUsd = newGdpNominal,
            gdpGrowthAnnualPct = newAnnualGdpGrowth,
            treasuryCashBillionUsd = newTreasury,
            nationalDebtBillionUsd = newNationalDebt,
            dailyRevenue = dailyRevenue,
            dailyExpense = dailyExpense,
            dailySurplusDeficit = netSurplusDeficit,
            inflationRatePct = newInflation,
            unemploymentRatePct = unemployment,
            averageMonthlyWageUsd = avgWage,
            consumerConfidence = consumerConf,
            dailyExportsBillionUsd = dailyExports,
            dailyImportsBillionUsd = dailyImports,
            tradeBalanceBillionUsd = tradeBal,
            energyStockpile = newEnergy,
            mineralsStockpile = newMinerals,
            foodStockpile = newFood,
            industrialStockpile = newIndustrial,
            chipsStockpile = newChips,
            gdpHistory30Days = updatedGdpHistory
        )

        // 9. الاستقرار العام والمشاعر البشرية
        val newStability = min(100, max(10, playerCountry.stabilityIndex - dailyCrisisStabilityPenalty + (if (consumerConf > 70) 1 else if (consumerConf < 40) -1 else 0)))

        // تحديث مصفوفة المشاعر النفسية بناء على الواقع الاقتصادي والأمني
        val currentPsy = currentState.psychology
        val fearDelta = if (currentState.activeCrises.isNotEmpty()) 0.2 else -0.1
        val angerDelta = if (unemployment > 8.0 || newInflation > 6.0) 0.3 else -0.1
        val joyDelta = if (newAnnualGdpGrowth > 3.0) 0.2 else -0.1
        val hopeDelta = if (newTreasury > 20.0) 0.2 else -0.1

        val updatedIndiv = currentPsy.individual.copy(
            fear = (currentPsy.individual.fear + fearDelta).coerceIn(0.0, 100.0),
            anger = (currentPsy.individual.anger + angerDelta).coerceIn(0.0, 100.0),
            joy = (currentPsy.individual.joy + joyDelta).coerceIn(0.0, 100.0),
            hope = (currentPsy.individual.hope + hopeDelta).coerceIn(0.0, 100.0),
            dignity = if (currentState.military.overallCombatPower > 70) 80.0 else 65.0
        )

        val panicRisk = if (newInflation > 10.0 || unemployment > 12.0) 45.0 else 8.0
        val updatedCollective = currentPsy.collective.copy(
            financialPanicBankRun = panicRisk,
            generalAnomie = (currentPsy.collective.generalAnomie + (if (newStability < 40) 0.2 else -0.1)).coerceIn(0.0, 100.0)
        )
        val updatedPsychology = currentPsy.copy(
            individual = updatedIndiv,
            collective = updatedCollective
        )

        // 10. الفصائل السياسية
        val updatedFactions = FactionsEngine.updateFactionsDaily(
            currentFactions = currentState.factions,
            economy = updatedEconomy,
            military = currentState.military,
            stabilityIndex = newStability
        )

        // 11. تقدم الوزراء الـ25
        val updatedMinisters = currentState.ministers.map { min ->
            min.copy(tenureDays = min.tenureDays + 1)
        }

        // 12. حركات الانفصال والاستقلال
        val updatedSeparatists = currentState.separatists.map { sep ->
            val tensionChange = if (newStability < 40) 0.2 else if (sep.autonomyOffered) -0.5 else 0.0
            sep.copy(popularSupportPct = (sep.popularSupportPct + tensionChange).coerceIn(5.0, 95.0))
        }

        // 13. محاكاة الـ 200 دولة
        val updatedWorldMacro = currentState.worldMacroMap.mapValues { (id, macro) ->
            if (id == playerCountry.id) {
                macro.copy(gdpBillionUsd = newGdpNominal, stabilityIndex = newStability, militaryPower = currentState.military.overallCombatPower)
            } else {
                val randomGrowth = (Random.nextDouble(-0.02, 0.05) / 365.0)
                macro.copy(
                    gdpBillionUsd = max(0.1, macro.gdpBillionUsd * (1.0 + randomGrowth))
                )
            }
        }

        // تسجيل الدورية الشهرية بالعربية الصارمة
        if (day % 30 == 0) {
            newLogs.add(0, EventLogItem(day, "الموجز الاستراتيجي الشهري", "بلغ الناتج الاسمي $${String.format("%.1f", newGdpNominal)} مليار دولار بنمو سنوي قدره ${String.format("%.2f", newAnnualGdpGrowth)}%. الاستقرار السياسي عند $newStability%.", "INFO"))
        }

        return currentState.copy(
            currentDay = day,
            economy = updatedEconomy,
            ministers = updatedMinisters,
            factions = updatedFactions,
            constructionQueue = updatedQueue,
            completedProjects = updatedCompletedProjects,
            physics = updatedPhysics,
            psychology = updatedPsychology,
            separatists = updatedSeparatists,
            worldMacroMap = updatedWorldMacro,
            eventLogs = newLogs.take(100)
        )
    }
}
