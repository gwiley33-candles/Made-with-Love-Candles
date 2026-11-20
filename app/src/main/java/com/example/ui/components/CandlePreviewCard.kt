package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun CandlePreviewCard(
    candle: CustomCandle,
    modifier: Modifier = Modifier,
    isLit: Boolean = true,
    onToggleLit: () -> Unit = {},
    onToggleFavorite: () -> Unit = {},
    showControls: Boolean = true
) {
    // Flame flicker animation
    val infiniteTransition = rememberInfiniteTransition(label = "flame_flicker")
    val flameScaleY by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(180, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_y"
    )
    val flameSway by infiniteTransition.animateFloat(
        initialValue = -2.5f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(220, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_sway"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("candle_preview_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with status & actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isLit) AmberGoldPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, if (isLit) AmberGoldPrimary.copy(alpha = 0.4f) else Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isLit) FlameMid else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isLit) "BURNING • ${candle.vessel.burnTime}" else "UNLIT • READY",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isLit) AmberGoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("favorite_candle_button")
                    ) {
                        Icon(
                            imageVector = if (candle.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite candle",
                            tint = if (candle.isFavorite) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (showControls) {
                        FilledTonalIconButton(
                            onClick = onToggleLit,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("toggle_flame_button"),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = if (isLit) AmberGoldPrimary else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocalFireDepartment,
                                contentDescription = if (isLit) "Snuff flame" else "Light candle",
                                tint = if (isLit) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Interactive Candle Display Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clickable { onToggleLit() }
                    .testTag("interactive_candle_canvas_container"),
                contentAlignment = Alignment.Center
            ) {
                // Background radial glow when lit
                if (isLit) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    FlameMid.copy(alpha = 0.35f * glowAlpha),
                                    FlameCore.copy(alpha = 0.15f * glowAlpha),
                                    Color.Transparent
                                ),
                                center = Offset(size.width / 2f, size.height * 0.28f),
                                radius = size.width * 0.45f
                            )
                        )
                    }
                }

                // Vessel Canvas & Flame Drawing
                Canvas(
                    modifier = Modifier
                        .width(180.dp)
                        .height(220.dp)
                ) {
                    drawCandleScene(
                        candle = candle,
                        isLit = isLit,
                        flameScaleY = flameScaleY,
                        flameSway = flameSway
                    )
                }

                // Custom Printed Label overlay
                CustomLabelOverlay(
                    candle = candle,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = 22.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Candle Recipe Specs Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SpecColumn(title = "VESSEL", value = candle.vessel.sizeLabel)
                Divider(
                    modifier = Modifier
                        .height(20.dp)
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                SpecColumn(title = "WAX", value = candle.wax.displayName.split(" ").first())
                Divider(
                    modifier = Modifier
                        .height(20.dp)
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                SpecColumn(title = "WICK", value = if (candle.wick == WickType.WOOD_WICK) "Wood Crackle" else "Cotton")
                Divider(
                    modifier = Modifier
                        .height(20.dp)
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                SpecColumn(title = "TOTAL", value = candle.formattedPrice, isHighlight = true)
            }
        }
    }
}

@Composable
private fun SpecColumn(title: String, value: String, isHighlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 9.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isHighlight) AmberGoldPrimary else MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}

