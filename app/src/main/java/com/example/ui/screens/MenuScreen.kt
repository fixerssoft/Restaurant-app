package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.getValue
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
import com.example.data.model.MenuItem
import com.example.ui.components.FoodDishCard
import com.example.ui.theme.CrimsonRedPrimary
import com.example.ui.theme.HalalGreen

@Composable
fun MenuScreen(
    menuItems: List<MenuItem>,
    selectedCategory: String,
    searchQuery: String,
    onCategorySelect: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onDishClick: (MenuItem) -> Unit,
    onQuickAdd: (MenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var vegOnlyFilter by remember { mutableStateOf(false) }
    var spicyOnlyFilter by remember { mutableStateOf(false) }

    val filteredList = menuItems.filter { item ->
        val vegMatch = if (vegOnlyFilter) item.isVeg else true
        val spicyMatch = if (spicyOnlyFilter) item.isSpicy else true
        vegMatch && spicyMatch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("menu_screen_root")
    ) {
        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search dishes, ingredients, or drinks...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
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
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("menu_search_input")
        )

        // Categories Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
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
                        .testTag("menu_cat_tab_$cat")
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

        // Dietary quick toggles
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (vegOnlyFilter) HalalGreen else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { vegOnlyFilter = !vegOnlyFilter }
            ) {
                Text(
                    text = if (vegOnlyFilter) "✓ Vegetarian" else "Vegetarian",
                    color = if (vegOnlyFilter) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (spicyOnlyFilter) CrimsonRedPrimary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { spicyOnlyFilter = !spicyOnlyFilter }
            ) {
                Text(
                    text = if (spicyOnlyFilter) "✓ Spicy Dishes" else "Spicy Dishes",
                    color = if (spicyOnlyFilter) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        // Dish List
        if (filteredList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No dishes found",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Try clearing search or filters to see delicious options.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList, key = { it.id }) { dish ->
                    FoodDishCard(
                        dish = dish,
                        onDishClick = { onDishClick(dish) },
                        onQuickAdd = { onQuickAdd(dish) }
                    )
                }
            }
        }
    }
}
