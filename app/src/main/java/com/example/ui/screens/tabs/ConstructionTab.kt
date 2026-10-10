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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActiveProject
import com.example.model.ConstructionBlueprint
import com.example.model.GameState
import com.example.model.ProjectCategory
import com.example.simulation.ConstructionCatalog
import com.example.ui.theme.*

@Composable
fun ConstructionTab(
    gameState: GameState,
    onStartConstruction: (ConstructionBlueprint) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<ProjectCategory?>(null) }

    val blueprints = remember(selectedCategory) {
        if (selectedCategory == null) ConstructionCatalog.allBlueprints
        else ConstructionCatalog.allBlueprints.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .testTag("construction_tab"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Active Queue Header & Items
        if (gameState.constructionQueue.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CommandSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Engineering, contentDescription = null, tint = SovereignGold, modifier = Modifier.size(18.dp))
                            Text(
                                "المشاريع الجارية قيد الإنشاء (${gameState.constructionQueue.size})",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SovereignGold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        gameState.constructionQueue.forEach { active ->
                            ActiveProjectItem(active)
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    label = { Text("كافة القطاعات", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan,
                        selectedLabelColor = DeepNavy
                    )
                )

                ProjectCategory.entries.forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat.titleAr, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonCyan,
                            selectedLabelColor = DeepNavy
                        )
                    )
                }
            }
        }

        // Available Blueprints
        items(blueprints, key = { it.id }) { blueprint ->
            val isInQueue = gameState.constructionQueue.any { it.blueprint.id == blueprint.id }
            BlueprintCard(
                blueprint = blueprint,
                isInQueue = isInQueue,
                onStart = { onStartConstruction(blueprint) }
            )
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
private fun ActiveProjectItem(active: ActiveProject) {
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
                Text(active.blueprint.nameAr, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(
                    "متبقي ${active.daysRemaining} يوم",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { active.progressPct },
                color = SovereignGold,
                trackColor = DeepNavy,
                modifier = Modifier.fillMaxWidth().height(6.dp)
            )
        }
    }
}

@Composable
private fun BlueprintCard(
    blueprint: ConstructionBlueprint,
    isInQueue: Boolean,
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("blueprint_card_${blueprint.id}"),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        blueprint.nameAr,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(blueprint.category.titleAr, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 10.sp)
                }

                AssistChip(
                    onClick = {},
                    label = { Text("$${blueprint.costBillionUsd}B", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = CommandCard, labelColor = SovereignGold)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(blueprint.descriptionAr, style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 11.sp)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "مدة التنفيذ: ${blueprint.buildDays} يوماً · صيانة: $${blueprint.maintenanceBillionUsdPerDay}B / يوم",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    color = TextSecondary
                )

                if (isInQueue) {
                    Text("قيد البناء...", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                } else {
                    Button(
                        onClick = onStart,
                        colors = ButtonDefaults.buttonColors(containerColor = SovereignGold),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("start_build_${blueprint.id}")
                    ) {
                        Icon(Icons.Default.Build, contentDescription = null, tint = DeepNavy, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("بدء الإنشاء", color = DeepNavy, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
