package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.CandleMockupGenerator
import com.example.ai.LabelArtworkTheme
import com.example.ai.MockupScene
import com.example.data.CandleDatabase
import com.example.data.CandleRepository
import com.example.model.*
import com.google.firebase.ai.type.QuotaExceededException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

private const val TAG = "CandleViewModel"

sealed interface MockupUiState {
    data object Idle : MockupUiState
    data object Loading : MockupUiState
    data class Success(val bitmap: Bitmap, val labelSummary: String) : MockupUiState
    data class Error(val message: String) : MockupUiState
}

enum class AppScreen {
    HOME,
    STUDIO,
    CURATED_DETAIL,
    QUIZ,
    CART,
    CHECKOUT,
    ORDERS,
    ORDER_TRACKING,
    ORDER_SUCCESS
}

class CandleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CandleRepository
    private val mockupGenerator by lazy { CandleMockupGenerator() }

    init {
        val db = CandleDatabase.getDatabase(application)
        repository = CandleRepository(db.candleDao())
    }

    // Navigation State
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf<AppScreen>()

    // Selected Curated Candle for details
    private val _selectedCuratedCandle = MutableStateFlow<CuratedCandle?>(null)
    val selectedCuratedCandle: StateFlow<CuratedCandle?> = _selectedCuratedCandle.asStateFlow()

    // Active Custom Candle in Studio
    private val _activeCandle = MutableStateFlow(CustomCandle())
    val activeCandle: StateFlow<CustomCandle> = _activeCandle.asStateFlow()

    // Studio Step (0: Vessel, 1: Wax & Wick, 2: Scent Notes, 3: Label & Lid)
    private val _studioStep = MutableStateFlow(0)
    val studioStep: StateFlow<Int> = _studioStep.asStateFlow()

    // Candle preview lit flame state
    private val _isCandleLit = MutableStateFlow(true)
    val isCandleLit: StateFlow<Boolean> = _isCandleLit.asStateFlow()

    // Curated category filter
    private val _curatedFilter = MutableStateFlow("All")
    val curatedFilter: StateFlow<String> = _curatedFilter.asStateFlow()

    // Quiz state
    private val _quizQuestionIndex = MutableStateFlow(0)
    val quizQuestionIndex: StateFlow<Int> = _quizQuestionIndex.asStateFlow()

    private val _quizSelectedAnswers = MutableStateFlow<List<SampleData.QuizChoice>>(emptyList())
    val quizSelectedAnswers: StateFlow<List<SampleData.QuizChoice>> = _quizSelectedAnswers.asStateFlow()

    private val _quizResultCandle = MutableStateFlow<CustomCandle?>(null)
    val quizResultCandle: StateFlow<CustomCandle?> = _quizResultCandle.asStateFlow()

    // Cart Items from Database
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Saved Candles from Database
    val savedCandles: StateFlow<List<CustomCandle>> = repository.savedCandles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Orders from Database
    val orders: StateFlow<List<Order>> = repository.orders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Latest Placed Order
    private val _latestOrder = MutableStateFlow<Order?>(null)
    val latestOrder: StateFlow<Order?> = _latestOrder.asStateFlow()

    // Selected Order for Live Order Tracking
    private val _selectedOrderId = MutableStateFlow<String?>(null)
    val selectedOrder: StateFlow<Order?> = combine(
        orders,
        _selectedOrderId,
        _latestOrder
    ) { orderList, selectedId, latest ->
        val id = selectedId ?: latest?.orderId ?: return@combine null
        orderList.find { it.orderId == id } ?: latest?.takeIf { it.orderId == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Checkout Flow State (0: Shipping Address, 1: Confirm Candle & Order)
    private val _checkoutStep = MutableStateFlow(0)
    val checkoutStep: StateFlow<Int> = _checkoutStep.asStateFlow()

    private val _shippingDraft = MutableStateFlow(ShippingAddressDraft())
    val shippingDraft: StateFlow<ShippingAddressDraft> = _shippingDraft.asStateFlow()

    // User Message / Snackbar
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    // AI Candle Mockup Visualizer State
    private val _mockupUiState = MutableStateFlow<MockupUiState>(MockupUiState.Idle)
    val mockupUiState: StateFlow<MockupUiState> = _mockupUiState.asStateFlow()

    private val _labelArtworkTheme = MutableStateFlow(LabelArtworkTheme.PEONY_WATERCOLOR)
    val labelArtworkTheme: StateFlow<LabelArtworkTheme> = _labelArtworkTheme.asStateFlow()

    private val _mockupScene = MutableStateFlow(MockupScene.ROSE_MARBLE_VANITY)
    val mockupScene: StateFlow<MockupScene> = _mockupScene.asStateFlow()

    private val _customLabelNotes = MutableStateFlow("")
    val customLabelNotes: StateFlow<String> = _customLabelNotes.asStateFlow()

    private val _uploadedLabelBitmap = MutableStateFlow<Bitmap?>(null)
    val uploadedLabelBitmap: StateFlow<Bitmap?> = _uploadedLabelBitmap.asStateFlow()

    // Navigation Methods
    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            val prev = screenStack.removeAt(screenStack.size - 1)
            _currentScreen.value = prev
            return true
        }
        if (_currentScreen.value != AppScreen.HOME) {
            _currentScreen.value = AppScreen.HOME
            return true
        }
        return false
    }

    // Studio Actions
    fun setStudioStep(step: Int) {
        _studioStep.value = step.coerceIn(0, 4)
    }

    fun toggleCandleLit() {
        _isCandleLit.value = !_isCandleLit.value
    }

    fun updateActiveVessel(vessel: VesselType) {
        _activeCandle.value = _activeCandle.value.copy(vessel = vessel)
    }

    fun updateActiveWax(wax: WaxType) {
        _activeCandle.value = _activeCandle.value.copy(wax = wax)
    }

    fun updateActiveWick(wick: WickType) {
        _activeCandle.value = _activeCandle.value.copy(wick = wick)
    }

    fun updateTopNote(note: String) {
        _activeCandle.value = _activeCandle.value.copy(topNote = note)
    }

    fun updateHeartNote(note: String) {
        _activeCandle.value = _activeCandle.value.copy(heartNote = note)
    }

    fun updateBaseNote(note: String) {
        _activeCandle.value = _activeCandle.value.copy(baseNote = note)
    }

    fun updateIntensity(intensity: Float) {
        _activeCandle.value = _activeCandle.value.copy(intensity = intensity)
    }

    fun updateCandleName(name: String) {
        _activeCandle.value = _activeCandle.value.copy(name = name)
    }

    fun updateCustomInscription(inscription: String) {
        _activeCandle.value = _activeCandle.value.copy(customInscription = inscription)
    }

    fun updateLabelStyle(style: LabelStyle) {
        _activeCandle.value = _activeCandle.value.copy(labelStyle = style)
    }

    fun updateLidOption(lid: LidOption) {
        _activeCandle.value = _activeCandle.value.copy(lid = lid)
    }

    fun updateBotanicalTopping(topping: BotanicalTopping) {
        _activeCandle.value = _activeCandle.value.copy(botanicalTopping = topping)
    }

    fun shuffleFragranceNotes() {
        val randomTop = SampleData.topNotes.random().name
        val randomHeart = SampleData.heartNotes.random().name
        val randomBase = SampleData.baseNotes.random().name
        _activeCandle.value = _activeCandle.value.copy(
            topNote = randomTop,
            heartNote = randomHeart,
            baseNote = randomBase
        )
        viewModelScope.launch {
            _toastMessage.emit("Artisan fragrance blend inspired!")
        }
    }

    fun loadCandleIntoStudio(candle: CustomCandle) {
        _activeCandle.value = candle.copy(id = java.util.UUID.randomUUID().toString())
        _studioStep.value = 0
        _currentScreen.value = AppScreen.STUDIO
    }

    fun toggleFavoriteCurrentCandle() {
        val updated = _activeCandle.value.copy(isFavorite = !_activeCandle.value.isFavorite)
        _activeCandle.value = updated
        viewModelScope.launch {
            repository.saveCandle(updated)
            _toastMessage.emit(if (updated.isFavorite) "Saved to your custom recipe library!" else "Removed from saved")
        }
    }

    // Save Candle Recipe
    fun saveCurrentCandleRecipe() {
        viewModelScope.launch {
            repository.saveCandle(_activeCandle.value)
            _toastMessage.emit("Custom blend '${_activeCandle.value.name}' saved to your collection!")
        }
    }

    fun deleteSavedCandle(candleId: String) {
        viewModelScope.launch {
            repository.deleteSavedCandle(candleId)
            _toastMessage.emit("Recipe removed")
        }
    }

    // Cart Actions
    fun addActiveCandleToCart(
        quantity: Int = 1,
        addMatches: Boolean = true,
        addWickTrimmer: Boolean = false,
        addGiftWrap: Boolean = false,
        giftNote: String = ""
    ) {
        viewModelScope.launch {
            val item = CartItem(
                candle = _activeCandle.value,
                quantity = quantity,
                addMatches = addMatches,
                addWickTrimmer = addWickTrimmer,
                addGiftWrap = addGiftWrap,
                giftMessage = giftNote
            )
            repository.addToCart(item)
            _toastMessage.emit("Added '${_activeCandle.value.name}' to cart!")
        }
    }

    fun addCuratedCandleToCart(curated: CuratedCandle) {
        viewModelScope.launch {
            val item = CartItem(
                candle = curated.defaultCandle,
                quantity = 1,
                addMatches = true,
                addWickTrimmer = false,
                addGiftWrap = false
            )
            repository.addToCart(item)
            _toastMessage.emit("Added '${curated.name}' to cart!")
        }
    }

    fun updateCartItemQuantity(itemId: String, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(itemId, newQuantity)
        }
    }

    fun removeCartItem(itemId: String) {
        viewModelScope.launch {
            repository.removeCartItem(itemId)
            _toastMessage.emit("Item removed from cart")
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // Checkout & Order Placement
    fun openCheckout() {
        if (cartItems.value.isEmpty()) return
        _checkoutStep.value = 0
        navigateTo(AppScreen.CHECKOUT)
    }

    fun setCheckoutStep(step: Int) {
        _checkoutStep.value = step.coerceIn(0, 1)
    }

    fun updateShippingDraft(update: (ShippingAddressDraft) -> ShippingAddressDraft) {
        _shippingDraft.value = update(_shippingDraft.value)
    }

    fun checkoutFromDraft() {
        val draft = _shippingDraft.value
        if (!draft.isValid()) return
        checkout(
            customerName = draft.fullName.trim(),
            customerEmail = draft.email.trim(),
            shippingAddress = draft.formattedSingleLine(),
            giftNote = draft.deliveryNote.trim()
        )
    }

    fun checkout(
        customerName: String,
        customerEmail: String,
        shippingAddress: String,
        giftNote: String
    ) {
        val items = cartItems.value
        if (items.isEmpty()) return

        viewModelScope.launch {
            val order = repository.placeOrder(
                items = items,
                customerName = customerName,
                customerEmail = customerEmail,
                shippingAddress = shippingAddress,
                giftNote = giftNote
            )
            _latestOrder.value = order
            _selectedOrderId.value = order.orderId
            _checkoutStep.value = 0
            screenStack.clear()
            _currentScreen.value = AppScreen.ORDER_SUCCESS
            _toastMessage.emit("Order confirmed! Your custom candles will be hand-poured.")
        }
    }

    // Order Tracking Actions
    fun openOrderTracking(orderId: String) {
        _selectedOrderId.value = orderId
        navigateTo(AppScreen.ORDER_TRACKING)
    }

    fun advanceOrderStage(orderId: String) {
        val currentOrder = orders.value.find { it.orderId == orderId }
            ?: _latestOrder.value?.takeIf { it.orderId == orderId }
            ?: return
        val nextStage = currentOrder.status.nextStage ?: return
        setOrderStage(orderId, nextStage)
    }

    fun setOrderStage(orderId: String, targetStatus: OrderStatus) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, targetStatus)
            _latestOrder.value?.let { latest ->
                if (latest.orderId == orderId) {
                    _latestOrder.value = latest.copy(
                        status = targetStatus,
                        estimatedArrival = targetStatus.estimatedWindowForStage
                    )
                }
            }
            _toastMessage.emit("Order #$orderId updated: ${targetStatus.displayName}")
        }
    }

    fun reorderOrderItems(order: Order) {
        if (order.items.isEmpty()) return
        viewModelScope.launch {
            order.items.forEach { item ->
                repository.addToCart(
                    item.copy(id = java.util.UUID.randomUUID().toString())
                )
            }
            _toastMessage.emit("Added ${order.items.sumOf { it.quantity }} candle(s) from #${order.orderId} to cart!")
            navigateTo(AppScreen.CART)
        }
    }

    fun notifyCopiedTracking(trackingNumber: String) {
        viewModelScope.launch {
            _toastMessage.emit("Tracking $trackingNumber copied to clipboard")
        }
    }

    // Curated Candles
    fun selectCuratedCandle(candle: CuratedCandle) {
        _selectedCuratedCandle.value = candle
        navigateTo(AppScreen.CURATED_DETAIL)
    }

    fun setCuratedFilter(filter: String) {
        _curatedFilter.value = filter
    }

    // Quiz Actions
    fun startQuiz() {
        _quizQuestionIndex.value = 0
        _quizSelectedAnswers.value = emptyList()
        _quizResultCandle.value = null
        navigateTo(AppScreen.QUIZ)
    }

    fun answerQuizQuestion(choice: SampleData.QuizChoice) {
        val currentAnswers = _quizSelectedAnswers.value.toMutableList()
        currentAnswers.add(choice)
        _quizSelectedAnswers.value = currentAnswers

        val nextIndex = _quizQuestionIndex.value + 1
        if (nextIndex < SampleData.quizQuestions.size) {
            _quizQuestionIndex.value = nextIndex
        } else {
            // Calculate Quiz Result Blend
            val firstChoice = currentAnswers.getOrNull(0) ?: choice
            val lastChoice = currentAnswers.lastOrNull() ?: choice

            val resultCandle = CustomCandle(
                name = when {
                    firstChoice.title.contains("Hearth") -> "Fireside Reverie"
                    firstChoice.title.contains("Twilight") -> "Twilight Sanctuary"
                    firstChoice.title.contains("Mountain") -> "Alpine Mist & Pine"
                    else -> "Sunlit Solstice"
                },
                subtitle = "Quiz Signature Formulation",
                vessel = firstChoice.recommendedVessel,
                wax = WaxType.COCONUT_APRICOT,
                wick = if (lastChoice.title.contains("Wood")) WickType.WOOD_WICK else WickType.BRAIDED_COTTON,
                topNote = firstChoice.topNote,
                heartNote = firstChoice.heartNote,
                baseNote = firstChoice.baseNote,
                intensity = 0.80f,
                labelStyle = LabelStyle.MODERN_SERIF,
                lid = LidOption.BAMBOO_WOOD,
                botanicalTopping = BotanicalTopping.DRIED_LAVENDER,
                customInscription = "Crafted for my sanctuary"
            )
            _quizResultCandle.value = resultCandle
        }
    }

    fun customizeQuizResult() {
        _quizResultCandle.value?.let { candle ->
            loadCandleIntoStudio(candle)
        }
    }

    // AI Candle Mockup Visualizer Actions
    fun setLabelArtworkTheme(theme: LabelArtworkTheme) {
        _labelArtworkTheme.value = theme
    }

    fun setMockupScene(scene: MockupScene) {
        _mockupScene.value = scene
    }

    fun setCustomLabelNotes(notes: String) {
        _customLabelNotes.value = notes
    }

    fun clearUploadedLabelBitmap() {
        _uploadedLabelBitmap.value = null
    }

    fun resetMockupState() {
        _mockupUiState.value = MockupUiState.Idle
    }

    fun loadLabelArtworkFromUri(uri: Uri) {
        viewModelScope.launch {
            try {
                val bitmap = withContext(Dispatchers.IO) {
                    val context = getApplication<Application>()
                    val raw = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(context.contentResolver, uri)
                        ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                            val maxDim = maxOf(info.size.width, info.size.height)
                            if (maxDim > 1024) {
                                val scale = 1024f / maxDim
                                decoder.setTargetSize(
                                    (info.size.width * scale).toInt().coerceAtLeast(1),
                                    (info.size.height * scale).toInt().coerceAtLeast(1)
                                )
                            }
                        }
                    } else {
                        @Suppress("DEPRECATION")
                        context.contentResolver.openInputStream(uri)?.use { stream ->
                            BitmapFactory.decodeStream(stream)
                        }
                    }
                    raw?.let { scaleBitmapIfNeeded(it, 1024) }
                }
                if (bitmap != null) {
                    _uploadedLabelBitmap.value = bitmap
                    _toastMessage.emit("Custom label artwork attached!")
                } else {
                    _toastMessage.emit("Could not read selected image.")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load label image from Uri", e)
                _toastMessage.emit("Failed to load label image: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    private fun scaleBitmapIfNeeded(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val longest = maxOf(width, height)
        if (longest <= maxDimension) return bitmap
        val scale = maxDimension.toFloat() / longest
        val targetW = (width * scale).toInt().coerceAtLeast(1)
        val targetH = (height * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, targetW, targetH, true)
    }

    fun generateCandleMockup(candleOverride: CustomCandle? = null) {
        val candle = candleOverride ?: _activeCandle.value
        val theme = _labelArtworkTheme.value
        val scene = _mockupScene.value
        val notes = _customLabelNotes.value
        val uploadedBitmap = _uploadedLabelBitmap.value
        val isLit = _isCandleLit.value

        viewModelScope.launch {
            _mockupUiState.value = MockupUiState.Loading
            try {
                val resultBitmap = withTimeout(60_000L) {
                    mockupGenerator.generateMockup(
                        candle = candle,
                        artworkTheme = theme,
                        scene = scene,
                        customDesignNotes = notes,
                        uploadedLabelBitmap = uploadedBitmap,
                        isLit = isLit
                    )
                }
                if (resultBitmap != null) {
                    val summary = if (uploadedBitmap != null) {
                        "${candle.vessel.displayName} • Custom Uploaded Label • ${scene.displayName}"
                    } else {
                        "${candle.name} • ${theme.displayName} • ${scene.displayName}"
                    }
                    _mockupUiState.value = MockupUiState.Success(resultBitmap, summary)
                } else {
                    _mockupUiState.value = MockupUiState.Error(
                        "The image model did not return a mockup image. Please try adjusting your label description and retry."
                    )
                }
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Candle mockup generation timed out", e)
                _mockupUiState.value = MockupUiState.Error(
                    "Mockup generation timed out after 60 seconds. Please check your connection and try again."
                )
            } catch (e: QuotaExceededException) {
                Log.e(TAG, "Firebase AI quota exceeded", e)
                _mockupUiState.value = MockupUiState.Error(
                    "Image generation quota exceeded. Ensure your Firebase project is on the Blaze plan or try again later."
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Candle mockup generation failed", e)
                _mockupUiState.value = MockupUiState.Error(
                    e.localizedMessage ?: "Candle mockup generation failed."
                )
            }
        }
    }
}
