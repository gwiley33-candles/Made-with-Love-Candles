package com.example.ui.components

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ai.LabelArtworkTheme
import com.example.ai.MockupScene
import com.example.model.CustomCandle
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.BotanicalSage
import com.example.ui.theme.PlayfairFontFamily
import com.example.viewmodel.MockupUiState

@Composable
fun CandleMockupStudioCard(
    candle: CustomCandle,
    mockupUiState: MockupUiState,
    artworkTheme: LabelArtworkTheme,
    mockupScene: MockupScene,
    customLabelNotes: String,
    uploadedLabelBitmap: Bitmap?,
    onSelectArtworkTheme: (LabelArtworkTheme) -> Unit,
    onSelectMockupScene: (MockupScene) -> Unit,
    onCustomLabelNotesChange: (String) -> Unit,
    onPickLabelImage: (android.net.Uri) -> Unit,
    onClearUploadedLabel: () -> Unit,
    onGenerateMockup: () -> Unit,
    modifier: Modifier = Modifier
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onPickLabelImage(uri)
        }
    }

    var showFullscreenMockup by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ai_mockup_studio_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.28f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(AmberGoldPrimary.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = AmberGoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "AI Candle Mockup Visualizer",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Preview '${candle.name}' on a photorealistic ${candle.vessel.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Upload Custom Label Art Option (Zero-permission Photo Picker)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uploadedLabelBitmap != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            bitmap = uploadedLabelBitmap.asImageBitmap(),
                            contentDescription = "Uploaded custom label artwork",
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, AmberGoldPrimary, RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Custom Label Artwork Attached",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BotanicalSage
                            )
                            Text(
                                text = "AI will wrap your uploaded artwork onto the candle jar.",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = onClearUploadedLabel,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("clear_uploaded_label_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove uploaded label",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .padding(12.dp)
                            .testTag("upload_label_art_button"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = AmberGoldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Upload Label Artwork (Optional)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Pick a logo or custom label graphic from your photos, or let AI design it below.",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "Browse",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AmberGoldPrimary
                        )
                    }
                }
            }

            // Label Artwork Motif (when generating text-to-mockup without uploaded art)
            if (uploadedLabelBitmap == null) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Label Artwork Motif",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LabelArtworkTheme.entries.forEach { theme ->
                        val selected = theme == artworkTheme
                        FilterChip(
                            selected = selected,
                            onClick = { onSelectArtworkTheme(theme) },
                            label = {
                                Text(
                                    text = theme.displayName,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmberGoldPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("artwork_theme_${theme.name}")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Studio Photography Scene
            Text(
                text = "Mockup Photography Backdrop",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MockupScene.entries.forEach { scene ->
                    val selected = scene == mockupScene
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectMockupScene(scene) },
                        label = {
                            Text(
                                text = scene.displayName,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BotanicalSage,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("mockup_scene_${scene.name}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Custom Design Notes
            OutlinedTextField(
                value = customLabelNotes,
                onValueChange = onCustomLabelNotesChange,
                label = { Text("Custom Label Art Details (Optional)") },
                placeholder = { Text("e.g. Soft watercolor roses, gold foil arch border...") },
                maxLines = 2,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mockup_custom_notes_input")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Generate CTA Button
            Button(
                onClick = onGenerateMockup,
                enabled = mockupUiState !is MockupUiState.Loading,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .testTag("generate_mockup_button")
            ) {
                if (mockupUiState is MockupUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generating Mockup...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(
                        imageVector = if (mockupUiState is MockupUiState.Success) {
                            Icons.Default.Refresh
                        } else {
                            Icons.Default.AutoAwesome
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (mockupUiState is MockupUiState.Success) {
                            "Regenerate Candle Mockup"
                        } else {
                            "Visualize Label on Candle Mockup"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Output Result / Loading / Error Area
            when (mockupUiState) {
                is MockupUiState.Idle -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(20.dp)
                            .testTag("mockup_idle_box"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = AmberGoldPrimary.copy(alpha = 0.7f),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Ready to Render Studio Mockup",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap above to see \"${candle.name}\" styled on your ${candle.vessel.displayName} with your chosen label style and backdrop.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                is MockupUiState.Loading -> {
                    ShimmerLoadingMockupBox(
                        candleName = candle.name,
                        vesselName = candle.vessel.displayName
                    )
                }

                is MockupUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mockup_success_container")
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(18.dp))
                                .border(
                                    1.dp,
                                    AmberGoldPrimary.copy(alpha = 0.35f),
                                    RoundedCornerShape(18.dp)
                                )
                                .clickable { showFullscreenMockup = true }
                        ) {
                            Image(
                                bitmap = mockupUiState.bitmap.asImageBitmap(),
                                contentDescription = "AI generated candle mockup for ${candle.name}",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("generated_mockup_image"),
                                contentScale = ContentScale.Crop
                            )

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.Black.copy(alpha = 0.58f),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ZoomIn,
                                        contentDescription = "Zoom mockup",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Tap to Expand",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = mockupUiState.labelSummary,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is MockupUiState.Error -> {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mockup_error_box")
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Could Not Generate Mockup",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = mockupUiState.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = onGenerateMockup,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .heightIn(min = 48.dp)
                                    .testTag("mockup_retry_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Try Again")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showFullscreenMockup && mockupUiState is MockupUiState.Success) {
        Dialog(
            onDismissRequest = { showFullscreenMockup = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.92f))
                    .clickable { showFullscreenMockup = false }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        bitmap = mockupUiState.bitmap.asImageBitmap(),
                        contentDescription = "Fullscreen candle mockup",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = mockupUiState.labelSummary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    FilledTonalButton(
                        onClick = { showFullscreenMockup = false },
                        modifier = Modifier.heightIn(min = 48.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Close Preview")
                    }
                }
            }
        }
    }
}

@Composable
private fun ShimmerLoadingMockupBox(
    candleName: String,
    vesselName: String
) {
    val transition = rememberInfiniteTransition(label = "mockup_shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
            AmberGoldPrimary.copy(alpha = 0.22f),
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        ),
        start = Offset(translateAnim - 300f, translateAnim - 300f),
        end = Offset(translateAnim, translateAnim)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(18.dp))
            .background(shimmerBrush)
            .border(1.dp, AmberGoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
            .padding(24.dp)
            .testTag("mockup_loading_shimmer"),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = AmberGoldPrimary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Crafting Your AI Candle Mockup",
                style = MaterialTheme.typography.titleSmall,
                fontFamily = PlayfairFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Rendering \"$candleName\" on $vesselName.\nStudio synthesis typically takes 30–60 seconds...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
