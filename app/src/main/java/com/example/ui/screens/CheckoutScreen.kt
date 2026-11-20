package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.CartItem
import com.example.model.ShippingAddressDraft
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.BotanicalSage
import com.example.ui.theme.FlameMid
import com.example.ui.theme.PlayfairFontFamily
import com.example.viewmodel.AppScreen
import com.example.viewmodel.CandleViewModel
import java.util.Locale

@Composable
fun CheckoutScreen(
    viewModel: CandleViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val checkoutStep by viewModel.checkoutStep.collectAsStateWithLifecycle()
    val shippingDraft by viewModel.shippingDraft.collectAsStateWithLifecycle()

    var showValidationErrors by remember { mutableStateOf(false) }
    var specsConfirmed by remember { mutableStateOf(true) }

    val subtotal = cartItems.sumOf { it.itemTotal }
    val freeShippingGoal = 50.0
    val shipping = if (subtotal >= freeShippingGoal || subtotal == 0.0) 0.0 else 5.0
    val total = subtotal + shipping

    val validationErrors = remember(shippingDraft) { shippingDraft.validate() }

    BackHandler {
        if (checkoutStep == 1) {
            viewModel.setCheckoutStep(0)
        } else {
            viewModel.navigateBack()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("checkout_screen"),
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (checkoutStep == 1) {
                                    viewModel.setCheckoutStep(0)
                                } else {
                                    viewModel.navigateBack()
                                }
                            },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("checkout_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = if (checkoutStep == 1) "Back to Shipping Address" else "Back to Cart"
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (checkoutStep == 0) "Shipping Address" else "Confirm Custom Candle",
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = PlayfairFontFamily,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "MADE WITH LOVE CANDLES • STEP ${checkoutStep + 1} OF 2",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                color = AmberGoldPrimary,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }

                    CheckoutStepIndicator(
                        currentStep = checkoutStep,
                        onSelectStep = { target ->
                            if (target == 0) {
                                viewModel.setCheckoutStep(0)
                            } else if (shippingDraft.isValid()) {
                                viewModel.setCheckoutStep(1)
                            } else {
                                showValidationErrors = true
                            }
                        }
                    )
                }
            }
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 10.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (checkoutStep == 0) {
                            Column {
                                Text(
                                    text = "ESTIMATED TOTAL",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$${String.format(Locale.US, "%.2f", total)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGoldPrimary
                                )
                            }

                            Button(
                                onClick = {
                                    if (shippingDraft.isValid()) {
                                        showValidationErrors = false
                                        viewModel.setCheckoutStep(1)
                                    } else {
                                        showValidationErrors = true
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                                modifier = Modifier
                                    .heightIn(min = 48.dp)
                                    .testTag("checkout_continue_to_confirm_button")
                            ) {
                                Text("Review Candle", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else {
                            OutlinedButton(
                                onClick = { viewModel.setCheckoutStep(0) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .heightIn(min = 48.dp)
                                    .testTag("checkout_back_to_shipping_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Shipping")
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Button(
                                onClick = {
                                    if (!shippingDraft.isValid()) {
                                        showValidationErrors = true
                                        viewModel.setCheckoutStep(0)
                                    } else if (specsConfirmed) {
                                        viewModel.checkoutFromDraft()
                                    }
                                },
                                enabled = specsConfirmed,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("place_order_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Confirm & Place Order",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.widthIn(max = 400.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingBag,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Your basket is empty",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Add a custom candle to your basket before checking out.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.STUDIO) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                        modifier = Modifier.heightIn(min = 48.dp)
                    ) {
                        Text("Open Candle Studio", fontWeight = FontWeight.Bold)
                    }
                }
            }
            return@Scaffold
        }

        AnimatedContent(
            targetState = checkoutStep,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "checkoutStepTransition"
        ) { step ->
            if (step == 0) {
                ShippingAddressStep(
                    draft = shippingDraft,
                    errors = if (showValidationErrors) validationErrors else emptyMap(),
                    onUpdateDraft = { updater -> viewModel.updateShippingDraft(updater) },
                    itemCount = cartItems.sumOf { it.quantity },
                    total = total,
                    shipping = shipping
                )
            } else {
                CandleConfigurationConfirmStep(
                    draft = shippingDraft,
                    cartItems = cartItems,
                    subtotal = subtotal,
                    shipping = shipping,
                    total = total,
                    specsConfirmed = specsConfirmed,
                    onToggleSpecsConfirmed = { specsConfirmed = it },
                    onEditShipping = { viewModel.setCheckoutStep(0) },
                    onEditCandleInStudio = { item -> viewModel.loadCandleIntoStudio(item.candle) }
                )
            }
        }
    }
}

@Composable
private fun CheckoutStepIndicator(
    currentStep: Int,
    onSelectStep: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StepChip(
            stepNumber = 1,
            label = "Shipping Address",
            isActive = currentStep == 0,
            isCompleted = currentStep > 0,
            onClick = { onSelectStep(0) },
            modifier = Modifier
                .weight(1f)
                .testTag("checkout_step_shipping")
        )

        Box(
            modifier = Modifier
                .width(24.dp)
                .height(2.dp)
                .background(
                    if (currentStep > 0) BotanicalSage else MaterialTheme.colorScheme.outlineVariant
                )
        )

        StepChip(
            stepNumber = 2,
            label = "Confirm Candle",
            isActive = currentStep == 1,
            isCompleted = false,
            onClick = { onSelectStep(1) },
            modifier = Modifier
                .weight(1f)
                .testTag("checkout_step_confirm")
        )
    }
}

@Composable
private fun StepChip(
    stepNumber: Int,
    label: String,
    isActive: Boolean,
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = when {
        isCompleted -> BotanicalSage
        isActive -> AmberGoldPrimary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = when {
            isCompleted -> BotanicalSage.copy(alpha = 0.12f)
            isActive -> AmberGoldPrimary.copy(alpha = 0.12f)
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        border = BorderStroke(
            1.dp,
            if (isActive || isCompleted) accent.copy(alpha = 0.45f) else Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(accent),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                } else {
                    Text(
                        text = "$stepNumber",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isActive || isCompleted) FontWeight.Bold else FontWeight.Medium,
                color = accent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ShippingAddressStep(
    draft: ShippingAddressDraft,
    errors: Map<String, String>,
    onUpdateDraft: ((ShippingAddressDraft) -> ShippingAddressDraft) -> Unit,
    itemCount: Int,
    total: Double,
    shipping: Double
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("checkout_shipping_step_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    AmberGoldPrimary.copy(alpha = 0.08f),
                                    BotanicalSage.copy(alpha = 0.06f)
                                )
                            )
                        )
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AmberGoldPrimary.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalShipping,
                            contentDescription = null,
                            tint = AmberGoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Where should we send your batch?",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$itemCount custom candle(s) • ${if (shipping == 0.0) "Free Express Shipping" else "Standard Shipping ($5.00)"} • Total $${String.format(Locale.US, "%.2f", total)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Recipient & Delivery Address",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = draft.fullName,
                        onValueChange = { v -> onUpdateDraft { it.copy(fullName = v) } },
                        label = { Text("Recipient Full Name *") },
                        placeholder = { Text("Clara Bennett") },
                        isError = errors.containsKey("fullName"),
                        supportingText = errors["fullName"]?.let { msg -> { Text(msg) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_name_input")
                    )

                    OutlinedTextField(
                        value = draft.email,
                        onValueChange = { v -> onUpdateDraft { it.copy(email = v) } },
                        label = { Text("Email for Pour & Tracking Updates *") },
                        placeholder = { Text("clara@example.com") },
                        isError = errors.containsKey("email"),
                        supportingText = errors["email"]?.let { msg -> { Text(msg) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_email_input")
                    )

                    OutlinedTextField(
                        value = draft.streetAddress,
                        onValueChange = { v -> onUpdateDraft { it.copy(streetAddress = v) } },
                        label = { Text("Street Address *") },
                        placeholder = { Text("245 Rosewater Lane") },
                        isError = errors.containsKey("streetAddress"),
                        supportingText = errors["streetAddress"]?.let { msg -> { Text(msg) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_street_input")
                    )

                    OutlinedTextField(
                        value = draft.apartmentOrSuite,
                        onValueChange = { v -> onUpdateDraft { it.copy(apartmentOrSuite = v) } },
                        label = { Text("Apt, Suite, Unit (Optional)") },
                        placeholder = { Text("Apt 4B") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_apt_input")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = draft.city,
                            onValueChange = { v -> onUpdateDraft { it.copy(city = v) } },
                            label = { Text("City *") },
                            placeholder = { Text("Portland") },
                            isError = errors.containsKey("city"),
                            supportingText = errors["city"]?.let { msg -> { Text(msg) } },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("checkout_city_input")
                        )

                        OutlinedTextField(
                            value = draft.state,
                            onValueChange = { v -> onUpdateDraft { it.copy(state = v) } },
                            label = { Text("State *") },
                            placeholder = { Text("OR") },
                            isError = errors.containsKey("state"),
                            supportingText = errors["state"]?.let { msg -> { Text(msg) } },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Characters,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(0.9f)
                                .testTag("checkout_state_input")
                        )
                    }

                    OutlinedTextField(
                        value = draft.postalCode,
                        onValueChange = { v -> onUpdateDraft { it.copy(postalCode = v) } },
                        label = { Text("ZIP / Postal Code *") },
                        placeholder = { Text("97201") },
                        isError = errors.containsKey("postalCode"),
                        supportingText = errors["postalCode"]?.let { msg -> { Text(msg) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_zip_input")
                    )

                    OutlinedTextField(
                        value = draft.deliveryNote,
                        onValueChange = { v -> onUpdateDraft { it.copy(deliveryNote = v) } },
                        label = { Text("Delivery or Gift Note (Optional)") },
                        placeholder = { Text("Leave in shaded porch / Happy Birthday!") },
                        minLines = 2,
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_note_input")
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CandleConfigurationConfirmStep(
    draft: ShippingAddressDraft,
    cartItems: List<CartItem>,
    subtotal: Double,
    shipping: Double,
    total: Double,
    specsConfirmed: Boolean,
    onToggleSpecsConfirmed: (Boolean) -> Unit,
    onEditShipping: () -> Unit,
    onEditCandleInStudio: (CartItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("checkout_confirm_step_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Shipping Destination Confirmation
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .testTag("checkout_shipping_summary_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.LocalShipping,
                                contentDescription = null,
                                tint = BotanicalSage,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Shipping To",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        TextButton(
                            onClick = onEditShipping,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("checkout_edit_shipping_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontSize = 12.sp)
                        }
                    }

                    Text(
                        text = draft.fullName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = draft.formattedSingleLine(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = draft.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                    if (draft.deliveryNote.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AmberGoldPrimary.copy(alpha = 0.09f)
                        ) {
                            Text(
                                text = "Note: “${draft.deliveryNote}”",
                                style = MaterialTheme.typography.bodySmall,
                                color = AmberGoldPrimary,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2. Final Custom Candle Configuration Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Final Custom Candle Configuration",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AmberGoldPrimary.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = "${cartItems.sumOf { it.quantity }} Candle(s)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AmberGoldPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // 3. Detailed Configuration Card for Each Cart Item
        items(cartItems, key = { it.id }) { item ->
            CustomCandleConfigConfirmationCard(
                item = item,
                onEditInStudio = { onEditCandleInStudio(item) },
                modifier = Modifier.widthIn(max = 600.dp)
            )
        }

        // 4. Specification Verification Toggle
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onToggleSpecsConfirmed(!specsConfirmed) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (specsConfirmed) {
                        BotanicalSage.copy(alpha = 0.10f)
                    } else {
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
                    }
                ),
                border = BorderStroke(
                    1.dp,
                    if (specsConfirmed) BotanicalSage.copy(alpha = 0.45f) else MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = specsConfirmed,
                        onCheckedChange = onToggleSpecsConfirmed,
                        colors = CheckboxDefaults.colors(checkedColor = BotanicalSage),
                        modifier = Modifier.testTag("checkout_confirm_specs_checkbox")
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Confirm Custom Candle Specifications",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "I verified the vessel, scent blend, botanicals, and custom label inscription before small-batch pouring begins.",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 5. Final Order Totals Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Summary",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CheckoutSummaryLine("Candles & Extras Subtotal", "$${String.format(Locale.US, "%.2f", subtotal)}")
                    CheckoutSummaryLine(
                        "Handcrafted Shipping",
                        if (shipping == 0.0) "FREE" else "$${String.format(Locale.US, "%.2f", shipping)}"
                    )
                    CheckoutSummaryLine("Crafting & Delivery", "3-4 business days")
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Due",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${String.format(Locale.US, "%.2f", total)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AmberGoldPrimary
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CustomCandleConfigConfirmationCard(
    item: CartItem,
    onEditInStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val candle = item.candle
    val throwLabel = when {
        candle.intensity >= 0.85f -> "Intense (${(candle.intensity * 100).toInt()}%)"
        candle.intensity >= 0.55f -> "Balanced (${(candle.intensity * 100).toInt()}%)"
        else -> "Subtle (${(candle.intensity * 100).toInt()}%)"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("checkout_confirm_item_${item.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
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
                    Text(
                        text = candle.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Qty: ${item.quantity} • ${item.formattedItemTotal}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = AmberGoldPrimary
                    )
                }

                TextButton(
                    onClick = onEditInStudio,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("checkout_edit_candle_${item.id}")
                ) {
                    Text("Adjust", fontSize = 12.sp)
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            ConfigSpecRow("Vessel", "${candle.vessel.displayName} (${candle.vessel.sizeLabel})")
            ConfigSpecRow("Wax Formula", candle.wax.displayName)
            ConfigSpecRow("Wick", candle.wick.displayName)
            ConfigSpecRow("Top Note", candle.topNote)
            ConfigSpecRow("Heart Note", candle.heartNote)
            ConfigSpecRow("Base Note", candle.baseNote)
            ConfigSpecRow("Scent Throw", throwLabel)
            ConfigSpecRow("Label Style", candle.labelStyle.displayName)
            ConfigSpecRow("Lid Finish", candle.lid.displayName)
            ConfigSpecRow("Botanicals", candle.botanicalTopping.displayName)
            if (candle.customInscription.isNotBlank()) {
                ConfigSpecRow("Inscription", "“${candle.customInscription}”")
            }

            val extras = buildList {
                if (item.addMatches) add("Apothecary Match Jar (+$4)")
                if (item.addWickTrimmer) add("Brass Wick Trimmer (+$9)")
                if (item.addGiftWrap) add("Luxury Gift Wrap (+$5)")
            }
            if (extras.isNotEmpty()) {
                ConfigSpecRow("Extras", extras.joinToString(", "))
            }
            if (item.giftMessage.isNotBlank()) {
                ConfigSpecRow("Gift Card", "“${item.giftMessage}”")
            }
        }
    }
}

@Composable
private fun ConfigSpecRow(label: String, value: String) {
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
            modifier = Modifier.weight(0.36f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.64f)
        )
    }
}

@Composable
private fun CheckoutSummaryLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}
