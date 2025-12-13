package com.example.clothingapp_frontend.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.math.max
import com.example.app_ui.R
private val Olive = Color(0xFF434819)
private val TextPrimary = Color(0xFF121111)
private val TextSecondary = Color(0xFF878787)
private val DividerGrey = Color(0xFFE6E6E6)
private val HeartRed = Color(0xFFFF3B30)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SneakerDetailScreen(
    onSneakerClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onAddToCart: (Int) -> Unit = {}
) {
    var quantity by remember { mutableIntStateOf(1) }
    var selectedSize by remember { mutableStateOf("EU 42") }
    var selectedColor by remember { mutableStateOf(Color(0xFF0687FF)) }
    var isFavorite by remember { mutableStateOf(false) }

    val availableSizes = listOf("EU 40", "EU 41", "EU 42", "EU 43")
    val colorOptions = listOf(
        Color(0xFFFF1010),
        Color(0xFF5D15D5),
        Color(0xFF121111)
    )

    // Calculate total price based on quantity
    val unitPrice = 1330.00
    val totalPrice = unitPrice * quantity

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        // Bigger top bar with more padding
        DetailTopBar(onBack, onNotifications)
        Spacer(Modifier.height(24.dp)) // Increased spacing

        // Larger image frame with favorite toggle
        HeroSneakerImage(
            isFavorite = isFavorite,
            onFavoriteClick = { isFavorite = !isFavorite } // Toggle favorite state
        )
        Spacer(Modifier.height(32.dp)) // Increased spacing

        DetailCard(
            title = "LV Trainer Sneaker",
            reviewsLabel = "5.0 (7,932 reviews)",
            quantity = quantity,
            onDecrement = { quantity = max(1, quantity - 1) },
            onIncrement = { quantity = quantity + 1 }
        )
        Spacer(Modifier.height(20.dp)) // Increased spacing

        // Description with "Read More" in black bold
        DescriptionText()
        Spacer(Modifier.height(20.dp)) // Increased spacing

        // ORIGINAL VERSION - Select your size and Color sections
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text("Select your size", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    availableSizes.forEach { size ->
                        SizeChip(
                            label = size,
                            selected = selectedSize == size,
                            onClick = { selectedSize = size }
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.Start) {
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
        }
        Spacer(Modifier.weight(1f))

        // Add to Cart button with cart icon and dynamic price
        AddToCartButton(
            quantity = quantity,
            totalPrice = totalPrice,
            onClick = { onAddToCart(quantity) }
        )
        Spacer(Modifier.height(16.dp)) // Increased bottom spacing
    }
}

@Composable
private fun DetailTopBar(onBack: () -> Unit, onNotifications: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp), // Increased padding for bigger top bar
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(48.dp) // Larger icon button
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Olive,
                modifier = Modifier.size(28.dp) // Larger icon
            )
        }
        Text(
            text = "Details",
            fontSize = 28.sp, // Larger font size
            fontWeight = FontWeight.Bold, // Bolder
            color = Olive
        )
        IconButton(
            onClick = onNotifications,
            modifier = Modifier.size(48.dp) // Larger icon button
        ) {
            Icon(
                Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = Olive,
                modifier = Modifier.size(28.dp) // Larger icon
            )
        }
    }
}

@Composable
private fun HeroSneakerImage(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp) // Increased height for bigger image frame
            .clip(RoundedCornerShape(24.dp)) // Slightly more rounded
            .background(Color(0xFFDFEDFF)),
        contentAlignment = Alignment.TopEnd
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.img_product_lv_sneaker)
                .crossfade(true)
                .build(),
            contentDescription = "LV Trainer Sneaker",
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp), // Added padding to make image slightly smaller within frame
            contentScale = ContentScale.Fit // Changed to Fit to show full image
        )
        Surface(
            modifier = Modifier
                .padding(20.dp) // Adjusted padding
                .size(48.dp), // Slightly larger favorite button
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    // Toggle between outline and filled heart based on isFavorite state
                    if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) HeartRed else Color.Black, // Red when favorite, black when not
                    modifier = Modifier.size(24.dp) // Larger icon
                )
            }
        }
    }
}

@Composable
private fun DescriptionText() {
    Text(
        text = buildAnnotatedString {
            append("This striking two-tone edition of the LV Trainer sneaker combines Monogram grained calf leather with glossy patent leather. ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = TextPrimary)) {
                append("Read More...")
            }
        },
        color = TextSecondary,
        fontSize = 13.sp, // Slightly larger
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
                color = Color(0xCC434819), // Your specified color with transparency
                shape = RoundedCornerShape(size = 45.dp) // Your specified rounded corners
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
                "Add to Cart | $${"%.2f".format(totalPrice)}", // Dynamic price with 2 decimal places
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
            Column {
                Text(
                    text = title,
                    fontSize = 22.sp, // Slightly larger
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD33C),
                        modifier = Modifier.size(20.dp) // Slightly larger
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(reviewsLabel, fontSize = 13.sp, color = Color(0xFF347EFB))
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuantityButton(symbol = "−", onClick = onDecrement)
                Text(
                    quantity.toString(),
                    fontSize = 18.sp, // Slightly larger
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF292526)
                )
                QuantityButton(symbol = "+", onClick = onIncrement)
            }
        }
        Spacer(Modifier.height(20.dp)) // Increased spacing
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
                .size(40.dp) // Slightly larger quantity buttons
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                symbol,
                fontSize = 20.sp, // Slightly larger
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun SizeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (selected) Olive else DividerGrey),
        color = if (selected) Color.White else Color.Transparent,
        shadowElevation = if (selected) 2.dp else 0.dp,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Olive else TextPrimary
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
