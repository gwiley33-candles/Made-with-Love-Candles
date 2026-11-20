package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.BotanicalSage
import com.example.ui.theme.PlayfairFontFamily
import com.example.viewmodel.AppScreen
import com.example.viewmodel.CandleViewModel

@Composable
fun OrderSuccessScreen(
    viewModel: CandleViewModel,
    modifier: Modifier = Modifier
) {
    val order = viewModel.latestOrder.collectAsState().value

    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .testTag("order_success_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(BotanicalSage),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Order Confirmed!",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = PlayfairFontFamily,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Your custom batch has been scheduled for hand-pouring.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Order Number", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(order?.orderId ?: "MLC-89412", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = AmberGoldPrimary)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Amount Paid", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(order?.formattedTotal ?: "$34.00", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tracking Number", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(order?.trackingNumber ?: "MLC-TRACK-59281", style = MaterialTheme.typography.labelSmall)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    Text(
                        text = "Crafting Timeline:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("1. Wax formulation heated & botanicals weighed", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
                    Text("2. Poured into selected vessel with custom wick", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
                    Text("3. 48-Hour slow room temperature fragrance curing", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
                    Text("4. Hand-labeled & packed with care", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val placedOrder = order
                    if (placedOrder != null) {
                        viewModel.openOrderTracking(placedOrder.orderId)
                    } else {
                        viewModel.navigateTo(AppScreen.ORDERS)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .testTag("view_orders_button")
            ) {
                Text("Track Live Order Progress", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { viewModel.navigateTo(AppScreen.HOME) },
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("back_to_shop_button")
            ) {
                Text("Back to Atelier Shop")
            }
        }
    }
}
