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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomerProfile
import com.example.data.model.RestaurantConstant
import com.example.ui.theme.CrimsonRedPrimary
import com.example.ui.theme.HalalGreen
import com.example.ui.theme.SaffronOrange

@Composable
fun ProfileScreen(
    customerProfile: CustomerProfile?,
    isAdminMode: Boolean,
    onToggleAdminMode: (Boolean) -> Unit,
    onUpdateProfile: (CustomerProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isEditingProfile by remember { mutableStateOf(false) }

    var editName by remember(customerProfile) { mutableStateOf(customerProfile?.name ?: "Ahmed Al Maktoum") }
    var editPhone by remember(customerProfile) { mutableStateOf(customerProfile?.phone ?: "+971 50 123 4567") }
    var editEmail by remember(customerProfile) { mutableStateOf(customerProfile?.email ?: "ahmed.dubai@example.com") }

    // Expanded states for FAQs
    var faq1Open by remember { mutableStateOf(false) }
    var faq2Open by remember { mutableStateOf(false) }
    var faq3Open by remember { mutableStateOf(false) }
    var faq4Open by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen")
    ) {
        // Customer Profile Card
        item {
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(CrimsonRedPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (customerProfile?.name?.take(2) ?: "BJ").uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = customerProfile?.name ?: "Ahmed Al Maktoum",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = customerProfile?.phone ?: "+971 50 123 4567",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = customerProfile?.email ?: "customer@dubai.ae",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = { isEditingProfile = !isEditingProfile },
                            modifier = Modifier.testTag("edit_profile_toggle")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = CrimsonRedPrimary
                            )
                        }
                    }

                    if (isEditingProfile) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Name") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = editPhone,
                            onValueChange = { editPhone = it },
                            label = { Text("Phone") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = editEmail,
                            onValueChange = { editEmail = it },
                            label = { Text("Email") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                customerProfile?.let {
                                    onUpdateProfile(
                                        it.copy(name = editName, phone = editPhone, email = editEmail)
                                    )
                                }
                                isEditingProfile = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("SAVE PROFILE")
                        }
                    }
                }
            }
        }

        // Restaurant Staff & Admin Mode Toggle
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAdminMode) CrimsonRedPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(CrimsonRedPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Restaurant Staff & Admin Portal",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Manage live kitchen orders, menu pricing & sales analytics",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = isAdminMode,
                        onCheckedChange = { onToggleAdminMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CrimsonRedPrimary,
                            checkedTrackColor = CrimsonRedPrimary.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.testTag("admin_mode_toggle_switch")
                    )
                }
            }
        }

        // Restaurant Official Info Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Baba Ji Restaurant Dubai Info",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = CrimsonRedPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = RestaurantConstant.RESTAURANT_ADDRESS,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = SaffronOrange,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "11:00 AM - 02:00 AM Daily (Late Night Delivery)",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = HalalGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "100% Halal Meat & Fresh Poultry Daily",
                            fontSize = 12.sp,
                            color = HalalGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${RestaurantConstant.RESTAURANT_PHONE}")
                                }
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Restaurant", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val url = "https://api.whatsapp.com/send?phone=${RestaurantConstant.RESTAURANT_WHATSAPP}&text=Hello%20Baba%20Ji%20Restaurant,%20I%20have%20an%20inquiry."
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HalalGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // FAQs Section
        item {
            Text(
                text = "Frequently Asked Questions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { faq1Open = !faq1Open }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Is all food 100% Halal certified?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Icon(
                            imageVector = if (faq1Open) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    if (faq1Open) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Yes, absolutely. All beef, mutton, and chicken supplied to Baba Ji Restaurant are 100% Halal certified by UAE food authorities.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { faq2Open = !faq2Open }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "What are the delivery charges across Dubai?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Icon(
                            imageVector = if (faq2Open) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    if (faq2Open) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Delivery charges range from AED 5 in Deira and Bur Dubai to AED 12-16 for Marina and Downtown. Orders above AED 100 or with code DUBAIFREE qualify for free delivery!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { faq3Open = !faq3Open }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Do you accept Cash on Delivery (COD)?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Icon(
                            imageVector = if (faq3Open) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    if (faq3Open) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Yes, we gladly offer Cash on Delivery without requiring any online card payment, as well as card machine on delivery.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { faq4Open = !faq4Open }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Can I order catering for Dubai events?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Icon(
                            imageVector = if (faq4Open) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    if (faq4Open) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Yes! We cater weddings, office lunches, and family gatherings. Reach out via WhatsApp on +971523257028 for customized catering packages.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
