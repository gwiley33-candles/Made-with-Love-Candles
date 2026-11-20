package com.example.model

import java.util.UUID

enum class VesselType(
    val id: String,
    val displayName: String,
    val sizeLabel: String,
    val burnTime: String,
    val basePrice: Double,
    val colorHex: Long,
    val description: String
) {
    AMBER_APOTHECARY(
        id = "amber",
        displayName = "Amber Apothecary Glass",
        sizeLabel = "8 oz • 225g",
        burnTime = "50-55 hrs",
        basePrice = 28.0,
        colorHex = 0xFFB45309,
        description = "Vintage apothecary jar that casts an amber, fireside glow through warm glass."
    ),
    MATTE_OBSIDIAN(
        id = "obsidian",
        displayName = "Matte Obsidian Ceramic",
        sizeLabel = "10 oz • 285g",
        burnTime = "60-65 hrs",
        basePrice = 34.0,
        colorHex = 0xFF222224,
        description = "Fluted ribbed black stoneware ceramic with a velvety matte artisan glaze."
    ),
    TERRACOTTA_CLAY(
        id = "terracotta",
        displayName = "Artisan Terracotta Clay",
        sizeLabel = "9 oz • 255g",
        burnTime = "55-60 hrs",
        basePrice = 32.0,
        colorHex = 0xFFB4533C,
        description = "Hand-thrown earthen clay pot with organic rustic texture and Mediterranean warmth."
    ),
    FROSTED_PEARL(
        id = "frosted",
        displayName = "Frosted Rose Quartz Glass",
        sizeLabel = "12 oz • 340g",
        burnTime = "70-75 hrs",
        basePrice = 38.0,
        colorHex = 0xFFF2C2D0,
        description = "Luminous opalescent frosted rose quartz tumbler with a soft blooming moonlight glow."
    ),
    BRUSHED_BRASS(
        id = "brass",
        displayName = "Brushed Brass Travel Tin",
        sizeLabel = "6 oz • 170g",
        burnTime = "35-40 hrs",
        basePrice = 22.0,
        colorHex = 0xFFD4AF37,
        description = "Spun brass canister with tight-fitting lid, perfect for travel or small cozy spaces."
    ),
    CONCRETE_MARBLE(
        id = "concrete",
        displayName = "Marbled Concrete Vessel",
        sizeLabel = "14 oz • 400g",
        burnTime = "80-85 hrs",
        basePrice = 44.0,
        colorHex = 0xFF7D7A75,
        description = "Substantial sculptural vessel cast in raw concrete with delicate charcoal marbling."
    )
}

enum class WaxType(
    val id: String,
    val displayName: String,
    val extraPrice: Double,
    val burnNote: String,
    val description: String
) {
    GOLDEN_SOY(
        id = "soy",
        displayName = "100% Golden Soy Wax",
        extraPrice = 0.0,
        burnNote = "Clean 55h Burn",
        description = "Sustainably grown American soy beans. Velvety cream pour with zero petroleum additives."
    ),
    COCONUT_APRICOT(
        id = "coconut",
        displayName = "Coconut Apricot Crème",
        extraPrice = 4.0,
        burnNote = "Luxe Slow Burn",
        description = "Ultra-luxe plant wax blend. Silky smooth melt pool with unmatched cold and hot fragrance throw."
    ),
    BEESWAX_BLEND(
        id = "beeswax",
        displayName = "Wildflower Beeswax Blend",
        extraPrice = 6.0,
        burnNote = "Air Purifying",
        description = "Pure raw beeswax with natural honey undertones. Emits negative ions to gently cleanse the air."
    ),
    OLIVE_SOY(
        id = "olive",
        displayName = "Mediterranean Olive & Soy",
        extraPrice = 3.0,
        burnNote = "Smooth Botanical",
        description = "Botanical olive oil wax offering long burn endurance and silky porcelain surface texture."
    )
}

enum class WickType(
    val id: String,
    val displayName: String,
    val extraPrice: Double,
    val subtitle: String,
    val description: String
) {
    WOOD_WICK(
        id = "wood",
        displayName = "FSC Crackling Wood Wick",
        extraPrice = 2.0,
        subtitle = "Soothing Fireplace Crackle",
        description = "Sustainably harvested American hardwood that delivers a subtle campfire crackle."
    ),
    BRAIDED_COTTON(
        id = "cotton",
        displayName = "Organic Braided Cotton Wick",
        extraPrice = 0.0,
        subtitle = "Steady Smokeless Flame",
        description = "Unbleached pure cotton filaments woven with paper threads for steady, smokeless burn."
    ),
    DUAL_COTTON(
        id = "dual",
        displayName = "Double Whispering Cotton Wicks",
        extraPrice = 3.0,
        subtitle = "Intense Fragrance Pool",
        description = "Dual wicks engineered for wider vessels to create a fast, room-filling scent throw."
    )
}

