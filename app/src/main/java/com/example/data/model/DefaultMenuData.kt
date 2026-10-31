package com.example.data.model

object DefaultMenuData {
    val categories = listOf(
        "Pakistani Food",
        "BBQ",
        "Fast Food",
        "Indian Food",
        "Bread & Extras",
        "Drinks",
        "Desserts"
    )

    val dubaiDeliveryAreas = listOf(
        DubaiArea("Deira", 5.0, 30.0, "25-35 min", true),
        DubaiArea("Bur Dubai", 7.0, 35.0, "30-40 min", true),
        DubaiArea("Karama", 6.0, 30.0, "25-35 min", true),
        DubaiArea("Downtown Dubai", 12.0, 50.0, "40-50 min", true),
        DubaiArea("Business Bay", 12.0, 50.0, "40-50 min", true),
        DubaiArea("Dubai Marina", 15.0, 60.0, "45-55 min", true),
        DubaiArea("JBR (Jumeirah Beach)", 16.0, 60.0, "45-55 min", true),
        DubaiArea("Al Barsha", 12.0, 45.0, "35-45 min", true),
        DubaiArea("JLT (Jumeirah Lake Towers)", 15.0, 55.0, "40-50 min", true),
        DubaiArea("Al Nahda Dubai", 8.0, 35.0, "30-40 min", true)
    )

    val defaultCoupons = listOf(
        Coupon("BABAGI20", discountPercent = 20, minOrderAed = 50.0, description = "20% OFF on all orders above 50 AED"),
        Coupon("DUBAIFREE", fixedDiscountAed = 10.0, minOrderAed = 40.0, description = "Free Delivery on orders above 40 AED"),
        Coupon("WELCOME15", fixedDiscountAed = 15.0, minOrderAed = 60.0, description = "15 AED OFF on your first feast"),
        Coupon("FEAST30", discountPercent = 30, minOrderAed = 120.0, description = "30% Mega Family Discount above 120 AED")
    )

