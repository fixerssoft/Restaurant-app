package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.data.model.AddOnOption
import com.example.data.model.MenuItem
import com.example.data.model.PortionOption
import com.example.ui.RestaurantViewModel
import com.example.ui.theme.CharcoalMedium
import com.example.ui.theme.CrimsonRedPrimary
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.HalalGreen
import com.example.ui.theme.SaffronOrange
import com.example.ui.theme.SpicyRed
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailsBottomSheet(
    dish: MenuItem?,
    selectedPortionIndex: Int,
    selectedSpiceLevel: String,
    selectedAddOns: Set<String>,
    quantity: Int,
    specialInstructions: String,
    onPortionSelected: (Int) -> Unit,
    onSpiceSelected: (String) -> Unit,
    onAddOnToggled: (String) -> Unit,
    onQuantityAdjust: (Int) -> Unit,
    onSpecialInstructionsChange: (String) -> Unit,
    onAddToCart: () -> Unit,
    onDismiss: () -> Unit
) {
    if (dish == null) return

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val portions = remember(dish.portionSizesJson) {
        parsePortionsFromJson(dish.portionSizesJson)
    }

    val addOns = remember(dish.addOnsJson) {
        parseAddOnsFromJson(dish.addOnsJson)
    }

    val portionDelta = portions.getOrNull(selectedPortionIndex)?.additionalPrice ?: 0.0
    val addOnsPrice = addOns.filter { selectedAddOns.contains(it.name) }.sumOf { it.price }
    val unitPrice = dish.price + portionDelta + addOnsPrice
    val totalPrice = unitPrice * quantity

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.testTag("food_details_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Large Food Image with close button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(dish.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = dish.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .size(36.dp)
                            .testTag("details_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Halal tag
                    Surface(
                        color = HalalGreen,
                        shape = RoundedCornerShape(bottomEnd = 12.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "100% CERTIFIED HALAL",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                // Title & Price Section
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = dish.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = dish.category,
                                color = CrimsonRedPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            text = "AED ${"%.2f".format(dish.price)}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = CrimsonRedPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating & prep time
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = GoldenAmber.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldenAmber,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${dish.rating} (${dish.reviewsCount}+ ratings)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = CharcoalMedium,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = dish.preparationTime,
                                fontSize = 12.sp,
                                color = CharcoalMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = dish.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }

                // Portion Sizes
                if (portions.size > 1) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Select Portion Size",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        portions.forEachIndexed { index, option ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (selectedPortionIndex == index) CrimsonRedPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onPortionSelected(index) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = selectedPortionIndex == index,
                                            onClick = { onPortionSelected(index) },
                                            colors = RadioButtonDefaults.colors(selectedColor = CrimsonRedPrimary)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = option.name, fontWeight = FontWeight.Medium)
                                    }

                                    if (option.additionalPrice > 0) {
                                        Text(
                                            text = "+AED ${"%.2f".format(option.additionalPrice)}",
                                            color = CrimsonRedPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Spice Level Selector
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Choose Spice Level",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Mild", "Medium Spiced", "Extra Spicy").forEach { spice ->
                            val isSelected = selectedSpiceLevel == spice
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) CrimsonRedPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onSpiceSelected(spice) }
                            ) {
                                Text(
                                    text = spice,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Add-Ons Checkboxes
                if (addOns.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Add Extra Accompaniments",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        addOns.forEach { addOn ->
                            val isChecked = selectedAddOns.contains(addOn.name)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onAddOnToggled(addOn.name) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { onAddOnToggled(addOn.name) },
                                            colors = CheckboxDefaults.colors(checkedColor = CrimsonRedPrimary)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = addOn.name, fontSize = 13.sp)
                                    }

                                    Text(
                                        text = "+AED ${"%.2f".format(addOn.price)}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = CrimsonRedPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                // Special Cooking Instructions
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Special Cooking Instructions",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = specialInstructions,
                        onValueChange = onSpecialInstructionsChange,
                        placeholder = { Text("e.g. Less oil, separate green chilies, extra crisp naan...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 3
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bottom Sticky Bar: Quantity & Add to Cart
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quantity selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        IconButton(
                            onClick = { onQuantityAdjust(-1) },
                            enabled = quantity > 1,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease",
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = "$quantity",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        IconButton(
                            onClick = { onQuantityAdjust(1) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Add to Cart Button with total price
                    Button(
                        onClick = onAddToCart,
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRedPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("details_add_to_cart_button")
                    ) {
                        Text(
                            text = "Add to Basket • AED ${"%.2f".format(totalPrice)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

private fun parsePortionsFromJson(json: String): List<PortionOption> {
    return try {
        val list = mutableListOf<PortionOption>()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(PortionOption(obj.getString("name"), obj.optDouble("additionalPrice", 0.0)))
        }
        if (list.isEmpty()) listOf(PortionOption("Regular", 0.0)) else list
    } catch (e: Exception) {
        listOf(PortionOption("Regular", 0.0))
    }
}

private fun parseAddOnsFromJson(json: String): List<AddOnOption> {
    return try {
        val list = mutableListOf<AddOnOption>()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(AddOnOption(obj.getString("name"), obj.optDouble("price", 0.0)))
        }
        list
    } catch (e: Exception) {
        emptyList()
    }
}