enum class LabelStyle(
    val id: String,
    val displayName: String,
    val description: String
) {
    APOTHECARY_MINIMAL(
        id = "apothecary",
        displayName = "Apothecary Border",
        description = "Double line border, serif batch numbers, vintage herbarium aesthetic."
    ),
    MODERN_SERIF(
        id = "modern",
        displayName = "Modern Atelier",
        description = "Spacious tracking, clean typographic hierarchy, elegant luxury finish."
    ),
    BOTANICAL_ARCH(
        id = "botanical",
        displayName = "Botanical Arch",
        description = "Graceful curved dome arch with delicate hand-drawn leaf vignettes."
    ),
    GOLD_FOIL(
        id = "foil",
        displayName = "Gold Foil Luxe",
        description = "Deep noir textured paper with embossed metallic gold typography."
    )
}

enum class LidOption(
    val id: String,
    val displayName: String,
    val extraPrice: Double
) {
    BAMBOO_WOOD("bamboo", "Oiled Bamboo Wood", 0.0),
    BRUSHED_BRASS("brass", "Spun Brushed Brass", 3.0),
    MATTE_BLACK("black", "Matte Black Steel", 2.0),
    NATURAL_CORK("cork", "Chunky Artisan Cork", 2.0)
}

enum class BotanicalTopping(
    val id: String,
    val displayName: String,
    val description: String
) {
    NONE("none", "Clean Pour (No Toppings)", "Smooth, pure wax surface without botanicals"),
    ROSE_PETALS("rose", "Damask Pink Rose Petals", "Organic crushed pink damask rose and peony petals"),
    DRIED_LAVENDER("lavender", "French Lavender Buds", "Calming purple dried lavender florets sprinkled on top"),
    CALENDULA_PETALS("calendula", "Golden Calendula Petals", "Vibrant sunset-orange organic marigold petals"),
    CINNAMON_DUST("cinnamon", "Crushed Ceylon Spice", "Subtle warm cinnamon bark and clove dust"),
    ROSEMARY_SPRIGS("rosemary", "Aromatic Wild Rosemary", "Earthy dried rosemary tips for botanical charm")
}

data class CustomCandle(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "Sunday Rose & Peony",
    val subtitle: String = "Botanical Floral Blend",
    val vessel: VesselType = VesselType.FROSTED_PEARL,
    val wax: WaxType = WaxType.GOLDEN_SOY,
    val wick: WickType = WickType.WOOD_WICK,
    val topNote: String = "Blood Orange & Blossom",
    val heartNote: String = "Damask Rose & Peony",
    val baseNote: String = "Golden Amber & Labdanum",
    val intensity: Float = 0.80f, // 0.25f (Subtle) to 1.0f (Intense)
    val labelStyle: LabelStyle = LabelStyle.BOTANICAL_ARCH,
    val lid: LidOption = LidOption.BAMBOO_WOOD,
    val botanicalTopping: BotanicalTopping = BotanicalTopping.ROSE_PETALS,
    val customInscription: String = "Blossoming with grace & light",
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    val totalPrice: Double
        get() = vessel.basePrice + wax.extraPrice + wick.extraPrice + lid.extraPrice

    val formattedPrice: String
        get() = "$${String.format(java.util.Locale.US, "%.2f", totalPrice)}"
}

data class CartItem(
    val id: String = UUID.randomUUID().toString(),
    val candle: CustomCandle,
    val quantity: Int = 1,
    val addMatches: Boolean = true, // Custom glass match jar with striker (+$4)
    val addWickTrimmer: Boolean = false, // Brass candle care trimmer (+$9)
    val addGiftWrap: Boolean = false, // Kraft gift box with wax seal (+$5)
    val giftMessage: String = ""
) {
    val unitPrice: Double
        get() {
            var p = candle.totalPrice
            if (addMatches) p += 4.0
            if (addWickTrimmer) p += 9.0
            if (addGiftWrap) p += 5.0
            return p
        }

    val itemTotal: Double
        get() = unitPrice * quantity

    val formattedItemTotal: String
        get() = "$${String.format(java.util.Locale.US, "%.2f", itemTotal)}"
}

