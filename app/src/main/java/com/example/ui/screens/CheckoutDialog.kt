package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomerProfile
import com.example.data.model.DubaiArea
import com.example.ui.CartSummary
import com.example.ui.theme.CharcoalMedium
import com.example.ui.theme.CrimsonRedPrimary
import com.example.ui.theme.HalalGreen
import com.example.ui.theme.SaffronOrange
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutBottomSheet(
    cartSummary: CartSummary,
    customerProfile: CustomerProfile?,
    selectedArea: DubaiArea,
    deliveryAreas: List<DubaiArea>,
    onSelectArea: (DubaiArea) -> Unit,
    onPlaceOrder: (name: String, phone: String, area: String, building: String, apt: String, instructions: String, paymentMethod: String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    var name by remember(customerProfile) { mutableStateOf(customerProfile?.name ?: "Ahmed Al Maktoum") }
    var phone by remember(customerProfile) { mutableStateOf(customerProfile?.phone ?: "+971 50 123 4567") }
    var building by remember(customerProfile) { mutableStateOf(customerProfile?.buildingVilla ?: "Rigga Pearl Tower") }
    var aptNumber by remember(customerProfile) { mutableStateOf(customerProfile?.apartmentNumber ?: "Apt 504") }
    var instructions by remember(customerProfile) { mutableStateOf(customerProfile?.deliveryNotes ?: "Ring bell, leave with security if away") }

    // Payment method: "COD" vs "ONLINE"
    var paymentOption by remember { mutableStateOf("COD") }

    // Simulated card fields (Stripe / Telr gateway architecture)
    var cardName by remember { mutableStateOf("Ahmed Al Maktoum") }
    var cardNumber by remember { mutableStateOf("•••• •••• •••• 4242") }
    var cardExpiry by remember { mutableStateOf("12/28") }
    var cardCvv by remember { mutableStateOf("•••") }

    var isProcessingPayment by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.testTag("checkout_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Checkout Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Checkout & Delivery",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Baba Ji Restaurant • Dubai Delivery",
                            fontSize = 12.sp,
                            color = CrimsonRedPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Surface(
                        color = CrimsonRedPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "AED ${"%.2f".format(cartSummary.total)}",
                            fontWeight = FontWeight.ExtraBold,
                            color = CrimsonRedPrimary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 1. Customer Information
                Text(
                    text = "1. Customer Contact",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CrimsonRedPrimary) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_name_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("UAE Mobile Number (+971...)") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = CrimsonRedPrimary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_phone_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Dubai Delivery Address
                Text(
                    text = "2. Dubai Delivery Address",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Area Selection
                Text(
                    text = "Select Dubai Delivery District:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    deliveryAreas.take(3).forEach { area ->
                        val isSelected = selectedArea.name == area.name
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) CrimsonRedPrimary else MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSelectArea(area) }
                        ) {
                            Text(
                                text = area.name,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = building,
                    onValueChange = { building = it },
                    label = { Text("Building / Villa Name / Street") },
                    leadingIcon = { Icon(Icons.Default.Apartment, contentDescription = null, tint = CrimsonRedPrimary) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = aptNumber,
                    onValueChange = { aptNumber = it },
                    label = { Text("Apartment / Suite / Villa Number") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = CrimsonRedPrimary) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Delivery Landmark & Instructions") },
                    leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, tint = CrimsonRedPrimary) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Payment Method
                Text(
                    text = "3. Payment Method",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Option A: Cash on Delivery (COD)
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (paymentOption == "COD") CrimsonRedPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { paymentOption = "COD" }
                        .border(
                            width = if (paymentOption == "COD") 1.5.dp else 0.dp,
                            color = if (paymentOption == "COD") CrimsonRedPrimary else Color.Transparent,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .testTag("payment_option_cod")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = paymentOption == "COD",
                            onClick = { paymentOption = "COD" },
                            colors = RadioButtonDefaults.colors(selectedColor = CrimsonRedPrimary)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.Money,
                            contentDescription = null,
                            tint = CrimsonRedPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Cash on Delivery (COD)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Pay with cash or card to the rider upon delivery at your doorstep",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Option B: Online Payment Gateway (Stripe / Telr UAE)
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (paymentOption == "ONLINE") CrimsonRedPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { paymentOption = "ONLINE" }
                        .border(
                            width = if (paymentOption == "ONLINE") 1.5.dp else 0.dp,
                            color = if (paymentOption == "ONLINE") CrimsonRedPrimary else Color.Transparent,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .testTag("payment_option_online")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = paymentOption == "ONLINE",
                                onClick = { paymentOption = "ONLINE" },
                                colors = RadioButtonDefaults.colors(selectedColor = CrimsonRedPrimary)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(
                                imageVector = Icons.Default.CreditCard,
                                contentDescription = null,
                                tint = CrimsonRedPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Online Card Payment (Stripe / Telr UAE)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                                Text(
                                    text = "Supports Visa, Mastercard & Apple Pay. 256-bit encrypted SSL.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (paymentOption == "ONLINE") {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = HalalGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Secure UAE Payment Gateway Gateway Tokenization",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = HalalGreen
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = cardName,
                                        onValueChange = { cardName = it },
                                        label = { Text("Name on Card", fontSize = 11.sp) },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    OutlinedTextField(
                                        value = cardNumber,
                                        onValueChange = { cardNumber = it },
                                        label = { Text("Card Number", fontSize = 11.sp) },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedTextField(
                                            value = cardExpiry,
                                            onValueChange = { cardExpiry = it },
                                            label = { Text("MM/YY", fontSize = 11.sp) },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f)
                                        )
                                        OutlinedTextField(
                                            value = cardCvv,
                                            onValueChange = { cardCvv = it },
                                            label = { Text("CVV", fontSize = 11.sp) },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = CrimsonRedPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Bottom Confirm Button
            Surface(
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            if (name.isBlank() || phone.isBlank() || building.isBlank()) {
                                errorMessage = "Please enter your name, phone and building details."
                                return@Button
                            }

                            errorMessage = null
                            val paymentMethodLabel = if (paymentOption == "COD") {
                                "Cash on Delivery (COD)"
                            } else {
                                "Online Card (Stripe/Telr Gateway)"
                            }

                            if (paymentOption == "ONLINE") {
                                isProcessingPayment = true
                                coroutineScope.launch {
                                    // Process encrypted gateway simulation
                                    delay(1200)
                                    isProcessingPayment = false
                                    onPlaceOrder(name, phone, selectedArea.name, building, aptNumber, instructions, paymentMethodLabel)
                                }
                            } else {
                                onPlaceOrder(name, phone, selectedArea.name, building, aptNumber, instructions, paymentMethodLabel)
                            }
                        },
                        enabled = !isProcessingPayment,
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("confirm_order_button")
                    ) {
                        if (isProcessingPayment) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Authorizing Payment...", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        } else {
                            Text(
                                text = "CONFIRM ORDER (AED ${"%.2f".format(cartSummary.total)})",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