private fun DrawScope.drawCandleScene(
    candle: CustomCandle,
    isLit: Boolean,
    flameScaleY: Float,
    flameSway: Float
) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    // Vessel dimensions
    val vesselWidth = canvasWidth * 0.78f
    val vesselHeight = canvasHeight * 0.62f
    val vesselLeft = (canvasWidth - vesselWidth) / 2f
    val vesselTop = canvasHeight * 0.34f

    // 1. Draw Vessel Body
    val vesselColor = Color(candle.vessel.colorHex)
    val vesselCorner = 12.dp.toPx()

    // Vessel background shadow / ambient occlusion
    drawRoundRect(
        color = Color(0x33000000),
        topLeft = Offset(vesselLeft + 4, vesselTop + 6),
        size = Size(vesselWidth - 8, vesselHeight),
        cornerRadius = CornerRadius(vesselCorner, vesselCorner)
    )

    // Vessel Base Gradient
    val vesselBrush = when (candle.vessel) {
        VesselType.AMBER_APOTHECARY -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF6B2F04),
                Color(0xFFB45309),
                Color(0xFFD97706),
                Color(0xFFB45309),
                Color(0xFF522101)
            ),
            startX = vesselLeft,
            endX = vesselLeft + vesselWidth
        )
        VesselType.MATTE_OBSIDIAN -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF18181A),
                Color(0xFF2C2C30),
                Color(0xFF38383D),
                Color(0xFF222225),
                Color(0xFF141416)
            ),
            startX = vesselLeft,
            endX = vesselLeft + vesselWidth
        )
        VesselType.TERRACOTTA_CLAY -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF8F3924),
                Color(0xFFB4533C),
                Color(0xFFC86850),
                Color(0xFFA5452F),
                Color(0xFF752C1B)
            ),
            startX = vesselLeft,
            endX = vesselLeft + vesselWidth
        )
        VesselType.FROSTED_PEARL -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFE28DA6),
                Color(0xFFF7B7CA),
                Color(0xFFFFF2F6),
                Color(0xFFF7B7CA),
                Color(0xFFDA7D98)
            ),
            startX = vesselLeft,
            endX = vesselLeft + vesselWidth
        )
        VesselType.BRUSHED_BRASS -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF8C711D),
                Color(0xFFD4AF37),
                Color(0xFFF3E5AB),
                Color(0xFFC59B27),
                Color(0xFF6B5515)
            ),
            startX = vesselLeft,
            endX = vesselLeft + vesselWidth
        )
        VesselType.CONCRETE_MARBLE -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF5A5856),
                Color(0xFF7D7A75),
                Color(0xFF9E9A94),
                Color(0xFF73706B),
                Color(0xFF4C4A48)
            ),
            startX = vesselLeft,
            endX = vesselLeft + vesselWidth
        )
    }

    drawRoundRect(
        brush = vesselBrush,
        topLeft = Offset(vesselLeft, vesselTop),
        size = Size(vesselWidth, vesselHeight),
        cornerRadius = CornerRadius(vesselCorner, vesselCorner)
    )

    // Vessel Glass/Stoneware Rim
    val rimHeight = 8.dp.toPx()
    drawRoundRect(
        color = Color(0x33FFFFFF),
        topLeft = Offset(vesselLeft + 2, vesselTop),
        size = Size(vesselWidth - 4, rimHeight),
        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
    )

    // 2. Wax Surface Layer inside Rim
    val waxColor = when (candle.wax) {
        WaxType.GOLDEN_SOY -> Color(0xFFF9F6EE)
        WaxType.COCONUT_APRICOT -> Color(0xFFFCFAF5)
        WaxType.BEESWAX_BLEND -> Color(0xFFF3DF9A)
        WaxType.OLIVE_SOY -> Color(0xFFF1F2E8)
    }
    drawRoundRect(
        color = waxColor,
        topLeft = Offset(vesselLeft + 8, vesselTop + 3),
        size = Size(vesselWidth - 16, 12.dp.toPx()),
        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
    )

    // 3. Botanical Sprinkles on Wax
    when (candle.botanicalTopping) {
        BotanicalTopping.ROSE_PETALS -> {
            drawCircle(Color(0xFFE57399), 3.5f, Offset(vesselLeft + 22, vesselTop + 8))
            drawCircle(Color(0xFFF06292), 2.5f, Offset(vesselLeft + 32, vesselTop + 6))
            drawCircle(Color(0xFFD81B60), 3f, Offset(vesselLeft + vesselWidth - 26, vesselTop + 7))
            drawCircle(Color(0xFFFF80AB), 2.2f, Offset(vesselLeft + vesselWidth - 18, vesselTop + 9))
            drawCircle(Color(0xFFF48FB1), 2.8f, Offset(vesselLeft + vesselWidth / 2 - 14, vesselTop + 7))
        }
        BotanicalTopping.DRIED_LAVENDER -> {
            drawCircle(Color(0xFF7E57C2), 2.5f, Offset(vesselLeft + 22, vesselTop + 8))
            drawCircle(Color(0xFF9575CD), 2f, Offset(vesselLeft + 30, vesselTop + 6))
            drawCircle(Color(0xFF5E35B1), 2.8f, Offset(vesselLeft + vesselWidth - 28, vesselTop + 7))
            drawCircle(Color(0xFF7E57C2), 2.2f, Offset(vesselLeft + vesselWidth - 20, vesselTop + 9))
        }
        BotanicalTopping.CALENDULA_PETALS -> {
            drawCircle(Color(0xFFFFA000), 3f, Offset(vesselLeft + 24, vesselTop + 8))
            drawCircle(Color(0xFFFFB300), 2.5f, Offset(vesselLeft + vesselWidth - 25, vesselTop + 7))
        }
        BotanicalTopping.CINNAMON_DUST -> {
            drawCircle(Color(0xFF6D4C41), 1.5f, Offset(vesselLeft + 20, vesselTop + 8))
            drawCircle(Color(0xFF5D4037), 1.5f, Offset(vesselLeft + 28, vesselTop + 6))
            drawCircle(Color(0xFF6D4C41), 1.5f, Offset(vesselLeft + vesselWidth - 22, vesselTop + 7))
        }
        BotanicalTopping.ROSEMARY_SPRIGS -> {
            drawLine(Color(0xFF388E3C), Offset(vesselLeft + 18, vesselTop + 9), Offset(vesselLeft + 28, vesselTop + 6), 2f)
            drawLine(Color(0xFF2E7D32), Offset(vesselLeft + vesselWidth - 28, vesselTop + 6), Offset(vesselLeft + vesselWidth - 18, vesselTop + 9), 2f)
        }
        BotanicalTopping.NONE -> Unit
    }

    // 4. Wick Drawing
    val wickCenterX = canvasWidth / 2f
    val wickBottom = vesselTop + 5

    when (candle.wick) {
        WickType.WOOD_WICK -> {
            val wickWidth = 14.dp.toPx()
            val wickHeight = 12.dp.toPx()
            drawRect(
                color = WoodWickBrown,
                topLeft = Offset(wickCenterX - wickWidth / 2, wickBottom - wickHeight),
                size = Size(wickWidth, wickHeight)
            )
            // Charred top edge
            drawRect(
                color = Color(0xFF1A1009),
                topLeft = Offset(wickCenterX - wickWidth / 2, wickBottom - wickHeight),
                size = Size(wickWidth, 2.5.dp.toPx())
            )
        }
        WickType.BRAIDED_COTTON -> {
            val wickWidth = 3.dp.toPx()
            val wickHeight = 14.dp.toPx()
            drawLine(
                color = Color(0xFF231B15),
                start = Offset(wickCenterX, wickBottom),
                end = Offset(wickCenterX, wickBottom - wickHeight),
                strokeWidth = wickWidth
            )
        }
        WickType.DUAL_COTTON -> {
            val offsetSpacing = 10.dp.toPx()
            val wickWidth = 2.5.dp.toPx()
            val wickHeight = 12.dp.toPx()
            drawLine(
                color = Color(0xFF231B15),
                start = Offset(wickCenterX - offsetSpacing, wickBottom),
                end = Offset(wickCenterX - offsetSpacing, wickBottom - wickHeight),
                strokeWidth = wickWidth
            )
            drawLine(
                color = Color(0xFF231B15),
                start = Offset(wickCenterX + offsetSpacing, wickBottom),
                end = Offset(wickCenterX + offsetSpacing, wickBottom - wickHeight),
                strokeWidth = wickWidth
            )
        }
    }

    // 5. Flame Drawing (If Lit)
    if (isLit) {
        val flameBaseY = wickBottom - 10.dp.toPx()
        val flameHeight = 32.dp.toPx() * flameScaleY
        val flameWidth = 14.dp.toPx()

        drawFlame(
            centerX = wickCenterX + flameSway,
            baseY = flameBaseY,
            width = flameWidth,
            height = flameHeight
        )

        // For dual wicks, draw second flame
        if (candle.wick == WickType.DUAL_COTTON) {
            val offsetSpacing = 10.dp.toPx()
            drawFlame(
                centerX = wickCenterX - offsetSpacing + flameSway * 0.7f,
                baseY = flameBaseY,
                width = flameWidth * 0.85f,
                height = flameHeight * 0.85f
            )
            drawFlame(
                centerX = wickCenterX + offsetSpacing - flameSway * 0.7f,
                baseY = flameBaseY,
                width = flameWidth * 0.85f,
                height = flameHeight * 0.85f
            )
        }
    }
}

