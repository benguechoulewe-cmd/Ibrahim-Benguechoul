package com.example.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActiveCrisis
import com.example.model.CrisisResponseChoice
import com.example.model.CrisisSeverity
import com.example.model.GameState
import com.example.ui.theme.*

@Composable
fun CrisesTab(
    gameState: GameState,
    onResolveOption: (crisisId: String, choiceId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val crises = gameState.activeCrises

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .testTag("crises_tab"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = AlertCrimson, modifier = Modifier.size(28.dp))
                    Column {
                        Text("بروتوكول إدارة الأزمات والطوارئ القومية", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AlertCrimson)
                        Text(
                            if (crises.isEmpty()) "الأوضاع السيادية مستقرة ولا توجد أزمات طارئة تهدد الأمن القومي."
                            else "${crises.size} أزمات جيوسياسية واقتصادية نشطة تتطلب التدخل الفوري.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        if (crises.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(CommandSurface, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("كافة المسارح المحلية والإقليمية مستقرة وهادئة.", color = TextMuted, fontSize = 13.sp)
                }
            }
        } else {
            items(crises, key = { it.id }) { crisis ->
                CrisisCard(
                    crisis = crisis,
                    onExecuteChoice = { choiceId -> onResolveOption(crisis.id, choiceId) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
private fun CrisisCard(
    crisis: ActiveCrisis,
    onExecuteChoice: (String) -> Unit
) {
    val severityColor = when (crisis.severity) {
        CrisisSeverity.LOW -> NeonCyan
        CrisisSeverity.MEDIUM -> WarningOrange
        CrisisSeverity.CRITICAL -> AlertCrimson
        CrisisSeverity.CATASTROPHIC -> AlertCrimson
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, severityColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .testTag("crisis_card_${crisis.id}"),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(crisis.titleAr, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                AssistChip(
                    onClick = {},
                    label = { Text(crisis.severity.labelAr, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = CommandCard, labelColor = severityColor)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(crisis.triggerCauseAr, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DeepNavy, RoundedCornerShape(6.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("الأثر اليومي على النمو: -${String.format("%.2f", crisis.dailyGdpPenaltyPct)}%", style = MaterialTheme.typography.bodySmall, color = AlertCrimson, fontSize = 10.sp)
                Text("استنزاف الاستقرار: -${crisis.dailyStabilityPenalty} نقاط/يوم", style = MaterialTheme.typography.bodySmall, color = WarningOrange, fontSize = 10.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text("القرارات والإجراءات التنفيذية المتاحة:", style = MaterialTheme.typography.labelSmall, color = SovereignGold)

            Spacer(modifier = Modifier.height(6.dp))
            crisis.responseOptions.forEach { choice ->
                CrisisChoiceItem(choice, onExecute = { onExecuteChoice(choice.id) })
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun CrisisChoiceItem(
    choice: CrisisResponseChoice,
    onExecute: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CommandCard),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(choice.titleAr, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(choice.descriptionAr, style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 10.sp)
                }

                Button(
                    onClick = onExecute,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("execute_crisis_choice_${choice.id}")
                ) {
                    Text("تنفيذ القرار", color = DeepNavy, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "التكلفة: $${choice.financialCostBillionUsd}B · استعادة الاستقرار: +${choice.expectedStabilityGain}% · نسبة النجاح: ${choice.resolveChancePct}%",
                style = MaterialTheme.typography.bodySmall,
                color = NeonCyan,
                fontSize = 9.sp
            )
        }
    }
}
