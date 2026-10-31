package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Entity(tableName = "menu_items")
data class MenuItem(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val description: String,
    val price: Double, // In AED
    val imageUrl: String,
    val isAvailable: Boolean = true,
    val rating: Double = 4.8,
    val reviewsCount: Int = 120,
    val isPopular: Boolean = false,
    val isBestseller: Boolean = false,
    val isSpicy: Boolean = false,
    val isVeg: Boolean = false,
    val preparationTime: String = "25-30 min",
    val portionSizesJson: String = "[]",
    val addOnsJson: String = "[]"
)

data class PortionOption(
    val name: String,
    val additionalPrice: Double
)

data class AddOnOption(
    val name: String,
    val price: Double
)

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val cartItemId: String,
    val menuItemId: String,
    val name: String,
    val basePrice: Double,
    val portionName: String = "Regular",
    val portionPriceDelta: Double = 0.0,
    val spiceLevel: String = "Medium",
    val selectedAddOnsJson: String = "[]",
    val specialInstructions: String = "",
    val quantity: Int = 1,
    val imageUrl: String = ""
) {
    val unitPrice: Double
        get() {
            var total = basePrice + portionPriceDelta
            // Moshi or simple JSON parse for add-ons
            return total
        }

    val itemTotal: Double
        get() = unitPrice * quantity
}

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val id: String, // e.g. "BJ-20491"
    val customerName: String,
    val customerPhone: String,
    val area: String,
    val buildingVilla: String,
    val apartmentNumber: String,
    val deliveryInstructions: String,
    val itemsSummary: String, // Readable summary or JSON
    val subtotal: Double,
    val deliveryFee: Double,
    val vatAmount: Double,
    val discountAmount: Double,
    val totalAmount: Double,
    val paymentMethod: String, // "Cash on Delivery (COD)" or "Online Card (Stripe/Telr)"
    val status: OrderStatus = OrderStatus.ORDER_RECEIVED,
    val timestamp: Long = System.currentTimeMillis(),
    val riderName: String = "Tariq Mahmood",
    val riderPhone: String = "+971 52 325 7028",
    val estimatedArrival: String = "35-40 min"
)

enum class OrderStatus(val label: String, val stepIndex: Int, val description: String) {
    ORDER_RECEIVED("Order Received", 0, "We have received your order at Baba Ji kitchen."),
    ORDER_CONFIRMED("Order Confirmed", 1, "Chef has confirmed your delicious meal."),
    PREPARING("Preparing Food", 2, "Fresh ingredients are sizzling on the grill and handi."),
    READY_FOR_DELIVERY("Ready for Delivery", 3, "Packed in heat-retaining containers."),
    OUT_FOR_DELIVERY("Out for Delivery", 4, "Rider is en route to your Dubai address."),
    DELIVERED("Delivered", 5, "Order successfully delivered. Enjoy your meal!"),
    CANCELLED("Cancelled", -1, "Order was cancelled.")
}

@Entity(tableName = "customer_profile")
data class CustomerProfile(
    @PrimaryKey val id: String = "current_user",
    val name: String = "Guest Customer",
    val phone: String = "+971 50 123 4567",
    val email: String = "customer@dubai.ae",
    val selectedArea: String = "Deira",
    val buildingVilla: String = "Al Rigga Tower",
    val apartmentNumber: String = "Apt 402",
    val deliveryNotes: String = "Please ring doorbell",
    val isLoggedIn: Boolean = false
)

data class Coupon(
    val code: String,
    val discountPercent: Int = 0,
    val fixedDiscountAed: Double = 0.0,
    val minOrderAed: Double = 40.0,
    val description: String
)

data class DubaiArea(
    val name: String,
    val deliveryFee: Double,
    val minOrderAmount: Double,
    val estimatedMinutes: String,
    val isAvailable: Boolean = true
)

object RestaurantConstant {
    const val RESTAURANT_NAME = "Baba Ji Restaurant"
    const val RESTAURANT_TAGLINE = "Authentic Pakistani & Indian Cuisine, BBQ & Fast Food"
    const val RESTAURANT_PHONE = "+971 52 325 7028"
    const val RESTAURANT_WHATSAPP = "+971523257028"
    const val RESTAURANT_ADDRESS = "Al Rigga Road, Near Clock Tower, Deira, Dubai, UAE"
    const val CURRENCY = "AED"
    const val VAT_PERCENT = 0.05 // 5% UAE VAT
}
