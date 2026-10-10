package com.example.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

enum class SovereigntySubSection(val arabicTitle: String) {
    EMOTIONS("المشاعر والوعي"),
    GOVERNANCE("الحكم والدستور"),
    DECREES("الأوامر السيادية (25)"),
    METAPHYSICS("الميتافيزيقا (17)"),
    SEPARATISM("الاستقلال والانفصال")
}

@Composable
fun SovereigntyTab(
    gameState: GameState,
    onExecuteDecree: (String) -> Unit,
    onToggleAutocracyTool: (String) -> Unit,
    onToggleCosmicDomain: (String) -> Unit,
    onToggleLaw: (String) -> Unit,
    onOfferAutonomy: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeSection by remember { mutableStateOf(SovereigntySubSection.EMOTIONS) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .testTag("sovereignty_tab"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Section Selector Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SovereigntySubSection.entries.forEach { section ->
                    val isSelected = activeSection == section
                    FilterChip(
                        selected = isSelected,
                        onClick = { activeSection = section },
                        label = { Text(section.arabicTitle, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SovereignGold,
                            selectedLabelColor = DeepNavy,
                            containerColor = CommandCard,
                            labelColor = TextPrimary
                        ),
                        modifier = Modifier.testTag("sovereignty_chip_${section.name}")
                    )
                }
            }
        }

        when (activeSection) {
            SovereigntySubSection.EMOTIONS -> {
                item {
                    EmotionsSectionCard(gameState.psychology)
                }
            }
            SovereigntySubSection.GOVERNANCE -> {
                item {
                    GovernanceSectionCard(
                        gameState = gameState,
                        onToggleAutocracyTool = onToggleAutocracyTool,
                        onToggleLaw = onToggleLaw
                    )
                }
            }
            SovereigntySubSection.DECREES -> {
                items(gameState.sovereignDecrees, key = { it.id }) { decree ->
                    DecreeItemCard(decree = decree, onExecute = { onExecuteDecree(decree.id) })
                }
            }
            SovereigntySubSection.METAPHYSICS -> {
                items(gameState.cosmicDomains, key = { it.id }) { domain ->
                    MetaphysicsDomainCard(domain = domain, onToggle = { onToggleCosmicDomain(domain.id) })
                }
            }
            SovereigntySubSection.SEPARATISM -> {
                items(gameState.separatists, key = { it.id }) { sep ->
                    SeparatismCard(movement = sep, onOfferAutonomy = { onOfferAutonomy(sep.id) })
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun EmotionsSectionCard(psy: PsychologicalMatrix) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("مصفوفة المشاعر الفردية للشعب", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
            Spacer(modifier = Modifier.height(8.dp))
            EmotionMeter("الخوف من الحرب والفقر", psy.individual.fear, AlertCrimson)
            EmotionMeter("الغضب الشعبي والتمرد", psy.individual.anger, AlertCrimson)
            EmotionMeter("الأمل في المستقبل والنهضة", psy.individual.hope, EmeraldSuccess)
            EmotionMeter("الكرامة القومية والشرف", psy.individual.dignity, SovereignGold)
            EmotionMeter("الانتماء للوطن والهوية", psy.individual.belonging, NeonCyan)
            EmotionMeter("الحزن والحداد", psy.individual.grief, TextSecondary)

            Divider(modifier = Modifier.padding(vertical = 10.dp), color = BorderHighlight)

            Text("مؤشرات الوعي والمشاعر الجماعية", style = MaterialTheme.typography.labelSmall, color = SovereignGold)
            Spacer(modifier = Modifier.height(8.dp))
            EmotionMeter("الذعر المالي والتدافع البنكي", psy.collective.financialPanicBankRun, WarningOrange)
            EmotionMeter("الهستيريا الجماهيرية العامة", psy.collective.massHysteria, AlertCrimson)
            EmotionMeter("الحماس القومي للحرب", psy.collective.warEuphoria, NeonCyan)
            EmotionMeter("الإحباط والضياع القيمي (Anomie)", psy.collective.generalAnomie, TextMuted)
        }
    }
}

@Composable
private fun EmotionMeter(title: String, value: Double, color: androidx.compose.ui.graphics.Color) {
    val progress = (value / 100.0).toFloat().coerceIn(0f, 1f)
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(title, color = TextPrimary, fontSize = 11.sp)
            Text("${value.toInt()}%", color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { progress },
            color = color,
            trackColor = BorderHighlight,
            modifier = Modifier.fillMaxWidth().height(5.dp)
        )
    }
}

@Composable
private fun GovernanceSectionCard(
    gameState: GameState,
    onToggleAutocracyTool: (String) -> Unit,
    onToggleLaw: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Regime Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = CommandSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("نظام الحكم الحالي للدولة", style = MaterialTheme.typography.labelSmall, color = SovereignGold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(gameState.regimeType.arabicName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = NeonCyan)
                Text(gameState.regimeType.descriptionArabic, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
            }
        }

        // Autocracy Controls
        Card(
            colors = CardDefaults.cardColors(containerColor = CommandSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("أدوات الحكم المطلق والاستبداد الصارم", style = MaterialTheme.typography.labelSmall, color = AlertCrimson)
                Spacer(modifier = Modifier.height(8.dp))

                AutocracyRow("إلغاء الانتخابات وتجميد التداول السلمي", gameState.autocracyTools.electionsAbolished) { onToggleAutocracyTool("elections") }
                AutocracyRow("حل الأحزاب السياسية وحظر التكتلات", gameState.autocracyTools.partiesDissolved) { onToggleAutocracyTool("parties") }
                AutocracyRow("فرض الرقابة الشاملة وحجب المنصات", gameState.autocracyTools.totalCensorshipActive) { onToggleAutocracyTool("censorship") }
                AutocracyRow("تجميد الدستور وإعلان الأحكام العرفية", gameState.autocracyTools.constitutionFrozen) { onToggleAutocracyTool("constitution") }
                AutocracyRow("إعلان الحكم مدى الحياة وتوريث السلطة", gameState.autocracyTools.lifeTermDeclared) { onToggleAutocracyTool("life_term") }
            }
        }

        // Parliamentary Legislation
        Card(
            colors = CardDefaults.cardColors(containerColor = CommandSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("تشريعات وقوانين البرلمان السارية", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                Spacer(modifier = Modifier.height(8.dp))
                gameState.laws.forEach { law ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CommandCard, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(law.titleArabic, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(law.descriptionArabic, style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 10.sp)
                        }
                        Switch(
                            checked = law.isEnacted,
                            onCheckedChange = { onToggleLaw(law.id) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SovereignGold,
                                checkedTrackColor = DeepNavy
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun AutocracyRow(title: String, isEnabled: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = TextPrimary, fontSize = 11.sp, modifier = Modifier.weight(1f))
        Switch(
            checked = isEnabled,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AlertCrimson,
                checkedTrackColor = DeepNavy
            )
        )
    }
}

@Composable
private fun DecreeItemCard(decree: SovereignDecreeItem, onExecute: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(decree.nameArabic, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = SovereignGold)
                    Text("(${decree.categoryArabic})", style = MaterialTheme.typography.bodySmall, color = NeonCyan, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(decree.effectArabic, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                Text("التكلفة: $${decree.costBillionUsd}B | الاستقرار: ${if (decree.stabilityChange >= 0) "+" else ""}${decree.stabilityChange}%", color = TextMuted, fontSize = 10.sp)
            }

            Button(
                onClick = onExecute,
                colors = ButtonDefaults.buttonColors(containerColor = if (decree.isExecuted) EmeraldSuccess else SovereignGold),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(if (decree.isExecuted) "منفذ" else "إصدار", color = DeepNavy, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun MetaphysicsDomainCard(domain: CosmicDomain, onToggle: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (domain.isActive) Modifier.border(1.dp, NeonCyan, RoundedCornerShape(10.dp)) else Modifier),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(domain.titleArabic, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NeonCyan)
                Button(
                    onClick = onToggle,
                    colors = ButtonDefaults.buttonColors(containerColor = if (domain.isActive) AlertCrimson else CommandCard),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text(if (domain.isActive) "تعطيل" else "تفعيل", color = TextPrimary, fontSize = 10.sp)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("• ${domain.element1Arabic}", color = TextSecondary, fontSize = 10.sp)
            Text("• ${domain.element2Arabic}", color = TextSecondary, fontSize = 10.sp)
            Text("• ${domain.element3Arabic}", color = TextSecondary, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("⚠️ ${domain.collapseRuleArabic}", color = WarningOrange, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SeparatismCard(movement: SeparatistMovement, onOfferAutonomy: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(movement.regionNameArabic, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AlertCrimson)
                Text("التأييد: ${movement.popularSupportPct.toInt()}%", color = WarningOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("المسلحون: ${movement.armedMilitiaStrength} مقاتل | المظالم: ${movement.grievancesScore}/100", color = TextMuted, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onOfferAutonomy,
                colors = ButtonDefaults.buttonColors(containerColor = if (movement.autonomyOffered) EmeraldSuccess else SovereignGold),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth().height(34.dp)
            ) {
                Text(if (movement.autonomyOffered) "تم منح الحكم الذاتي الفيدرالي" else "منح حكم ذاتي فدرالي لنزع فتيل الانفصال", color = DeepNavy, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
