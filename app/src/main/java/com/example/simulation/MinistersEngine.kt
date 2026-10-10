package com.example.simulation

import com.example.model.EconomyState
import com.example.model.Minister
import com.example.model.MinisterPortfolio
import com.example.model.MinisterProposal
import kotlin.random.Random

object MinistersEngine {

    /**
     * Generate initial 25 ministers for the country.
     */
    fun createInitialMinisters(): List<Minister> {
        val namesEn = listOf(
            "Alexander Hayes", "Elena Rostova", "Marcus Vance", "Sophia Lin",
            "Tariq Al-Mansoor", "David Chen", "Amara Okafor", "Viktor Novak",
            "Claire Dubois", "Ibrahim Khalil", "Hannah Schmidt", "Carlos Mendoza",
            "Fatima Al-Zahra", "Arthur Pendelton", "General James Sterling", "Lord Julian Vance",
            "Judge Sarah Al-Sabah", "Dr. Kenji Takahashi", "Dr. Greta Lindholm", "Mateo Silva",
            "Maya Patel", "Kassim Al-Harbi", "Benoit Laurent", "Colonel Omar Farouk", "Salim Al-Qadi"
        )

        return MinisterPortfolio.entries.mapIndexed { index, portfolio ->
            val name = namesEn.getOrElse(index) { "Minister ${portfolio.id}" }
            val competence = 65 + (index * 7) % 30
            val experience = 60 + (index * 11) % 35
            val loyalty = 70 + (index * 13) % 25
            val integrity = 65 + (index * 17) % 30
            val approval = 60 + (index * 5) % 30

            val factionId = when (portfolio.category) {
                "Economy" -> "CAP"
                "Security" -> "SEC"
                "Social" -> "SOC"
                "Innovation" -> "TEC"
                "Environment" -> "ECO"
                else -> "NAT"
            }

            Minister(
                id = "MIN_${portfolio.id}",
                name = name,
                portfolio = portfolio,
                competence = competence,
                experience = experience,
                loyalty = loyalty,
                integrity = integrity,
                publicApproval = approval,
                alignedFactionId = factionId,
                activeProposal = generateProposalForPortfolio(portfolio)
            )
        }
    }

