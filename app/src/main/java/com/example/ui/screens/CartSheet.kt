package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.CartItem
import com.example.data.model.Coupon
import com.example.ui.CartSummary
import com.example.ui.theme.CharcoalMedium
import com.example.ui.theme.CrimsonRedPrimary
import com.example.ui.theme.HalalGreen
import com.example.ui.theme.SaffronOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBottomSheet(
    cartSummary: CartSummary,
    appliedCoupon: Coupon?,
    onQuantityChange: (CartItem, Int) -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onClearCart: () -> Unit,
    onApplyCoupon: (String) -> Unit,
    onRemoveCoupon: () -> Unit,
    onProceedToCheckout: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var couponInput by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.testTag("cart_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = CrimsonRedPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Your Basket (${cartSummary.itemCount})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (cartSummary.items.isNotEmpty()) {
                    TextButton(onClick = onClearCart) {
                        Text("Clear All", color = CrimsonRedPrimary, fontSize = 12.sp)
                    }
                }
            }

            if (cartSummary.items.isEmpty()) {
                // Empty Cart State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = CrimsonRedPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Your Basket is Empty",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Craving Biryani, Karahi, or BBQ? Explore our menu and add your favorite dishes.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("BROWSE MENU")
                    }
                }
            } else {
                // Cart Items List
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartSummary.items, key = { it.cartItemId }) { item ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(item.imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = item.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )

                                    Text(
                                        text = "${item.portionName} • ${item.spiceLevel}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    if (item.selectedAddOnsJson.isNotBlank()) {
                                        Text(
                                            text = "+ ${item.selectedAddOnsJson}",
                                            fontSize = 10.sp,
                                            color = CrimsonRedPrimary
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "AED ${"%.2f".format(item.itemTotal)}",
                                        fontWeight = FontWeight.ExtraBold,
                                        color = CrimsonRedPrimary,
                                        fontSize = 13.sp
                                    )
                                }

                                // Quantity Controls
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                        .padding(2.dp)
                                ) {
                                    IconButton(
                                        onClick = { onQuantityChange(item, -1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (item.quantity == 1) Icons.Default.DeleteOutline else Icons.Default.Remove,
                                            contentDescription = "Decrease",
                                            tint = if (item.quantity == 1) CrimsonRedPrimary else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    Text(
                                        text = "${item.quantity}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp)
                                    )

                                    IconButton(
                                        onClick = { onQuantityChange(item, 1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Increase",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Coupon Section
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalOffer,
                                        contentDescription = null,
                                        tint = SaffronOrange,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Apply Promo Coupon",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                if (appliedCoupon != null) {
                                    Surface(
                                        color = HalalGreen.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 6.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = "✓ ${appliedCoupon.code} applied!",
                                                    color = HalalGreen,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp
                                                )
                                                Text(
                                                    text = appliedCoupon.description,
                                                    fontSize = 10.sp,
                                                    color = CharcoalMedium
                                                )
                                            }
                                            TextButton(onClick = onRemoveCoupon) {
                                                Text("Remove", color = CrimsonRedPrimary, fontSize = 11.sp)
                                            }
                                        }
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = couponInput,
                                            onValueChange = { couponInput = it.uppercase() },
                                            placeholder = { Text("e.g. BABAGI20", fontSize = 12.sp) },
                                            singleLine = true,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Button(
                                            onClick = {
                                                if (couponInput.isNotBlank()) {
                                                    onApplyCoupon(couponInput)
                                                }
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary)
                                        ) {
                                            Text("Apply", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    // Quick tags
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        listOf("BABAGI20", "DUBAIFREE", "WELCOME15").forEach { code ->
                                            Surface(
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.clickable { onApplyCoupon(code) }
                                            ) {
                                                Text(
                                                    text = code,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = CrimsonRedPrimary,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Bill Breakdown
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Order Bill Summary",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Items Subtotal", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("AED ${"%.2f".format(cartSummary.subtotal)}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Delivery Charges", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    if (cartSummary.deliveryFee == 0.0) {
                                        Text("FREE", fontSize = 12.sp, color = HalalGreen, fontWeight = FontWeight.Bold)
                                    } else {
                                        Text("AED ${"%.2f".format(cartSummary.deliveryFee)}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    }
                                }

                                if (cartSummary.discount > 0) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Promo Discount", fontSize = 12.sp, color = HalalGreen)
                                        Text("-AED ${"%.2f".format(cartSummary.discount)}", fontSize = 12.sp, color = HalalGreen, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("UAE VAT (5%)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("AED ${"%.2f".format(cartSummary.vatAmount)}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Total Payable", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(
                                        text = "AED ${"%.2f".format(cartSummary.total)}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 17.sp,
                                        color = CrimsonRedPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                // Bottom Action Buttons
                Surface(
                    tonalElevation = 6.dp,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Button(
                            onClick = onProceedToCheckout,
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("proceed_to_checkout_button")
                        ) {
                            Text(
                                text = "PROCEED TO CHECKOUT (AED ${"%.2f".format(cartSummary.total)})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Continue Adding Dishes", color = CrimsonRedPrimary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
