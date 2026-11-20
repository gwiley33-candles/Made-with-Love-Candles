package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.configureAppCheck
import com.example.ui.screens.*
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PlayfairFontFamily
import com.example.viewmodel.AppScreen
import com.example.viewmodel.CandleViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: CandleViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureAppCheck(this, intent)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val cartItems by viewModel.cartItems.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                // Collect toast messages
                LaunchedEffect(Unit) {
                    viewModel.toastMessage.collectLatest { msg ->
                        snackbarHostState.showSnackbar(
                            message = msg,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                // Global BackHandler for secondary screens
                BackHandler(enabled = currentScreen != AppScreen.HOME) {
                    viewModel.navigateBack()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        if (currentScreen != AppScreen.QUIZ &&
                            currentScreen != AppScreen.ORDER_SUCCESS &&
                            currentScreen != AppScreen.ORDER_TRACKING &&
                            currentScreen != AppScreen.CHECKOUT
                        ) {
                            MadeWithLoveTopBar(
                                currentScreen = currentScreen,
                                cartItemCount = cartItems.sumOf { it.quantity },
                                onOpenCart = { viewModel.navigateTo(AppScreen.CART) },
                                onOpenSaved = { viewModel.navigateTo(AppScreen.ORDERS) }
                            )
                        }
                    },
                    bottomBar = {
                        if (currentScreen != AppScreen.QUIZ &&
                            currentScreen != AppScreen.ORDER_SUCCESS &&
                            currentScreen != AppScreen.CHECKOUT
                        ) {
                            MadeWithLoveBottomNav(
                                currentScreen = currentScreen,
                                cartItemCount = cartItems.sumOf { it.quantity },
                                onNavigate = { screen -> viewModel.navigateTo(screen) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                            AppScreen.STUDIO -> StudioScreen(viewModel = viewModel)
                            AppScreen.CURATED_DETAIL -> CuratedDetailScreen(viewModel = viewModel)
                            AppScreen.QUIZ -> QuizScreen(viewModel = viewModel)
                            AppScreen.CART -> CartScreen(viewModel = viewModel)
                            AppScreen.CHECKOUT -> CheckoutScreen(viewModel = viewModel)
                            AppScreen.ORDERS -> OrdersAndSavedScreen(viewModel = viewModel)
                            AppScreen.ORDER_TRACKING -> OrderTrackingScreen(viewModel = viewModel)
                            AppScreen.ORDER_SUCCESS -> OrderSuccessScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MadeWithLoveTopBar(
    currentScreen: AppScreen,
    cartItemCount: Int,
    onOpenCart: () -> Unit,
    onOpenSaved: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = AmberGoldPrimary.copy(alpha = 0.15f),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = AmberGoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Made with Love Candles",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "CUSTOM CANDLE ATELIER",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.5.sp,
                        color = AmberGoldPrimary,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = onOpenSaved,
                modifier = Modifier.testTag("top_bar_saved_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = "Saved Recipes & Orders"
                )
            }

            IconButton(
                onClick = onOpenCart,
                modifier = Modifier.testTag("top_bar_cart_button")
            ) {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge(
                                containerColor = AmberGoldPrimary,
                                contentColor = Color.White
                            ) {
                                Text("$cartItemCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingBag,
                        contentDescription = "Shopping Cart"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun MadeWithLoveBottomNav(
    currentScreen: AppScreen,
    cartItemCount: Int,
    onNavigate: (AppScreen) -> Unit
) {
    NavigationBar(
        modifier = Modifier.testTag("bottom_navigation_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == AppScreen.HOME,
            onClick = { onNavigate(AppScreen.HOME) },
            icon = {
                Icon(
                    if (currentScreen == AppScreen.HOME) Icons.Filled.Storefront else Icons.Outlined.Storefront,
                    contentDescription = "Shop"
                )
            },
            label = { Text("Shop") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = AmberGoldPrimary.copy(alpha = 0.2f)),
            modifier = Modifier.testTag("nav_item_shop")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.STUDIO,
            onClick = { onNavigate(AppScreen.STUDIO) },
            icon = {
                Icon(
                    if (currentScreen == AppScreen.STUDIO) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                    contentDescription = "Studio"
                )
            },
            label = { Text("Studio") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = AmberGoldPrimary.copy(alpha = 0.2f)),
            modifier = Modifier.testTag("nav_item_studio")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.CART,
            onClick = { onNavigate(AppScreen.CART) },
            icon = {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge(
                                containerColor = AmberGoldPrimary,
                                contentColor = Color.White
                            ) {
                                Text("$cartItemCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        if (currentScreen == AppScreen.CART) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                        contentDescription = "Cart"
                    )
                }
            },
            label = { Text("Cart") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = AmberGoldPrimary.copy(alpha = 0.2f)),
            modifier = Modifier.testTag("nav_item_cart")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.ORDERS || currentScreen == AppScreen.ORDER_TRACKING,
            onClick = { onNavigate(AppScreen.ORDERS) },
            icon = {
                Icon(
                    if (currentScreen == AppScreen.ORDERS || currentScreen == AppScreen.ORDER_TRACKING) {
                        Icons.Filled.Inventory2
                    } else {
                        Icons.Outlined.Inventory2
                    },
                    contentDescription = "Orders"
                )
            },
            label = { Text("Orders") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = AmberGoldPrimary.copy(alpha = 0.2f)),
            modifier = Modifier.testTag("nav_item_orders")
        )
    }
}
