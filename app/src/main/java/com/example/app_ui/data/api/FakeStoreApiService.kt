package com.example.app_ui.data.api

import androidx.compose.ui.graphics.vector.Path
import com.example.app_ui.data.api.model.ApiProduct
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApiService {
    @GET("products/category/{category}")
    suspend fun getMensClothingProducts(
        @Path("category") category: String
    ): List<ApiProduct>
}

