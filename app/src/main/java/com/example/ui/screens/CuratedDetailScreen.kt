package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CandlePreviewCard
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.BotanicalSage
import com.example.ui.theme.PlayfairFontFamily
import com.example.viewmodel.CandleViewModel

@Composable
fun CuratedDetailScreen(
    viewModel: CandleViewModel,
    modifier: Modifier = Modifier
) {
    val candle = viewModel.selectedCuratedCandle.collectAsState().value

    BackHandler {
        viewModel.navigateBack()
    }

    if (candle == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No candle selected")
        }
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
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
                            text = "PRICE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = candle.defaultCandle.formattedPrice,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AmberGoldPrimary
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.loadCandleIntoStudio(candle.defaultCandle) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("curated_customize_button")
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Customize")
                        }

                        Button(
                            onClick = { viewModel.addCuratedCandleToCart(candle) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                            modifier = Modifier.testTag("curated_add_cart_button")
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add to Cart", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("curated_detail_content"),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("curated_back_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = candle.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Visualizer Preview
            item {
                CandlePreviewCard(
                    candle = candle.defaultCandle,
                    isLit = true,
                    showControls = false
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Title and Description
            item {
                Text(
                    text = candle.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = candle.tagLine,
                    style = MaterialTheme.typography.titleSmall,
                    color = AmberGoldPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFA000),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${candle.rating}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = " (${candle.reviewCount} customer reviews) • Handcrafted in USA",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = candle.description,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Olfactory Architecture Pyramid
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Olfactory Scent Pyramid",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        PyramidRow("TOP NOTE", candle.defaultCandle.topNote, "Sparkling opening chord")
                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        PyramidRow("HEART NOTE", candle.defaultCandle.heartNote, "Long-lasting character & warm body")
                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        PyramidRow("BASE NOTE", candle.defaultCandle.baseNote, "Resinous deep anchor that lingers")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Technical Specifications
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Candle Specifications",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        SpecItem("Vessel", candle.defaultCandle.vessel.displayName)
                        SpecItem("Wax Formulation", candle.defaultCandle.wax.displayName)
                        SpecItem("Wick Type", candle.defaultCandle.wick.displayName)
                        SpecItem("Approximate Burn", candle.defaultCandle.vessel.burnTime)
                        SpecItem("Lid Included", candle.defaultCandle.lid.displayName)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun PyramidRow(tier: String, note: String, desc: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = AmberGoldPrimary.copy(alpha = 0.12f)
        ) {
            Text(
                text = tier,
                style = MaterialTheme.typography.labelSmall,
                color = AmberGoldPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = note, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }
    }
}

@Composable
private fun SpecItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}
