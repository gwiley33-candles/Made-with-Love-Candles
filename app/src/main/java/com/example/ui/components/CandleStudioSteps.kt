package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.BotanicalSage

@Composable
fun VesselSelector(
    selectedVessel: VesselType,
    onSelectVessel: (VesselType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Select Handcrafted Vessel",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Each vessel is artisan-crafted and designed to be repurposed as home decor.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        VesselType.entries.forEach { vessel ->
            val isSelected = vessel == selectedVessel
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onSelectVessel(vessel) }
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) AmberGoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("vessel_option_${vessel.id}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) AmberGoldPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(vessel.colorHex))
                            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = if (vessel == VesselType.FROSTED_PEARL) Color.Black else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = vessel.displayName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$${String.format(java.util.Locale.US, "%.0f", vessel.basePrice)}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AmberGoldPrimary
                            )
                        }
                        Text(
                            text = "${vessel.sizeLabel} • Burn: ${vessel.burnTime}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = vessel.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WaxSelector(
    selectedWax: WaxType,
    onSelectWax: (WaxType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Choose Artisan Wax Blend",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "100% natural, paraffin-free, non-toxic clean-burning plant waxes.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        WaxType.entries.forEach { wax ->
            val isSelected = wax == selectedWax
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onSelectWax(wax) }
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) AmberGoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("wax_option_${wax.id}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) AmberGoldPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectWax(wax) },
                        colors = RadioButtonDefaults.colors(selectedColor = AmberGoldPrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = wax.displayName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            if (wax.extraPrice > 0) {
                                Text(
                                    text = "+$${String.format(java.util.Locale.US, "%.0f", wax.extraPrice)}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGoldPrimary
                                )
                            } else {
                                Text(
                                    text = "Included",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BotanicalSage
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BotanicalSage.copy(alpha = 0.12f),
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = wax.burnNote,
                                style = MaterialTheme.typography.labelSmall,
                                color = BotanicalSage,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = wax.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WickSelector(
    selectedWick: WickType,
    onSelectWick: (WickType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Select Flame & Wick",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "The soul of the candle. Choose crackling wood ambience or classic silent burn.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        WickType.entries.forEach { wick ->
            val isSelected = wick == selectedWick
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onSelectWick(wick) }
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) AmberGoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("wick_option_${wick.id}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) AmberGoldPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectWick(wick) },
                        colors = RadioButtonDefaults.colors(selectedColor = AmberGoldPrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = wick.displayName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            if (wick.extraPrice > 0) {
                                Text(
                                    text = "+$${String.format(java.util.Locale.US, "%.0f", wick.extraPrice)}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGoldPrimary
                                )
                            } else {
                                Text(
                                    text = "Included",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BotanicalSage
                                )
                            }
                        }
                        Text(
                            text = wick.subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = AmberGoldPrimary
                        )
                        Text(
                            text = wick.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FragranceArchitectureStep(
    topNote: String,
    heartNote: String,
    baseNote: String,
    intensity: Float,
    onSelectTopNote: (String) -> Unit,
    onSelectHeartNote: (String) -> Unit,
    onSelectBaseNote: (String) -> Unit,
    onIntensityChange: (Float) -> Unit,
    onSurpriseMe: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Fragrance Formulation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Craft your custom 3-tier scent pyramid with pure botanical oils.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            OutlinedButton(
                onClick = onSurpriseMe,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag("fragrance_inspire_me_button")
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Inspire", style = MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 1. Top Note
        FragranceTierSection(
            tierTitle = "TOP NOTE • FIRST IMPRESSION",
            tierDescription = "The immediate sparkling aura released upon lighting (burns first 15 mins).",
            selectedOption = topNote,
            options = SampleData.topNotes,
            onSelect = onSelectTopNote
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Heart Note
        FragranceTierSection(
            tierTitle = "HEART NOTE • PERSONALITY & CHARACTER",
            tierDescription = "The lingering body and emotional warmth of your candle room throw.",
            selectedOption = heartNote,
            options = SampleData.heartNotes,
            onSelect = onSelectHeartNote
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Base Note
        FragranceTierSection(
            tierTitle = "BASE NOTE • DEEP FOUNDATION",
            tierDescription = "Rich resinous roots, aged woods, and lingering grounding resins.",
            selectedOption = baseNote,
            options = SampleData.baseNotes,
            onSelect = onSelectBaseNote
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Scent Throw Intensity Slider
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Fragrance Throw Intensity",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = when {
                            intensity < 0.6f -> "Subtle & Delicate"
                            intensity < 0.85f -> "Balanced Living Room"
                            else -> "Intense Room-Filler"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = AmberGoldPrimary
                    )
                }
                Slider(
                    value = intensity,
                    onValueChange = onIntensityChange,
                    valueRange = 0.4f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = AmberGoldPrimary,
                        activeTrackColor = AmberGoldPrimary
                    ),
                    modifier = Modifier.testTag("fragrance_intensity_slider")
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Intimate Nook", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Signature Balance", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Grand Throw", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun FragranceTierSection(
    tierTitle: String,
    tierDescription: String,
    selectedOption: String,
    options: List<FragranceOption>,
    onSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = tierTitle,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = AmberGoldPrimary,
                letterSpacing = 0.8.sp
            )
            Text(
                text = tierDescription,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal or wrapped chips for fragrance notes
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                options.forEach { opt ->
                    val isSelected = opt.name == selectedOption
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelect(opt.name) }
                            .border(
                                width = if (isSelected) 1.5.dp else 0.5.dp,
                                color = if (isSelected) AmberGoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        color = if (isSelected) AmberGoldPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onSelect(opt.name) },
                                modifier = Modifier.size(20.dp),
                                colors = RadioButtonDefaults.colors(selectedColor = AmberGoldPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = opt.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                                Text(
                                    text = opt.aura,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.sp
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = opt.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LabelAndPersonalizationStep(
    candleName: String,
    onCandleNameChange: (String) -> Unit,
    customInscription: String,
    onInscriptionChange: (String) -> Unit,
    selectedLabelStyle: LabelStyle,
    onSelectLabelStyle: (LabelStyle) -> Unit,
    selectedLid: LidOption,
    onSelectLid: (LidOption) -> Unit,
    selectedBotanical: BotanicalTopping,
    onSelectBotanical: (BotanicalTopping) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Label & Bespoke Inscription",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Personalize the vessel label for yourself or as a thoughtful gift.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(14.dp))

        // Candle Name Input
        OutlinedTextField(
            value = candleName,
            onValueChange = onCandleNameChange,
            label = { Text("Custom Candle Title") },
            placeholder = { Text("e.g. Sunday Morning, Elena's Sanctuary") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("candle_title_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Inscription Input
        OutlinedTextField(
            value = customInscription,
            onValueChange = onInscriptionChange,
            label = { Text("Personal Dedication / Subtitle") },
            placeholder = { Text("e.g. Poured for Sarah • Happy 30th Birthday") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("candle_inscription_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Label Aesthetic Style
        Text(
            text = "Label Typography Style",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LabelStyle.entries.forEach { style ->
                val isSelected = style == selectedLabelStyle
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectLabelStyle(style) }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) AmberGoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) AmberGoldPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = style.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Vessel Lid Selection
        Text(
            text = "Vessel Lid Selection",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LidOption.entries.forEach { lid ->
            val isSelected = lid == selectedLid
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelectLid(lid) }
                    .border(
                        width = if (isSelected) 1.5.dp else 0.5.dp,
                        color = if (isSelected) AmberGoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                color = if (isSelected) AmberGoldPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectLid(lid) },
                        colors = RadioButtonDefaults.colors(selectedColor = AmberGoldPrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = lid.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )
                    if (lid.extraPrice > 0) {
                        Text(
                            text = "+$${String.format(java.util.Locale.US, "%.0f", lid.extraPrice)}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AmberGoldPrimary
                        )
                    } else {
                        Text("Included", style = MaterialTheme.typography.labelSmall, color = BotanicalSage)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Botanical Sprinkles on Wax
        Text(
            text = "Dried Botanical Wax Sprinkle",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Hand-sprinkled organic dried florals and spices gently set on top of the poured wax.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        BotanicalTopping.entries.forEach { bot ->
            val isSelected = bot == selectedBotanical
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelectBotanical(bot) }
                    .border(
                        width = if (isSelected) 1.5.dp else 0.5.dp,
                        color = if (isSelected) AmberGoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                color = if (isSelected) AmberGoldPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectBotanical(bot) },
                        colors = RadioButtonDefaults.colors(selectedColor = AmberGoldPrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = bot.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        Text(
                            text = bot.description,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}
