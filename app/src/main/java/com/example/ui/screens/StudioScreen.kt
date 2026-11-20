package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.CustomCandle
import com.example.ui.components.*
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.BotanicalSage
import com.example.viewmodel.AppScreen
import com.example.viewmodel.CandleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(
    viewModel: CandleViewModel,
    modifier: Modifier = Modifier
) {
    val activeCandle by viewModel.activeCandle.collectAsStateWithLifecycle()
    val studioStep by viewModel.studioStep.collectAsStateWithLifecycle()
    val isLit by viewModel.isCandleLit.collectAsStateWithLifecycle()
    val mockupUiState by viewModel.mockupUiState.collectAsStateWithLifecycle()
    val artworkTheme by viewModel.labelArtworkTheme.collectAsStateWithLifecycle()
    val mockupScene by viewModel.mockupScene.collectAsStateWithLifecycle()
    val customLabelNotes by viewModel.customLabelNotes.collectAsStateWithLifecycle()
    val uploadedLabelBitmap by viewModel.uploadedLabelBitmap.collectAsStateWithLifecycle()

    var showAddToCartSheet by remember { mutableStateOf(false) }

    val stepTitles = listOf("Vessel", "Wax & Wick", "Fragrance", "Label & Lid", "AI Mockup")

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("studio_screen_content"),
            contentPadding = PaddingValues(bottom = 110.dp)
        ) {
            // Top Live Candle Visualizer
            item {
                CandlePreviewCard(
                    candle = activeCandle,
                    isLit = isLit,
                    onToggleLit = { viewModel.toggleCandleLit() },
                    onToggleFavorite = { viewModel.toggleFavoriteCurrentCandle() },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 10.dp)
                )
                if (studioStep != 4) {
                    OutlinedButton(
                        onClick = { viewModel.setStudioStep(4) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .heightIn(min = 48.dp)
                            .testTag("jump_to_ai_mockup_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AmberGoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Visualize Label on AI Candle Mockup",
                            color = AmberGoldPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Step Indicator Tabs
            item {
                ScrollableTabRow(
                    selectedTabIndex = studioStep,
                    edgePadding = 16.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = AmberGoldPrimary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    stepTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = studioStep == index,
                            onClick = { viewModel.setStudioStep(index) },
                            text = {
                                Text(
                                    text = "${index + 1}. $title",
                                    fontWeight = if (studioStep == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            },
                            modifier = Modifier.testTag("studio_tab_$index")
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Active Step Content
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    when (studioStep) {
                        0 -> VesselSelector(
                            selectedVessel = activeCandle.vessel,
                            onSelectVessel = { viewModel.updateActiveVessel(it) }
                        )
                        1 -> Column {
                            WaxSelector(
                                selectedWax = activeCandle.wax,
                                onSelectWax = { viewModel.updateActiveWax(it) }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            WickSelector(
                                selectedWick = activeCandle.wick,
                                onSelectWick = { viewModel.updateActiveWick(it) }
                            )
                        }
                        2 -> FragranceArchitectureStep(
                            topNote = activeCandle.topNote,
                            heartNote = activeCandle.heartNote,
                            baseNote = activeCandle.baseNote,
                            intensity = activeCandle.intensity,
                            onSelectTopNote = { viewModel.updateTopNote(it) },
                            onSelectHeartNote = { viewModel.updateHeartNote(it) },
                            onSelectBaseNote = { viewModel.updateBaseNote(it) },
                            onIntensityChange = { viewModel.updateIntensity(it) },
                            onSurpriseMe = { viewModel.shuffleFragranceNotes() }
                        )
                        3 -> LabelAndPersonalizationStep(
                            candleName = activeCandle.name,
                            onCandleNameChange = { viewModel.updateCandleName(it) },
                            customInscription = activeCandle.customInscription,
                            onInscriptionChange = { viewModel.updateCustomInscription(it) },
                            selectedLabelStyle = activeCandle.labelStyle,
                            onSelectLabelStyle = { viewModel.updateLabelStyle(it) },
                            selectedLid = activeCandle.lid,
                            onSelectLid = { viewModel.updateLidOption(it) },
                            selectedBotanical = activeCandle.botanicalTopping,
                            onSelectBotanical = { viewModel.updateBotanicalTopping(it) }
                        )
                        4 -> CandleMockupStudioCard(
                            candle = activeCandle,
                            mockupUiState = mockupUiState,
                            artworkTheme = artworkTheme,
                            mockupScene = mockupScene,
                            customLabelNotes = customLabelNotes,
                            uploadedLabelBitmap = uploadedLabelBitmap,
                            onSelectArtworkTheme = { viewModel.setLabelArtworkTheme(it) },
                            onSelectMockupScene = { viewModel.setMockupScene(it) },
                            onCustomLabelNotesChange = { viewModel.setCustomLabelNotes(it) },
                            onPickLabelImage = { uri -> viewModel.loadLabelArtworkFromUri(uri) },
                            onClearUploadedLabel = { viewModel.clearUploadedLabelBitmap() },
                            onGenerateMockup = { viewModel.generateCandleMockup() }
                        )
                    }
                }
            }

            // Step Navigation Buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (studioStep > 0) {
                        OutlinedButton(
                            onClick = { viewModel.setStudioStep(studioStep - 1) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("studio_prev_step_button")
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Previous")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    if (studioStep < 4) {
                        Button(
                            onClick = { viewModel.setStudioStep(studioStep + 1) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                            modifier = Modifier.testTag("studio_next_step_button")
                        ) {
                            Text("Next: ${stepTitles[studioStep + 1]}")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        Button(
                            onClick = { showAddToCartSheet = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                            modifier = Modifier.testTag("studio_review_add_button")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Complete & Add to Cart", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Bottom Sticky Action Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CUSTOM FORMULATION",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = activeCandle.formattedPrice,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AmberGoldPrimary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalIconButton(
                        onClick = { viewModel.saveCurrentCandleRecipe() },
                        modifier = Modifier.testTag("studio_save_recipe_button")
                    ) {
                        Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Save Recipe")
                    }

                    Button(
                        onClick = { showAddToCartSheet = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                        modifier = Modifier.testTag("studio_add_to_cart_cta")
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add to Cart", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Add to Cart / Gift Options Modal Sheet
        if (showAddToCartSheet) {
            AddToCartModalSheet(
                candle = activeCandle,
                onDismiss = { showAddToCartSheet = false },
                onConfirmAddToCart = { qty, matches, trimmer, wrap, note ->
                    viewModel.addActiveCandleToCart(qty, matches, trimmer, wrap, note)
                    showAddToCartSheet = false
                    viewModel.navigateTo(AppScreen.CART)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToCartModalSheet(
    candle: CustomCandle,
    onDismiss: () -> Unit,
    onConfirmAddToCart: (quantity: Int, matches: Boolean, trimmer: Boolean, giftWrap: Boolean, note: String) -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
    var addMatches by remember { mutableStateOf(true) }
    var addTrimmer by remember { mutableStateOf(false) }
    var addGiftWrap by remember { mutableStateOf(false) }
    var giftNote by remember { mutableStateOf("") }

    val calculatedUnit = remember(quantity, addMatches, addTrimmer, addGiftWrap) {
        var base = candle.totalPrice
        if (addMatches) base += 4.0
        if (addTrimmer) base += 9.0
        if (addGiftWrap) base += 5.0
        base * quantity
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("add_to_cart_sheet")
        ) {
            Text(
                text = "Order Custom Candle",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "'${candle.name}' • ${candle.vessel.sizeLabel}",
                style = MaterialTheme.typography.bodyMedium,
                color = AmberGoldPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quantity Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quantity",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalIconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }

                    Text(
                        text = "$quantity",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    FilledTonalIconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Artisan Accessories & Gifting",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Accessory Checkbox 1: Matches
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = addMatches,
                    onCheckedChange = { addMatches = it },
                    colors = CheckboxDefaults.colors(checkedColor = AmberGoldPrimary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Apothecary Match Jar with Striker", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Text(text = "Glass bottle with 40 black-tip wooden matches (+$4.00)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Accessory Checkbox 2: Wick Trimmer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = addTrimmer,
                    onCheckedChange = { addTrimmer = it },
                    colors = CheckboxDefaults.colors(checkedColor = AmberGoldPrimary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Brushed Brass Wick Trimmer", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Text(text = "Heavy brass scissors designed to catch wick debris (+$9.00)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Accessory Checkbox 3: Gift Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = addGiftWrap,
                    onCheckedChange = { addGiftWrap = it },
                    colors = CheckboxDefaults.colors(checkedColor = AmberGoldPrimary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Luxury Gift Packaging & Card", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Text(text = "Kraft gift box, velvet ribbon & wax-sealed note (+$5.00)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            if (addGiftWrap) {
                OutlinedTextField(
                    value = giftNote,
                    onValueChange = { giftNote = it },
                    label = { Text("Personal Gift Message") },
                    placeholder = { Text("To Emma, with love from Mark...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Total & Confirm Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total with extras", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "$${String.format(java.util.Locale.US, "%.2f", calculatedUnit)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = AmberGoldPrimary
                    )
                }

                Button(
                    onClick = {
                        onConfirmAddToCart(quantity, addMatches, addTrimmer, addGiftWrap, giftNote)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                    modifier = Modifier.testTag("confirm_add_to_cart_button")
                ) {
                    Text("Confirm & View Cart", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
