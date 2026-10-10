package com.example.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorldDataCatalog
import com.example.model.EventLogItem
import com.example.model.GameState
import com.example.model.PoliticalFaction
import com.example.ui.theme.*

@Composable
fun ReportsTab(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    val playerCountry = WorldDataCatalog.getCountryById(gameState.playerCountryId)

    // Compute player's rank across all 200 countries
    val gdpRank = remember(gameState.economy.gdpBillionUsd) {
        WorldDataCatalog.allCountries.count { it.gdpBillionUsd > gameState.economy.gdpBillionUsd } + 1
    }
    val militaryRank = remember(gameState.military.overallCombatPower) {
        WorldDataCatalog.allCountries.count { it.militaryRating > gameState.military.overallCombatPower } + 1
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .testTag("reports_tab"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // 1. Global Power Rankings Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("التصنيف والترتيب العالمي بين 200 دولة", style = MaterialTheme.typography.labelSmall, color = SovereignGold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        RankingBox("ترتيب الناتج القومي", "#$gdpRank من 200", SovereignGold)
                        RankingBox("الترتيب العسكري", "#$militaryRank من 200", AlertCrimson)
                        RankingBox("مؤشر الاستقرار", "${playerCountry?.stabilityIndex ?: 75}%", NeonCyan)
                    }
                }
            }
        }

        // 2. The 6 Political Factions Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("الفصائل السياسية والبرلمانية الست", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                    Spacer(modifier = Modifier.height(8.dp))
                    gameState.factions.forEach { faction ->
                        FactionRow(faction)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }

        // 3. The 5 Physical Simulation Mechanisms Telemetry
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("الآليات الفيزيائية الخمس للمحاكاة الواقعية", style = MaterialTheme.typography.labelSmall, color = SovereignGold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val p = gameState.physics
                    PhysicsMetricRow("1. إنتاجية الموارد وتدفق ليونتيف", "${String.format("%.1f", p.productionThrottleFactor * 100)}% من الطاقة القصوى")
                    PhysicsMetricRow("2. سلاسل الإمداد والشحن اللوجستي", "${p.logisticsCongestionPct.toInt()}% نسبة الازدحام")
                    PhysicsMetricRow("3. توازن شبكة الطاقة والكهرباء", "${if (p.energyGridDeficitSurplusPct >= 0) "+" else ""}${String.format("%.1f", p.energyGridDeficitSurplusPct)}% احتياطي الفائض")
                    PhysicsMetricRow("4. الإجهاد البيئي ومخاطر المناخ", "مخاطر الجفاف: ${p.climateDroughtFloodRiskPct.toInt()}% · التلوث: ${p.industrialPollutionIndex.toInt()}")
                    PhysicsMetricRow("5. قدرة الاستيعاب واهتراء المنشآت", "تشغيل المصانع: ${p.factoryUtilizationRatePct.toInt()}% · الاهتراء: ${p.infrastructureWearAndTearPct.toInt()}%")
                }
            }
        }

        // 4. Strategic Event Log
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("سجل الوقائع والأحداث التاريخية", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (gameState.eventLogs.isEmpty()) {
                        Text("لا توجد أحداث مسجلة حتى الآن.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    } else {
                        gameState.eventLogs.takeLast(15).reversed().forEach { log ->
                            EventLogItemRow(log)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
private fun RankingBox(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodySmall, fontSize = 9.sp, color = TextMuted)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun FactionRow(faction: PoliticalFaction) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CommandCard),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(faction.type.titleAr, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(faction.coreAgendaArabic, style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 9.sp)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text("النفوذ: ${faction.politicalLeveragePct.toInt()}%", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                    Text("الرضا: ${faction.approvalRating}%", style = MaterialTheme.typography.bodySmall, color = if (faction.approvalRating > 50) EmeraldSuccess else AlertCrimson, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun PhysicsMetricRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
        Text(value, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 11.sp)
    }
}

@Composable
private fun EventLogItemRow(log: EventLogItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CommandCard, RoundedCornerShape(6.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("اليوم ${log.day}:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = SovereignGold, fontSize = 10.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(log.title, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontSize = 11.sp)
    }
}
