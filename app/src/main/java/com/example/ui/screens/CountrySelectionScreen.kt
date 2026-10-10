package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.WorldDataCatalog
import com.example.model.Continent
import com.example.model.Country
import com.example.model.GameState
import com.example.ui.components.InteractiveWorldMap
import com.example.ui.theme.*

enum class CountrySortMode(val label: String) {
    GDP_HIGH("الناتج الأعلى"),
    MILITARY_HIGH("القوة العسكرية"),
    POPULATION_HIGH("عدد السكان"),
    ALPHABETICAL("الترتيب الأبجدي")
}

@Composable
fun CountrySelectionScreen(
    gameState: GameState?,
    selectedCountry: Country,
    onCountrySelected: (Country) -> Unit,
    onStartCampaign: (Country) -> Unit,
    onResumeCampaign: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedContinent by remember { mutableStateOf<Continent?>(null) }
    var sortMode by remember { mutableStateOf(CountrySortMode.GDP_HIGH) }

    // Filter and Sort all 200 countries
    val filteredCountries = remember(searchQuery, selectedContinent, sortMode) {
        WorldDataCatalog.allCountries.filter { country ->
            val matchesSearch = country.nameArabic.contains(searchQuery, ignoreCase = true) ||
                    country.name.contains(searchQuery, ignoreCase = true) ||
                    country.capital.contains(searchQuery, ignoreCase = true) ||
                    country.id.contains(searchQuery, ignoreCase = true)
            val matchesContinent = selectedContinent == null || country.continent == selectedContinent
            matchesSearch && matchesContinent
        }.let { list ->
            when (sortMode) {
                CountrySortMode.ALPHABETICAL -> list.sortedBy { it.nameArabic }
                CountrySortMode.GDP_HIGH -> list.sortedByDescending { it.gdpBillionUsd }
                CountrySortMode.MILITARY_HIGH -> list.sortedByDescending { it.militaryRating }
                CountrySortMode.POPULATION_HIGH -> list.sortedByDescending { it.populationMillions }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavy)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("country_selection_screen")
    ) {
        // App Header Banner
        Surface(
            color = CommandSurface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "حروب الأموال: محاكاة العالم الكبرى",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = SovereignGold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        "إصدار الـ 200 دولة الكامل · إستراتيجية سيادية وجيوسياسية شاملة",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan,
                        fontSize = 11.sp
                    )
                }

                if (gameState?.isCampaignStarted == true && onResumeCampaign != null) {
                    Button(
                        onClick = onResumeCampaign,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("resume_campaign_button")
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("متابعة المحاكاة (${gameState.currentDay} يوم)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            // Section 1: Interactive World Map
            item {
                Spacer(modifier = Modifier.height(10.dp))
                InteractiveWorldMap(
                    gameState = gameState,
                    selectedCountry = selectedCountry,
                    onCountrySelected = onCountrySelected
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Section 2: Selected Country Inspector Card
            item {
                SelectedCountryInspectorCard(
                    country = selectedCountry,
                    onStartCampaign = { onStartCampaign(selectedCountry) }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Section 3: Search and Filter Controls
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CommandSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Search text field
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("ابحث بالاسم أو العاصمة أو الرمز في 200 دولة...", color = TextMuted) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonCyan) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "مسح", tint = TextMuted)
                                    }
                                }
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = BorderHighlight,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = CommandCard,
                                unfocusedContainerColor = CommandCard
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("country_search_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Continent Filter Chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = selectedContinent == null,
                                onClick = { selectedContinent = null },
                                label = { Text("كافة القارات (200 دولة)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonCyan,
                                    selectedLabelColor = DeepNavy
                                ),
                                modifier = Modifier.testTag("filter_continent_all")
                            )
                            Continent.entries.forEach { cont ->
                                FilterChip(
                                    selected = selectedContinent == cont,
                                    onClick = { selectedContinent = cont },
                                    label = { Text(cont.arabicName) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NeonCyan,
                                        selectedLabelColor = DeepNavy
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Sort Mode Selector
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("الترتيب حسب:", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                            CountrySortMode.entries.forEach { mode ->
                                val isSelected = sortMode == mode
                                SuggestionChip(
                                    onClick = { sortMode = mode },
                                    label = { Text(mode.label, fontSize = 11.sp) },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = if (isSelected) SovereignGold else CommandCard,
                                        labelColor = if (isSelected) DeepNavy else TextPrimary
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "عرض ${filteredCountries.size} من أصل 200 دولة وإقليم حول العالم",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }

            // Section 4: 200 Countries List items
            items(filteredCountries, key = { it.id }) { country ->
                val isSelected = country.id == selectedCountry.id
                CountryListItem(
                    country = country,
                    isSelected = isSelected,
                    onClick = { onCountrySelected(country) }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun SelectedCountryInspectorCard(
    country: Country,
    onStartCampaign: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, SovereignGold, RoundedCornerShape(12.dp))
            .testTag("selected_country_card"),
        colors = CardDefaults.cardColors(containerColor = CommandCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Flag, Name, Capital, Continent
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(country.flagEmoji, fontSize = 36.sp)
                    Column {
                        Text(
                            country.nameArabic,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            "العاصمة: ${country.capital} · القارة: ${country.continent.arabicName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                AssistChip(
                    onClick = {},
                    label = { Text(country.continent.arabicName, fontSize = 10.sp) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = CommandSurface, labelColor = NeonCyan)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderHighlight)

            // Grid Metrics
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CountryMetricCell("عدد السكان", "${country.populationMillions} مليون")
                CountryMetricCell("الناتج الاسمي", "$${country.gdpBillionUsd} مليار")
                CountryMetricCell("العملة السيادية", "${country.currencyCode} (${country.currencyName})")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CountryMetricCell("الميزانية العامة", "$${country.govtBudgetBillionUsd} مليار")
                CountryMetricCell("القوة العسكرية", "${country.militaryRating} / 100")
                CountryMetricCell("الاستقرار الداخلي", "${country.stabilityIndex}%")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CountryMetricCell("المستوى التقني", "المستوى ${country.techLevel} / 10")
                CountryMetricCell("النفط والطاقة", "${country.resources.energyOilGas.toInt()}%")
                CountryMetricCell("المعادن والموارد", "${country.resources.mineralsMetals.toInt()}%")
            }

            if (country.isEstimatedData) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "* ملاحظة: بيانات بعض الأقاليم الصغرى تم تقديرها بالاعتماد على الحوليات والتقارير الاقتصادية الدولية.",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    color = WarningOrange
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Primary Start Button
            Button(
                onClick = onStartCampaign,
                colors = ButtonDefaults.buttonColors(containerColor = SovereignGold),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("confirm_country_start_button")
            ) {
                Icon(Icons.Default.Flag, contentDescription = null, tint = DeepNavy)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "قيادة ${country.nameArabic} (بدء المحاكاة السيادية)",
                    color = DeepNavy,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun CountryMetricCell(label: String, value: String) {
    Column(modifier = Modifier.widthIn(min = 90.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall, fontSize = 10.sp, color = TextMuted)
        Text(value, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}

@Composable
private fun CountryListItem(
    country: Country,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.border(1.5.dp, NeonCyan, RoundedCornerShape(8.dp))
                else Modifier
            )
            .testTag("country_item_${country.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) CommandCard else CommandSurface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(country.flagEmoji, fontSize = 22.sp)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(country.nameArabic, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(country.id, style = MaterialTheme.typography.labelSmall, color = SovereignGold, fontSize = 10.sp)
                    }
                    Text("${country.capital} · ${country.continent.arabicName}", style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("$${country.gdpBillionUsd} مليار ناتج", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NeonCyan)
                Text("جيش: ${country.militaryRating} | سكان: ${country.populationMillions}M", style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 10.sp)
            }
        }
    }
}
