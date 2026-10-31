package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CartItem
import com.example.data.model.CustomerProfile
import com.example.data.model.MenuItem
import com.example.data.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_items ORDER BY name ASC")
    fun getAllMenuItems(): Flow<List<MenuItem>>

    @Query("SELECT * FROM menu_items WHERE category = :category ORDER BY name ASC")
    fun getMenuItemsByCategory(category: String): Flow<List<MenuItem>>

    @Query("SELECT * FROM menu_items WHERE id = :id LIMIT 1")
    suspend fun getMenuItemById(id: String): MenuItem?

    @Query("SELECT * FROM menu_items WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchMenuItems(query: String): Flow<List<MenuItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItems(items: List<MenuItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(item: MenuItem)

    @Update
    suspend fun updateMenuItem(item: MenuItem)

    @Delete
    suspend fun deleteMenuItem(item: MenuItem)

    @Query("UPDATE menu_items SET isAvailable = :isAvailable WHERE id = :id")
    suspend fun updateAvailability(id: String, isAvailable: Boolean)

    @Query("SELECT COUNT(*) FROM menu_items")
    suspend fun countMenuItems(): Int
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Update
    suspend fun updateCartItem(item: CartItem)

    @Delete
    suspend fun deleteCartItem(item: CartItem)

    @Query("DELETE FROM cart_items WHERE cartItemId = :cartItemId")
    suspend fun deleteById(cartItemId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    fun getOrderById(orderId: String): Flow<Order?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteOrder(orderId: String)
}

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customer_profile WHERE id = 'current_user' LIMIT 1")
    fun getProfile(): Flow<CustomerProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: CustomerProfile)
}
