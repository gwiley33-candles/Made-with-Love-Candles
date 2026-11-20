package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.SampleData
import com.example.ui.components.CandlePreviewCard
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.BotanicalSage
import com.example.ui.theme.PlayfairFontFamily
import com.example.viewmodel.AppScreen
import com.example.viewmodel.CandleViewModel

@Composable
fun QuizScreen(
    viewModel: CandleViewModel,
    modifier: Modifier = Modifier
) {
    val questionIndex by viewModel.quizQuestionIndex.collectAsState()
    val resultCandle by viewModel.quizResultCandle.collectAsState()

    BackHandler {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("quiz_back_button")
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = if (resultCandle == null) "Fragrance Matchmaker Quiz" else "Your Bespoke Scent Profile",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        if (resultCandle == null) {
            // Active Question View
            val currentQ = SampleData.quizQuestions.getOrNull(questionIndex)
            if (currentQ != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .testTag("quiz_questions_view")
                ) {
                    // Progress Indicator
                    val progress = (questionIndex + 1).toFloat() / SampleData.quizQuestions.size
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AmberGoldPrimary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "QUESTION ${questionIndex + 1} OF ${SampleData.quizQuestions.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberGoldPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = currentQ.question,
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentQ.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(currentQ.options) { option ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { viewModel.answerQuizQuestion(option) }
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                                    .testTag("quiz_option_${option.title.replace(" ", "_")}"),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(AmberGoldPrimary.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = AmberGoldPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = option.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = option.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Result View
            val candle = resultCandle!!
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("quiz_result_view"),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Surface(
                        color = BotanicalSage.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = BotanicalSage,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Formula matched to your sanctuary and mood preferences!",
                                style = MaterialTheme.typography.labelMedium,
                                color = BotanicalSage,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                item {
                    CandlePreviewCard(
                        candle = candle,
                        isLit = true,
                        showControls = false
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = candle.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Suggested formulation: ${candle.vessel.displayName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AmberGoldPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Crafted with ${candle.wax.displayName} and ${candle.wick.displayName} to create an optimal ambient experience.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Formulated Scent Notes",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("• Top: ${candle.topNote}", style = MaterialTheme.typography.bodyMedium)
                            Text("• Heart: ${candle.heartNote}", style = MaterialTheme.typography.bodyMedium)
                            Text("• Base: ${candle.baseNote}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.customizeQuizResult() },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quiz_open_studio_button")
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Tweak in Studio")
                        }

                        Button(
                            onClick = {
                                viewModel.addActiveCandleToCart()
                                viewModel.navigateTo(AppScreen.CART)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quiz_add_cart_button")
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Order This ($${String.format(java.util.Locale.US, "%.0f", candle.totalPrice)})", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
