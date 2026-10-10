package com.example.ui.screens.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.ui.theme.*

@Composable
fun EconomyTab(
    gameState: GameState,
    onUpdateTaxRates: (corp: Double, income: Double, tariff: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val econ = gameState.economy

    var corpTax by remember(econ.corporateTaxPct) { mutableFloatStateOf(econ.corporateTaxPct.toFloat()) }
    var incomeTax by remember(econ.personalIncomeTaxPct) { mutableFloatStateOf(econ.personalIncomeTaxPct.toFloat()) }
    var tariffTax by remember(econ.tradeTariffPct) { mutableFloatStateOf(econ.tradeTariffPct.toFloat()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .testTag("economy_tab"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // 1. Macro Summary Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("نظرة عامة على الاقتصاد الكلي والسيادة المالية", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        EconStatBox("الناتج المحلي الإجمالي", "$${String.format("%.1f", econ.gdpBillionUsd)}B", SovereignGold)
                        EconStatBox("النمو السنوي", "${String.format("%.2f", econ.gdpGrowthAnnualPct)}%", if (econ.gdpGrowthAnnualPct >= 0) EmeraldSuccess else AlertCrimson)
                        EconStatBox("احتياطي الخزينة", "$${String.format("%.1f", econ.treasuryCashBillionUsd)}B", if (econ.treasuryCashBillionUsd >= 0) NeonCyan else AlertCrimson)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        EconStatBox("الدين السيادي", "$${String.format("%.1f", econ.nationalDebtBillionUsd)}B", TextSecondary)
                        EconStatBox("معدل التضخم", "${String.format("%.1f", econ.inflationRatePct)}%", if (econ.inflationRatePct < 5) EmeraldSuccess else AlertCrimson)
                        EconStatBox("معدل البطالة", "${String.format("%.1f", econ.unemploymentRatePct)}%", if (econ.unemploymentRatePct < 6) EmeraldSuccess else WarningOrange)
                    }
                }
            }
        }

        // 2. Daily Fiscal Budget Balance
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("التسوية المالية والميزانية اليومية للدولة", style = MaterialTheme.typography.labelSmall, color = SovereignGold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("الإيرادات الضريبية اليومية:", color = TextSecondary, fontSize = 12.sp)
                        Text("+$${String.format("%.2f", econ.dailyRevenue)}B / يوم", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("الإنفاق الحكومي اليومي:", color = TextSecondary, fontSize = 12.sp)
                        Text("-$${String.format("%.2f", econ.dailyExpense)}B / يوم", color = AlertCrimson, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = BorderHighlight)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("صافي الميزان اليومي:", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        val net = econ.dailySurplusDeficit
                        Text(
                            (if (net >= 0) "+$" else "-$") + String.format("%.2f", kotlin.math.abs(net)) + "B / يوم",
                            color = if (net >= 0) EmeraldSuccess else AlertCrimson,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // 3. Tax Policy Sliders
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("السياسات والنسب الضريبية العامة", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                        Button(
                            onClick = { onUpdateTaxRates(corpTax.toDouble(), incomeTax.toDouble(), tariffTax.toDouble()) },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .height(30.dp)
                                .testTag("apply_tax_policy_button")
                        ) {
                            Text("اعتماد النسب", color = DeepNavy, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    TaxSliderRow("ضريبة أرباح الشركات", "${corpTax.toInt()}%", corpTax, 5f..45f) { corpTax = it }
                    TaxSliderRow("ضريبة الدخل الفردي", "${incomeTax.toInt()}%", incomeTax, 5f..55f) { incomeTax = it }
                    TaxSliderRow("الرسوم الجمركية على الواردات", "${tariffTax.toInt()}%", tariffTax, 0f..25f) { tariffTax = it }
                }
            }
        }

        // 4. Strategic Resource Stockpiles
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("المخزون الاستراتيجي للسلع والموارد الأساسية", style = MaterialTheme.typography.labelSmall, color = SovereignGold)
                    Spacer(modifier = Modifier.height(10.dp))
                    ResourceBarItem("الطاقة والوقود والنفط", econ.energyStockpile, NeonCyan)
                    ResourceBarItem("المعادن والموارد التعدينية", econ.mineralsStockpile, SovereignGold)
                    ResourceBarItem("الأمن الغذائي والمحاصيل", econ.foodStockpile, EmeraldSuccess)
                    ResourceBarItem("السلع والمعدات الصناعية", econ.industrialStockpile, TextSecondary)
                    ResourceBarItem("أشباه الموصلات والرقائق الذكية", econ.chipsStockpile, AlertCrimson)
                }
            }
        }

        // 5. Foreign Trade & Balance of Payments
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CommandSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("التجارة الخارجية وميزان المدفوعات السيادي", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        EconStatBox("الصادرات اليومية", "$${String.format("%.2f", econ.dailyExportsBillionUsd)}B", EmeraldSuccess)
                        EconStatBox("الواردات اليومية", "$${String.format("%.2f", econ.dailyImportsBillionUsd)}B", AlertCrimson)
                        EconStatBox("الميزان التجاري", "$${String.format("%.2f", econ.tradeBalanceBillionUsd)}B", SovereignGold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        EconStatBox("متوسط الراتب الشهري", "$${econ.averageMonthlyWageUsd.toInt()}", TextPrimary)
                        EconStatBox("ثقة المستهلك", "${econ.consumerConfidence.toInt()}/100", NeonCyan)
                        EconStatBox("التصنيف الائتماني", econ.creditRating, SovereignGold)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
private fun EconStatBox(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.widthIn(min = 90.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall, fontSize = 9.sp, color = TextMuted)
        Text(value, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = color, fontSize = 13.sp)
    }
}

@Composable
private fun TaxSliderRow(label: String, displayVal: String, value: Float, range: ClosedFloatingPointRange<Float>, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
            Text(displayVal, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NeonCyan)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan, inactiveTrackColor = BorderHighlight),
            modifier = Modifier.height(26.dp)
        )
    }
}

@Composable
private fun ResourceBarItem(label: String, stockpile: Double, barColor: androidx.compose.ui.graphics.Color) {
    val progress = (stockpile / 120.0).toFloat().coerceIn(0f, 1f)
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
            Text("${stockpile.toInt()} نقطة", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = barColor)
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { progress },
            color = barColor,
            trackColor = BorderHighlight,
            modifier = Modifier.fillMaxWidth().height(6.dp),
        )
    }
}
