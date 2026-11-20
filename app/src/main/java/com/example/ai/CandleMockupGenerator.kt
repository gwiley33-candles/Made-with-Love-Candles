package com.example.ai

import android.graphics.Bitmap
import com.example.model.BotanicalTopping
import com.example.model.CustomCandle
import com.example.model.LabelStyle
import com.example.model.LidOption
import com.example.model.VesselType
import com.example.model.WickType
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.ImagePart
import com.google.firebase.ai.type.ResponseModality
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig

enum class LabelArtworkTheme(
    val displayName: String,
    val promptDescription: String
) {
    PEONY_WATERCOLOR(
        displayName = "Pink Peony Watercolor",
        promptDescription = "delicate blush pink peony and damask rose watercolor illustration with soft botanical leaves on textured cotton paper"
    ),
    ROSE_GOLD_FOIL(
        displayName = "Rose Gold Botanical",
        promptDescription = "luxurious embossed rose gold metallic botanical line art and thin border on blush cream matte paper"
    ),
    PRESSED_FLORALS(
        displayName = "Pressed Wildflowers",
        promptDescription = "vintage pressed pink blossoms, rosebuds, and sage sprigs arranged around elegant serif typography"
    ),
    ATELIER_MINIMAL(
        displayName = "Minimalist Atelier",
        promptDescription = "refined minimalist high-fashion apothecary typography with generous negative space and delicate blush pink framing"
    )
}

enum class MockupScene(
    val displayName: String,
    val promptDescription: String
) {
    ROSE_MARBLE_VANITY(
        displayName = "Pink Marble Vanity",
        promptDescription = "styled on a sunlit rose-veined white marble vanity beside fresh pink peonies and soft linen shadows"
    ),
    BOTANICAL_FLORIST_TABLE(
        displayName = "Florist Table",
        promptDescription = "styled on a warm weathered oak florist workbench surrounded by scattered pink rose petals, dried eucalyptus, and golden sunlight"
    ),
    COZY_LINEN_BEDSIDE(
        displayName = "Linen Bedside",
        promptDescription = "styled on a serene blush linen bedside table next to ceramic trinkets and warm morning window light"
    ),
    MOODY_ATELIER_GLOW(
        displayName = "Evening Glow",
        promptDescription = "styled in a cozy candlelit evening studio with soft bokeh lights, rose quartz accents, and warm romantic shadows"
    )
}

class CandleMockupGenerator {

    private val imageModel by lazy {
        val config = generationConfig {
            responseModalities = listOf(ResponseModality.IMAGE)
        }
        Firebase.ai.generativeModel(
            modelName = "gemini-nano-banana-2.1",
            generationConfig = config
        )
    }

