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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.ui.theme.CrimsonRedPrimary
import com.example.ui.theme.HalalGreen
import com.example.ui.theme.SaffronOrange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyOrdersScreen(
    orders: List<Order>,
    onTrackOrder: (String) -> Unit,
    onBrowseMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("my_orders_screen")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Order History & Tracking",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Track current orders or re-order your favorite Baba Ji meals",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (orders.isEmpty()) {
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
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = CrimsonRedPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No Orders Yet",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Your placed orders will appear here for live tracking and past receipts.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onBrowseMenu,
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("START ORDERING")
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(orders, key = { it.id }) { order ->
                    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                    val formattedDate = dateFormat.format(Date(order.timestamp))
                    val isLiveOrder = order.status != OrderStatus.DELIVERED && order.status != OrderStatus.CANCELLED

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTrackOrder(order.id) }
                            .testTag("order_card_${order.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Order #${order.id}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    if (isLiveOrder) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .background(HalalGreen, CircleShape)
                                        )
                                    }
                                }

                                Surface(
                                    color = when (order.status) {
                                        OrderStatus.DELIVERED -> HalalGreen
                                        OrderStatus.CANCELLED -> Color.Red
                                        else -> SaffronOrange
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = order.status.label,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = formattedDate,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = order.itemsSummary,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 2
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "AED ${"%.2f".format(order.totalAmount)}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        color = CrimsonRedPrimary
                                    )
                                    Text(
                                        text = order.paymentMethod,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Button(
                                    onClick = { onTrackOrder(order.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isLiveOrder) CrimsonRedPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = if (isLiveOrder) Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (isLiveOrder) "Track Live 🛵" else "View Details",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
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
