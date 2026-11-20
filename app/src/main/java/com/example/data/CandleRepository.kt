package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CandleRepository(private val dao: CandleDao) {

    // Helper functions for Candle Serialization
    fun serializeCandle(c: CustomCandle): String {
        return listOf(
            c.id,
            c.name.replace("|", "/"),
            c.subtitle.replace("|", "/"),
            c.vessel.id,
            c.wax.id,
            c.wick.id,
            c.topNote.replace("|", "/"),
            c.heartNote.replace("|", "/"),
            c.baseNote.replace("|", "/"),
            c.intensity.toString(),
            c.labelStyle.id,
            c.lid.id,
            c.botanicalTopping.id,
            c.customInscription.replace("|", "/"),
            c.isFavorite.toString(),
            c.createdAt.toString()
        ).joinToString("|")
    }

    fun deserializeCandle(str: String): CustomCandle {
        return try {
            val parts = str.split("|")
            CustomCandle(
                id = parts.getOrNull(0) ?: java.util.UUID.randomUUID().toString(),
                name = parts.getOrNull(1) ?: "Sunday Morning Reverie",
                subtitle = parts.getOrNull(2) ?: "Artisan Blend",
                vessel = VesselType.entries.find { it.id == parts.getOrNull(3) } ?: VesselType.AMBER_APOTHECARY,
                wax = WaxType.entries.find { it.id == parts.getOrNull(4) } ?: WaxType.GOLDEN_SOY,
                wick = WickType.entries.find { it.id == parts.getOrNull(5) } ?: WickType.WOOD_WICK,
                topNote = parts.getOrNull(6) ?: "Bergamot & Lime Zest",
                heartNote = parts.getOrNull(7) ?: "Wild French Lavender",
                baseNote = parts.getOrNull(8) ?: "Golden Amber & Sandalwood",
                intensity = parts.getOrNull(9)?.toFloatOrNull() ?: 0.75f,
                labelStyle = LabelStyle.entries.find { it.id == parts.getOrNull(10) } ?: LabelStyle.APOTHECARY_MINIMAL,
                lid = LidOption.entries.find { it.id == parts.getOrNull(11) } ?: LidOption.BAMBOO_WOOD,
                botanicalTopping = BotanicalTopping.entries.find { it.id == parts.getOrNull(12) } ?: BotanicalTopping.DRIED_LAVENDER,
                customInscription = parts.getOrNull(13) ?: "Poured with intention & calm",
                isFavorite = parts.getOrNull(14)?.toBooleanStrictOrNull() ?: false,
                createdAt = parts.getOrNull(15)?.toLongOrNull() ?: System.currentTimeMillis()
            )
        } catch (e: Exception) {
            CustomCandle()
        }
    }

    // Saved Candles
    val savedCandles: Flow<List<CustomCandle>> = dao.getAllSavedCandles().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun saveCandle(candle: CustomCandle) {
        dao.insertSavedCandle(SavedCandleEntity.fromDomain(candle))
    }

    suspend fun deleteSavedCandle(id: String) {
        dao.deleteSavedCandleById(id)
    }

    // Cart Items
    val cartItems: Flow<List<CartItem>> = dao.getAllCartItems().map { entities ->
        entities.map { entity ->
            CartItem(
                id = entity.id,
                candle = deserializeCandle(entity.candleJson),
                quantity = entity.quantity,
                addMatches = entity.addMatches,
                addWickTrimmer = entity.addWickTrimmer,
                addGiftWrap = entity.addGiftWrap,
                giftMessage = entity.giftMessage
            )
        }
    }

    suspend fun addToCart(cartItem: CartItem) {
        val entity = CartItemEntity(
            id = cartItem.id,
            candleJson = serializeCandle(cartItem.candle),
            quantity = cartItem.quantity,
            addMatches = cartItem.addMatches,
            addWickTrimmer = cartItem.addWickTrimmer,
            addGiftWrap = cartItem.addGiftWrap,
            giftMessage = cartItem.giftMessage
        )
        dao.insertCartItem(entity)
    }

    suspend fun updateCartQuantity(id: String, quantity: Int) {
        if (quantity <= 0) {
            dao.deleteCartItemById(id)
        } else {
            dao.updateQuantity(id, quantity)
        }
    }

    suspend fun removeCartItem(id: String) {
        dao.deleteCartItemById(id)
    }

    suspend fun clearCart() {
        dao.clearCart()
    }

    // Orders
    val orders: Flow<List<Order>> = dao.getAllOrders().map { entities ->
        entities.map { entity ->
            val items = try {
                entity.itemsSummary.split(";;").filter { it.isNotBlank() }.map { itemStr ->
                    val segments = itemStr.split("~~")
                    val candle = deserializeCandle(segments[0])
                    val qty = segments.getOrNull(1)?.toIntOrNull() ?: 1
                    CartItem(candle = candle, quantity = qty)
                }
            } catch (e: Exception) {
                emptyList()
            }
            entity.toDomain(items)
        }
    }

    suspend fun placeOrder(
        items: List<CartItem>,
        customerName: String,
        customerEmail: String,
        shippingAddress: String,
        giftNote: String
    ): Order {
        val subtotal = items.sumOf { it.itemTotal }
        val shipping = if (subtotal >= 50.0) 0.0 else 5.0
        val total = subtotal + shipping
        val orderId = "MLC-${(10000..99999).random()}"

        val itemsSummary = items.joinToString(";;") { item ->
            "${serializeCandle(item.candle)}~~${item.quantity}"
        }

        val orderEntity = OrderEntity(
            orderId = orderId,
            placedAt = System.currentTimeMillis(),
            itemsSummary = itemsSummary,
            total = total,
            customerName = customerName,
            customerEmail = customerEmail,
            shippingAddress = shippingAddress,
            giftNote = giftNote,
            statusName = OrderStatus.POURING.name,
            trackingNumber = "MLC-TRACK-${(100000..999999).random()}",
            estimatedArrival = OrderStatus.POURING.estimatedWindowForStage
        )

        dao.insertOrder(orderEntity)
        dao.clearCart()

        return orderEntity.toDomain(items)
    }

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        dao.updateOrderStatus(
            orderId = orderId,
            statusName = status.name,
            estimatedArrival = status.estimatedWindowForStage
        )
    }
}
