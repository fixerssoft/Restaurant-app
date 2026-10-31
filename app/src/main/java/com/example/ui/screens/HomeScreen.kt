package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DefaultMenuData
import com.example.data.model.DubaiArea
import com.example.data.model.MenuItem
import com.example.data.model.RestaurantConstant
import com.example.ui.components.FoodDishCard
import com.example.ui.theme.CrimsonRedLight
import com.example.ui.theme.CrimsonRedPrimary
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.HalalGreen
import com.example.ui.theme.SaffronOrange

@Composable
fun HomeScreen(
    menuItems: List<MenuItem>,
    selectedCategory: String,
    searchQuery: String,
    selectedArea: DubaiArea,
    onCategorySelect: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onDishClick: (MenuItem) -> Unit,
    onQuickAdd: (MenuItem) -> Unit,
    onAreaSelectorClick: () -> Unit,
    onNavigateToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bestsellers = menuItems.filter { it.isBestseller }
    val bbqItems = menuItems.filter { it.category == "BBQ" }
    val pakistaniItems = menuItems.filter { it.category == "Pakistani Food" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Dubai Location Delivery info bar
        item {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAreaSelectorClick() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ElectricBike,
                            contentDescription = null,
                            tint = CrimsonRedPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Delivering to ${selectedArea.name}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = SaffronOrange,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedArea.estimatedMinutes,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = SaffronOrange
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• Fee: AED ${selectedArea.deliveryFee.toInt()}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 2. Search Field
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            "Search biryani, karahi, tikka, burgers...",
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = CrimsonRedPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CrimsonRedPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_input")
                )
            }
        }

        // 3. Hero Promo Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("hero_promo_banner")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(CrimsonRedPrimary, SaffronOrange, GoldenAmber)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                color = Color.White.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalOffer,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "USE CODE: BABAGI20",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }

                            Surface(
                                color = HalalGreen,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "100% HALAL",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Authentic Taste of Punjab & Karachi in Dubai",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 25.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Enjoy 20% OFF on all signature Biryani, Karahi & Coal Charred Grills.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = onNavigateToMenu,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = CrimsonRedPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("hero_order_now_button")
                        ) {
                            Text(
                                text = "EXPLORE MENU",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // 4. Food Categories Filter Chips
        item {
            Column(modifier = Modifier.padding(top = 20.dp)) {
                Text(
                    text = "Explore Categories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    val allCats = listOf("All") + DefaultMenuData.categories
                    items(allCats) { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) CrimsonRedPrimary else MaterialTheme.colorScheme.surface,
                            shadowElevation = if (isSelected) 3.dp else 1.dp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onCategorySelect(cat) }
                                .testTag("cat_chip_$cat")
                        ) {
                            Text(
                                text = cat,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // 5. Popular Bestsellers Section
        if (bestsellers.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(top = 24.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dubai Best Sellers 🔥",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "See All",
                            color = CrimsonRedPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { onNavigateToMenu() }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(bestsellers) { dish ->
                            FoodDishCard(
                                dish = dish,
                                onDishClick = { onDishClick(dish) },
                                onQuickAdd = { onQuickAdd(dish) },
                                modifier = Modifier.width(220.dp)
                            )
                        }
                    }
                }
            }
        }

        // 6. Direct WhatsApp & Phone Ordering Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(HalalGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Prefer to order on WhatsApp?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Direct chef inquiries & custom Dubai catering",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val url = "https://api.whatsapp.com/send?phone=${RestaurantConstant.RESTAURANT_WHATSAPP}&text=Hello%20Baba%20Ji%20Restaurant,%20I%20would%20like%20to%20place%20an%20order."
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HalalGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("whatsapp_order_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${RestaurantConstant.RESTAURANT_PHONE}")
                                }
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("call_order_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 7. Signature BBQ & Grills
        if (bbqItems.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Charcoal BBQ & Grills",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            items(bbqItems.take(4)) { dish ->
                FoodDishCard(
                    dish = dish,
                    onDishClick = { onDishClick(dish) },
                    onQuickAdd = { onQuickAdd(dish) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        // 8. Pakistani Curries & Biryani
        if (pakistaniItems.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp)) {
                    Text(
                        text = "Karahi & Biryani Specialities",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            items(pakistaniItems.take(4)) { dish ->
                FoodDishCard(
                    dish = dish,
                    onDishClick = { onDishClick(dish) },
                    onQuickAdd = { onQuickAdd(dish) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}
