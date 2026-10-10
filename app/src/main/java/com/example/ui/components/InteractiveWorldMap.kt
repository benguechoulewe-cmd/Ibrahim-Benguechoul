package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorldDataCatalog
import com.example.model.Country
import com.example.model.GameState
import com.example.ui.theme.*
import kotlin.math.sqrt

enum class MapLayer(val labelAr: String) {
    GEOPOLITICAL("الدبلوماسية والتحالفات"),
    ECONOMY("الناتج والثروة الاقتصادية"),
    MILITARY("القوة والتسليح العسكري"),
    RESOURCES("الموارد والطاقة الاستراتيجية"),
    STABILITY("مؤشر الاستقرار السياسي"),
    CRISES("بؤر الأزمات المشتعلة")
}

@Composable
fun InteractiveWorldMap(
    gameState: GameState?,
    selectedCountry: Country,
    onCountrySelected: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1.0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var activeLayer by remember { mutableStateOf(MapLayer.GEOPOLITICAL) }
    var showLayerMenu by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(DeepNavy, RoundedCornerShape(12.dp))
            .testTag("interactive_world_map")
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(0.7f, 4.5f)
                        offset += pan
                    }
                }
                .pointerInput(scale, offset) {
                    detectTapGestures { tapOffset ->
                        val width = size.width
                        val height = size.height
                        var closestCountry: Country? = null
                        var minDistance = Float.MAX_VALUE

                        for (country in WorldDataCatalog.allCountries) {
                            val mapX = ((country.mapLongitude + 180f) / 360f) * width
                            val mapY = ((90f - country.mapLatitude) / 180f) * height
                            val transformedX = (mapX - width / 2f) * scale + width / 2f + offset.x
                            val transformedY = (mapY - height / 2f) * scale + height / 2f + offset.y

                            val dx = tapOffset.x - transformedX
                            val dy = tapOffset.y - transformedY
                            val dist = sqrt(dx * dx + dy * dy)
                            if (dist < minDistance && dist < 48.dp.toPx()) {
                                minDistance = dist
                                closestCountry = country
                            }
                        }

                        if (closestCountry != null) {
                            onCountrySelected(closestCountry)
                        }
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // 1. Draw Grid lines
            val gridColor = Color(0x1A5BC0BE)
            for (lon in -180..180 step 45) {
                val x = ((lon + 180f) / 360f) * canvasWidth
                val tx = (x - canvasWidth / 2f) * scale + canvasWidth / 2f + offset.x
                drawLine(gridColor, Offset(tx, 0f), Offset(tx, canvasHeight), strokeWidth = 1f)
            }
            for (lat in -90..90 step 30) {
                val y = ((90f - lat) / 180f) * canvasHeight
                val ty = (y - canvasHeight / 2f) * scale + canvasHeight / 2f + offset.y
                drawLine(gridColor, Offset(0f, ty), Offset(canvasWidth, ty), strokeWidth = 1f)
            }

            // 2. Draw Equator line
            val equatorY = (0.5f * canvasHeight - canvasHeight / 2f) * scale + canvasHeight / 2f + offset.y
            drawLine(Color(0x33F4D06F), Offset(0f, equatorY), Offset(canvasWidth, equatorY), strokeWidth = 1.5f)

            // 3. Draw All 200 Countries
            val countries = WorldDataCatalog.allCountries
            for (c in countries) {
                val normX = ((c.mapLongitude + 180f) / 360f) * canvasWidth
                val normY = ((90f - c.mapLatitude) / 180f) * canvasHeight
                val screenX = (normX - canvasWidth / 2f) * scale + canvasWidth / 2f + offset.x
                val screenY = (normY - canvasHeight / 2f) * scale + canvasHeight / 2f + offset.y

                // Cull if out of view
                if (screenX < -20f || screenX > canvasWidth + 20f || screenY < -20f || screenY > canvasHeight + 20f) {
                    continue
                }

                val isPlayer = gameState?.playerCountryId == c.id
                val isSelected = selectedCountry.id == c.id

                val nodeColor = when (activeLayer) {
                    MapLayer.GEOPOLITICAL -> {
                        when {
                            isPlayer -> SovereignGold
                            gameState?.worldMacroMap?.get(c.id)?.isAlliedWithPlayer == true -> EmeraldSuccess
                            c.id in (gameState?.military?.activeTensions ?: emptyList()) -> AlertCrimson
                            else -> TacticalSlate
                        }
                    }
                    MapLayer.ECONOMY -> {
                        when {
                            c.gdpBillionUsd > 2000.0 -> SovereignGold
                            c.gdpBillionUsd > 500.0 -> NeonCyan
                            c.gdpBillionUsd > 100.0 -> EmeraldSuccess
                            else -> TacticalSlate
                        }
                    }
                    MapLayer.MILITARY -> {
                        when {
                            c.militaryRating > 80 -> AlertCrimson
                            c.militaryRating > 60 -> WarningOrange
                            c.militaryRating > 40 -> SovereignGold
                            else -> TacticalSlate
                        }
                    }
                    MapLayer.RESOURCES -> {
                        if (c.resources.energyOilGas > 80.0 || c.resources.mineralsMetals > 80.0) SovereignGold else NeonCyan
                    }
                    MapLayer.STABILITY -> {
                        when {
                            c.stabilityIndex > 80 -> EmeraldSuccess
                            c.stabilityIndex > 50 -> SovereignGold
                            else -> AlertCrimson
                        }
                    }
                    MapLayer.CRISES -> {
                        val inCrisis = gameState?.activeCrises?.any { it.affectedCountriesIds.contains(c.id) } == true
                        if (inCrisis) AlertCrimson else TacticalSlate
                    }
                }

                val baseRadius = when {
                    isPlayer -> 7f * scale.coerceAtMost(2.5f)
                    isSelected -> 6.5f * scale.coerceAtMost(2.5f)
                    c.gdpBillionUsd > 1000.0 -> 4.5f * scale.coerceAtMost(2f)
                    else -> 3f * scale.coerceAtMost(2f)
                }

                // Halo ring for player or selected country
                if (isPlayer) {
                    drawCircle(
                        color = SovereignGold.copy(alpha = 0.4f),
                        radius = baseRadius * 2.2f,
                        center = Offset(screenX, screenY)
                    )
                }
                if (isSelected) {
                    drawCircle(
                        color = NeonCyan.copy(alpha = 0.6f),
                        radius = baseRadius * 1.8f,
                        center = Offset(screenX, screenY),
                        style = Stroke(width = 2f)
                    )
                }

                drawCircle(
                    color = nodeColor,
                    radius = baseRadius,
                    center = Offset(screenX, screenY)
                )
            }
        }

        // Overlay Controls: Zoom In, Zoom Out, Layer Picker, Selected Country Badge
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(
                onClick = { scale = (scale * 1.25f).coerceAtMost(4.5f) },
                modifier = Modifier
                    .size(34.dp)
                    .background(CommandCard, RoundedCornerShape(6.dp))
                    .testTag("map_zoom_in")
            ) {
                Icon(Icons.Default.ZoomIn, contentDescription = "تكبير", tint = NeonCyan, modifier = Modifier.size(18.dp))
            }
            IconButton(
                onClick = { scale = (scale / 1.25f).coerceAtLeast(0.7f) },
                modifier = Modifier
                    .size(34.dp)
                    .background(CommandCard, RoundedCornerShape(6.dp))
                    .testTag("map_zoom_out")
            ) {
                Icon(Icons.Default.ZoomOut, contentDescription = "تصغير", tint = NeonCyan, modifier = Modifier.size(18.dp))
            }
            Box {
                IconButton(
                    onClick = { showLayerMenu = !showLayerMenu },
                    modifier = Modifier
                        .size(34.dp)
                        .background(CommandCard, RoundedCornerShape(6.dp))
                        .testTag("map_layers_button")
                ) {
                    Icon(Icons.Default.Layers, contentDescription = "طبقات الخريطة", tint = SovereignGold, modifier = Modifier.size(18.dp))
                }
                DropdownMenu(
                    expanded = showLayerMenu,
                    onDismissRequest = { showLayerMenu = false }
                ) {
                    MapLayer.entries.forEach { layer ->
                        DropdownMenuItem(
                            text = { Text(layer.labelAr) },
                            onClick = {
                                activeLayer = layer
                                showLayerMenu = false
                            }
                        )
                    }
                }
            }
        }

        // Selected Country Badge
        Card(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = CommandSurface.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(selectedCountry.flagEmoji, fontSize = 16.sp)
                Column {
                    Text(
                        "${selectedCountry.nameArabic} (${selectedCountry.id})",
                        style = MaterialTheme.typography.labelMedium,
                        color = SovereignGold
                    )
                    Text(
                        "الطبقة: ${activeLayer.labelAr}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
