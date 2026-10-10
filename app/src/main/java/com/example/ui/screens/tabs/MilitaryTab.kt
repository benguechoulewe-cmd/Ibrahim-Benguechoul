package com.example.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.model.MilitaryBranch
import com.example.ui.theme.*

@Composable
fun MilitaryTab(
    gameState: GameState,
    onToggleMissileDefense: () -> Unit,
    onToggleDroneSwarms: () -> Unit,
    onSetAlertLevel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val mil = gameState.military

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .testTag("military_tab"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // 1. Readiness & Strategic Posture
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("الجاهزية العسكرية والوضع الاستراتيجي", style = MaterialTheme.typography.labelSmall, color = AlertCrimson)
                        AssistChip(
                            onClick = {},
                            label = { Text(mil.warReadinessAlert, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                            colors = AssistChipDefaults.assistChipColors(containerColor = CommandCard, labelColor = SovereignGold)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        MilStatBox("القوة القتالية", "${mil.overallCombatPower} / 100", SovereignGold)
                        MilStatBox("نسبة الجاهزية", "${mil.readinessPercentage.toInt()}%", if (mil.readinessPercentage > 75) EmeraldSuccess else AlertCrimson)
                        MilStatBox("القوات العاملة", "${mil.totalActivePersonnel / 1000} ألف", NeonCyan)
                        MilStatBox("قوات الاحتياط", "${mil.totalReserves / 1000} ألف", TextSecondary)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "الإنفاق اليومي للصيانة والتسليح: $${String.format("%.2f", mil.dailyDefenseBudgetBillionUsd)}B / يوم",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // 2. The 3 Armed Service Branches
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("الأفرع العسكرية الرئيسية للقوات المسلحة", style = MaterialTheme.typography.labelSmall, color = SovereignGold)
                    Spacer(modifier = Modifier.height(10.dp))
                    BranchItem(mil.army)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderHighlight)
                    BranchItem(mil.navy)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderHighlight)
                    BranchItem(mil.airForce)
                }
            }
        }

        // 3. Next-Gen Defense Systems (R&D)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("منظومات الردع التكنولوجية والتسليح المتقدم", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                    Spacer(modifier = Modifier.height(8.dp))

                    TechToggleRow(
                        titleAr = "درع الدفاع الصاروخي الحركي المتقدم",
                        isActive = mil.missileDefenseActive,
                        onToggle = onToggleMissileDefense,
                        testTag = "toggle_missile_shield"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    TechToggleRow(
                        titleAr = "شبكة أسراب المسيرات الهجومية المستقلة",
                        isActive = mil.droneSwarmsActive,
                        onToggle = onToggleDroneSwarms,
                        testTag = "toggle_drone_swarms"
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CommandCard, RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("الحرب السيبرانية والاستطلاع الذكي:", style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                        Text(
                            "المستوى ${mil.cyberWarfareTier} / 5",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    }
                }
            }
        }

        // 4. Alert Level Commands
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("أوامر القيادة العامة ومستويات التأهب الوطني", style = MaterialTheme.typography.labelSmall, color = SovereignGold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onSetAlertLevel("DEFCON 5: سلام اعتيادي") },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(2.dp)
                        ) {
                            Text("DEFCON 5 (سلام)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        }

                        Button(
                            onClick = { onSetAlertLevel("DEFCON 3: تأهب دفاعي") },
                            colors = ButtonDefaults.buttonColors(containerColor = WarningOrange),
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(2.dp)
                        ) {
                            Text("DEFCON 3 (استنفار)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        }

                        Button(
                            onClick = { onSetAlertLevel("DEFCON 1: حالة حرب قصوى") },
                            colors = ButtonDefaults.buttonColors(containerColor = AlertCrimson),
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(2.dp)
                        ) {
                            Text("DEFCON 1 (حرب)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
private fun MilStatBox(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodySmall, fontSize = 9.sp, color = TextMuted)
        Text(value, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = color, fontSize = 12.sp)
    }
}

@Composable
private fun BranchItem(branch: MilitaryBranch) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(branch.nameArabic, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(
                "الأفراد: ${branch.personnel / 1000} ألف مقاتل · العتاد: ${branch.heavyEquipmentUnits} وحدة",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text("${branch.readinessPct.toInt()}% جاهزية", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NeonCyan)
            Text("$${String.format("%.2f", branch.dailyUpkeepBillionUsd)}B / يوم", style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 10.sp)
        }
    }
}

@Composable
private fun TechToggleRow(
    titleAr: String,
    isActive: Boolean,
    onToggle: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (isActive) NeonCyan else BorderHighlight, RoundedCornerShape(8.dp))
            .background(if (isActive) CommandCard else DeepNavy, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(titleAr, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(
                if (isActive) "المنظومة في الخدمة وتوفر حماية سيادية" else "المنظومة معطلة وتنتظر التفعيل",
                style = MaterialTheme.typography.bodySmall,
                color = if (isActive) EmeraldSuccess else TextMuted,
                fontSize = 9.sp
            )
        }

        Switch(
            checked = isActive,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeonCyan,
                checkedTrackColor = DeepNavy
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
