package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.DefaultMenuData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Baba Ji Restaurant", appName)
  }

  @Test
  fun `verify menu items are populated with required Pakistani dishes`() {
    val dishes = DefaultMenuData.sampleDishes
    assertTrue(dishes.isNotEmpty())
    val biryani = dishes.find { it.name == "Chicken Biryani" }
    assertNotNull(biryani)
    assertEquals("Pakistani Food", biryani?.category)
    assertEquals(24.0, biryani?.price ?: 0.0, 0.01)

    val karahi = dishes.find { it.name == "Chicken Karahi" }
    assertNotNull(karahi)
    assertEquals(38.0, karahi?.price ?: 0.0, 0.01)
  }

  @Test
  fun `verify Dubai delivery areas and coupons`() {
    val areas = DefaultMenuData.dubaiDeliveryAreas
    assertTrue(areas.any { it.name == "Deira" })
    assertTrue(areas.any { it.name == "Dubai Marina" })

    val coupons = DefaultMenuData.defaultCoupons
    assertTrue(coupons.any { it.code == "BABAGI20" })
  }
}
