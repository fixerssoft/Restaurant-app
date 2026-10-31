package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DefaultMenuData
import com.example.data.model.DubaiArea
import com.example.data.model.MenuItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.ui.theme.CrimsonRedPrimary
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.HalalGreen
import com.example.ui.theme.SaffronOrange
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    orders: List<Order>,
    menuItems: List<MenuItem>,
    deliveryAreas: List<DubaiArea>,
    onUpdateOrderStatus: (String, OrderStatus) -> Unit,
    onToggleDishAvailability: (String, Boolean) -> Unit,
    onAddNewDish: (MenuItem) -> Unit,
    onUpdateDish: (MenuItem) -> Unit,
    onDeleteDish: (MenuItem) -> Unit,
    onUpdateAreaFee: (areaName: String, fee: Double, min: Double, isAvailable: Boolean) -> Unit,
    onExitAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Orders", "Menu", "Delivery", "Sales")

    // State for editing a dish
    var dishToEdit by remember { mutableStateOf<MenuItem?>(null) }
    var showAddDishDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Baba Ji Staff Portal", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text("Restaurant Admin & Kitchen", fontSize = 11.sp, color = CrimsonRedPrimary)
                    }
                },
                actions = {
                    TextButton(onClick = onExitAdmin, modifier = Modifier.testTag("admin_exit_button")) {
                        Text("Exit Portal", color = CrimsonRedPrimary, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            if (selectedTab == 1) {
                FloatingActionButton(
                    onClick = { showAddDishDialog = true },
                    containerColor = CrimsonRedPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("admin_add_dish_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Dish")
                }
            }
        },
        modifier = modifier.testTag("admin_dashboard_screen")
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) CrimsonRedPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> AdminOrdersTab(orders = orders, onUpdateStatus = onUpdateOrderStatus)
                1 -> AdminMenuTab(
                    menuItems = menuItems,
                    onToggleAvailability = onToggleDishAvailability,
                    onEditDish = { dishToEdit = it },
                    onDeleteDish = onDeleteDish
                )
                2 -> AdminDeliveryTab(deliveryAreas = deliveryAreas, onUpdateAreaFee = onUpdateAreaFee)
                3 -> AdminSalesTab(orders = orders, menuItems = menuItems)
            }
        }
    }

    // Add Dish Dialog
    if (showAddDishDialog) {
        DishEditDialog(
            initialDish = null,
            onSave = {
                onAddNewDish(it)
                showAddDishDialog = false
            },
            onDismiss = { showAddDishDialog = false }
        )
    }

    // Edit Dish Dialog
    dishToEdit?.let { dish ->
        DishEditDialog(
            initialDish = dish,
            onSave = {
                onUpdateDish(it)
                dishToEdit = null
            },
            onDismiss = { dishToEdit = null }
        )
    }
}

