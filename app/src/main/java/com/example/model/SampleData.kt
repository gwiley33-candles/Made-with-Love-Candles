package com.example.model

object SampleData {

    val topNotes = listOf(
        FragranceOption("Bergamot & Lime Zest", "Citrus", "Crisp Italian sunburst with sparkling rind", "citrus"),
        FragranceOption("Blood Orange & Blossom", "Citrus", "Sweet Mediterranean nectar & blossom", "orange"),
        FragranceOption("Crushed Eucalyptus & Mint", "Fresh", "Cool Alpine air with crisp botanical bite", "mint"),
        FragranceOption("Crisp Anjou Pear", "Fruity", "Juicy orchard harvest with dewy sweetness", "pear"),
        FragranceOption("Pink Peppercorn & Pomelo", "Spicy", "Effervescent spice with pink citrus lift", "spice"),
        FragranceOption("Sea Salt & Coastal Mist", "Aquatic", "Pacific ocean breeze over morning driftwood", "wave"),
        FragranceOption("White Tea & Thyme", "Herbal", "Zen temple courtyard with subtle clean steam", "tea")
    )

    val heartNotes = listOf(
        FragranceOption("Wild French Lavender", "Floral", "Sun-warmed Provence hillside in late July", "lavender"),
        FragranceOption("Smoked Vanilla Orchid", "Gourmand", "Velvety Madagascar vanilla with charred wood warmth", "vanilla"),
        FragranceOption("White Mountain Sage", "Herbal", "Clarifying ceremonial herbal smudge with dry earth", "sage"),
        FragranceOption("Night-Blooming Jasmine", "Floral", "Intoxicating evening petals beneath moonlight", "jasmine"),
        FragranceOption("Ceylon Cinnamon Bark", "Spice", "Freshly grated golden cinnamon with sweet heat", "cinnamon"),
        FragranceOption("Green Cardamom & Nutmeg", "Spice", "Aromatic bazaar spices with warm nutty complexity", "cardamom"),
        FragranceOption("Damask Rose & Peony", "Floral", "Heirloom velvet petals with morning dew", "rose"),
        FragranceOption("Black Fig & Wild Honey", "Gourmand", "Sun-ripened Adriatic fig dripping with raw honey", "fig")
    )

    val baseNotes = listOf(
        FragranceOption("Golden Amber & Labdanum", "Resin", "Warm golden resinous embrace with lingering glow", "amber"),
        FragranceOption("Siberian Cedar & Pine", "Woody", "Crisp old-growth coniferous forest and crisp needles", "cedar"),
        FragranceOption("Santal & Creamy Sandalwood", "Woody", "Smooth sacred Australian sandalwood with milky warmth", "sandalwood"),
        FragranceOption("Dark Tonka Bean & Cacao", "Gourmand", "Warm almond-sweet tonka with bitter dark chocolate", "tonka"),
        FragranceOption("Birch Tar & Worn Leather", "Smoky", "Cozy fireside library armchair with aged books", "leather"),
        FragranceOption("Smoky Haitian Vetiver", "Earthy", "Damp roots, cooling rain, and rich dark loamy soil", "vetiver"),
        FragranceOption("Aged Dark Patchouli", "Earthy", "Deep bohemian woodiness aged in vintage casks", "patchouli")
    )

