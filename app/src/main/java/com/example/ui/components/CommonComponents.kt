package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.CartItem
import com.example.data.model.DubaiArea
import com.example.data.model.MenuItem
import com.example.data.model.RestaurantConstant
import com.example.ui.NavDestination
import com.example.ui.theme.CharcoalDark
import com.example.ui.theme.CharcoalMedium
import com.example.ui.theme.CrimsonRedPrimary
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.HalalGreen
import com.example.ui.theme.SaffronOrange
import com.example.ui.theme.SpicyRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabaJiTopBar(
    selectedArea: DubaiArea,
    cartItemCount: Int,
    onAreaClick: () -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onAreaClick() }
                    .padding(vertical = 4.dp, horizontal = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(CrimsonRedPrimary, SaffronOrange)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Baba Ji Restaurant",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = CrimsonRedPrimary
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = SaffronOrange,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${selectedArea.name}, Dubai",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = CharcoalMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select Area",
                            tint = CharcoalMedium,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        },
        actions = {
            // Quick Call Button
            IconButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${RestaurantConstant.RESTAURANT_PHONE}")
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.testTag("top_bar_call_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call Restaurant",
                    tint = CrimsonRedPrimary
                )
            }

            // Cart Icon Button with Badged Count
            IconButton(
                onClick = onCartClick,
                modifier = Modifier.testTag("top_bar_cart_button")
            ) {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge(
                                containerColor = CrimsonRedPrimary,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = cartItemCount.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Cart",
                        tint = if (cartItemCount > 0) CrimsonRedPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun BabaJiBottomNavigationBar(
    currentDestination: NavDestination,
    onNavigate: (NavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = currentDestination == NavDestination.HOME,
            onClick = { onNavigate(NavDestination.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CrimsonRedPrimary,
                selectedTextColor = CrimsonRedPrimary,
                indicatorColor = CrimsonRedPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("bottom_nav_home")
        )

        NavigationBarItem(
            selected = currentDestination == NavDestination.MENU,
            onClick = { onNavigate(NavDestination.MENU) },
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Menu") },
            label = { Text("Menu", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CrimsonRedPrimary,
                selectedTextColor = CrimsonRedPrimary,
                indicatorColor = CrimsonRedPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("bottom_nav_menu")
        )

        NavigationBarItem(
            selected = currentDestination == NavDestination.ORDERS,
            onClick = { onNavigate(NavDestination.ORDERS) },
            icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Orders") },
            label = { Text("Orders", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CrimsonRedPrimary,
                selectedTextColor = CrimsonRedPrimary,
                indicatorColor = CrimsonRedPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("bottom_nav_orders")
        )

        NavigationBarItem(
            selected = currentDestination == NavDestination.OFFERS,
            onClick = { onNavigate(NavDestination.OFFERS) },
            icon = { Icon(Icons.Default.Whatshot, contentDescription = "Offers") },
            label = { Text("Offers", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CrimsonRedPrimary,
                selectedTextColor = CrimsonRedPrimary,
                indicatorColor = CrimsonRedPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("bottom_nav_offers")
        )

        NavigationBarItem(
            selected = currentDestination == NavDestination.PROFILE,
            onClick = { onNavigate(NavDestination.PROFILE) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CrimsonRedPrimary,
                selectedTextColor = CrimsonRedPrimary,
                indicatorColor = CrimsonRedPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("bottom_nav_profile")
        )
    }
}

@Composable
fun FoodDishCard(
    dish: MenuItem,
    onDishClick: () -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDishClick() }
            .testTag("dish_card_${dish.id}")
    ) {
        Column {
            // Food Image with overlay tags
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(dish.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = dish.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Top badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (dish.isBestseller) {
                        Surface(
                            color = SaffronOrange,
                            shape = RoundedCornerShape(6.dp),
                            shadowElevation = 2.dp
                        ) {
                            Text(
                                text = "BESTSELLER",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    } else if (dish.isPopular) {
                        Surface(
                            color = CrimsonRedPrimary,
                            shape = RoundedCornerShape(6.dp),
                            shadowElevation = 2.dp
                        ) {
                            Text(
                                text = "POPULAR",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // Rating badge
                    Surface(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = GoldenAmber,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "%.1f".format(dish.rating),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Availability overlay if unavailable
                if (!dish.isAvailable) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.65f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = Color.Red.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "CURRENTLY SOLD OUT",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Dish Details Content
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dish.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // Veg or Non-Veg Halal Icon
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(
                                color = if (dish.isVeg) HalalGreen else SpicyRed,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = dish.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Price and Add button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AED ${"%.2f".format(dish.price)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = CrimsonRedPrimary
                        )
                    }

                    Button(
                        onClick = {
                            if (dish.isAvailable) {
                                onDishClick()
                            }
                        },
                        enabled = dish.isAvailable,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CrimsonRedPrimary,
                            disabledContainerColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 14.dp,
                            vertical = 6.dp
                        ),
                        modifier = Modifier.testTag("add_dish_${dish.id}")
                    ) {
                        Text(
                            text = if (dish.isAvailable) "ADD +" else "UNAVAILABLE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FloatingCartBanner(
    itemCount: Int,
    totalAed: Double,
    onViewCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = itemCount > 0,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
    ) {
        Surface(
            color = CrimsonRedPrimary,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onViewCart() }
                .testTag("floating_cart_banner")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$itemCount",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "View Basket",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "AED ${"%.2f".format(totalAed)} (incl. VAT)",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Checkout",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