@Composable
fun AdminOrdersTab(
    orders: List<Order>,
    onUpdateStatus: (String, OrderStatus) -> Unit
) {
    var statusFilter by remember { mutableStateOf("ALL") }

    val filteredOrders = orders.filter { order ->
        when (statusFilter) {
            "ACTIVE" -> order.status != OrderStatus.DELIVERED && order.status != OrderStatus.CANCELLED
            "DELIVERED" -> order.status == OrderStatus.DELIVERED
            else -> true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("ALL", "ACTIVE", "DELIVERED").forEach { filter ->
                val isSelected = statusFilter == filter
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) CrimsonRedPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { statusFilter = filter }
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }

        if (filteredOrders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No orders in this filter", color = Color.Gray)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredOrders, key = { it.id }) { order ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Order #${order.id}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )

                                Surface(
                                    color = when (order.status) {
                                        OrderStatus.DELIVERED -> HalalGreen
                                        OrderStatus.CANCELLED -> Color.Red
                                        else -> SaffronOrange
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = order.status.label,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Customer: ${order.customerName} (${order.customerPhone})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "Address: ${order.buildingVilla}, ${order.apartmentNumber}, ${order.area}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Items: ${order.itemsSummary}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total: AED ${"%.2f".format(order.totalAmount)} (${order.paymentMethod})",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CrimsonRedPrimary,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(10.dp))

                            // Action buttons to advance status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                when (order.status) {
                                    OrderStatus.ORDER_RECEIVED -> {
                                        Button(
                                            onClick = { onUpdateStatus(order.id, OrderStatus.ORDER_CONFIRMED) },
                                            colors = ButtonDefaults.buttonColors(containerColor = SaffronOrange),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Confirm Order", fontSize = 11.sp)
                                        }
                                        Button(
                                            onClick = { onUpdateStatus(order.id, OrderStatus.CANCELLED) },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Reject", fontSize = 11.sp)
                                        }
                                    }
                                    OrderStatus.ORDER_CONFIRMED -> {
                                        Button(
                                            onClick = { onUpdateStatus(order.id, OrderStatus.PREPARING) },
                                            colors = ButtonDefaults.buttonColors(containerColor = SaffronOrange),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Start Cooking (Kitchen)", fontSize = 11.sp)
                                        }
                                    }
                                    OrderStatus.PREPARING -> {
                                        Button(
                                            onClick = { onUpdateStatus(order.id, OrderStatus.READY_FOR_DELIVERY) },
                                            colors = ButtonDefaults.buttonColors(containerColor = GoldenAmber),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Mark Ready & Packed", fontSize = 11.sp, color = Color.Black)
                                        }
                                    }
                                    OrderStatus.READY_FOR_DELIVERY -> {
                                        Button(
                                            onClick = { onUpdateStatus(order.id, OrderStatus.OUT_FOR_DELIVERY) },
                                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Handover to Rider (Out for Delivery)", fontSize = 11.sp)
                                        }
                                    }
                                    OrderStatus.OUT_FOR_DELIVERY -> {
                                        Button(
                                            onClick = { onUpdateStatus(order.id, OrderStatus.DELIVERED) },
                                            colors = ButtonDefaults.buttonColors(containerColor = HalalGreen),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Mark Delivered ✓", fontSize = 11.sp)
                                        }
                                    }
                                    else -> {
                                        Text(
                                            text = "Status finalized",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMenuTab(
    menuItems: List<MenuItem>,
    onToggleAvailability: (String, Boolean) -> Unit,
    onEditDish: (MenuItem) -> Unit,
    onDeleteDish: (MenuItem) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(menuItems, key = { it.id }) { dish ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = dish.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${dish.category})",
                                fontSize = 11.sp,
                                color = CrimsonRedPrimary
                            )
                        }

                        Text(
                            text = "AED ${"%.2f".format(dish.price)}",
                            fontWeight = FontWeight.ExtraBold,
                            color = CrimsonRedPrimary,
                            fontSize = 13.sp
                        )

                        Text(
                            text = if (dish.isAvailable) "✓ In Stock" else "✗ Sold Out",
                            fontSize = 11.sp,
                            color = if (dish.isAvailable) HalalGreen else Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = dish.isAvailable,
                            onCheckedChange = { onToggleAvailability(dish.id, dish.isAvailable) },
                            colors = SwitchDefaults.colors(checkedThumbColor = HalalGreen)
                        )

                        IconButton(onClick = { onEditDish(dish) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CrimsonRedPrimary)
                        }

                        IconButton(onClick = { onDeleteDish(dish) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDeliveryTab(
    deliveryAreas: List<DubaiArea>,
    onUpdateAreaFee: (String, Double, Double, Boolean) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Dubai Delivery Districts & Fees",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Adjust delivery charges and minimum order values per district",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(deliveryAreas) { area ->
            var feeText by remember(area.deliveryFee) { mutableStateOf(area.deliveryFee.toInt().toString()) }
            var minText by remember(area.minOrderAmount) { mutableStateOf(area.minOrderAmount.toInt().toString()) }
            var isAvail by remember(area.isAvailable) { mutableStateOf(area.isAvailable) }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = area.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (isAvail) "Active" else "Closed", fontSize = 11.sp, color = if (isAvail) HalalGreen else Color.Red)
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = isAvail,
                                onCheckedChange = {
                                    isAvail = it
                                    onUpdateAreaFee(area.name, feeText.toDoubleOrNull() ?: area.deliveryFee, minText.toDoubleOrNull() ?: area.minOrderAmount, isAvail)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = feeText,
                            onValueChange = {
                                feeText = it
                                val fee = it.toDoubleOrNull()
                                if (fee != null) {
                                    onUpdateAreaFee(area.name, fee, minText.toDoubleOrNull() ?: area.minOrderAmount, isAvail)
                                }
                            },
                            label = { Text("Fee (AED)", fontSize = 10.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = minText,
                            onValueChange = {
                                minText = it
                                val min = it.toDoubleOrNull()
                                if (min != null) {
                                    onUpdateAreaFee(area.name, feeText.toDoubleOrNull() ?: area.deliveryFee, min, isAvail)
                                }
                            },
                            label = { Text("Min Order (AED)", fontSize = 10.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSalesTab(
    orders: List<Order>,
    menuItems: List<MenuItem>
) {
    val totalRevenue = orders.sumOf { it.totalAmount }
    val todayRevenue = orders.filter { System.currentTimeMillis() - it.timestamp < 24 * 60 * 60 * 1000 }.sumOf { it.totalAmount }
    val weeklyRevenue = orders.filter { System.currentTimeMillis() - it.timestamp < 7 * 24 * 60 * 60 * 1000 }.sumOf { it.totalAmount }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Sales & Kitchen Performance 📊",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CrimsonRedPrimary),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Today's Sales", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                        Text(
                            "AED ${"%.2f".format(todayRevenue)}",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SaffronOrange),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Total Orders", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                        Text(
                            "${orders.size}",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Revenue Highlights", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("7-Day Sales:", fontSize = 12.sp)
                        Text("AED ${"%.2f".format(weeklyRevenue)}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Lifetime App Revenue:", fontSize = 12.sp)
                        Text("AED ${"%.2f".format(totalRevenue)}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CrimsonRedPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun DishEditDialog(
    initialDish: MenuItem?,
    onSave: (MenuItem) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialDish?.name ?: "") }
    var category by remember { mutableStateOf(initialDish?.category ?: "Pakistani Food") }
    var priceText by remember { mutableStateOf(initialDish?.price?.toString() ?: "25.0") }
    var description by remember { mutableStateOf(initialDish?.description ?: "") }
    var imageUrl by remember { mutableStateOf(initialDish?.imageUrl ?: "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=800&q=80") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (initialDish == null) "Add New Dish" else "Edit Dish: ${initialDish.name}")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Dish Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Price (AED)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Image URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = priceText.toDoubleOrNull() ?: 20.0
                    val dish = initialDish?.copy(
                        name = name,
                        category = category,
                        price = price,
                        description = description,
                        imageUrl = imageUrl
                    ) ?: MenuItem(
                        id = "dish_${UUID.randomUUID().toString().take(6)}",
                        name = name,
                        category = category,
                        price = price,
                        description = description,
                        imageUrl = imageUrl
                    )
                    onSave(dish)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary)
            ) {
                Text("Save Dish")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