    private fun generateProposalForPortfolio(portfolio: MinisterPortfolio): MinisterProposal {
        return when (portfolio) {
            MinisterPortfolio.PRIME_MINISTER -> MinisterProposal(
                id = "PROP_PM_1",
                title = "National Strategic Cohesion Act",
                titleArabic = "قانون التماسك الاستراتيجي الوطني",
                description = "Synchronize inter-ministerial departments to increase administrative productivity.",
                costBillionUsd = 2.5,
                stabilityImpact = 6,
                gdpGrowthImpact = 0.3,
                factionAlignmentId = "NAT"
            )
            MinisterPortfolio.FINANCE -> MinisterProposal(
                id = "PROP_FIN_1",
                title = "Treasury Modernization & Tax Reform",
                titleArabic = "تحديث الخزانة وإصلاح المنظومة الضريبية",
                description = "Close tax avoidance loopholes and streamline revenue collection.",
                costBillionUsd = 1.2,
                stabilityImpact = 2,
                gdpGrowthImpact = 0.5,
                factionAlignmentId = "CAP"
            )
            MinisterPortfolio.ECONOMY_PLANNING -> MinisterProposal(
                id = "PROP_ECON_1",
                title = "Five-Year Industrial Growth Pact",
                titleArabic = "الميثاق الصناعي الخماسي للنمو",
                description = "Incentivize local manufacturing with capital expenditure write-offs.",
                costBillionUsd = 5.0,
                stabilityImpact = 4,
                gdpGrowthImpact = 0.8,
                factionAlignmentId = "CAP"
            )
            MinisterPortfolio.INDUSTRY_COMMERCE -> MinisterProposal(
                id = "PROP_IND_1",
                title = "Export Subsidy & Trade Logistics Corridor",
                titleArabic = "دعم الصادرات وممر الخدمات اللوجستية التجارية",
                description = "Subsidize maritime container freight fees to boost exports.",
                costBillionUsd = 3.5,
                stabilityImpact = 3,
                gdpGrowthImpact = 0.6,
                factionAlignmentId = "CAP"
            )
            MinisterPortfolio.AGRICULTURE_FOOD -> MinisterProposal(
                id = "PROP_AGRI_1",
                title = "Strategic Grain Reserves & Desalination Farming",
                titleArabic = "الاحتياطي الاستراتيجي للحبوب والزراعة المروية",
                description = "Expand grain silos and hydroponic desert farming systems.",
                costBillionUsd = 2.8,
                stabilityImpact = 5,
                gdpGrowthImpact = 0.2,
                factionAlignmentId = "NAT"
            )
            MinisterPortfolio.ENERGY_PETROLEUM -> MinisterProposal(
                id = "PROP_ENGY_1",
                title = "Grid Capacity Expansion & Clean Hydrogen",
                titleArabic = "توسيع سعة الشبكة والهيدروجين النظيف",
                description = "Upgrade high-voltage transmission lines and establish hydrogen terminals.",
                costBillionUsd = 6.0,
                stabilityImpact = 4,
                gdpGrowthImpact = 0.7,
                factionAlignmentId = "ECO"
            )
            MinisterPortfolio.NATURAL_RESOURCES -> MinisterProposal(
                id = "PROP_RES_1",
                title = "Critical Minerals Exploration Initiative",
                titleArabic = "مبادرة استكشاف وتعدين المعادن الحرجة",
                description = "Issue sovereign mining leases for rare earths and lithium.",
                costBillionUsd = 3.2,
                stabilityImpact = 1,
                gdpGrowthImpact = 0.5,
                factionAlignmentId = "CAP"
            )
            MinisterPortfolio.DEFENSE -> MinisterProposal(
                id = "PROP_DEF_1",
                title = "Integrated Drone & Air Defense Grid",
                titleArabic = "شبكة الدفاع الجوي المتكاملة والمسيرات",
                description = "Procure next-gen layered air defense batteries.",
                costBillionUsd = 8.0,
                stabilityImpact = 7,
                gdpGrowthImpact = 0.1,
                factionAlignmentId = "SEC"
            )
            MinisterPortfolio.SCIENCE_TECH -> MinisterProposal(
                id = "PROP_TECH_1",
                title = "National Semiconductor & AI Foundry",
                titleArabic = "المجمع الوطني للذكاء الاصطناعي وأشباه الموصلات",
                description = "Establish state advanced microchip fabrication labs.",
                costBillionUsd = 12.0,
                stabilityImpact = 5,
                gdpGrowthImpact = 1.2,
                factionAlignmentId = "TEC"
            )
            MinisterPortfolio.ENVIRONMENT -> MinisterProposal(
                id = "PROP_ENV_1",
                title = "Industrial Carbon Capture Mandate",
                titleArabic = "إلزام احتجاز الكربون الصناعي ومكافحة التلوث",
                description = "Enforce scrubber filters on heavy industrial smelting plants.",
                costBillionUsd = 2.0,
                stabilityImpact = 2,
                gdpGrowthImpact = -0.1,
                factionAlignmentId = "ECO"
            )
            else -> MinisterProposal(
                id = "PROP_${portfolio.id}_1",
                title = "Department Efficiency Initiative",
                titleArabic = "مبادرة رفع كفاءة الوزارة",
                description = "Optimize workflow, audit procurement, and increase department throughput.",
                costBillionUsd = 1.0,
                stabilityImpact = 2,
                gdpGrowthImpact = 0.2,
                factionAlignmentId = "NAT"
            )
        }
    }

    /**
     * Generates a new pool of candidate ministers if the player fires someone.
     */
    fun generateCandidate(portfolio: MinisterPortfolio): Minister {
        val rand = Random.nextInt(100, 999)
        return Minister(
            id = "CAND_${portfolio.id}_$rand",
            name = "Candidate Al-${portfolio.id.lowercase().capitalize()} $rand",
            portfolio = portfolio,
            competence = Random.nextInt(55, 95),
            experience = Random.nextInt(50, 95),
            loyalty = Random.nextInt(60, 98),
            integrity = Random.nextInt(60, 98),
            publicApproval = Random.nextInt(50, 85),
            alignedFactionId = "NAT",
            activeProposal = generateProposalForPortfolio(portfolio)
        )
    }
}
