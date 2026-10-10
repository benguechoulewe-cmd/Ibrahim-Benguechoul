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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.model.Minister
import com.example.ui.theme.*

@Composable
fun MinistersTab(
    gameState: GameState,
    onApproveProposal: (ministerId: String, proposalId: String) -> Unit,
    onFireMinister: (ministerId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    val categories = remember {
        listOf("كافة الحقائب (25)") + gameState.ministers.map { it.portfolio.categoryAr }.distinct()
    }

    val filteredMinisters = remember(selectedCategoryFilter, gameState.ministers) {
        if (selectedCategoryFilter == null || selectedCategoryFilter == "كافة الحقائب (25)") {
            gameState.ministers
        } else {
            gameState.ministers.filter { it.portfolio.categoryAr == selectedCategoryFilter }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .testTag("ministers_tab"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = (selectedCategoryFilter == null && cat == "كافة الحقائب (25)") || (selectedCategoryFilter == cat)
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryFilter = if (cat == "كافة الحقائب (25)") null else cat },
                        label = { Text(cat, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SovereignGold,
                            selectedLabelColor = DeepNavy
                        )
                    )
                }
            }
        }

        items(filteredMinisters, key = { it.id }) { minister ->
            MinisterCard(
                minister = minister,
                onApproveProposal = { onApproveProposal(minister.id, it) },
                onFireMinister = { onFireMinister(minister.id) }
            )
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
private fun MinisterCard(
    minister: Minister,
    onApproveProposal: (String) -> Unit,
    onFireMinister: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("minister_card_${minister.portfolio.id}"),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header Row: Portfolio, Minister Name, Category Pill, Fire Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        minister.portfolio.titleAr,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        fontSize = 12.sp
                    )
                    Text(
                        minister.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    AssistChip(
                        onClick = {},
                        label = { Text(minister.portfolio.categoryAr, fontSize = 9.sp) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = CommandCard, labelColor = SovereignGold)
                    )

                    IconButton(
                        onClick = onFireMinister,
                        modifier = Modifier
                            .size(30.dp)
                            .testTag("fire_minister_${minister.portfolio.id}")
                    ) {
                        Icon(
                            Icons.Default.PersonRemove,
                            contentDescription = "إعفاء وتعيين بديل",
                            tint = AlertCrimson,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Stats Row: Competence, Experience, Loyalty, Integrity, Approval
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CommandCard, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MiniStat("الكفاءة", "${minister.competence}%", NeonCyan)
                MiniStat("الخبرة", "${minister.experience}%", TextSecondary)
                MiniStat("الولاء", "${minister.loyalty}%", EmeraldSuccess)
                MiniStat("النزاهة", "${minister.integrity}%", SovereignGold)
                MiniStat("الشعبية", "${minister.publicApproval}%", if (minister.publicApproval > 60) NeonCyan else AlertCrimson)
            }

            // Active AI Proposal Block
            val proposal = minister.activeProposal
            if (proposal != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderHighlight, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = DeepNavy)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("مقترح إصلاحي وزاري:", style = MaterialTheme.typography.bodySmall, fontSize = 9.sp, color = SovereignGold, fontWeight = FontWeight.Bold)
                                Text(proposal.titleArabic, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }

                            if (proposal.isApproved) {
                                Text("معتمد ومطبق", color = EmeraldSuccess, fontWeight = FontWeight.Black, fontSize = 11.sp)
                            } else {
                                Button(
                                    onClick = { onApproveProposal(proposal.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .height(30.dp)
                                        .testTag("approve_proposal_${proposal.id}")
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("اعتماد ($${proposal.costBillionUsd}B)", color = DeepNavy, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(proposal.description, style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniStat(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 9.sp, color = TextMuted)
        Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