    suspend fun generateMockup(
        candle: CustomCandle,
        artworkTheme: LabelArtworkTheme,
        scene: MockupScene,
        customDesignNotes: String,
        uploadedLabelBitmap: Bitmap?,
        isLit: Boolean
    ): Bitmap? {
        val vesselDesc = when (candle.vessel) {
            VesselType.AMBER_APOTHECARY -> "warm translucent amber apothecary glass candle jar"
            VesselType.MATTE_OBSIDIAN -> "fluted matte black obsidian stoneware ceramic candle vessel"
            VesselType.TERRACOTTA_CLAY -> "hand-thrown matte terracotta clay candle vessel"
            VesselType.FROSTED_PEARL -> "translucent frosted blush rose quartz glass candle tumbler"
            VesselType.BRUSHED_BRASS -> "spun brushed gold brass candle vessel"
            VesselType.CONCRETE_MARBLE -> "sculptural marbled concrete stone candle vessel"
        }

        val lidDesc = when (candle.lid) {
            LidOption.BAMBOO_WOOD -> "an oiled bamboo wood lid resting casually beside the jar"
            LidOption.BRUSHED_BRASS -> "a spun brushed brass lid resting casually beside the jar"
            LidOption.MATTE_BLACK -> "a sleek matte black metal lid resting casually beside the jar"
            LidOption.NATURAL_CORK -> "a natural artisan cork lid resting casually beside the jar"
        }

        val wickDesc = when (candle.wick) {
            WickType.WOOD_WICK -> "a flat wooden wick"
            WickType.BRAIDED_COTTON -> "a centered braided cotton wick"
            WickType.DUAL_COTTON -> "two symmetrical cotton wicks"
        }

        val flameDesc = if (isLit) {
            "lit with a soft warm glowing flame and shallow glistening melted wax pool"
        } else {
            "unlit with smooth creamy poured wax surface"
        }

        val toppingDesc = when (candle.botanicalTopping) {
            BotanicalTopping.NONE -> "clean smooth wax top"
            BotanicalTopping.ROSE_PETALS -> "delicate dried pink damask rose petals sprinkled near the rim of the wax"
            BotanicalTopping.DRIED_LAVENDER -> "dried purple French lavender buds lightly sprinkled on the wax edge"
            BotanicalTopping.CALENDULA_PETALS -> "golden dried calendula petals lightly sprinkled on the wax edge"
            BotanicalTopping.CINNAMON_DUST -> "a delicate pinch of warm cinnamon spice dust on the wax edge"
            BotanicalTopping.ROSEMARY_SPRIGS -> "tiny dried rosemary sprigs near the wax edge"
        }

        val labelStyleDesc = when (candle.labelStyle) {
            LabelStyle.APOTHECARY_MINIMAL -> "classic double-bordered rectangular apothecary label"
            LabelStyle.MODERN_SERIF -> "clean contemporary square luxury fragrance label"
            LabelStyle.BOTANICAL_ARCH -> "graceful arched dome-shaped die-cut botanical label"
            LabelStyle.GOLD_FOIL -> "deep plum-noir textured label with embossed metallic rose-gold typography"
        }

        val notesClause = if (customDesignNotes.isNotBlank()) {
            " Incorporate these specific custom label design details: ${customDesignNotes.trim()}."
        } else {
            ""
        }

        val inscriptionText = if (candle.customInscription.isNotBlank()) {
            " and smaller italic subtitle \"${candle.customInscription}\""
        } else {
            ""
        }

        val scentSummary = "${candle.topNote.split(" & ").first()} • ${candle.heartNote.split(" & ").first()}"

        val response = if (uploadedLabelBitmap != null) {
            val editPrompt = """
                Photorealistic luxury editorial studio product photograph of a single custom scented candle in a $vesselDesc,
                $flameDesc with $wickDesc and $toppingDesc, with $lidDesc.
                Apply the provided label artwork image cleanly and realistically onto the front center of the candle jar as a $labelStyleDesc,
                following the natural cylindrical curvature, subtle paper texture, and realistic studio lighting highlights of the vessel.
                Keep the label sharp, legible, and seamlessly adhered to the vessel.$notesClause
                The candle is ${scene.promptDescription}.
                Shot on 85mm macro product lens, f/2.8, soft natural shadows, no extra text outside the candle label.
            """.trimIndent()

            imageModel.generateContent(
                content {
                    image(uploadedLabelBitmap)
                    text(editPrompt)
                }
            )
        } else {
            val textPrompt = """
                Photorealistic luxury editorial studio product photograph of a single custom scented candle in a $vesselDesc,
                $flameDesc with $wickDesc and $toppingDesc, with $lidDesc.
                On the front of the candle jar is a crisp, centered $labelStyleDesc featuring ${artworkTheme.promptDescription}.
                The printed label clearly and legibly displays the brand header "MADE WITH LOVE CANDLES" at the top,
                the candle title "${candle.name}" in refined editorial serif lettering,
                fragrance notes "$scentSummary"$inscriptionText.$notesClause
                The candle is ${scene.promptDescription}.
                Shot on 85mm product photography lens, f/2.8, crisp paper label texture, warm romantic botanical aesthetic.
            """.trimIndent()

            imageModel.generateContent(textPrompt)
        }

        val imagePart = response.candidates.firstOrNull()
            ?.content?.parts?.filterIsInstance<ImagePart>()
            ?.firstOrNull()

        return imagePart?.image
    }
}
