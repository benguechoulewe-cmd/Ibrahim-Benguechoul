package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorldDataCatalog
import com.example.model.GameState
import com.example.model.SimulationSpeed
import com.example.ui.theme.*

@Composable
fun TopCommandBar(
    gameState: GameState,
    onSpeedChange: (SimulationSpeed) -> Unit,
    onReturnToCountrySelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playerCountry = WorldDataCatalog.getCountryById(gameState.playerCountryId)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("top_command_bar"),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Row 1: Country identity, Day, Speed Button, Nations Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        playerCountry?.flagEmoji ?: "🌐",
                        fontSize = 24.sp,
                        modifier = Modifier.testTag("player_flag")
                    )
                    Column {
                        Text(
                            playerCountry?.nameArabic ?: "القيادة السيادية",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            "اليوم ${gameState.currentDay} من المحاكاة",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = NeonCyan,
                            modifier = Modifier.testTag("simulation_day_label")
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Time Button with 4 Speed Modes
                    Button(
                        onClick = {
                            val nextSpeed = when (gameState.speed) {
                                SimulationSpeed.PAUSED -> SimulationSpeed.NORMAL
                                SimulationSpeed.NORMAL -> SimulationSpeed.FAST
                                SimulationSpeed.FAST -> SimulationSpeed.ULTRA
                                SimulationSpeed.ULTRA -> SimulationSpeed.PAUSED
                            }
                            onSpeedChange(nextSpeed)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (gameState.speed) {
                                SimulationSpeed.PAUSED -> TacticalSlate
                                SimulationSpeed.NORMAL -> EmeraldSuccess
                                SimulationSpeed.FAST -> WarningOrange
                                SimulationSpeed.ULTRA -> AlertCrimson
                            }
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("time_speed_button")
                    ) {
                        Text(
                            "${gameState.speed.icon} ${gameState.speed.label}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    // Return to Country Selection / Nations List
                    IconButton(
                        onClick = onReturnToCountrySelection,
                        modifier = Modifier
                            .size(36.dp)
                            .background(CommandCard, RoundedCornerShape(8.dp))
                            .testTag("return_nations_button")
                    ) {
                        Icon(
                            Icons.Default.Public,
                            contentDescription = "الخريطة والدول",
                            tint = SovereignGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Row 2: Tactical Telemetry Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TelemetryPill(
                    label = "الخزينة",
                    value = "$${String.format("%.1f", gameState.economy.treasuryCashBillionUsd)}B",
                    valueColor = if (gameState.economy.treasuryCashBillionUsd >= 0) SovereignGold else AlertCrimson,
                    tag = "telemetry_treasury"
                )
                TelemetryPill(
                    label = "الميزان اليومي",
                    value = (if (gameState.economy.dailySurplusDeficit >= 0) "+$" else "-$") +
                            "${String.format("%.2f", kotlin.math.abs(gameState.economy.dailySurplusDeficit))}B",
                    valueColor = if (gameState.economy.dailySurplusDeficit >= 0) EmeraldSuccess else AlertCrimson,
                    tag = "telemetry_surplus"
                )
                TelemetryPill(
                    label = "الدين العام",
                    value = "$${String.format("%.1f", gameState.economy.nationalDebtBillionUsd)}B",
                    valueColor = TextSecondary,
                    tag = "telemetry_debt"
                )
                TelemetryPill(
                    label = "الاستقرار",
                    value = "${playerCountry?.stabilityIndex ?: 75}%",
                    valueColor = if ((playerCountry?.stabilityIndex ?: 75) > 65) EmeraldSuccess else WarningOrange,
                    tag = "telemetry_support"
                )
            }
        }
    }
}

@Composable
private fun TelemetryPill(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color,
    tag: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CommandCard),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.testTag(tag)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                fontSize = 9.sp
            )
            Text(
                value,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = valueColor,
                fontSize = 11.sp
            )
        }
    }
}