    val sampleDishes: List<MenuItem> = listOf(
        // Pakistani Food
        MenuItem(
            id = "pak_1",
            name = "Chicken Biryani",
            category = "Pakistani Food",
            description = "Fragrant long-grain basmati rice layered with succulent marinated chicken, saffron, potatoes, and royal Pakistani spices.",
            price = 24.0,
            imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = true,
            isVeg = false,
            portionSizesJson = """[{"name":"Single","additionalPrice":0.0},{"name":"Full Portion","additionalPrice":10.0},{"name":"Family Pack","additionalPrice":28.0}]""",
            addOnsJson = """[{"name":"Extra Boiled Egg","price":2.0},{"name":"Extra Zeera Raita","price":3.0},{"name":"Kachumber Salad","price":4.0}]"""
        ),
        MenuItem(
            id = "pak_2",
            name = "Beef Biryani",
            category = "Pakistani Food",
            description = "Rich, aromatic spiced Karachi-style dum biryani with tender melt-in-mouth beef chunks and plum spices.",
            price = 28.0,
            imageUrl = "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = true,
            isVeg = false,
            portionSizesJson = """[{"name":"Single","additionalPrice":0.0},{"name":"Full Portion","additionalPrice":12.0},{"name":"Family Pack","additionalPrice":32.0}]""",
            addOnsJson = """[{"name":"Extra Zeera Raita","price":3.0},{"name":"Spicy Mint Chutney","price":2.0}]"""
        ),
        MenuItem(
            id = "pak_3",
            name = "Chicken Pulao",
            category = "Pakistani Food",
            description = "Traditional aromatic Yakhni pulao cooked in rich chicken broth with whole garam masala and fragrant basmati.",
            price = 22.0,
            imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = false,
            portionSizesJson = """[{"name":"Single","additionalPrice":0.0},{"name":"Full Portion","additionalPrice":8.0}]"""
        ),
        MenuItem(
            id = "pak_4",
            name = "Chicken Karahi",
            category = "Pakistani Food",
            description = "Signature Baba Ji wok-tossed chicken in ripe tomatoes, green chilies, julienned ginger, and crushed coriander.",
            price = 38.0,
            imageUrl = "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = true,
            isVeg = false,
            portionSizesJson = """[{"name":"Half Karahi (500g)","additionalPrice":0.0},{"name":"Full Karahi (1kg)","additionalPrice":30.0}]""",
            addOnsJson = """[{"name":"Garlic Naan (2 pcs)","price":8.0},{"name":"Extra Ginger & Chili Topping","price":2.0}]"""
        ),
        MenuItem(
            id = "pak_5",
            name = "Mutton Karahi",
            category = "Pakistani Food",
            description = "Premium tender mutton braised in freshly ground spices, garlic butter, and fiery tomatoes in an authentic cast-iron karahi.",
            price = 52.0,
            imageUrl = "https://images.unsplash.com/photo-1545247181-516773cae754?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = true,
            isVeg = false,
            portionSizesJson = """[{"name":"Half Karahi (500g)","additionalPrice":0.0},{"name":"Full Karahi (1kg)","additionalPrice":45.0}]""",
            addOnsJson = """[{"name":"Butter Naan (2 pcs)","price":7.0},{"name":"Raita","price":4.0}]"""
        ),
        MenuItem(
            id = "pak_6",
            name = "Chicken Handi",
            category = "Pakistani Food",
            description = "Boneless chicken simmered in creamy cashew gravy, fenugreek leaves (kasuri methi), and mild aromatic spices.",
            price = 36.0,
            imageUrl = "https://images.unsplash.com/photo-1588166524941-3bf61a9c41db?w=800&q=80",
            isPopular = true,
            isBestseller = false,
            isSpicy = false,
            isVeg = false,
            portionSizesJson = """[{"name":"Half Handi","additionalPrice":0.0},{"name":"Full Handi","additionalPrice":28.0}]"""
        ),
        MenuItem(
            id = "pak_7",
            name = "Beef Nihari",
            category = "Pakistani Food",
            description = "Slow-cooked overnight beef shank stew infused with royal Awadhi spices, bone marrow essence, ginger, and lime.",
            price = 32.0,
            imageUrl = "https://images.unsplash.com/photo-1545247181-516773cae754?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = true,
            isVeg = false,
            portionSizesJson = """[{"name":"Regular Portion","additionalPrice":0.0},{"name":"Special Nalli (Marrow) Nihari","additionalPrice":14.0}]""",
            addOnsJson = """[{"name":"Hot Roghani Naan","price":4.0},{"name":"Fried Onion & Lime Extra","price":2.0}]"""
        ),
        MenuItem(
            id = "pak_8",
            name = "Chicken Haleem",
            category = "Pakistani Food",
            description = "Slow-cooked hearty porridge of shredded chicken, seven wholesome lentils, cracked wheat, and caramelized fried onions.",
            price = 25.0,
            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = true,
            isVeg = false,
            portionSizesJson = """[{"name":"Regular Bowl","additionalPrice":0.0},{"name":"Large Bowl","additionalPrice":10.0}]"""
        ),
        MenuItem(
            id = "pak_9",
            name = "Chicken Qorma",
            category = "Pakistani Food",
            description = "Mughlai style velvety chicken curry with roasted onions, yogurt, kewra water, and crushed cardamom.",
            price = 30.0,
            imageUrl = "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = false
        ),

        // BBQ
        MenuItem(
            id = "bbq_1",
            name = "Chicken Tikka",
            category = "BBQ",
            description = "Juicy quarter chicken (breast or leg) marinated in yogurt, Kashmiri red chili, and char-grilled over natural coal.",
            price = 26.0,
            imageUrl = "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = true,
            isVeg = false,
            portionSizesJson = """[{"name":"Chicken Tikka Leg","additionalPrice":0.0},{"name":"Chicken Tikka Breast","additionalPrice":2.0}]""",
            addOnsJson = """[{"name":"Plain Naan","price":3.0},{"name":"Mint Yogurt Chutney","price":2.0}]"""
        ),
        MenuItem(
            id = "bbq_2",
            name = "Chicken Malai Boti",
            category = "BBQ",
            description = "Melt-in-the-mouth boneless chicken cubes marinated in heavy cream, green cardamom, mild white pepper, and grilled to perfection.",
            price = 30.0,
            imageUrl = "https://images.unsplash.com/photo-1544025162-d76694265947?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = false,
            isVeg = false,
            portionSizesJson = """[{"name":"6 Pieces","additionalPrice":0.0},{"name":"12 Pieces","additionalPrice":25.0}]"""
        ),
        MenuItem(
            id = "bbq_3",
            name = "Chicken Seekh Kebab",
            category = "BBQ",
            description = "Finely minced chicken spiced with fresh herbs, coriander seeds, and ginger, skewered and flame-broiled.",
            price = 28.0,
            imageUrl = "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&q=80",
            isPopular = true,
            isBestseller = false,
            isSpicy = true,
            isVeg = false,
            portionSizesJson = """[{"name":"4 Skewers","additionalPrice":0.0},{"name":"8 Skewers","additionalPrice":24.0}]"""
        ),
        MenuItem(
            id = "bbq_4",
            name = "Beef Seekh Kebab",
            category = "BBQ",
            description = "Traditional minced beef kebabs blended with bone marrow fat, roasted cumin, smoked paprika, and grilled on skewers.",
            price = 32.0,
            imageUrl = "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = true,
            isVeg = false
        ),
        MenuItem(
            id = "bbq_5",
            name = "Reshmi Kebab",
            category = "BBQ",
            description = "Silky texture chicken kebabs enriched with saffron, egg white marinade, and royal herbs.",
            price = 30.0,
            imageUrl = "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = false
        ),
        MenuItem(
            id = "bbq_6",
            name = "BBQ Platter",
            category = "BBQ",
            description = "The ultimate Baba Ji sharing feast: 2 Chicken Tikka, 4 Malai Boti, 2 Chicken Seekh, 2 Beef Seekh, 2 Plain Naans, and salad.",
            price = 65.0,
            imageUrl = "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = true,
            isVeg = false,
            portionSizesJson = """[{"name":"Regular Platter (2-3 Persons)","additionalPrice":0.0},{"name":"Jumbo Platter (4-5 Persons)","additionalPrice":55.0}]"""
        ),

        // Fast Food
        MenuItem(
            id = "ff_1",
            name = "Zinger Burger",
            category = "Fast Food",
            description = "Extra crunchy spiced fried chicken fillet topped with spicy mayo, iceberg lettuce, and melted cheese in a toasted brioche bun.",
            price = 20.0,
            imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = true,
            isVeg = false,
            portionSizesJson = """[{"name":"Single Burger","additionalPrice":0.0},{"name":"Make it a Meal (Fries + Drink)","additionalPrice":8.0}]""",
            addOnsJson = """[{"name":"Extra Cheddar Cheese","price":3.0},{"name":"Jalapeño Slices","price":2.0}]"""
        ),
        MenuItem(
            id = "ff_2",
            name = "Chicken Burger",
            category = "Fast Food",
            description = "Flame-grilled chicken patty seasoned with house herbs, garlic aioli, tomato, and fresh lettuce.",
            price = 18.0,
            imageUrl = "https://images.unsplash.com/photo-1586190848861-99aa4a171e90?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = false
        ),
        MenuItem(
            id = "ff_3",
            name = "Beef Burger",
            category = "Fast Food",
            description = "100% pure Angus beef smashed patty with caramelized onions, American cheddar, dill pickles, and secret house sauce.",
            price = 24.0,
            imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&q=80",
            isPopular = true,
            isBestseller = false,
            isSpicy = false,
            isVeg = false
        ),
        MenuItem(
            id = "ff_4",
            name = "Club Sandwich",
            category = "Fast Food",
            description = "Triple-decker toasted sandwich stuffed with grilled chicken breast, fried egg, cheddar slice, tomatoes, and mayo.",
            price = 22.0,
            imageUrl = "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = false
        ),
        MenuItem(
            id = "ff_5",
            name = "Chicken Shawarma",
            category = "Fast Food",
            description = "Thinly shaved spit-roasted chicken rolled in warm Arabic Saj bread with thick toum garlic sauce, pickles, and crispy fries.",
            price = 14.0,
            imageUrl = "https://images.unsplash.com/photo-1642686616462-84067ef99984?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = false,
            isVeg = false,
            portionSizesJson = """[{"name":"Regular Shawarma","additionalPrice":0.0},{"name":"Large Sarookh Size","additionalPrice":6.0},{"name":"Shawarma Platter with Fries","additionalPrice":12.0}]"""
        ),
        MenuItem(
            id = "ff_6",
            name = "French Fries",
            category = "Fast Food",
            description = "Golden, extra-crispy potato fries sprinkled with chef's peri-peri salt seasoning.",
            price = 10.0,
            imageUrl = "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),

        // Indian Food
        MenuItem(
            id = "ind_1",
            name = "Butter Chicken",
            category = "Indian Food",
            description = "Smoked tandoori chicken cooked in creamy tomato, butter, and honey gravy with aromatic dried fenugreek.",
            price = 36.0,
            imageUrl = "https://images.unsplash.com/photo-1588166524941-3bf61a9c41db?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = false,
            isVeg = false,
            portionSizesJson = """[{"name":"Regular Bowl","additionalPrice":0.0},{"name":"Large Bowl","additionalPrice":18.0}]"""
        ),
        MenuItem(
            id = "ind_2",
            name = "Chicken Tikka Masala",
            category = "Indian Food",
            description = "Tandoor roasted spiced chicken pieces tossed in thick bell pepper, onion, and tomato masala gravy.",
            price = 34.0,
            imageUrl = "https://images.unsplash.com/photo-1565557623262-b51c2513a641?w=800&q=80",
            isPopular = true,
            isBestseller = false,
            isSpicy = true,
            isVeg = false
        ),
        MenuItem(
            id = "ind_3",
            name = "Dal Makhani",
            category = "Indian Food",
            description = "Slow simmered whole black lentils and red kidney beans finished with white butter, fresh cream, and subtle spices.",
            price = 22.0,
            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800&q=80",
            isPopular = true,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),
        MenuItem(
            id = "ind_4",
            name = "Paneer Butter Masala",
            category = "Indian Food",
            description = "Fresh cottage cheese cubes cooked in silk smooth makhani gravy with butter and aromatic spices.",
            price = 26.0,
            imageUrl = "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),
        MenuItem(
            id = "ind_5",
            name = "Garlic Naan",
            category = "Indian Food",
            description = "Clay oven tandoori flatbread brushed with garlic butter and fresh coriander leaves.",
            price = 6.0,
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = false,
            isVeg = true
        ),

        // Bread and Extras
        MenuItem(
            id = "brd_1",
            name = "Plain Naan",
            category = "Bread & Extras",
            description = "Traditional soft and fluffy leavened bread freshly baked on the clay walls of our tandoor.",
            price = 3.0,
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),
        MenuItem(
            id = "brd_2",
            name = "Butter Naan",
            category = "Bread & Extras",
            description = "Multi-layered flaky tandoor bread lavishly brushed with pure cow butter.",
            price = 4.0,
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=800&q=80",
            isPopular = true,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),
        MenuItem(
            id = "brd_3",
            name = "Chapati",
            category = "Bread & Extras",
            description = "Whole wheat thin home-style roti freshly puffed on the tawa.",
            price = 2.0,
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),
        MenuItem(
            id = "brd_4",
            name = "Raita",
            category = "Bread & Extras",
            description = "Chilled creamy yogurt dip infused with roasted cumin, rock salt, mint, and diced cucumbers.",
            price = 4.0,
            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),
        MenuItem(
            id = "brd_5",
            name = "Fresh Salad",
            category = "Bread & Extras",
            description = "Crunchy garden plate of cucumbers, red onions, tomatoes, lemon wedges, and fresh green chilies.",
            price = 6.0,
            imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),

        // Drinks
        MenuItem(
            id = "drk_1",
            name = "Fresh Lime Juice",
            category = "Drinks",
            description = "Zesty freshly squeezed Dubai Persian limes with mint leaves and crushed ice (sweet or salted).",
            price = 12.0,
            imageUrl = "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=800&q=80",
            isPopular = true,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),
        MenuItem(
            id = "drk_2",
            name = "Mango Juice",
            category = "Drinks",
            description = "Thick, luscious nectar made from premium Chaunsa sweet mangoes, served ice-cold.",
            price = 15.0,
            imageUrl = "https://images.unsplash.com/photo-1546173159-315724a31696?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = false,
            isVeg = true
        ),
        MenuItem(
            id = "drk_3",
            name = "Lassi",
            category = "Drinks",
            description = "Traditional Punjabi churned yogurt shake topped with thick malai cream (Sweet, Salty, or Mango).",
            price = 14.0,
            imageUrl = "https://images.unsplash.com/photo-1546173159-315724a31696?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = false,
            isVeg = true,
            portionSizesJson = """[{"name":"Sweet Lassi","additionalPrice":0.0},{"name":"Salted Zeera Lassi","additionalPrice":0.0},{"name":"Mango Malai Lassi","additionalPrice":4.0}]"""
        ),
        MenuItem(
            id = "drk_4",
            name = "Soft Drinks",
            category = "Drinks",
            description = "Chilled 330ml can (Coca Cola, Diet Coke, Sprite, Fanta, Pakola, Mountain Dew).",
            price = 5.0,
            imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),
        MenuItem(
            id = "drk_5",
            name = "Mineral Water",
            category = "Drinks",
            description = "Pure bottled drinking water 500ml.",
            price = 3.0,
            imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1bc4e?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),

        // Desserts
        MenuItem(
            id = "dst_1",
            name = "Gulab Jamun",
            category = "Desserts",
            description = "Two warm, golden khoya milk dumplings soaked in fragrant cardamom and saffron rose syrup, garnished with pistachios.",
            price = 12.0,
            imageUrl = "https://images.unsplash.com/photo-1667822935277-c754d9f6916a?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = false,
            isVeg = true
        ),
        MenuItem(
            id = "dst_2",
            name = "Kheer",
            category = "Desserts",
            description = "Traditional slow-cooked basmati rice pudding simmered in whole milk, infused with kewra, crushed almonds, and silver vark.",
            price = 14.0,
            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=800&q=80",
            isPopular = false,
            isBestseller = false,
            isSpicy = false,
            isVeg = true
        ),
        MenuItem(
            id = "dst_3",
            name = "Rasmalai",
            category = "Desserts",
            description = "Delicate, spongy cottage cheese patties soaked in chilled, saffron-scented sweetened condensed milk with sliced pistachios.",
            price = 16.0,
            imageUrl = "https://images.unsplash.com/photo-1667822935277-c754d9f6916a?w=800&q=80",
            isPopular = true,
            isBestseller = true,
            isSpicy = false,
            isVeg = true
        )
    )
}
