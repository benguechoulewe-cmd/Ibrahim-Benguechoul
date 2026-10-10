package com.example.model

/**
 * المشاعر البشرية الفردية والجماعية (Human Emotions & Psychological Matrix)
 */
data class IndividualEmotions(
    val fear: Double,       // الخوف (من الحرب، الفقر، الاعتقال) 0..100
    val anger: Double,      // الغضب (من الظلم، الفساد، الغلاء) 0..100
    val grief: Double,      // الحزن (فقدان وطن، كرامة، أعزاء) 0..100
    val joy: Double,        // الفرح (نصر، رخاء، حرية) 0..100
    val hope: Double,       // الأمل (بالمستقبل والتغيير) 0..100
    val dignity: Double,    // الكرامة والشرف القومي 0..100
    val belonging: Double,  // الانتماء (للوطن، الدين، الهوية) 0..100
    val envy: Double,       // الحسد الطبقي والإقليمي 0..100
    val love: Double,       // الحب والولاء للقائد والوطن 0..100
    val hatred: Double      // الكراهية والعداء 0..100
)

data class CollectiveEmotions(
    val massHysteria: Double,       // الهستيريا الجماعية 0..100
    val nationalMourning: Double,   // الحداد الوطني 0..100
    val massCelebration: Double,    // الاحتفال الجماهيري 0..100
    val financialPanicBankRun: Double,// الذعر المالي والتدافع البنكي 0..100
    val warEuphoria: Double,        // الحماس الحربي والتعبوي 0..100
    val generalAnomie: Double,      // الإحباط العام والضياع القيمي 0..100
    val societalDepression: Double, // الاكتئاب المجتمعي 0..100
    val culturalShock: Double       // الصدمة الثقافية 0..100
)

data class PsychologicalMatrix(
    val individual: IndividualEmotions,
    val collective: CollectiveEmotions,
    val emotionalContagionRate: Double, // معدل انتقال العدوى العاطفية
    val collectiveTraumaIndex: Double,  // مؤشر الصدمات الجماعية المؤجلة
    val generationalTraumaActive: Boolean = false // صدمة الأجيال
)

/**
 * أنظمة الحكم وأدوات الاستبداد والحكم المطلق (Regimes & Absolute Rule)
 */
enum class RegimeType(val arabicName: String, val descriptionArabic: String) {
    ABSOLUTE_DICTATORSHIP("ديكتاتورية مطلقة", "جميع السلطات التنفيذية والتشريعية والقضائية متركزة بيد الحاكم المطلق"),
    ABSOLUTE_MONARCHY("ملكية مطلقة", "حكم وراثي سيادي غير مقيد بدستور أو برلمان"),
    THEOCRACY("ثيوقراطية دينية", "سلطة دينية مطلقة تحكم باسم الشريعة والعقيدة"),
    MILITARY_JUNTA("مجلس عسكري مباشر", "حكم قادة القوات المسلحة وإدارة البلاد بالأوامر العسكرية"),
    ONE_PARTY("حزب شمولي أحادي", "احتكار حزبي كامل للسلطة والإعلام والقرارات"),
    TECHNOCRACY("تكنوقراطية تنفيذية", "حكم لجان الخبراء والعلماء المستقلين وفق المؤشرات الكمية"),
    PLURALIST_DEMOCRACY("ديمقراطية تعددية", "تداول سلمي للسلطة، سيادة القانون، وحريات دستورية شاملة"),
    SOVEREIGN_FEDERATION("فيدرالية اتحادية", "توزيع دقيق للصلاحيات بين الحكومة المركزية وحكومات الأقاليم")
}

data class AutocracyToolsState(
    val electionsAbolished: Boolean = false,     // إلغاء الانتخابات
    val partiesDissolved: Boolean = false,       // حل الأحزاب السياسية
    val oppositionDetained: Boolean = false,     // اعتقال قادة المعارضة
    val totalCensorshipActive: Boolean = false,  // رقابة إعلامية شاملة وحجب المنصات
    val constitutionFrozen: Boolean = false,     // تجميد الدستور
    val lifeTermDeclared: Boolean = false,       // إعلان الحكم مدى الحياة
    val hereditarySuccessionSet: Boolean = false,// توريث السلطة
    val politicalPurgesActive: Boolean = false,  // إقصاءات سياسية وتصفيات
    val showTrialsEnacted: Boolean = false,      // محاكمات صورية استثنائية
    val martialLawDeclared: Boolean = false      // الأحكام العرفية الشاملة
)