private fun DrawScope.drawFlame(
    centerX: Float,
    baseY: Float,
    width: Float,
    height: Float
) {
    // Outer flame teardrop path
    val outerPath = Path().apply {
        moveTo(centerX, baseY - height)
        cubicTo(
            centerX + width, baseY - height * 0.45f,
            centerX + width * 0.8f, baseY,
            centerX, baseY
        )
        cubicTo(
            centerX - width * 0.8f, baseY,
            centerX - width, baseY - height * 0.45f,
            centerX, baseY - height
        )
        close()
    }

    // Outer warm amber-orange flame
    drawPath(
        path = outerPath,
        brush = Brush.verticalGradient(
            colors = listOf(FlameOuter.copy(alpha = 0.9f), FlameMid, FlameCore),
            startY = baseY - height,
            endY = baseY
        )
    )

    // Inner bright golden core
    val innerWidth = width * 0.48f
    val innerHeight = height * 0.55f
    val innerPath = Path().apply {
        moveTo(centerX, baseY - innerHeight)
        cubicTo(
            centerX + innerWidth, baseY - innerHeight * 0.4f,
            centerX + innerWidth * 0.7f, baseY,
            centerX, baseY
        )
        cubicTo(
            centerX - innerWidth * 0.7f, baseY,
            centerX - innerWidth, baseY - innerHeight * 0.4f,
            centerX, baseY - innerHeight
        )
        close()
    }

    drawPath(
        path = innerPath,
        brush = Brush.verticalGradient(
            colors = listOf(FlameCore, Color.White),
            startY = baseY - innerHeight,
            endY = baseY
        )
    )
}

