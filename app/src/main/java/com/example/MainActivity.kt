package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.NavDestination
import com.example.ui.RestaurantViewModel
import com.example.ui.components.BabaJiBottomNavigationBar
import com.example.ui.components.BabaJiTopBar
import com.example.ui.components.FloatingCartBanner
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CartBottomSheet
import com.example.ui.screens.CheckoutBottomSheet
import com.example.ui.screens.DubaiAreaPickerBottomSheet
import com.example.ui.screens.FoodDetailsBottomSheet
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.screens.MyOrdersScreen
import com.example.ui.screens.OffersScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.BabaJiTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BabaJiTheme {
                BabaJiApp()
            }
        }
    }
}

@Composable
fun BabaJiApp(
    viewModel: RestaurantViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect ViewModel states
    val currentDestination by viewModel.currentDestination.collectAsState()
    val menuItems by viewModel.menuItems.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val selectedDishForDetails by viewModel.selectedDishForDetails.collectAsState()
    val detailPortionIndex by viewModel.detailPortionIndex.collectAsState()
    val detailSpiceLevel by viewModel.detailSpiceLevel.collectAsState()
    val detailSelectedAddOns by viewModel.detailSelectedAddOns.collectAsState()
    val detailQuantity by viewModel.detailQuantity.collectAsState()
    val detailSpecialInstructions by viewModel.detailSpecialInstructions.collectAsState()

    val cartItems by viewModel.cartItems.collectAsState()
    val cartSummary by viewModel.cartSummary.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val selectedArea by viewModel.selectedArea.collectAsState()
    val deliveryAreas by viewModel.deliveryAreas.collectAsState()

    val customerProfile by viewModel.customerProfile.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val trackedOrderId by viewModel.trackedOrderId.collectAsState()
    val showOrderTrackingScreen by viewModel.showOrderTrackingScreen.collectAsState()

    val showCheckoutSheet by viewModel.showCheckoutSheet.collectAsState()
    val showCartSheet by viewModel.showCartSheet.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()

    var showAreaPicker by remember { mutableStateOf(false) }

    // Toast and feedback collector
    LaunchedEffect(Unit) {
        viewModel.toastEvents.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Back handling
    BackHandler(enabled = showOrderTrackingScreen || isAdminMode || currentDestination != NavDestination.HOME) {
        when {
            showOrderTrackingScreen -> viewModel.closeOrderTracking()
            isAdminMode -> viewModel.toggleAdminMode(false)
            currentDestination != NavDestination.HOME -> viewModel.navigateTo(NavDestination.HOME)
        }
    }

    if (showOrderTrackingScreen) {
        val trackedOrder = allOrders.find { it.id == trackedOrderId } ?: allOrders.firstOrNull()
        if (trackedOrder != null) {
            OrderTrackingScreen(
                order = trackedOrder,
                onBack = { viewModel.closeOrderTracking() }
            )
        } else {
            viewModel.closeOrderTracking()
        }
    } else if (isAdminMode && currentDestination == NavDestination.ADMIN) {
        AdminDashboardScreen(
            orders = allOrders,
            menuItems = menuItems,
            deliveryAreas = deliveryAreas,
            onUpdateOrderStatus = { id, status -> viewModel.updateOrderStatus(id, status) },
            onToggleDishAvailability = { id, avail -> viewModel.toggleDishAvailability(id, avail) },
            onAddNewDish = { viewModel.addNewDish(it) },
            onUpdateDish = { viewModel.updateDish(it) },
            onDeleteDish = { viewModel.deleteDish(it) },
            onUpdateAreaFee = { name, fee, min, avail -> viewModel.updateAreaFee(name, fee, min, avail) },
            onExitAdmin = { viewModel.toggleAdminMode(false) }
        )
    } else {
        Scaffold(
            topBar = {
                BabaJiTopBar(
                    selectedArea = selectedArea,
                    cartItemCount = cartSummary.itemCount,
                    onAreaClick = { showAreaPicker = true },
                    onCartClick = { viewModel.openCart() }
                )
            },
            bottomBar = {
                BabaJiBottomNavigationBar(
                    currentDestination = currentDestination,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentDestination) {
                    NavDestination.HOME -> {
                        HomeScreen(
                            menuItems = menuItems,
                            selectedCategory = selectedCategory,
                            searchQuery = searchQuery,
                            selectedArea = selectedArea,
                            onCategorySelect = { viewModel.selectCategory(it) },
                            onSearchChange = { viewModel.updateSearchQuery(it) },
                            onDishClick = { viewModel.openDishDetails(it) },
                            onQuickAdd = { viewModel.quickAddToCart(it) },
                            onAreaSelectorClick = { showAreaPicker = true },
                            onNavigateToMenu = { viewModel.navigateTo(NavDestination.MENU) }
                        )
                    }
                    NavDestination.MENU -> {
                        MenuScreen(
                            menuItems = menuItems,
                            selectedCategory = selectedCategory,
                            searchQuery = searchQuery,
                            onCategorySelect = { viewModel.selectCategory(it) },
                            onSearchChange = { viewModel.updateSearchQuery(it) },
                            onDishClick = { viewModel.openDishDetails(it) },
                            onQuickAdd = { viewModel.quickAddToCart(it) }
                        )
                    }
                    NavDestination.ORDERS -> {
                        MyOrdersScreen(
                            orders = allOrders,
                            onTrackOrder = { viewModel.setTrackedOrder(it) },
                            onBrowseMenu = { viewModel.navigateTo(NavDestination.MENU) }
                        )
                    }
                    NavDestination.OFFERS -> {
                        OffersScreen(
                            onApplyCoupon = { viewModel.applyCoupon(it) },
                            onNavigateToMenu = { viewModel.navigateTo(NavDestination.MENU) }
                        )
                    }
                    NavDestination.PROFILE -> {
                        ProfileScreen(
                            customerProfile = customerProfile,
                            isAdminMode = isAdminMode,
                            onToggleAdminMode = { viewModel.toggleAdminMode(it) },
                            onUpdateProfile = { viewModel.updateProfile(it) }
                        )
                    }
                    NavDestination.ADMIN -> {
                        // Handled above
                    }
                }

                // Floating Cart Banner if cart has items and neither sheet is shown
                if (cartSummary.itemCount > 0 && !showCartSheet && !showCheckoutSheet) {
                    FloatingCartBanner(
                        itemCount = cartSummary.itemCount,
                        totalAed = cartSummary.total,
                        onViewCart = { viewModel.openCart() },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }

    // Food Details Sheet
    if (selectedDishForDetails != null) {
        FoodDetailsBottomSheet(
            dish = selectedDishForDetails,
            selectedPortionIndex = detailPortionIndex,
            selectedSpiceLevel = detailSpiceLevel,
            selectedAddOns = detailSelectedAddOns,
            quantity = detailQuantity,
            specialInstructions = detailSpecialInstructions,
            onPortionSelected = { viewModel.setDetailPortionIndex(it) },
            onSpiceSelected = { viewModel.setDetailSpiceLevel(it) },
            onAddOnToggled = { viewModel.toggleDetailAddOn(it) },
            onQuantityAdjust = { viewModel.adjustDetailQuantity(it) },
            onSpecialInstructionsChange = { viewModel.setDetailSpecialInstructions(it) },
            onAddToCart = { viewModel.addCurrentDetailItemToCart() },
            onDismiss = { viewModel.closeDishDetails() }
        )
    }

    // Cart Sheet
    if (showCartSheet) {
        CartBottomSheet(
            cartSummary = cartSummary,
            appliedCoupon = appliedCoupon,
            onQuantityChange = { item, delta -> viewModel.updateCartItemQuantity(item, delta) },
            onRemoveItem = { viewModel.removeCartItem(it) },
            onClearCart = { viewModel.clearCart() },
            onApplyCoupon = { viewModel.applyCoupon(it) },
            onRemoveCoupon = { viewModel.removeCoupon() },
            onProceedToCheckout = {
                viewModel.closeCart()
                viewModel.openCheckout()
            },
            onDismiss = { viewModel.closeCart() }
        )
    }

    // Checkout Sheet
    if (showCheckoutSheet) {
        CheckoutBottomSheet(
            cartSummary = cartSummary,
            customerProfile = customerProfile,
            selectedArea = selectedArea,
            deliveryAreas = deliveryAreas,
            onSelectArea = { viewModel.selectDubaiArea(it) },
            onPlaceOrder = { name, phone, area, building, apt, instructions, paymentMethod ->
                viewModel.placeOrder(name, phone, area, building, apt, instructions, paymentMethod)
            },
            onDismiss = { viewModel.closeCheckout() }
        )
    }

    // Dubai Area Picker Sheet
    if (showAreaPicker) {
        DubaiAreaPickerBottomSheet(
            areas = deliveryAreas,
            selectedArea = selectedArea,
            onAreaSelected = { viewModel.selectDubaiArea(it) },
            onDismiss = { showAreaPicker = false }
        )
    }
}
