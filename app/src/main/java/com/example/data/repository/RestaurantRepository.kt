package com.example.data.repository

import com.example.data.local.BabaJiDatabase
import com.example.data.model.CartItem
import com.example.data.model.Coupon
import com.example.data.model.CustomerProfile
import com.example.data.model.DefaultMenuData
import com.example.data.model.DubaiArea
import com.example.data.model.MenuItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.RestaurantConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import kotlin.random.Random

class RestaurantRepository(
    private val database: BabaJiDatabase
) {
    private val menuItemDao = database.menuItemDao()
    private val cartDao = database.cartDao()
    private val orderDao = database.orderDao()
    private val customerDao = database.customerDao()

    // Menu Flows
    val allMenuItems: Flow<List<MenuItem>> = menuItemDao.getAllMenuItems()

    fun getMenuItemsByCategory(category: String): Flow<List<MenuItem>> {
        return if (category == "All") menuItemDao.getAllMenuItems() else menuItemDao.getMenuItemsByCategory(category)
    }

    fun searchMenu(query: String): Flow<List<MenuItem>> = menuItemDao.searchMenuItems(query)

    suspend fun getMenuItemById(id: String): MenuItem? = menuItemDao.getMenuItemById(id)

    // Cart Flows
    val cartItems: Flow<List<CartItem>> = cartDao.getCartItems()

    suspend fun addToCart(
        menuItem: MenuItem,
        portionName: String = "Regular",
        portionDelta: Double = 0.0,
        spiceLevel: String = "Medium",
        selectedAddOns: List<String> = emptyList(),
        specialInstructions: String = "",
        quantity: Int = 1
    ) {
        val cartItemId = "${menuItem.id}_${portionName}_${spiceLevel}_${selectedAddOns.sorted().joinToString()}"
        val existingItem = cartDao.getCartItems() // or we query
        val newCartItem = CartItem(
            cartItemId = cartItemId,
            menuItemId = menuItem.id,
            name = menuItem.name,
            basePrice = menuItem.price,
            portionName = portionName,
            portionPriceDelta = portionDelta,
            spiceLevel = spiceLevel,
            selectedAddOnsJson = selectedAddOns.joinToString(", "),
            specialInstructions = specialInstructions,
            quantity = quantity,
            imageUrl = menuItem.imageUrl
        )
        cartDao.insertCartItem(newCartItem)
    }

    suspend fun updateCartQuantity(cartItemId: String, currentQuantity: Int, delta: Int) {
        val newQty = currentQuantity + delta
        if (newQty <= 0) {
            cartDao.deleteById(cartItemId)
        } else {
            // Read and update
            // Using direct update
            // In room we can update if we query or we can update qty
        }
    }

    suspend fun updateCartItem(item: CartItem, delta: Int) {
        val newQuantity = item.quantity + delta
        if (newQuantity <= 0) {
            cartDao.deleteCartItem(item)
        } else {
            cartDao.updateCartItem(item.copy(quantity = newQuantity))
        }
    }

    suspend fun removeCartItem(item: CartItem) {
        cartDao.deleteCartItem(item)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    // Coupon State
    private val _appliedCoupon = MutableStateFlow<Coupon?>(null)
    val appliedCoupon: StateFlow<Coupon?> = _appliedCoupon.asStateFlow()

    fun applyCoupon(code: String, subtotal: Double): Pair<Boolean, String> {
        val matched = DefaultMenuData.defaultCoupons.find { it.code.equals(code.trim(), ignoreCase = true) }
        return if (matched != null) {
            if (subtotal >= matched.minOrderAed) {
                _appliedCoupon.value = matched
                Pair(true, "Coupon '${matched.code}' applied successfully!")
            } else {
                Pair(false, "Minimum order of AED ${matched.minOrderAed.toInt()} required for this coupon")
            }
        } else {
            Pair(false, "Invalid coupon code. Try 'BABAGI20' or 'DUBAIFREE'")
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
    }

    // Dubai Delivery Areas
    private val _areas = MutableStateFlow(DefaultMenuData.dubaiDeliveryAreas)
    val areas: StateFlow<List<DubaiArea>> = _areas.asStateFlow()

    private val _selectedArea = MutableStateFlow(DefaultMenuData.dubaiDeliveryAreas.first())
    val selectedArea: StateFlow<DubaiArea> = _selectedArea.asStateFlow()

    fun selectArea(area: DubaiArea) {
        _selectedArea.value = area
    }

    fun updateAreaFee(areaName: String, newFee: Double, newMin: Double, isAvailable: Boolean) {
        val updated = _areas.value.map {
            if (it.name == areaName) it.copy(deliveryFee = newFee, minOrderAmount = newMin, isAvailable = isAvailable)
            else it
        }
        _areas.value = updated
        if (_selectedArea.value.name == areaName) {
            _selectedArea.value = _selectedArea.value.copy(deliveryFee = newFee, minOrderAmount = newMin, isAvailable = isAvailable)
        }
    }

    // Customer Profile
    val customerProfile: Flow<CustomerProfile?> = customerDao.getProfile()

    suspend fun saveProfile(profile: CustomerProfile) {
        customerDao.saveProfile(profile)
    }

    // Orders
    val allOrders: Flow<List<Order>> = orderDao.getAllOrders()

    fun getOrderById(orderId: String): Flow<Order?> = orderDao.getOrderById(orderId)

    suspend fun placeOrder(
        customerName: String,
        customerPhone: String,
        area: String,
        buildingVilla: String,
        apartmentNumber: String,
        deliveryInstructions: String,
        cartItems: List<CartItem>,
        subtotal: Double,
        deliveryFee: Double,
        vatAmount: Double,
        discountAmount: Double,
        totalAmount: Double,
        paymentMethod: String
    ): Order {
        val randomNum = Random.nextInt(10000, 99999)
        val orderId = "BJ-$randomNum"
        val itemsSummary = cartItems.joinToString(", ") { "${it.quantity}x ${it.name} (${it.portionName})" }

        val newOrder = Order(
            id = orderId,
            customerName = customerName,
            customerPhone = customerPhone,
            area = area,
            buildingVilla = buildingVilla,
            apartmentNumber = apartmentNumber,
            deliveryInstructions = deliveryInstructions,
            itemsSummary = itemsSummary,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            vatAmount = vatAmount,
            discountAmount = discountAmount,
            totalAmount = totalAmount,
            paymentMethod = paymentMethod,
            status = OrderStatus.ORDER_RECEIVED,
            timestamp = System.currentTimeMillis(),
            riderName = "Tariq Mahmood",
            riderPhone = RestaurantConstant.RESTAURANT_PHONE,
            estimatedArrival = "35-40 min"
        )

        orderDao.insertOrder(newOrder)
        clearCart()
        removeCoupon()
        return newOrder
    }

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        orderDao.updateOrderStatus(orderId, status.name)
    }

    // Admin Operations
    suspend fun addMenuItem(item: MenuItem) {
        menuItemDao.insertMenuItem(item)
    }

    suspend fun updateMenuItem(item: MenuItem) {
        menuItemDao.updateMenuItem(item)
    }

    suspend fun deleteMenuItem(item: MenuItem) {
        menuItemDao.deleteMenuItem(item)
    }

    suspend fun toggleItemAvailability(id: String, isAvailable: Boolean) {
        menuItemDao.updateAvailability(id, isAvailable)
    }
}
