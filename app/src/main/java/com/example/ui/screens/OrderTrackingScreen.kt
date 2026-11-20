package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CartItem
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.BotanicalSage
import com.example.ui.theme.FlameMid
import com.example.ui.theme.PlayfairFontFamily
import com.example.viewmodel.AppScreen
import com.example.viewmodel.CandleViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderTrackingScreen(
    viewModel: CandleViewModel,
    modifier: Modifier = Modifier
) {
    val order by viewModel.selectedOrder.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    BackHandler {
        viewModel.navigateBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("order_tracking_screen")
    ) {
        // Dedicated Tracking Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("tracking_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Orders"
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order?.let { "Order #${it.orderId}" } ?: "Order Tracking",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "CRAFTING TO SHIPPING TIMELINE",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = AmberGoldPrimary,
                        letterSpacing = 0.8.sp
                    )
                }

                order?.let { currentOrder ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (currentOrder.status == OrderStatus.DELIVERED) {
                            BotanicalSage.copy(alpha = 0.14f)
                        } else {
                            AmberGoldPrimary.copy(alpha = 0.14f)
                        },
                        border = BorderStroke(
                            1.dp,
                            if (currentOrder.status == OrderStatus.DELIVERED) {
                                BotanicalSage.copy(alpha = 0.45f)
                            } else {
                                AmberGoldPrimary.copy(alpha = 0.4f)
                            }
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = currentOrder.status.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (currentOrder.status == OrderStatus.DELIVERED) BotanicalSage else AmberGoldPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        val currentOrder = order
        if (currentOrder == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.widthIn(max = 400.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalShipping,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Order Not Selected",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Select a candle order from your order history to view live crafting and shipping progress.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.ORDERS) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .testTag("tracking_back_to_orders_button")
                    ) {
                        Text("View All Orders", fontWeight = FontWeight.Bold)
                    }
                }
            }
            return
        }

        val placedDateStr = remember(currentOrder.placedAt) {
            SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault())
                .format(Date(currentOrder.placedAt))
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("order_tracking_scroll"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Hero Progress Banner
            item {
                HeroOrderStatusCard(
                    order = currentOrder,
                    placedDateStr = placedDateStr,
                    onAdvanceStage = { viewModel.advanceOrderStage(currentOrder.orderId) },
                    onResetDemo = { viewModel.setOrderStage(currentOrder.orderId, OrderStatus.POURING) },
                    modifier = Modifier.widthIn(max = 600.dp)
                )
            }

            // 2. Vertical Crafting-to-Shipping Timeline
            item {
                CraftingToShippingTimelineCard(
                    order = currentOrder,
                    onSelectStage = { targetStage ->
                        viewModel.setOrderStage(currentOrder.orderId, targetStage)
                    },
                    modifier = Modifier.widthIn(max = 600.dp)
                )
            }

            // 3. Shipping & Delivery Details
            item {
                ShippingDetailsCard(
                    order = currentOrder,
                    onCopyTracking = {
                        clipboardManager.setText(AnnotatedString(currentOrder.trackingNumber))
                        viewModel.notifyCopiedTracking(currentOrder.trackingNumber)
                    },
                    modifier = Modifier.widthIn(max = 600.dp)
                )
            }

            // 4. Candles in This Batch + Reorder
            item {
                OrderBatchItemsCard(
                    order = currentOrder,
                    onReorderBatch = { viewModel.reorderOrderItems(currentOrder) },
                    onCustomizeFirst = {
                        currentOrder.items.firstOrNull()?.candle?.let { candle ->
                            viewModel.loadCandleIntoStudio(candle)
                        }
                    },
                    modifier = Modifier.widthIn(max = 600.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun HeroOrderStatusCard(
    order: Order,
    placedDateStr: String,
    onAdvanceStage: () -> Unit,
    onResetDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = order.status.progressFraction,
        animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing),
        label = "orderProgress"
    )
    val percentage = (order.status.progressFraction * 100).toInt()
    val nextStage = order.status.nextStage

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hero_order_status_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.28f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            AmberGoldPrimary.copy(alpha = 0.09f),
                            BotanicalSage.copy(alpha = 0.06f)
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (order.status.isCraftingStage) {
                            AmberGoldPrimary.copy(alpha = 0.14f)
                        } else {
                            BotanicalSage.copy(alpha = 0.16f)
                        }
                    ) {
                        Text(
                            text = order.status.stageCategoryLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.8.sp,
                            color = if (order.status.isCraftingStage) AmberGoldPrimary else BotanicalSage,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = order.status.displayName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = order.status.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            if (order.status == OrderStatus.DELIVERED) BotanicalSage else AmberGoldPrimary
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = statusIcon(order.status),
                        contentDescription = order.status.displayName,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar & Percentage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Step ${order.status.stepIndex} of ${OrderStatus.entries.size}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$percentage% Complete",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (order.status == OrderStatus.DELIVERED) BotanicalSage else AmberGoldPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .testTag("order_progress_bar"),
                color = if (order.status == OrderStatus.DELIVERED) BotanicalSage else AmberGoldPrimary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Placed $placedDateStr",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
                Text(
                    text = "Est. Arrival: ${order.estimatedArrival}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = BotanicalSage,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (nextStage != null) {
                Button(
                    onClick = onAdvanceStage,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .testTag("advance_order_stage_button")
                ) {
                    Text(
                        text = "Advance to: ${nextStage.displayName}",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = BotanicalSage,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Batch delivered & ready to glow!",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = BotanicalSage
                        )
                    }
                    TextButton(
                        onClick = onResetDemo,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("reset_order_stage_button")
                    ) {
                        Text("Replay Stages", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CraftingToShippingTimelineCard(
    order: Order,
    onSelectStage: (OrderStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val statuses = OrderStatus.entries

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("order_timeline_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Live Batch Journey",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tap any milestone to inspect or update progress",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
                Icon(
                    imageVector = Icons.Outlined.Spa,
                    contentDescription = null,
                    tint = AmberGoldPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            statuses.forEachIndexed { index, status ->
                val isCompleted = status.stepIndex < order.status.stepIndex ||
                    (status == OrderStatus.DELIVERED && order.status == OrderStatus.DELIVERED)
                val isCurrent = status == order.status
                val isUpcoming = status.stepIndex > order.status.stepIndex
                val isLast = index == statuses.lastIndex

                TimelineMilestoneRow(
                    status = status,
                    isCompleted = isCompleted,
                    isCurrent = isCurrent,
                    isUpcoming = isUpcoming,
                    isLast = isLast,
                    onClick = { onSelectStage(status) }
                )
            }
        }
    }
}

@Composable
private fun TimelineMilestoneRow(
    status: OrderStatus,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isUpcoming: Boolean,
    isLast: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .testTag("timeline_step_${status.name.lowercase()}"),
        verticalAlignment = Alignment.Top
    ) {
        // Left timeline rail + badge
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCurrent -> AmberGoldPrimary
                            isCompleted -> BotanicalSage
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                    .then(
                        if (isCurrent) {
                            Modifier.border(3.dp, AmberGoldPrimary.copy(alpha = 0.28f), CircleShape)
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCompleted && !isCurrent -> {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    isCurrent -> {
                        Icon(
                            imageVector = statusIcon(status),
                            contentDescription = "Current stage",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    else -> {
                        Text(
                            text = "${status.stepIndex}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(if (isCurrent) 76.dp else 36.dp)
                        .background(
                            if (isCompleted) BotanicalSage.copy(alpha = 0.7f)
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right milestone content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 4.dp else 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = status.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Medium,
                    color = if (isUpcoming) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when {
                        isCurrent -> AmberGoldPrimary.copy(alpha = 0.14f)
                        isCompleted -> BotanicalSage.copy(alpha = 0.12f)
                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    }
                ) {
                    Text(
                        text = when {
                            isCurrent && status == OrderStatus.DELIVERED -> "Arrived"
                            isCurrent -> "In Progress"
                            isCompleted -> "Done"
                            status.isCraftingStage -> "Crafting"
                            else -> "Shipping"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            isCurrent -> AmberGoldPrimary
                            isCompleted -> BotanicalSage
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = status.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )

            AnimatedVisibility(
                visible = isCurrent,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = AmberGoldPrimary.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AmberGoldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = status.studioDetails,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShippingDetailsCard(
    order: Order,
    onCopyTracking: () -> Unit,
    modifier: Modifier = Modifier
) {
    val carrierLabel = when (order.status) {
        OrderStatus.SHIPPED, OrderStatus.DELIVERED -> "Artisan Eco-Parcel Express"
        else -> "Assigned after hand-labeling"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("shipping_details_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Shipping & Carrier Details",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = PlayfairFontFamily,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tracking Number Banner with Copy
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TRACKING NUMBER",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 0.6.sp
                        )
                        Text(
                            text = order.trackingNumber,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AmberGoldPrimary
                        )
                    }

                    IconButton(
                        onClick = onCopyTracking,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("copy_tracking_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "Copy tracking number",
                            tint = AmberGoldPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            DetailKeyValueRow(label = "Carrier", value = carrierLabel)
            DetailKeyValueRow(label = "Recipient", value = order.customerName)
            DetailKeyValueRow(label = "Delivery Address", value = order.shippingAddress)
            if (order.customerEmail.isNotBlank()) {
                DetailKeyValueRow(label = "Confirmation Email", value = order.customerEmail)
            }
            if (order.giftNote.isNotBlank()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Text(
                    text = "Gift / Enclosure Note",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "“${order.giftNote}”",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = AmberGoldPrimary
                )
            }
        }
    }
}

@Composable
private fun OrderBatchItemsCard(
    order: Order,
    onReorderBatch: () -> Unit,
    onCustomizeFirst: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("order_batch_items_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Candles in This Batch (${order.items.sumOf { it.quantity }})",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = PlayfairFontFamily,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (order.items.isEmpty()) {
                Text(
                    text = "Custom artisan candle batch",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                order.items.forEachIndexed { index, item ->
                    OrderBatchItemRow(item = item)
                    if (index < order.items.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            DetailKeyValueRow(label = "Subtotal", value = order.formattedSubtotal)
            DetailKeyValueRow(label = "Shipping", value = order.formattedShipping)

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order Total",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order.formattedTotal,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AmberGoldPrimary
                )
            }

            if (order.items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onCustomizeFirst,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                            .testTag("customize_from_order_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open in Studio", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onReorderBatch,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                            .testTag("reorder_batch_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reorder", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderBatchItemRow(item: CartItem) {
    val candle = item.candle
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(candle.vessel.colorHex)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = FlameMid,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "${item.quantity}x ${candle.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.formattedItemTotal,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AmberGoldPrimary
                )
            }

            Text(
                text = "${candle.vessel.displayName} • ${candle.vessel.sizeLabel}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )

            Text(
                text = "${candle.wax.displayName} • ${candle.wick.displayName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )

            Text(
                text = "${candle.topNote.split(" & ").first()} / ${candle.heartNote.split(" & ").first()} / ${candle.baseNote.split(" & ").first()}",
                style = MaterialTheme.typography.labelSmall,
                color = AmberGoldPrimary,
                fontSize = 10.sp
            )

            if (candle.customInscription.isNotBlank()) {
                Text(
                    text = "Label: “${candle.customInscription}”",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DetailKeyValueRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.42f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.58f)
        )
    }
}

private fun statusIcon(status: OrderStatus): ImageVector {
    return when (status) {
        OrderStatus.POURING -> Icons.Default.WaterDrop
        OrderStatus.CURING -> Icons.Default.HourglassTop
        OrderStatus.LABELING -> Icons.Default.Verified
        OrderStatus.SHIPPED -> Icons.Default.LocalShipping
        OrderStatus.DELIVERED -> Icons.Default.Home
    }
}
