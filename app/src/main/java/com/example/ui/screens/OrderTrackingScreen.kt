package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.RestaurantConstant
import com.example.ui.theme.CharcoalDark
import com.example.ui.theme.CharcoalMedium
import com.example.ui.theme.CrimsonRedPrimary
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.HalalGreen
import com.example.ui.theme.SaffronOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    order: Order,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentStep = order.status.stepIndex

    val transition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Live Order Tracking",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "Order ID: ${order.id}",
                            fontSize = 12.sp,
                            color = CrimsonRedPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("track_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier.testTag("order_tracking_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Live Status Hero Banner
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = when (order.status) {
                                OrderStatus.DELIVERED -> HalalGreen
                                OrderStatus.CANCELLED -> Color.Red
                                else -> SaffronOrange
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = order.status.label.uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = CrimsonRedPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ETA: ${order.estimatedArrival}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = CrimsonRedPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = order.status.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated Dubai Delivery Route Map Graphic
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val startX = 40f
                            val startY = size.height / 2
                            val endX = size.width - 40f
                            val endY = size.height / 2

                            // Draw dashed route line
                            drawLine(
                                color = Color.Gray.copy(alpha = 0.5f),
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = 6f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
                            )

                            // Active progress line
                            val progressFraction = when (order.status) {
                                OrderStatus.ORDER_RECEIVED -> 0.1f
                                OrderStatus.ORDER_CONFIRMED -> 0.25f
                                OrderStatus.PREPARING -> 0.45f
                                OrderStatus.READY_FOR_DELIVERY -> 0.65f
                                OrderStatus.OUT_FOR_DELIVERY -> 0.85f
                                OrderStatus.DELIVERED -> 1.0f
                                OrderStatus.CANCELLED -> 0.0f
                            }

                            drawLine(
                                color = CrimsonRedPrimary,
                                start = Offset(startX, startY),
                                end = Offset(startX + (endX - startX) * progressFraction, startY),
                                strokeWidth = 8f
                            )
                        }

                        // Restaurant Origin Pin
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(CrimsonRedPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Customer Destination Pin
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(HalalGreen, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Moving Rider Bike Indicator
                        if (order.status == OrderStatus.OUT_FOR_DELIVERY || order.status == OrderStatus.READY_FOR_DELIVERY) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(36.dp)
                                    .background(SaffronOrange.copy(alpha = pulseAlpha), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ElectricBike,
                                    contentDescription = "Rider Bike",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Baba Ji Restaurant (Deira)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CharcoalMedium
                        )
                        Text(
                            text = "${order.area}, Dubai",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CharcoalMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Assigned Rider Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(CrimsonRedPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = order.riderName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Assigned Baba Ji Delivery Hero ⭐ 4.9",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${order.riderPhone}")
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .background(Color.White, CircleShape)
                                .size(36.dp)
                                .testTag("call_rider_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call Rider",
                                tint = CrimsonRedPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                val url = "https://api.whatsapp.com/send?phone=${RestaurantConstant.RESTAURANT_WHATSAPP}&text=Hello,%20checking%20status%20for%20order%20${order.id}."
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .background(Color.White, CircleShape)
                                .size(36.dp)
                                .testTag("whatsapp_rider_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "WhatsApp Rider",
                                tint = HalalGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 6-Step Status Timeline
            Text(
                text = "Order Progress Steps",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            val statuses = listOf(
                OrderStatus.ORDER_RECEIVED,
                OrderStatus.ORDER_CONFIRMED,
                OrderStatus.PREPARING,
                OrderStatus.READY_FOR_DELIVERY,
                OrderStatus.OUT_FOR_DELIVERY,
                OrderStatus.DELIVERED
            )

            statuses.forEachIndexed { index, st ->
                val isDone = currentStep >= st.stepIndex
                val isCurrent = currentStep == st.stepIndex

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                color = if (isDone) HalalGreen else Color.LightGray.copy(alpha = 0.5f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = st.label,
                            fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (isCurrent) CrimsonRedPrimary else if (isDone) MaterialTheme.colorScheme.onSurface else Color.Gray
                        )
                        if (isCurrent) {
                            Text(
                                text = st.description,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (index < statuses.size - 1) {
                    Box(
                        modifier = Modifier
                            .padding(start = 13.dp)
                            .width(2.dp)
                            .height(16.dp)
                            .background(if (currentStep > st.stepIndex) HalalGreen else Color.LightGray.copy(alpha = 0.5f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Order Details Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = order.itemsSummary,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery To:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${order.buildingVilla}, ${order.apartmentNumber}, ${order.area}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Payment Method:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(order.paymentMethod, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CrimsonRedPrimary)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Amount:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("AED ${"%.2f".format(order.totalAmount)}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = CrimsonRedPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
