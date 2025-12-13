package com.example.app_ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app_ui.R
import com.example.app_ui.screens.ProductUi
import com.example.app_ui.utils.getCartViewModelFactory
import com.example.app_ui.utils.getFavoriteViewModelFactory
import com.example.app_ui.viewmodel.CartViewModel
import com.example.app_ui.viewmodel.FavoriteViewModel
import kotlin.math.max

private val Olive = Color(0xFF434819)
private val TextPrimary = Color(0xFF121111)
private val TextSecondary = Color(0xFF878787)
private val DividerGrey = Color(0xFFE6E6E6)
private val HeartRed = Color(0xFFFF3B30)

/**
 * Get the detail image for each product based on product ID
 * Uses rmbg images for products 1, 2, 3
 * Android converts hyphens to underscores in resource names
 */
private fun getProductDetailImage(productId: Int): Int {
    return when (productId) {
        1 -> R.drawable.fit_tshirt_rmbg // Regular Fit T-shirt (fit_t-shirt_rmbg.png)
        2 -> R.drawable.denim_rmbg // Regular Hem Denim Trousers (Denim_rmbg.png)
        3 -> R.drawable.crew_tshirt_rmbg // Fit Crew Neck Printed T-Shirt (crew_tshirt_rmbg.png)
        else -> R.drawable.img_product_lv_sneaker // Default for sneaker or other products
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: ProductUi,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onNotifications: () -> Unit = {},
    favoriteViewModel: FavoriteViewModel = viewModel(
        factory = getFavoriteViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    ),
    cartViewModel: CartViewModel = viewModel(
        factory = getCartViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    val context = LocalContext.current
    var quantity by remember { mutableIntStateOf(1) }
    var selectedSize by remember { mutableStateOf("M") }
    var selectedColor by remember { mutableStateOf(Color(0xFF0687FF)) }

    // Check if product is favorite
    val isFavoriteFlow = favoriteViewModel.isProductFavorite(product.id.toString())
    val isFavorite by isFavoriteFlow.collectAsState(initial = false)

    // Extract price as double
    val priceString = product.price.replace("$", "").replace(",", "")
    val unitPrice = priceString.toDoubleOrNull() ?: 0.0
    val totalPrice = unitPrice * quantity

    // Sizes for t-shirts
    val availableSizes = if (product.subtitle.contains("T-Shirt", ignoreCase = true)) {
        listOf("S", "M", "L", "XL", "XXL")
    } else if (product.subtitle.contains("Jeans", ignoreCase = true)) {
        listOf("28", "30", "32", "34", "36")
    } else {
        listOf("EU 40", "EU 41", "EU 42", "EU 43")
    }

    val colorOptions = listOf(
        Color(0xFFFF1010),
        Color(0xFF0687FF),
        Color(0xFF121111),
        Color(0xFF5D15D5)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        DetailTopBar(onBack, onNotifications)
        Spacer(Modifier.height(24.dp))

        HeroProductImage(
            imageRes = product.imageUrl?.let { null } ?: getProductDetailImage(product.id),
            imageUrl = product.imageUrl,
            productName = product.title,
            isFavorite = isFavorite,
            onFavoriteClick = {
                // For local products, use the original product image resource ID; for API products, use the URL
                val imageToSave = if (product.imageUrl != null) {
                    product.imageUrl
                } else {
                    // Local product - use the original product image resource ID
                    product.imageRes.toString()
                }
                favoriteViewModel.toggleFavorite(
                    productId = product.id.toString(),
                    name = product.title,
                    description = product.subtitle,
                    price = unitPrice,
                    imageUrl = imageToSave
                )
            }
        )
        Spacer(Modifier.height(32.dp))

        DetailCard(
            title = product.title,
            reviewsLabel = "${product.rating} (7,932 reviews)",
            quantity = quantity,
            onDecrement = { quantity = max(1, quantity - 1) },
            onIncrement = { quantity = quantity + 1 }
        )
        Spacer(Modifier.height(20.dp))

        DescriptionText(product.subtitle)
        Spacer(Modifier.height(20.dp))

        // Size section - takes full width
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Select your size", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableSizes.forEach { size ->
                    SizeChip(
                        label = size,
                        selected = selectedSize == size,
                        onClick = { selectedSize = size },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        
        Spacer(Modifier.height(24.dp)) // More spacing between size and color
        
        // Color section - takes full width
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Color", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                colorOptions.forEach { color ->
                    ColorSwatch(
                        swatchColor = color,
                        selected = selectedColor == color,
                        onClick = { selectedColor = color }
                    )
                }
            }
        }
        Spacer(Modifier.weight(1f))

        AddToCartButton(
            quantity = quantity,
            totalPrice = totalPrice,
            onClick = {
                // For local products, use the original product image resource ID; for API products, use the URL
                val imageToSave = if (product.imageUrl != null) {
                    product.imageUrl
                } else {
                    // Local product - use the original product image resource ID
                    product.imageRes.toString()
                }
                cartViewModel.addCartItem(
                    productId = product.id.toString(),
                    name = product.title,
                    description = product.subtitle,
                    price = unitPrice,
                    imageUrl = imageToSave,
                    quantity = quantity
                )
            }
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun DetailTopBar(onBack: () -> Unit, onNotifications: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Olive,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = "Details",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Olive
        )
        IconButton(
            onClick = onNotifications,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = Olive,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun HeroProductImage(
    imageRes: Int?,
    imageUrl: String? = null,
    productName: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF7F7F7)),
        contentAlignment = Alignment.TopEnd
    ) {
        // Handle both URL (API products) and drawable resource (local products)
        when {
            imageUrl != null && imageUrl.startsWith("http") -> {
                // API product with HTTP URL
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = productName,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentScale = ContentScale.Fit
                )
            }
            imageRes != null -> {
                // Local product with drawable resource
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = productName,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentScale = ContentScale.Fit
                )
            }
            else -> {
                // Fallback
                Image(
                    painter = painterResource(id = R.drawable.img_product_lv_sneaker),
                    contentDescription = productName,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Surface(
            modifier = Modifier
                .padding(20.dp)
                .size(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) HeartRed else Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun DescriptionText(subtitle: String) {
    Text(
        text = buildAnnotatedString {
            append("Premium quality ${subtitle.lowercase()}. Made with the finest materials for comfort and durability. Perfect for everyday wear. ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = TextPrimary)) {
                append("Read More...")
            }
        },
        color = TextSecondary,
        fontSize = 13.sp,
        lineHeight = 20.sp
    )
}

@Composable
private fun AddToCartButton(
    quantity: Int,
    totalPrice: Double,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(
                color = Color(0xCC434819),
                shape = RoundedCornerShape(size = 45.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = "Shopping Cart",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Text(
                "Add to Cart | $${"%.2f".format(totalPrice)}",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DetailCard(
    title: String,
    reviewsLabel: String,
    quantity: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false) // Limit title column to available space
                    .padding(end = 12.dp) // Add padding instead of separate Spacer
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth() // Ensure text respects boundaries
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD33C),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(reviewsLabel, fontSize = 13.sp, color = Color(0xFF347EFB))
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier // Keep quantity selector from shrinking - it will take only needed space
            ) {
                QuantityButton(symbol = "−", onClick = onDecrement)
                Text(
                    quantity.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF292526)
                )
                QuantityButton(symbol = "+", onClick = onIncrement)
            }
        }
        Spacer(Modifier.height(20.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DividerGrey)
        )
    }
}

@Composable
private fun QuantityButton(symbol: String, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        border = BorderStroke(1.dp, Color(0xFFDFDEDE)),
        color = Color.Transparent,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                symbol,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun SizeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (selected) Olive else DividerGrey),
        color = if (selected) Color.White else Color.Transparent,
        shadowElevation = if (selected) 2.dp else 0.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Olive else TextPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ColorSwatch(swatchColor: Color, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(swatchColor)
            .border(
                width = if (selected) 3.dp else 0.dp,
                color = if (selected) Color.White else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onClick() }
    )
}

