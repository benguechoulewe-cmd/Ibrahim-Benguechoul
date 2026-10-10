package com.example.model

/**
 * Exactly 25 distinct ministerial portfolios with complete Arabic localization
 */
enum class MinisterPortfolio(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val category: String,
    val categoryAr: String
) {
    PRIME_MINISTER("PM", "Prime Minister & State Coordinator", "رئيس الحكومة والمنسق العام", "Executive", "الشؤون التنفيذية"),
    FINANCE("FIN", "Minister of Finance & Treasury", "وزير المالية والخزانة", "Economy", "الاقتصاد والمالية"),
    ECONOMY_PLANNING("ECON", "Minister of Economy & Strategic Planning", "وزير الاقتصاد والتخطيط", "Economy", "الاقتصاد والمالية"),
    INDUSTRY_COMMERCE("IND", "Minister of Industry & Trade", "وزير الصناعة والتجارة", "Economy", "الاقتصاد والمالية"),
    AGRICULTURE_FOOD("AGRI", "Minister of Agriculture & Food Security", "وزير الزراعة والأمن الغذائي", "Resources", "الموارد والإنتاج"),
    ENERGY_PETROLEUM("ENGY", "Minister of Energy & Petroleum", "وزير الطاقة والبترول", "Resources", "الموارد والإنتاج"),
    NATURAL_RESOURCES("RES", "Minister of Natural Resources & Mining", "وزير الموارد والتعدين", "Resources", "الموارد والإنتاج"),
    TRANSPORT_LOGISTICS("TRNS", "Minister of Transport & Logistics", "وزير النقل واللوجستيات", "Infrastructure", "البنية التحتية"),
    INFRASTRUCTURE("INFR", "Minister of Public Works & Infrastructure", "وزير البنية التحتية والأشغال", "Infrastructure", "البنية التحتية"),
    HEALTH("HLTH", "Minister of Health & Public Care", "وزير الصحة والرعاية", "Social", "الشؤون الاجتماعية"),
    EDUCATION("EDUC", "Minister of Education & Higher Learning", "وزير التعليم والبحث العلمي", "Social", "الشؤون الاجتماعية"),
    LABOR_EMPLOYMENT("LABR", "Minister of Labor & Employment", "وزير العمل والتشغيل", "Social", "الشؤون الاجتماعية"),
    SOCIAL_AFFAIRS("SOC", "Minister of Social Affairs & Welfare", "وزير الشؤون الاجتماعية والتضامن", "Social", "الشؤون الاجتماعية"),
    INTERIOR("INT", "Minister of Interior & Homeland Security", "وزير الداخلية والأمن الداخلي", "Security", "الدفاع والأمن"),
    DEFENSE("DEF", "Minister of Defense & Armed Forces", "وزير الدفاع والقوات المسلحة", "Security", "الدفاع والأمن"),
    FOREIGN_AFFAIRS("FA", "Minister of Foreign Affairs & Diplomacy", "وزير الخارجية والدبلوماسية", "Diplomacy", "الدبلوماسية والخارجية"),
    JUSTICE("JUST", "Minister of Justice & Rule of Law", "وزير العدل وسيادة القانون", "Governance", "الحوكمة والعدل"),
    SCIENCE_TECH("TECH", "Minister of Science & High Technology", "وزير العلوم والتكنولوجيا المتقدمة", "Innovation", "الابتكار والتقنية"),
    ENVIRONMENT("ENV", "Minister of Environment & Climate Policy", "وزير البيئة والمناخ", "Environment", "البيئة والمناخ"),
    HOUSING("HOUS", "Minister of Housing & Urban Development", "وزير الإسكان والتنمية العمرانية", "Infrastructure", "البنية التحتية"),
    TELECOM_CYBER("TEL", "Minister of Telecom & Cyber Security", "وزير الاتصالات والأمن السيبراني", "Innovation", "الابتكار والتقنية"),
    STRATEGIC_PLANNING("PLAN", "Minister of Future Foresight & Reform", "وزير التخطيط واستشراف المستقبل", "Executive", "الشؤون التنفيذية"),
    PUBLIC_ADMIN("ADM", "Minister of Public Administration & Civil Service", "وزير الإدارة العامة والخدمة المدنية", "Governance", "الحوكمة والعدل"),
    CRISIS_MANAGEMENT("CRIS", "Minister of Crisis Response & Civil Defense", "وزير إدارة الأزمات والدفاع المدني", "Security", "الدفاع والأمن"),
    STATE_AUDIT("AUDT", "Minister of State Audit & Anti-Corruption", "وزير الرقابة ومكافحة الفساد", "Governance", "الحوكمة والعدل")
}

/**
 * Minister Policy Proposal
 */
data class MinisterProposal(
    val id: String,
    val title: String,
    val titleArabic: String,
    val description: String,
    val costBillionUsd: Double,
    val stabilityImpact: Int,
    val gdpGrowthImpact: Double,
    val factionAlignmentId: String,
    val isApproved: Boolean = false
)

/**
 * Minister Data Model
 */
data class Minister(
    val id: String,
    val name: String,
    val portfolio: MinisterPortfolio,
    val competence: Int,    // 0..100
    val experience: Int,    // 0..100
    val loyalty: Int,       // 0..100
    val integrity: Int,     // 0..100
    val publicApproval: Int,// 0..100
    val alignedFactionId: String,
    val activeProposal: MinisterProposal? = null,
    val tenureDays: Int = 0,
    val corruptionPenalty: Double = 0.0
)