    val curatedCollection = listOf(
        CuratedCandle(
            id = "c0",
            name = "Peony Blossom & Rose",
            tagLine = "Damask Rose, Blooming Peony & Soft Amber",
            description = "Our romantic floral signature. Freshly cut French garden roses and lush pink peonies layered over creamy sandalwood and warm amber in frosted rose quartz glass.",
            category = "Floral & Calming",
            rating = 4.99f,
            reviewCount = 418,
            isBestseller = true,
            defaultCandle = CustomCandle(
                name = "Peony Blossom & Rose",
                subtitle = "Heirloom Floral Series",
                vessel = VesselType.FROSTED_PEARL,
                wax = WaxType.COCONUT_APRICOT,
                wick = WickType.WOOD_WICK,
                topNote = "Blood Orange & Blossom",
                heartNote = "Damask Rose & Peony",
                baseNote = "Golden Amber & Labdanum",
                intensity = 0.85f,
                labelStyle = LabelStyle.BOTANICAL_ARCH,
                lid = LidOption.BRUSHED_BRASS,
                botanicalTopping = BotanicalTopping.ROSE_PETALS,
                customInscription = "Forever in bloom"
            )
        ),
        CuratedCandle(
            id = "c1",
            name = "Fireside & Cashmere",
            tagLine = "Smoked Vanilla, Aged Amber & Crackling Birch",
            description = "Our most beloved hearthside candle. Evokes a twilight evening beside a stone hearth under a warm cashmere throw with soft ember crackle.",
            category = "Woody & Warm",
            rating = 4.98f,
            reviewCount = 342,
            isBestseller = true,
            defaultCandle = CustomCandle(
                name = "Fireside & Cashmere",
                subtitle = "The Hearthside Blend",
                vessel = VesselType.MATTE_OBSIDIAN,
                wax = WaxType.COCONUT_APRICOT,
                wick = WickType.WOOD_WICK,
                topNote = "Pink Peppercorn & Pomelo",
                heartNote = "Smoked Vanilla Orchid",
                baseNote = "Golden Amber & Labdanum",
                intensity = 0.85f,
                labelStyle = LabelStyle.MODERN_SERIF,
                lid = LidOption.MATTE_BLACK,
                botanicalTopping = BotanicalTopping.CINNAMON_DUST,
                customInscription = "Warmth for quiet winter nights"
            )
        ),
        CuratedCandle(
            id = "c2",
            name = "Botanical Oak & Sage",
            tagLine = "Mountain Sage, Bergamot & Siberian Cedar",
            description = "An invigorating woodland sanctuary pour. Brings the grounding, restorative presence of misty mountain woods and clarifying herbs into your living room.",
            category = "Earthy & Fresh",
            rating = 4.92f,
            reviewCount = 219,
            isBestseller = true,
            defaultCandle = CustomCandle(
                name = "Botanical Oak & Sage",
                subtitle = "Forest Canopy Series",
                vessel = VesselType.AMBER_APOTHECARY,
                wax = WaxType.GOLDEN_SOY,
                wick = WickType.WOOD_WICK,
                topNote = "Bergamot & Lime Zest",
                heartNote = "White Mountain Sage",
                baseNote = "Siberian Cedar & Pine",
                intensity = 0.70f,
                labelStyle = LabelStyle.APOTHECARY_MINIMAL,
                lid = LidOption.BAMBOO_WOOD,
                botanicalTopping = BotanicalTopping.ROSEMARY_SPRIGS,
                customInscription = "Breathe deep and find clarity"
            )
        ),
        CuratedCandle(
            id = "c3",
            name = "Midnight In Provence",
            tagLine = "French Lavender, Night Jasmine & Tonka Bean",
            description = "A sleep-ritual masterpiece. Harvested French lavender blossoms combined with delicate night petals to soothe the nervous system into restorative slumber.",
            category = "Floral & Calming",
            rating = 4.95f,
            reviewCount = 184,
            isBestseller = false,
            defaultCandle = CustomCandle(
                name = "Midnight In Provence",
                subtitle = "Rest & Ritual Series",
                vessel = VesselType.TERRACOTTA_CLAY,
                wax = WaxType.BEESWAX_BLEND,
                wick = WickType.BRAIDED_COTTON,
                topNote = "White Tea & Thyme",
                heartNote = "Wild French Lavender",
                baseNote = "Dark Tonka Bean & Cacao",
                intensity = 0.75f,
                labelStyle = LabelStyle.BOTANICAL_ARCH,
                lid = LidOption.NATURAL_CORK,
                botanicalTopping = BotanicalTopping.DRIED_LAVENDER,
                customInscription = "Cast out the noise, welcome calm"
            )
        ),
        CuratedCandle(
            id = "c4",
            name = "Solar Santal & Fig",
            tagLine = "Black Fig, Blood Orange & Creamy Sandalwood",
            description = "A sun-drenched Mediterranean dream. Rich sweet Adriatic fig jam layered over glowing sandalwood and zest for an uplifting, chic aura.",
            category = "Fruity & Exotic",
            rating = 4.89f,
            reviewCount = 96,
            isBestseller = false,
            defaultCandle = CustomCandle(
                name = "Solar Santal & Fig",
                subtitle = "Riviera Summer Edition",
                vessel = VesselType.FROSTED_PEARL,
                wax = WaxType.COCONUT_APRICOT,
                wick = WickType.DUAL_COTTON,
                topNote = "Blood Orange & Blossom",
                heartNote = "Black Fig & Wild Honey",
                baseNote = "Santal & Creamy Sandalwood",
                intensity = 0.80f,
                labelStyle = LabelStyle.GOLD_FOIL,
                lid = LidOption.BRUSHED_BRASS,
                botanicalTopping = BotanicalTopping.CALENDULA_PETALS,
                customInscription = "Sunlight on warm skin"
            )
        ),
        CuratedCandle(
            id = "c5",
            name = "Cardamom Tea & Chai",
            tagLine = "Ceylon Cinnamon, Green Cardamom & Warm Amber",
            description = "A steaming mug of golden chai enjoyed on a rainy Sunday morning. Rich spices that envelop the room in comforting warmth.",
            category = "Woody & Warm",
            rating = 4.94f,
            reviewCount = 147,
            isBestseller = true,
            defaultCandle = CustomCandle(
                name = "Cardamom Tea & Chai",
                subtitle = "Cozy Spiced Formulation",
                vessel = VesselType.BRUSHED_BRASS,
                wax = WaxType.GOLDEN_SOY,
                wick = WickType.WOOD_WICK,
                topNote = "Crisp Anjou Pear",
                heartNote = "Ceylon Cinnamon Bark",
                baseNote = "Golden Amber & Labdanum",
                intensity = 0.85f,
                labelStyle = LabelStyle.APOTHECARY_MINIMAL,
                lid = LidOption.BRUSHED_BRASS,
                botanicalTopping = BotanicalTopping.CINNAMON_DUST,
                customInscription = "Slow mornings & spiced tea"
            )
        ),
        CuratedCandle(
            id = "c6",
            name = "Sea Cliff & Driftwood",
            tagLine = "Pacific Sea Salt, Vetiver & Coastal Birch",
            description = "Fog rolling across wild Northern California coastal bluffs. Crisp salty ocean foam colliding with damp cedar roots and beach bonfires.",
            category = "Earthy & Fresh",
            rating = 4.91f,
            reviewCount = 112,
            isBestseller = false,
            defaultCandle = CustomCandle(
                name = "Sea Cliff & Driftwood",
                subtitle = "Pacific Wilderness",
                vessel = VesselType.CONCRETE_MARBLE,
                wax = WaxType.OLIVE_SOY,
                wick = WickType.DUAL_COTTON,
                topNote = "Sea Salt & Coastal Mist",
                heartNote = "White Mountain Sage",
                baseNote = "Smoky Haitian Vetiver",
                intensity = 0.70f,
                labelStyle = LabelStyle.MODERN_SERIF,
                lid = LidOption.BAMBOO_WOOD,
                botanicalTopping = BotanicalTopping.NONE,
                customInscription = "Wild seas & timeless horizons"
            )
        )
    )

