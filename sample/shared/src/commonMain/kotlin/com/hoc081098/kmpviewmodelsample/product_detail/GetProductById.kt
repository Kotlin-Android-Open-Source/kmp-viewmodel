@file:Suppress("PackageNaming")

package com.hoc081098.kmpviewmodelsample.product_detail

import com.hoc081098.kmpviewmodelsample.AppDispatchers
import com.hoc081098.kmpviewmodelsample.FakeProductsJson
import com.hoc081098.kmpviewmodelsample.ProductItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class GetProductById(
  private val appDispatchers: AppDispatchers,
) {
  suspend operator fun invoke(id: Int): ProductItem = withContext(appDispatchers.io) {
    @Suppress("MagicNumber")
    delay(2_000)

    Json
      .decodeFromString<List<ProductItem>>(FakeProductsJson)
      .find { it.id == id }
      ?:
      @Suppress("TooGenericExceptionThrown")
      throw RuntimeException("Product with id = $id not found")
  }
}
