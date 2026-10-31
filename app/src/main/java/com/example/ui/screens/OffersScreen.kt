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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Coupon
import com.example.data.model.DefaultMenuData
import com.example.ui.theme.CrimsonRedPrimary
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.HalalGreen
import com.example.ui.theme.SaffronOrange

@Composable
fun OffersScreen(
    onApplyCoupon: (String) -> Unit,
    onNavigateToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coupons = DefaultMenuData.defaultCoupons

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("offers_screen")
    ) {
        item {
            Column {
                Text(
                    text = "Deals & Promo Offers",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Save on Dubai's favorite authentic desi cuisine & fast food",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Hero Combo Deals Section
        item {
            Text(
                text = "Featured Combo Meals 🌟",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(CrimsonRedPrimary, SaffronOrange)
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "MOST POPULAR DUBAI COMBO",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Biryani & BBQ Solo Feast",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "1x Chicken Biryani + 1x Chicken Tikka Quarter + 1x Zeera Raita + 1x Soft Drink",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "AED 45.00",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AED 56.00",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 13.sp,
                                    style = androidx.compose.ui.text.TextStyle(
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    )
                                )
                            }

                            Button(
                                onClick = onNavigateToMenu,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = CrimsonRedPrimary
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Order Deal", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = HalalGreen,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "FAMILY FEAST (4-5 PERSONS)",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Text(
                            text = "Save 25%",
                            color = CrimsonRedPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Baba Ji Mega Family Platter Deal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "1x Jumbo BBQ Platter + 2x Chicken Biryani + 4x Butter Naan + 1x Large Salad & Raita + 4x Drinks",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AED 125.00",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = CrimsonRedPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AED 165.00",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                style = androidx.compose.ui.text.TextStyle(
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                )
                            )
                        }

                        Button(
                            onClick = onNavigateToMenu,
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Order Combo", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Active Promo Coupons Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Discount Coupons & Vouchers 🎟️",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(coupons) { coupon ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = CrimsonRedPrimary.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = coupon.code,
                                    color = CrimsonRedPrimary,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = coupon.description,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "Min. Order: AED ${coupon.minOrderAed.toInt()}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = { onApplyCoupon(coupon.code) },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Apply Code",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
