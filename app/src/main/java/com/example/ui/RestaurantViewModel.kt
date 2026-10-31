package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BabaJiDatabase
import com.example.data.model.CartItem
import com.example.data.model.Coupon
import com.example.data.model.CustomerProfile
import com.example.data.model.DefaultMenuData
import com.example.data.model.DubaiArea
import com.example.data.model.MenuItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.PortionOption
import com.example.data.model.RestaurantConstant
import com.example.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray

enum class NavDestination(val label: String) {
    HOME("Home"),
    MENU("Menu"),
    ORDERS("My Orders"),
    OFFERS("Offers"),
    PROFILE("Profile"),
    ADMIN("Admin Portal")
}

data class CartSummary(
    val items: List<CartItem> = emptyList(),
    val itemCount: Int = 0,
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val discount: Double = 0.0,
    val vatAmount: Double = 0.0,
    val total: Double = 0.0
)

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {
    private val database = BabaJiDatabase.getDatabase(application, viewModelScope)
    val repository = RestaurantRepository(database)

    // Current Navigation Destination
    private val _currentDestination = MutableStateFlow(NavDestination.HOME)
    val currentDestination: StateFlow<NavDestination> = _currentDestination.asStateFlow()

    fun navigateTo(destination: NavDestination) {
        _currentDestination.value = destination
    }

    // Active Category Filter
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    // Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Menu Items filtered by category and search
    val menuItems: StateFlow<List<MenuItem>> = combine(
        repository.allMenuItems,
        _selectedCategory,
        _searchQuery
    ) { items, category, query ->
        items.filter { item ->
            val matchesCategory = if (category == "All") true else item.category == category
            val matchesQuery = if (query.isBlank()) true else {
                item.name.contains(query, ignoreCase = true) ||
                item.description.contains(query, ignoreCase = true) ||
                item.category.contains(query, ignoreCase = true)
            }
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Food Details Dialog State
    private val _selectedDishForDetails = MutableStateFlow<MenuItem?>(null)
    val selectedDishForDetails: StateFlow<MenuItem?> = _selectedDishForDetails.asStateFlow()

    private val _detailPortionIndex = MutableStateFlow(0)
    val detailPortionIndex: StateFlow<Int> = _detailPortionIndex.asStateFlow()

    private val _detailSpiceLevel = MutableStateFlow("Medium")
    val detailSpiceLevel: StateFlow<String> = _detailSpiceLevel.asStateFlow()

    private val _detailSelectedAddOns = MutableStateFlow<Set<String>>(emptySet())
    val detailSelectedAddOns: StateFlow<Set<String>> = _detailSelectedAddOns.asStateFlow()

    private val _detailQuantity = MutableStateFlow(1)
    val detailQuantity: StateFlow<Int> = _detailQuantity.asStateFlow()

    private val _detailSpecialInstructions = MutableStateFlow("")
    val detailSpecialInstructions: StateFlow<String> = _detailSpecialInstructions.asStateFlow()

    fun openDishDetails(item: MenuItem) {
        _selectedDishForDetails.value = item
        _detailPortionIndex.value = 0
        _detailSpiceLevel.value = if (item.isSpicy) "Medium Spiced" else "Mild"
        _detailSelectedAddOns.value = emptySet()
        _detailQuantity.value = 1
        _detailSpecialInstructions.value = ""
    }

    fun closeDishDetails() {
        _selectedDishForDetails.value = null
    }

    fun setDetailPortionIndex(index: Int) {
        _detailPortionIndex.value = index
    }

    fun setDetailSpiceLevel(spice: String) {
        _detailSpiceLevel.value = spice
    }

    fun toggleDetailAddOn(addOnName: String) {
        val current = _detailSelectedAddOns.value.toMutableSet()
        if (current.contains(addOnName)) current.remove(addOnName) else current.add(addOnName)
        _detailSelectedAddOns.value = current
    }

    fun adjustDetailQuantity(delta: Int) {
        val next = _detailQuantity.value + delta
        if (next in 1..20) {
            _detailQuantity.value = next
        }
    }

    fun setDetailSpecialInstructions(instructions: String) {
        _detailSpecialInstructions.value = instructions
    }

    fun addCurrentDetailItemToCart() {
        val item = _selectedDishForDetails.value ?: return
        viewModelScope.launch {
            // Parse portion
            val portions = parsePortions(item.portionSizesJson)
            val portion = portions.getOrNull(_detailPortionIndex.value)
            val portionName = portion?.name ?: "Regular"
            val portionDelta = portion?.additionalPrice ?: 0.0

            repository.addToCart(
                menuItem = item,
                portionName = portionName,
                portionDelta = portionDelta,
                spiceLevel = _detailSpiceLevel.value,
                selectedAddOns = _detailSelectedAddOns.value.toList(),
                specialInstructions = _detailSpecialInstructions.value,
                quantity = _detailQuantity.value
            )
            closeDishDetails()
            showToast("Added to your cart!")
        }
    }

    fun quickAddToCart(item: MenuItem) {
        viewModelScope.launch {
            repository.addToCart(
                menuItem = item,
                portionName = "Regular",
                portionDelta = 0.0,
                spiceLevel = if (item.isSpicy) "Medium Spiced" else "Mild",
                quantity = 1
            )
            showToast("Added 1x ${item.name} to cart")
        }
    }

    // Cart and Calculations
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appliedCoupon: StateFlow<Coupon?> = repository.appliedCoupon
    val selectedArea: StateFlow<DubaiArea> = repository.selectedArea
    val deliveryAreas: StateFlow<List<DubaiArea>> = repository.areas

    fun selectDubaiArea(area: DubaiArea) {
        repository.selectArea(area)
    }

    val cartSummary: StateFlow<CartSummary> = combine(
        cartItems,
        appliedCoupon,
        selectedArea
    ) { items, coupon, area ->
        val count = items.sumOf { it.quantity }
        val subtotal = items.sumOf { (it.basePrice + it.portionPriceDelta) * it.quantity }

        var discount = 0.0
        if (coupon != null && subtotal >= coupon.minOrderAed) {
            discount = if (coupon.discountPercent > 0) {
                (subtotal * coupon.discountPercent) / 100.0
            } else {
                coupon.fixedDiscountAed
            }
        }

        // Delivery fee is waived if coupon is DUBAIFREE or subtotal > 100 AED
        val baseDeliveryFee = if (coupon?.code == "DUBAIFREE" || subtotal >= 100.0) 0.0 else area.deliveryFee
        val taxableSubtotal = (subtotal - discount).coerceAtLeast(0.0)
        val vat = taxableSubtotal * RestaurantConstant.VAT_PERCENT
        val total = taxableSubtotal + baseDeliveryFee + vat

        CartSummary(
            items = items,
            itemCount = count,
            subtotal = subtotal,
            deliveryFee = baseDeliveryFee,
            discount = discount,
            vatAmount = vat,
            total = total
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CartSummary())

    fun updateCartItemQuantity(item: CartItem, delta: Int) {
        viewModelScope.launch {
            repository.updateCartItem(item, delta)
        }
    }

    fun removeCartItem(item: CartItem) {
        viewModelScope.launch {
            repository.removeCartItem(item)
            showToast("Item removed from cart")
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // Coupon Action
    private val _couponMessage = MutableStateFlow<String?>(null)
    val couponMessage: StateFlow<String?> = _couponMessage.asStateFlow()

    fun applyCoupon(code: String) {
        val currentSubtotal = cartSummary.value.subtotal
        val (success, message) = repository.applyCoupon(code, currentSubtotal)
        _couponMessage.value = message
        showToast(message)
    }

    fun removeCoupon() {
        repository.removeCoupon()
        _couponMessage.value = null
        showToast("Coupon removed")
    }

    // Profile & Customer Info
    val customerProfile: StateFlow<CustomerProfile?> = repository.customerProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun updateProfile(profile: CustomerProfile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
            showToast("Address & Profile saved")
        }
    }

    // Orders & Tracking
    val allOrders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _trackedOrderId = MutableStateFlow<String?>(null)
    val trackedOrderId: StateFlow<String?> = _trackedOrderId.asStateFlow()

    fun setTrackedOrder(orderId: String) {
        _trackedOrderId.value = orderId
        _showOrderTrackingScreen.value = true
    }

    private val _showOrderTrackingScreen = MutableStateFlow(false)
    val showOrderTrackingScreen: StateFlow<Boolean> = _showOrderTrackingScreen.asStateFlow()

    fun closeOrderTracking() {
        _showOrderTrackingScreen.value = false
    }

    // Checkout UI States
    private val _showCheckoutSheet = MutableStateFlow(false)
    val showCheckoutSheet: StateFlow<Boolean> = _showCheckoutSheet.asStateFlow()

    fun openCheckout() {
        _showCheckoutSheet.value = true
    }

    fun closeCheckout() {
        _showCheckoutSheet.value = false
    }

    // Cart Screen / Sheet State
    private val _showCartSheet = MutableStateFlow(false)
    val showCartSheet: StateFlow<Boolean> = _showCartSheet.asStateFlow()

    fun openCart() {
        _showCartSheet.value = true
    }

    fun closeCart() {
        _showCartSheet.value = false
    }

    // Place Order
    fun placeOrder(
        customerName: String,
        customerPhone: String,
        area: String,
        buildingVilla: String,
        apartmentNumber: String,
        deliveryInstructions: String,
        paymentMethod: String
    ) {
        val summary = cartSummary.value
        if (summary.items.isEmpty()) {
            showToast("Cart is empty!")
            return
        }

        viewModelScope.launch {
            val order = repository.placeOrder(
                customerName = customerName,
                customerPhone = customerPhone,
                area = area,
                buildingVilla = buildingVilla,
                apartmentNumber = apartmentNumber,
                deliveryInstructions = deliveryInstructions,
                cartItems = summary.items,
                subtotal = summary.subtotal,
                deliveryFee = summary.deliveryFee,
                vatAmount = summary.vatAmount,
                discountAmount = summary.discount,
                totalAmount = summary.total,
                paymentMethod = paymentMethod
            )
            // Save profile details for convenience
            repository.saveProfile(
                CustomerProfile(
                    name = customerName,
                    phone = customerPhone,
                    selectedArea = area,
                    buildingVilla = buildingVilla,
                    apartmentNumber = apartmentNumber,
                    deliveryNotes = deliveryInstructions,
                    isLoggedIn = true
                )
            )
            _showCheckoutSheet.value = false
            _showCartSheet.value = false
            _trackedOrderId.value = order.id
            _showOrderTrackingScreen.value = true
            showToast("Order #${order.id} Placed Successfully!")
        }
    }

    // Admin Mode & Actions
    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    fun toggleAdminMode(enabled: Boolean) {
        _isAdminMode.value = enabled
        if (enabled) {
            _currentDestination.value = NavDestination.ADMIN
        } else {
            _currentDestination.value = NavDestination.HOME
        }
    }

    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
            showToast("Order $orderId status updated to ${status.label}")
        }
    }

    fun toggleDishAvailability(id: String, currentAvailable: Boolean) {
        viewModelScope.launch {
            repository.toggleItemAvailability(id, !currentAvailable)
            showToast("Item availability updated")
        }
    }

    fun addNewDish(item: MenuItem) {
        viewModelScope.launch {
            repository.addMenuItem(item)
            showToast("Dish added to menu!")
        }
    }

    fun updateDish(item: MenuItem) {
        viewModelScope.launch {
            repository.updateMenuItem(item)
            showToast("Dish updated successfully!")
        }
    }

    fun deleteDish(item: MenuItem) {
        viewModelScope.launch {
            repository.deleteMenuItem(item)
            showToast("Dish removed from menu")
        }
    }

    fun updateAreaFee(areaName: String, fee: Double, min: Double, isAvailable: Boolean) {
        repository.updateAreaFee(areaName, fee, min, isAvailable)
        showToast("Delivery area settings updated")
    }

    // Shared Notifications / Toast events
    private val _toastEvents = MutableSharedFlow<String>()
    val toastEvents: SharedFlow<String> = _toastEvents.asSharedFlow()

    private fun showToast(msg: String) {
        viewModelScope.launch {
            _toastEvents.emit(msg)
        }
    }

    // Helper parser for portion sizes
    fun parsePortions(json: String): List<PortionOption> {
        return try {
            val list = mutableListOf<PortionOption>()
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    PortionOption(
                        name = obj.getString("name"),
                        additionalPrice = obj.optDouble("additionalPrice", 0.0)
                    )
                )
            }
            if (list.isEmpty()) listOf(PortionOption("Regular", 0.0)) else list
        } catch (e: Exception) {
            listOf(PortionOption("Regular", 0.0))
        }
    }
}