    data class QuizQuestion(
        val id: Int,
        val question: String,
        val subtitle: String,
        val options: List<QuizChoice>
    )

    data class QuizChoice(
        val title: String,
        val description: String,
        val icon: String,
        val recommendedVessel: VesselType,
        val topNote: String,
        val heartNote: String,
        val baseNote: String
    )

    val quizQuestions = listOf(
        QuizQuestion(
            id = 1,
            question = "What atmosphere are you crafting?",
            subtitle = "Set the emotional mood for your custom sanctuary",
            options = listOf(
                QuizChoice(
                    title = "Cozy Hearth & Rainy Book",
                    description = "Warm blankets, ambient crackle, deep comforting shadows",
                    icon = "hearth",
                    recommendedVessel = VesselType.MATTE_OBSIDIAN,
                    topNote = "Pink Peppercorn & Pomelo",
                    heartNote = "Smoked Vanilla Orchid",
                    baseNote = "Golden Amber & Labdanum"
                ),
                QuizChoice(
                    title = "Deep Rest & Twilight Ritual",
                    description = "Unwinding thoughts, soothing chamomile, restorative sleep",
                    icon = "moon",
                    recommendedVessel = VesselType.TERRACOTTA_CLAY,
                    topNote = "White Tea & Thyme",
                    heartNote = "Wild French Lavender",
                    baseNote = "Dark Tonka Bean & Cacao"
                ),
                QuizChoice(
                    title = "Crisp Mountain Breath & Focus",
                    description = "Mental clarity, fresh conifers, cleansing morning air",
                    icon = "mountain",
                    recommendedVessel = VesselType.AMBER_APOTHECARY,
                    topNote = "Crushed Eucalyptus & Mint",
                    heartNote = "White Mountain Sage",
                    baseNote = "Siberian Cedar & Pine"
                ),
                QuizChoice(
                    title = "Golden Hour & Social Glow",
                    description = "Vibrant citrus, champagne toasts, radiant dinner gatherings",
                    icon = "sun",
                    recommendedVessel = VesselType.FROSTED_PEARL,
                    topNote = "Blood Orange & Blossom",
                    heartNote = "Black Fig & Wild Honey",
                    baseNote = "Santal & Creamy Sandalwood"
                )
            )
        ),
        QuizQuestion(
            id = 2,
            question = "Where will your candle live?",
            subtitle = "Scent throw and vessel aesthetic for your chosen space",
            options = listOf(
                QuizChoice(
                    title = "Bedside Nightstand / Reading Nook",
                    description = "Intimate radius, gentle calming throw",
                    icon = "bed",
                    recommendedVessel = VesselType.TERRACOTTA_CLAY,
                    topNote = "White Tea & Thyme",
                    heartNote = "Wild French Lavender",
                    baseNote = "Santal & Creamy Sandalwood"
                ),
                QuizChoice(
                    title = "Open Living Room & Entryway",
                    description = "Grand presence, high hot throw that fills high ceilings",
                    icon = "room",
                    recommendedVessel = VesselType.CONCRETE_MARBLE,
                    topNote = "Bergamot & Lime Zest",
                    heartNote = "Ceylon Cinnamon Bark",
                    baseNote = "Golden Amber & Labdanum"
                ),
                QuizChoice(
                    title = "Spa Bathroom / Bath Soak",
                    description = "Steamy moisture resistance, botanical aromatherapy",
                    icon = "bath",
                    recommendedVessel = VesselType.AMBER_APOTHECARY,
                    topNote = "Crushed Eucalyptus & Mint",
                    heartNote = "White Mountain Sage",
                    baseNote = "Smoky Haitian Vetiver"
                ),
                QuizChoice(
                    title = "Studio Desk / Workspace",
                    description = "Non-distracting clean burn to spark creative flow",
                    icon = "desk",
                    recommendedVessel = VesselType.MATTE_OBSIDIAN,
                    topNote = "Bergamot & Lime Zest",
                    heartNote = "White Mountain Sage",
                    baseNote = "Siberian Cedar & Pine"
                )
            )
        ),
        QuizQuestion(
            id = 3,
            question = "Which sound and burn style calls to you?",
            subtitle = "The multisensory flame experience",
            options = listOf(
                QuizChoice(
                    title = "Soft Crackling Wood Wick",
                    description = "Campfire cadence, wide radiant flame pool",
                    icon = "wood",
                    recommendedVessel = VesselType.MATTE_OBSIDIAN,
                    topNote = "Pink Peppercorn & Pomelo",
                    heartNote = "Smoked Vanilla Orchid",
                    baseNote = "Birch Tar & Worn Leather"
                ),
                QuizChoice(
                    title = "Silent Whispering Cotton Flame",
                    description = "Steady, peaceful, meditation-friendly silent burn",
                    icon = "cotton",
                    recommendedVessel = VesselType.FROSTED_PEARL,
                    topNote = "White Tea & Thyme",
                    heartNote = "Night-Blooming Jasmine",
                    baseNote = "Golden Amber & Labdanum"
                )
            )
        )
    )
}
