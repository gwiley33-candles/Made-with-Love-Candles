package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.*

@Entity(tableName = "saved_candles")
data class SavedCandleEntity(
    @PrimaryKey val id: String,
    val name: String,
    val subtitle: String,
    val vesselId: String,
    val waxId: String,
    val wickId: String,
    val topNote: String,
    val heartNote: String,
    val baseNote: String,
    val intensity: Float,
    val labelStyleId: String,
    val lidId: String,
    val botanicalToppingId: String,
    val customInscription: String,
    val isFavorite: Boolean,
    val createdAt: Long
) {
    fun toDomain(): CustomCandle {
        return CustomCandle(
            id = id,
            name = name,
            subtitle = subtitle,
            vessel = VesselType.entries.find { it.id == vesselId } ?: VesselType.AMBER_APOTHECARY,
            wax = WaxType.entries.find { it.id == waxId } ?: WaxType.GOLDEN_SOY,
            wick = WickType.entries.find { it.id == wickId } ?: WickType.WOOD_WICK,
            topNote = topNote,
            heartNote = heartNote,
            baseNote = baseNote,
            intensity = intensity,
            labelStyle = LabelStyle.entries.find { it.id == labelStyleId } ?: LabelStyle.APOTHECARY_MINIMAL,
            lid = LidOption.entries.find { it.id == lidId } ?: LidOption.BAMBOO_WOOD,
            botanicalTopping = BotanicalTopping.entries.find { it.id == botanicalToppingId } ?: BotanicalTopping.DRIED_LAVENDER,
            customInscription = customInscription,
            isFavorite = isFavorite,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromDomain(candle: CustomCandle): SavedCandleEntity {
            return SavedCandleEntity(
                id = candle.id,
                name = candle.name,
                subtitle = candle.subtitle,
                vesselId = candle.vessel.id,
                waxId = candle.wax.id,
                wickId = candle.wick.id,
                topNote = candle.topNote,
                heartNote = candle.heartNote,
                baseNote = candle.baseNote,
                intensity = candle.intensity,
                labelStyleId = candle.labelStyle.id,
                lidId = candle.lid.id,
                botanicalToppingId = candle.botanicalTopping.id,
                customInscription = candle.customInscription,
                isFavorite = candle.isFavorite,
                createdAt = candle.createdAt
            )
        }
    }
}

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val id: String,
    val candleJson: String, // serialized candle properties
    val quantity: Int,
    val addMatches: Boolean,
    val addWickTrimmer: Boolean,
    val addGiftWrap: Boolean,
    val giftMessage: String
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val placedAt: Long,
    val itemsSummary: String, // Readable summary or serialized items
    val total: Double,
    val customerName: String,
    val customerEmail: String,
    val shippingAddress: String,
    val giftNote: String,
    val statusName: String,
    val trackingNumber: String,
    val estimatedArrival: String
) {
    fun toDomain(items: List<CartItem>): Order {
        val status = try {
            OrderStatus.valueOf(statusName)
        } catch (e: Exception) {
            OrderStatus.POURING
        }
        val computedSubtotal = if (items.isNotEmpty()) {
            items.sumOf { it.itemTotal }
        } else {
            total
        }
        val computedShipping = (total - computedSubtotal).coerceAtLeast(0.0)
        return Order(
            orderId = orderId,
            placedAt = placedAt,
            items = items,
            subtotal = computedSubtotal,
            shipping = computedShipping,
            total = total,
            customerName = customerName,
            customerEmail = customerEmail,
            shippingAddress = shippingAddress,
            giftNote = giftNote,
            status = status,
            trackingNumber = trackingNumber,
            estimatedArrival = estimatedArrival
        )
    }
}