@Composable
private fun CustomLabelOverlay(
    candle: CustomCandle,
    modifier: Modifier = Modifier
) {
    val isGoldFoil = candle.labelStyle == LabelStyle.GOLD_FOIL
    val labelBg = if (isGoldFoil) Color(0xFF23161C) else Color(0xFFFFF7F9)
    val textPrimary = if (isGoldFoil) Color(0xFFF3C5D3) else Color(0xFF28171C)
    val textSecondary = if (isGoldFoil) Color(0xFFE29AB1) else Color(0xFF755560)
    val borderColor = if (isGoldFoil) Color(0xFFE08CA4) else Color(0xFFD87D9A)

    Surface(
        modifier = modifier
            .width(134.dp)
            .shadow(4.dp, RoundedCornerShape(4.dp)),
        shape = RoundedCornerShape(if (candle.labelStyle == LabelStyle.BOTANICAL_ARCH) 16.dp else 4.dp),
        color = labelBg,
        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Atelier Monogram / Header
            Text(
                text = "MADE WITH LOVE CANDLES",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 6.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.7.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = textSecondary
            )

            Divider(
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .width(42.dp),
                color = textSecondary.copy(alpha = 0.4f),
                thickness = 0.5.dp
            )

            // Custom Candle Title
            Text(
                text = candle.name,
                style = MaterialTheme.typography.titleSmall,
                fontFamily = PlayfairFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                lineHeight = 13.sp,
                textAlign = TextAlign.Center,
                color = textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Fragrance Formula
            Text(
                text = "${candle.topNote.split(" & ").first()} • ${candle.heartNote.split(" & ").first()}",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 7.5.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                color = textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (candle.customInscription.isNotBlank()) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "“${candle.customInscription}”",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 6.5.sp,
                    textAlign = TextAlign.Center,
                    color = textSecondary.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
