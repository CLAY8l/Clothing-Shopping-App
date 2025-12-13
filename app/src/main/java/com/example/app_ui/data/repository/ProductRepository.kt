package com.example.app_ui.data.repository

import com.example.app_ui.data.api.RetrofitClient
import com.example.app_ui.data.api.model.ApiProduct
import com.example.app_ui.screens.ProductUi
import com.example.app_ui.R

class ProductRepository {
    private val apiService = RetrofitClient.apiService

    /**
     * Extract product type from title (e.g., "t-shirt", "jacket", "jeans", etc.)
     */
    private fun extractProductType(title: String, category: String): String {
        val titleLower = title.lowercase()
        val categoryLower = category.lowercase()
        
        // Check for common product types in title
        return when {
            titleLower.contains("t-shirt") || titleLower.contains("tshirt") || 
            titleLower.contains("tee") || titleLower.contains("shirt") -> "T-Shirt"
            titleLower.contains("jacket") -> "Jacket"
            titleLower.contains("jean") || titleLower.contains("denim") -> "Jeans"
            titleLower.contains("shoe") || titleLower.contains("sneaker") || 
            titleLower.contains("boot") -> "Shoes"
            titleLower.contains("sack") || titleLower.contains("backpack") || 
            titleLower.contains("bag") -> "Sack"
            titleLower.contains("pant") || titleLower.contains("trouser") -> "Jeans"
            titleLower.contains("hoodie") || titleLower.contains("sweatshirt") -> "Jacket"
            titleLower.contains("coat") -> "Jacket"
            else -> {
                // Fallback to category or default
                when {
                    categoryLower.contains("clothing") -> "T-Shirt" // Default for men's clothing
                    else -> "T-Shirt"
                }
            }
        }
    }

    suspend fun getMensClothingProducts(limit: Int = 4): List<ProductUi> {
        return try {
            val apiProducts = apiService.getMensClothingProducts("men's clothing")
            // Take only the first 4 products
            apiProducts.take(limit).mapIndexed { index, apiProduct ->
                // Extract product type from title
                val productType = extractProductType(apiProduct.title, apiProduct.category)
                
                // Map API product to ProductUi
                // Offset ID by 1000 to avoid conflicts with local products (IDs 1-4)
                ProductUi(
                    id = 1000 + apiProduct.id, // Offset to avoid ID conflicts
                    title = apiProduct.title,
                    subtitle = productType, // Use extracted product type instead of category
                    price = "$${String.format("%.2f", apiProduct.price)}",
                    rating = String.format("%.1f", apiProduct.rating.rate),
                    imageRes = R.drawable.img_product_green_tee, // Fallback drawable
                    imageUrl = apiProduct.image, // Use API image URL
                    isWishlisted = false
                )
            }
        } catch (e: Exception) {
            // Return empty list on error, or fallback to local products
            emptyList()
        }
    }
}