/**
 * البرلمان والدستور والقوانين والمحاكم (Parliament, Constitution & Judiciary)
 */
data class ParliamentState(
    val deputiesChamberSeats: Int = 300,        // مجلس النواب
    val senateChamberSeats: Int = 100,          // مجلس الشيوخ
    val rulingCoalitionPct: Double = 65.0,      // نسبة كتلة الائتلاف الحاكم
    val oppositionCoalitionPct: Double = 35.0,  // نسبة المعارضة
    val parliamentDissolved: Boolean = false,   // هل البرلمان محلول بمرسوم
    val filibusterActive: Boolean = false,      // تعطيل تشريعي مستمر
    val noConfidenceRiskPct: Double = 15.0      // خطر حجب الثقة عن الحكومة
)

data class ConstitutionArticle(
    val id: String,
    val titleArabic: String,
    val contentArabic: String,
    val isSuspended: Boolean = false,
    val amendmentThresholdPct: Double = 66.7
)

data class LegalAct(
    val id: String,
    val titleArabic: String,
    val categoryArabic: String,
    val descriptionArabic: String,
    val isEnacted: Boolean = false,
    val economicGrowthImpact: Double = 0.0,
    val stabilityImpact: Int = 0,
    val budgetCostBillionUsd: Double = 0.0
)

/**
 * حركات الاستقلال والانفصال (Separatism & Independence Movements)
 */
data class SeparatistMovement(
    val id: String,
    val regionNameArabic: String,
    val popularSupportPct: Double,     // التأييد الشعبي للاستقلال 0..100
    val armedMilitiaStrength: Int,     // عدد المقاتلين المسلحين
    val foreignBackingCountryId: String?,// الدولة الداعمة خارجياً
    val grievancesScore: Int,          // مستوى المظالم التاريخية والتمييز 0..100
    val autonomyOffered: Boolean = false, // هل تم عرض حكم ذاتي
    val referendumDemanded: Boolean = false, // المطالبة باستفتاء تقرير المصير
    val activeCivilWar: Boolean = false, // اندلاع حرب أهلية مسلحة
    val internationalRecognitionCount: Int = 0 // عدد الدول المعترفة بالانفصال
)

/**
 * المجالات الكونية والميتافيزيقية الـ17 (17 Cosmic & Metaphysical Domains)
 */
data class CosmicDomain(
    val id: String,
    val titleArabic: String,
    val element1Arabic: String,
    val element2Arabic: String,
    val element3Arabic: String,
    val collapseRuleArabic: String,
    val isActive: Boolean = false,
    val energyCostPerDay: Double = 100.0,
    val blowbackRiskPct: Double = 15.0
)

/**
 * الأوامر السيادية الـ25 (25 Sovereign Decrees)
 */
data class SovereignDecreeItem(
    val id: String,
    val nameArabic: String,
    val categoryArabic: String,
    val effectArabic: String,
    val costBillionUsd: Double,
    val stabilityChange: Int,
    val isExecuted: Boolean = false
)

/**
 * منشآت دفتر السيادة الـ50 (50 Sovereign Facilities)
 */
data class SovereignFacility50(
    val id: String,
    val number: Int,
    val nameArabic: String,
    val categoryArabic: String,
    val costBillionUsd: Double,
    val buildDays: Int,
    val dailyOutputArabic: String,
    val isConstructed: Boolean = false
)

/**
 * ميكانيكيات التمويل السيادي الـ10 (10 Sovereign Financing Mechanisms)
 */
data class SovereignFinancingOption(
    val id: String,
    val titleArabic: String,
    val mechanismArabic: String,
    val liquidityYieldBillionUsd: Double,
    val hiddenConstitutionalDebtPenalty: Double,
    val publicTrustPenalty: Int
)