enum class OrderStatus(
    val displayName: String,
    val stepIndex: Int,
    val description: String,
    val studioDetails: String,
    val estimatedWindowForStage: String
) {
    POURING(
        displayName = "Wax Poured",
        stepIndex = 1,
        description = "Your custom wax formulation has been heated and poured.",
        studioDetails = "Botanical oils blended at 185°F and hand-poured around your selected wick.",
        estimatedWindowForStage = "3-4 business days"
    ),
    CURING(
        displayName = "Curing (48h)",
        stepIndex = 2,
        description = "Candle is cooling at room temperature to lock in fragrance.",
        studioDetails = "Resting in our climate-controlled floral atelier so wax crystals bond cleanly.",
        estimatedWindowForStage = "2-3 business days"
    ),
    LABELING(
        displayName = "Hand Labeled & Inspected",
        stepIndex = 3,
        description = "Your personalized label is printed, pressed, and quality checked.",
        studioDetails = "Wick trimmed to 1/4\", botanicals inspected, and packed in recyclable cushioning.",
        estimatedWindowForStage = "1-2 business days"
    ),
    SHIPPED(
        displayName = "On The Way",
        stepIndex = 4,
        description = "Packed in eco-cushioning and handed to our shipping carrier.",
        studioDetails = "In transit with Artisan Eco-Parcel Express. Temperature-protected packaging.",
        estimatedWindowForStage = "Arriving in 1-2 days"
    ),
    DELIVERED(
        displayName = "Delivered",
        stepIndex = 5,
        description = "Delivered to your doorstep ready to glow.",
        studioDetails = "Allow your candle 2 hours to reach room temperature before your first 3-hour burn.",
        estimatedWindowForStage = "Delivered"
    );

    val progressFraction: Float
        get() = stepIndex.toFloat() / entries.size.toFloat()

    val isCraftingStage: Boolean
        get() = stepIndex <= 3

    val stageCategoryLabel: String
        get() = if (isCraftingStage) "ATELIER CRAFTING" else "SHIPPING & DELIVERY"

    val nextStage: OrderStatus?
        get() = entries.getOrNull(ordinal + 1)
}

data class Order(
    val orderId: String,
    val placedAt: Long = System.currentTimeMillis(),
    val items: List<CartItem>,
    val subtotal: Double,
    val shipping: Double,
    val total: Double,
    val customerName: String,
    val customerEmail: String,
    val shippingAddress: String,
    val giftNote: String = "",
    val status: OrderStatus = OrderStatus.POURING,
    val trackingNumber: String = "MLC-TRACK-${(100000..999999).random()}",
    val estimatedArrival: String = "3-4 business days"
) {
    val formattedTotal: String
        get() = "$${String.format(java.util.Locale.US, "%.2f", total)}"

    val formattedSubtotal: String
        get() = "$${String.format(java.util.Locale.US, "%.2f", subtotal)}"

    val formattedShipping: String
        get() = if (shipping <= 0.0) "FREE" else "$${String.format(java.util.Locale.US, "%.2f", shipping)}"
}

data class CuratedCandle(
    val id: String,
    val name: String,
    val tagLine: String,
    val description: String,
    val defaultCandle: CustomCandle,
    val rating: Float = 4.9f,
    val reviewCount: Int = 128,
    val category: String,
    val isBestseller: Boolean = false
)

data class FragranceOption(
    val name: String,
    val category: String, // Citrus, Floral, Woody, Gourmand, Earthy, Herbal
    val aura: String,
    val iconDescription: String
)

data class ShippingAddressDraft(
    val fullName: String = "",
    val email: String = "",
    val streetAddress: String = "",
    val apartmentOrSuite: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",
    val deliveryNote: String = ""
) {
    fun validate(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        if (fullName.isBlank()) errors["fullName"] = "Recipient full name is required"
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank() || !trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
            errors["email"] = "Enter a valid email address"
        }
        if (streetAddress.isBlank()) errors["streetAddress"] = "Street address is required"
        if (city.isBlank()) errors["city"] = "City is required"
        if (state.isBlank()) errors["state"] = "State / Province is required"
        if (postalCode.trim().length < 3) errors["postalCode"] = "Enter a valid ZIP / postal code"
        return errors
    }

    fun isValid(): Boolean = validate().isEmpty()

    fun formattedSingleLine(): String {
        val streetPart = listOf(streetAddress.trim(), apartmentOrSuite.trim())
            .filter { it.isNotBlank() }
            .joinToString(", ")
        val regionPart = listOf(
            city.trim(),
            listOf(state.trim(), postalCode.trim()).filter { it.isNotBlank() }.joinToString(" ")
        ).filter { it.isNotBlank() }.joinToString(", ")
        return listOf(streetPart, regionPart).filter { it.isNotBlank() }.joinToString(", ")
    }
}
