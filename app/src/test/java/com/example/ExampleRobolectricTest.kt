package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Made with Love Candles", appName)
    }

    @Test
    fun `custom candle price calculation is accurate`() {
        val customCandle = CustomCandle(
            name = "Winter Hearth",
            vessel = VesselType.MATTE_OBSIDIAN, // 34.0
            wax = WaxType.COCONUT_APRICOT,      // +4.0
            wick = WickType.WOOD_WICK,          // +2.0
            lid = LidOption.BRUSHED_BRASS       // +3.0
        )
        // 34 + 4 + 2 + 3 = 43.0
        assertEquals(43.0, customCandle.totalPrice, 0.001)
    }

    @Test
    fun `cart item calculation with accessories`() {
        val customCandle = CustomCandle(
            name = "Solstice Dawn",
            vessel = VesselType.AMBER_APOTHECARY, // 28.0
            wax = WaxType.GOLDEN_SOY,             // +0.0
            wick = WickType.BRAIDED_COTTON,       // +0.0
            lid = LidOption.BAMBOO_WOOD           // +0.0
        )
        val cartItem = CartItem(
            candle = customCandle,
            quantity = 2,
            addMatches = true,     // +4.0
            addWickTrimmer = true, // +9.0
            addGiftWrap = true     // +5.0
        )
        // unit price: 28 + 4 + 9 + 5 = 46.0
        // total for 2 items: 92.0
        assertEquals(46.0, cartItem.unitPrice, 0.001)
        assertEquals(92.0, cartItem.itemTotal, 0.001)
    }

    @Test
    fun `curated candles have valid non-empty recipes`() {
        assertTrue(SampleData.curatedCollection.isNotEmpty())
        SampleData.curatedCollection.forEach { curated ->
            assertTrue(curated.name.isNotBlank())
            assertTrue(curated.defaultCandle.totalPrice > 0.0)
            assertTrue(curated.defaultCandle.topNote.isNotBlank())
            assertTrue(curated.defaultCandle.heartNote.isNotBlank())
            assertTrue(curated.defaultCandle.baseNote.isNotBlank())
        }
    }

    @Test
    fun `order status progression transitions cleanly from crafting to delivered`() {
        val stages = OrderStatus.entries
        assertEquals(5, stages.size)

        // Crafting stages (1..3)
        assertEquals(OrderStatus.POURING, stages[0])
        assertTrue(OrderStatus.POURING.isCraftingStage)
        assertEquals(0.2f, OrderStatus.POURING.progressFraction, 0.001f)
        assertEquals(OrderStatus.CURING, OrderStatus.POURING.nextStage)

        assertTrue(OrderStatus.CURING.isCraftingStage)
        assertEquals(OrderStatus.LABELING, OrderStatus.CURING.nextStage)

        assertTrue(OrderStatus.LABELING.isCraftingStage)
        assertEquals(OrderStatus.SHIPPED, OrderStatus.LABELING.nextStage)

        // Shipping & delivery stages (4..5)
        assertEquals(false, OrderStatus.SHIPPED.isCraftingStage)
        assertEquals(OrderStatus.DELIVERED, OrderStatus.SHIPPED.nextStage)

        assertEquals(false, OrderStatus.DELIVERED.isCraftingStage)
        assertEquals(1.0f, OrderStatus.DELIVERED.progressFraction, 0.001f)
        assertEquals(null, OrderStatus.DELIVERED.nextStage)
    }

    @Test
    fun `order formatting reflects subtotal shipping and total`() {
        val candle = CustomCandle(
            name = "Rose Garden",
            vessel = VesselType.FROSTED_PEARL, // 38.0
            wax = WaxType.GOLDEN_SOY,          // +0.0
            wick = WickType.WOOD_WICK,         // +2.0
            lid = LidOption.BAMBOO_WOOD        // +0.0
        ) // 40.0
        val item = CartItem(candle = candle, quantity = 1, addMatches = false)
        val order = Order(
            orderId = "MLC-12345",
            items = listOf(item),
            subtotal = item.itemTotal,
            shipping = 5.0,
            total = item.itemTotal + 5.0,
            customerName = "Clara",
            customerEmail = "clara@example.com",
            shippingAddress = "742 Blossom Lane"
        )
        assertEquals(OrderStatus.POURING, order.status)
        assertEquals("$40.00", order.formattedSubtotal)
        assertEquals("$5.00", order.formattedShipping)
        assertEquals("$45.00", order.formattedTotal)

        val freeShippingOrder = order.copy(shipping = 0.0, total = item.itemTotal)
        assertEquals("FREE", freeShippingOrder.formattedShipping)
    }

    @Test
    fun `shipping address draft validation catches missing fields and formats valid address`() {
        val emptyDraft = ShippingAddressDraft()
        val errors = emptyDraft.validate()
        assertEquals(false, emptyDraft.isValid())
        assertTrue(errors.containsKey("fullName"))
        assertTrue(errors.containsKey("email"))
        assertTrue(errors.containsKey("streetAddress"))
        assertTrue(errors.containsKey("city"))
        assertTrue(errors.containsKey("state"))
        assertTrue(errors.containsKey("postalCode"))

        val invalidEmailDraft = ShippingAddressDraft(
            fullName = "Clara Bennett",
            email = "not-an-email",
            streetAddress = "245 Rosewater Lane",
            city = "Portland",
            state = "OR",
            postalCode = "97201"
        )
        assertEquals(false, invalidEmailDraft.isValid())
        assertTrue(invalidEmailDraft.validate().containsKey("email"))

        val validDraft = invalidEmailDraft.copy(
            email = "clara@example.com",
            apartmentOrSuite = "Suite 3B"
        )
        assertTrue(validDraft.isValid())
        assertEquals(
            "245 Rosewater Lane, Suite 3B, Portland, OR 97201",
            validDraft.formattedSingleLine()
        )
    }
}
