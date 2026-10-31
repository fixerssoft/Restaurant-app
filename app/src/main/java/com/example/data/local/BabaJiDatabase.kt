package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CartItem
import com.example.data.model.CustomerProfile
import com.example.data.model.DefaultMenuData
import com.example.data.model.MenuItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Converters {
    @TypeConverter
    fun fromOrderStatus(value: OrderStatus): String = value.name

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus = try {
        OrderStatus.valueOf(value)
    } catch (e: Exception) {
        OrderStatus.ORDER_RECEIVED
    }
}

@Database(
    entities = [MenuItem::class, CartItem::class, Order::class, CustomerProfile::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BabaJiDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun customerDao(): CustomerDao

    companion object {
        @Volatile
        private var INSTANCE: BabaJiDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BabaJiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BabaJiDatabase::class.java,
                    "babaji_restaurant_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database)
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        if (database.menuItemDao().countMenuItems() == 0) {
                            populateDatabase(database)
                        }
                    }
                }
            }

            suspend fun populateDatabase(database: BabaJiDatabase) {
                val menuItemDao = database.menuItemDao()
                val customerDao = database.customerDao()

                // Insert full menu
                menuItemDao.insertMenuItems(DefaultMenuData.sampleDishes)

                // Insert default profile
                customerDao.saveProfile(
                    CustomerProfile(
                        id = "current_user",
                        name = "Ahmed Al Maktoum",
                        phone = "+971 50 123 4567",
                        email = "ahmed.dubai@example.com",
                        selectedArea = "Deira",
                        buildingVilla = "Rigga Pearl Tower",
                        apartmentNumber = "Apt 504",
                        deliveryNotes = "Call on arrival at the security desk",
                        isLoggedIn = true
                    )
                )

                // Insert a sample previous order so customer can see live order tracking immediately
                database.orderDao().insertOrder(
                    Order(
                        id = "BJ-78214",
                        customerName = "Ahmed Al Maktoum",
                        customerPhone = "+971 50 123 4567",
                        area = "Deira",
                        buildingVilla = "Rigga Pearl Tower",
                        apartmentNumber = "Apt 504",
                        deliveryInstructions = "Near Al Rigga Metro Station, Gate 2",
                        itemsSummary = "1x Chicken Biryani (Family Pack), 2x Garlic Naan, 1x Chicken Tikka, 2x Mango Juice",
                        subtotal = 93.0,
                        deliveryFee = 5.0,
                        vatAmount = 4.65,
                        discountAmount = 15.0,
                        totalAmount = 87.65,
                        paymentMethod = "Cash on Delivery (COD)",
                        status = OrderStatus.OUT_FOR_DELIVERY,
                        timestamp = System.currentTimeMillis() - 15 * 60 * 1000,
                        riderName = "Tariq Mahmood",
                        riderPhone = "+971 52 325 7028",
                        estimatedArrival = "15-20 min"
                    )
                )
            }
        }
    }
}
