package com.example.app_ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app_ui.R
import com.example.app_ui.data.entity.ProductCart
import com.example.app_ui.utils.getCartViewModelFactory
import com.example.app_ui.viewmodel.CartViewModel
import kotlin.math.max

private val Olive = Color(0xFF434819)
private val Neutral = Color(0xFF1A1A1A)
private val Grey = Color(0xFF999999)
private val ErrorRed = Color(0xFFED1010)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBack: () -> Unit,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier,
    onNotification: () -> Unit = {},
    onHome: () -> Unit = {},
    onSearch: () -> Unit = {},
    onSaved: () -> Unit = {},
    onCart: () -> Unit = {},
    onAccount: () -> Unit = {},
    viewModel: CartViewModel = viewModel(
        factory = getCartViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    val cartItems by viewModel.allCartItems.collectAsState(initial = emptyList())
    val cartTotal by viewModel.cartTotal.collectAsState(initial = 0.0)
    val cartItemCount by viewModel.cartItemCount.collectAsState(initial = 0)

    val shippingFee = 80.0
    val vat = 0.0
    val subtotal = cartTotal ?: 0.0
    val total = subtotal + shippingFee + vat

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            CartTopBar(onBack, onNotification)
            Spacer(Modifier.height(16.dp))
            
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Your cart is empty",
                        fontSize = 18.sp,
                        color = Grey,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                cartItems.forEach { item ->
                    Spacer(Modifier.height(12.dp))
                    CartCard(
                        item = item,
                        onQuantityChange = { delta ->
                            val newQuantity = max(1, item.quantity + delta)
                            viewModel.updateCartItemQuantity(item, newQuantity)
                        },
                        onRemove = {
                            viewModel.removeCartItem(item)
                        }
                    )
                }
            }
            
            Spacer(Modifier.height(24.dp))
            if (cartItems.isNotEmpty()) {
                SummaryRow(label = "Sub-total", value = subtotal)
                SummaryRow(label = "VAT (%)", value = vat)
                SummaryRow(label = "Shipping fee", value = shippingFee)
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    color = Olive.copy(alpha = .4f)
                )
                SummaryRow(label = "Total", value = total, bold = true)
            }
            Spacer(Modifier.height(24.dp))
        }

        // Fixed bottom section (Checkout button and navigation)
        Column {
            // Checkout button - fixed at bottom above navigation
            if (cartItems.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 24.dp)
                ) {
                    Button(
                        onClick = onCheckout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .background(
                                color = Color(0xCC434819),
                                shape = RoundedCornerShape(size = 10.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Go To Checkout",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_right),
                                contentDescription = "Checkout",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }

            // Bottom navigation - fixed at very bottom
            BottomNav(
                onHome = onHome,
                onSearch = onSearch,
                onSaved = onSaved,
                onCart = onCart,
                onAccount = onAccount
            )
        }
    }
}

@Composable
private fun CartTopBar(onBack: () -> Unit, onNotification: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 8.dp),
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
            "My Cart",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Olive
        )
        IconButton(
            onClick = onNotification,
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

/**
 * Get the product image resource based on product ID for local products
 * This ensures we always use the correct image even if the saved resource ID is wrong
 * Uses the original product images (not rmbg versions) since those are saved when adding to cart
 */
private fun getProductImageByProductId(productId: String): Int {
    return when (productId.toIntOrNull()) {
        1 -> R.drawable.img_product_green_tee // Regular Fit T-shirt
        2 -> R.drawable.img_product_denim // Regular Hem Denim Trousers
        3 -> R.drawable.img_product_black_tee // Fit Crew Neck Printed T-Shirt
        4 -> R.drawable.img_product_lv_sneaker // LV Trainer Sneaker
        else -> R.drawable.img_product_green_tee // Default
    }
}

@Composable
private fun CartCard(
    item: ProductCart,
    onQuantityChange: (delta: Int) -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Olive),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image - handle both resource ID and URL
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                when {
                    item.imageUrl != null && item.imageUrl.startsWith("http") -> {
                        // API product with HTTP URL
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(item.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = item.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    item.imageUrl != null -> {
                        // Local product - try to parse as resource ID first
                        val imageRes = item.imageUrl.trim().toIntOrNull()
                        if (imageRes != null && imageRes > 0) {
                            // Use the parsed resource ID
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = item.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // If parsing fails, use product ID to get the correct image
                            val productImageRes = getProductImageByProductId(item.productId)
                            Image(
                                painter = painterResource(id = productImageRes),
                                contentDescription = item.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    else -> {
                        // No image URL provided, use product ID to get the correct image
                        val productImageRes = getProductImageByProductId(item.productId)
                        Image(
                            painter = painterResource(id = productImageRes),
                            contentDescription = item.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    item.name,
                    fontWeight = FontWeight.SemiBold,
                    color = Neutral,
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.description != null) {
                    Text(
                        item.description,
                        color = Grey,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    "$${String.format("%.2f", item.price)}",
                    fontSize = 13.sp,
                    color = Neutral,
                    fontWeight = FontWeight.Medium
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_trash),
                    contentDescription = "Remove",
                    tint = ErrorRed,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onRemove() }
                )
                QuantityChanger(
                    quantity = item.quantity,
                    onIncrease = { onQuantityChange(1) },
                    onDecrease = { onQuantityChange(-1) }
                )
            }
        }
    }
}

@Composable
private fun QuantityChanger(quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        SquareButton(
            iconRes = R.drawable.ic_minus,
            onClick = onDecrease
        )
        Text(quantity.toString(), fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Neutral)
        SquareButton(
            iconRes = R.drawable.ic_add,
            onClick = onIncrease
        )
    }
}

@Composable
private fun SquareButton(iconRes: Int, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
        color = Color.White,
        modifier = Modifier.size(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Neutral,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: Double, bold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = 16.sp,
            color = if (bold) Neutral else Grey,
            fontWeight = if (bold) FontWeight.Medium else FontWeight.Normal
        )
        Text(
            text = "$${String.format("%.2f", value)}",
            fontSize = 16.sp,
            fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Medium,
            color = Neutral
        )
    }
}

@Composable
private fun BottomNav(
    onHome: () -> Unit = {},
    onSearch: () -> Unit = {},
    onSaved: () -> Unit = {},
    onCart: () -> Unit = {},
    onAccount: () -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        val items = listOf("Home", "Search", "Saved", "Cart", "Account")
        items.forEach { label ->
            val selected = label == "Cart"
            val onClick = when (label) {
                "Home" -> onHome
                "Search" -> onSearch
                "Saved" -> onSaved
                "Cart" -> onCart
                "Account" -> onAccount
                else -> {}
            }
            NavigationBarItem(
                selected = selected,
                onClick = onClick as () -> Unit,
                icon = {
                    Icon(
                        painter = when (label) {
                            "Home" -> painterResource(R.drawable.ic_tab_home)
                            "Search" -> painterResource(R.drawable.ic_search)
                            "Saved" -> painterResource(R.drawable.ic_tab_saved)
                            "Account" -> painterResource(R.drawable.ic_tab_account)
                            else -> painterResource(R.drawable.ic_tab_cart)
                        },
                        contentDescription = label,
                        tint = if (selected) Neutral else Grey
                    )
                },
                label = {
                    Text(label, fontSize = 12.sp, color = if (selected) Neutral else Grey)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Neutral,
                    selectedTextColor = Neutral,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Grey,
                    unselectedTextColor = Grey
                )
            )
        }
    }
}
