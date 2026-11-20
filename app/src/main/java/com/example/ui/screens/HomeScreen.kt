package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.CuratedCandle
import com.example.model.SampleData
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.BotanicalSage
import com.example.ui.theme.FlameMid
import com.example.ui.theme.PlayfairFontFamily
import com.example.viewmodel.AppScreen
import com.example.viewmodel.CandleViewModel

@Composable
fun HomeScreen(
    viewModel: CandleViewModel,
    modifier: Modifier = Modifier
) {
    val curatedFilter by viewModel.curatedFilter.collectAsState()

    val categories = listOf("All", "Woody & Warm", "Earthy & Fresh", "Floral & Calming", "Fruity & Exotic")

    val filteredCandles = SampleData.curatedCollection.filter {
        curatedFilter == "All" || it.category == curatedFilter
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_content"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Visual Banner
        item {
            HeroBanner(
                onStartCustomizing = { viewModel.navigateTo(AppScreen.STUDIO) },
                onStartQuiz = { viewModel.startQuiz() }
            )
        }

        // Trust Badges / Guarantees
        item {
            TrustBadgesRow()
        }

        // Scent Matchmaker Quiz Promo Card
        item {
            ScentQuizPromoCard(
                onTakeQuiz = { viewModel.startQuiz() }
            )
        }

        // Curated Collection Header & Filters
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Artisan Signature Blends",
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = PlayfairFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Hand-poured signature formulations ready to order or customize.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = cat == curatedFilter
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCuratedFilter(cat) },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmberGoldPrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.testTag("filter_chip_$cat")
                        )
                    }
                }
            }
        }

        // Curated Candles Grid / Cards
        items(filteredCandles) { candle ->
            CuratedCandleCard(
                candle = candle,
                onSelect = { viewModel.selectCuratedCandle(candle) },
                onCustomize = { viewModel.loadCandleIntoStudio(candle.defaultCandle) },
                onQuickAdd = { viewModel.addCuratedCandleToCart(candle) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // Custom Candle Studio Callout Card with Image
        item {
            CustomStudioCallout(
                onOpenStudio = { viewModel.navigateTo(AppScreen.STUDIO) }
            )
        }

        // Candle Care & Burning Rituals Guide
        item {
            CandleCareGuide()
        }
    }
}

@Composable
private fun HeroBanner(
    onStartCustomizing: () -> Unit,
    onStartQuiz: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        // Hero Image
        Image(
            painter = painterResource(id = R.drawable.img_candle_hero),
            contentDescription = "Handcrafted artisan candle studio",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient Vignette Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.88f)
                        )
                    )
                )
        )

        // Hero Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Surface(
                color = AmberGoldPrimary.copy(alpha = 0.9f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "CUSTOM CHANDLERY & ATELIER",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bespoke Scented Candles,\nCrafted For Your Soul",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = PlayfairFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Select your vessel, natural plant waxes, 3-tier scent pyramid, and custom printed label.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onStartCustomizing,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                    modifier = Modifier.testTag("hero_design_candle_button")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Design My Candle", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onStartQuiz,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
                    modifier = Modifier.testTag("hero_scent_quiz_button")
                ) {
                    Text("Scent Quiz")
                }
            }
        }
    }
}

@Composable
private fun TrustBadgesRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BadgeItem(
            icon = Icons.Outlined.Eco,
            title = "100% Pure Wax",
            subtitle = "Zero Paraffin"
        )
        BadgeItem(
            icon = Icons.Outlined.Spa,
            title = "Clean Scents",
            subtitle = "Phthalate-Free"
        )
        BadgeItem(
            icon = Icons.Outlined.CheckCircle,
            title = "Hand-Poured",
            subtitle = "Small Batches"
        )
    }
}

@Composable
private fun BadgeItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(BotanicalSage.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BotanicalSage,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun ScentQuizPromoCard(onTakeQuiz: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onTakeQuiz() }
            .testTag("home_scent_quiz_banner"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AmberGoldPrimary.copy(alpha = 0.12f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(AmberGoldPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Find Your Signature Scent Blend",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Take our 1-minute quiz to discover the custom olfactory profile tailored to your home & mood.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Take Quiz",
                tint = AmberGoldPrimary
            )
        }
    }
}

@Composable
private fun CuratedCandleCard(
    candle: CuratedCandle,
    onSelect: () -> Unit,
    onCustomize: () -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onSelect() }
            .testTag("curated_candle_${candle.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Vessel Color Circle Avatar
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(candle.defaultCandle.vessel.colorHex))
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = FlameMid,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = candle.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = PlayfairFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = candle.defaultCandle.formattedPrice,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AmberGoldPrimary
                        )
                    }

                    Text(
                        text = candle.tagLine,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFA000),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${candle.rating}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " (${candle.reviewCount})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                        Text(
                            text = " • ${candle.defaultCandle.vessel.sizeLabel}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fragrance Notes Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                NoteTag("Top: ${candle.defaultCandle.topNote.split(" & ").first()}")
                NoteTag("Heart: ${candle.defaultCandle.heartNote.split(" & ").first()}")
                NoteTag("Base: ${candle.defaultCandle.baseNote.split(" & ").first()}")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCustomize,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("customize_curated_${candle.id}"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Customize Blend", style = MaterialTheme.typography.labelSmall)
                }

                Button(
                    onClick = onQuickAdd,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_add_curated_${candle.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                ) {
                    Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add to Cart", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun NoteTag(text: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 9.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            maxLines = 1
        )
    }
}

@Composable
private fun CustomStudioCallout(onOpenStudio: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.img_custom_craft),
                contentDescription = "Custom Candle Studio Workshop",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "The Chandlery Studio Experience",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Every custom candle is hand-poured in our studio using small-batch melting pots. Design a one-of-a-kind scent memory with personalized foil or vintage labels.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onOpenStudio,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open_studio_callout_button")
                ) {
                    Text("Open Candle Studio Configurator", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CandleCareGuide() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Artisan Candle Burning Rituals",
                style = MaterialTheme.typography.titleSmall,
                fontFamily = PlayfairFontFamily,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            CareTip(
                number = "1",
                title = "The Memory First Burn",
                desc = "Burn for at least 2 full hours on first lighting until the wax melts evenly to all vessel edges."
            )
            CareTip(
                number = "2",
                title = "Trim Wicks to 1/4 Inch",
                desc = "Always pinch off charred wood wick edges or snip cotton wicks before lighting for a clean, soot-free flame."
            )
            CareTip(
                number = "3",
                title = "Forever Vessels",
                desc = "When 1/2\" wax remains, wash with warm soapy water to repurpose your luxury vessel as a planter or desk organizer."
            )
        }
    }
}

@Composable
private fun CareTip(number: String, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = AmberGoldPrimary.copy(alpha = 0.2f),
            modifier = Modifier.size(20.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = AmberGoldPrimary,
                    fontSize = 10.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}
