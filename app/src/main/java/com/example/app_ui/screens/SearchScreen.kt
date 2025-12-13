package com.example.app_ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_ui.R

private val Olive = Color(0xFF434819)
private val Neutral900 = Color(0xFF1A1A1A)
private val Neutral300 = Color(0xFFB3B3B3)
private val White = Color(0xFFFFFFFF)

private val GeneralSans = FontFamily(
    Font(R.font.general_sans_regular, FontWeight.Normal),
    Font(R.font.general_sans_medium, FontWeight.Medium),
    Font(R.font.general_sans_semibold, FontWeight.SemiBold)
)

private val EncodeSans = FontFamily(
    Font(R.font.encode_sans_regular, FontWeight.Normal),
    Font(R.font.encode_sans_medium, FontWeight.Medium),
    Font(R.font.encode_sans_semicondensed_medium, FontWeight.Medium)
)

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNotification: () -> Unit = {},
    onProductClick: (ProductUi) -> Unit = {},
    productViewModel: com.example.app_ui.viewmodel.ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = com.example.app_ui.utils.getProductViewModelFactory(
            androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    var query by remember { mutableStateOf("") }
    val apiProducts by productViewModel.products.collectAsState()
    
    // Combine local and API products for search
    val allProducts = remember(apiProducts) {
        val productMap = mutableMapOf<Int, ProductUi>()
        sampleProducts.forEach { productMap[it.id] = it }
        apiProducts.forEach { productMap[it.id] = it }
        productMap.values.toList()
    }
    
    // Filter products based on search query
    val filteredProducts by remember(query, allProducts) {
        derivedStateOf {
            if (query.isBlank()) {
                emptyList()
            } else {
                allProducts.filter { product ->
                    product.title.contains(query, ignoreCase = true) ||
                    product.subtitle.contains(query, ignoreCase = true)
                }
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            TopBar(onBack = onBack, onNotification = onNotification)
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                SearchField(
                    query = query,
                    onQueryChange = { query = it }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            
            if (query.isBlank()) {
                // Show empty state when no search query
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Start typing to search for products",
                        fontFamily = GeneralSans,
                        fontSize = 16.sp,
                        color = Neutral300
                    )
                }
            } else if (filteredProducts.isEmpty()) {
                // Show no results message
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No products found for \"$query\"",
                        fontFamily = GeneralSans,
                        fontSize = 16.sp,
                        color = Neutral300
                    )
                }
            } else {
                // Show filtered results
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                ) {
                    ResultsList(
                        products = filteredProducts,
                        onProductClick = onProductClick
                    )
                }
            }
                // Show filtered results
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ResultsList(
                        products = filteredProducts,
                        onProductClick = onProductClick
                    )
                }
            }
        }
    }


@Composable
private fun TopBar(onBack: () -> Unit, onNotification: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Back arrow
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = "Back",
                tint = Olive,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(180f)
            )
        }
        
        // Title
        Text(
            text = "Search",
            fontFamily = GeneralSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = Olive,
            modifier = Modifier.align(Alignment.Center)
        )
        
        // Bell icon
        IconButton(
            onClick = onNotification,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_bell),
                contentDescription = "Notifications",
                tint = Olive,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .border(
                width = 1.dp,
                color = Olive,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = "Search",
            tint = Neutral900
        )
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = "Search",
                    fontFamily = GeneralSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Neutral300
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = GeneralSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Neutral900
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_mic),
            contentDescription = "Voice search",
            tint = Neutral300
        )
    }
}

@Composable
private fun ResultsList(
    products: List<ProductUi>,
    onProductClick: (ProductUi) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            items = products,
            key = { _, product -> product.id }
        ) { index, product ->
            SearchResultRow(
                product = product,
                onClick = { onProductClick(product) }
            )
            if (index == 0 && products.size > 1) {
                Divider(
                    color = Olive,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchResultRow(
    product: ProductUi,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Handle both URL (API products) and drawable resource (local products)
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFF5F5F5))
        ) {
            when {
                product.imageUrl != null && product.imageUrl.startsWith("http") -> {
                    // API product with HTTP URL
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(product.imageUrl)
                            .size(56, 56)
                            .crossfade(true)
                            .build(),
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                product.imageRes != null -> {
                    // Local product with drawable resource
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    // Fallback
                    Image(
                        painter = painterResource(id = R.drawable.img_product_green_tee),
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = product.title,
                fontFamily = EncodeSans,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF121111)
            )
            Text(
                text = product.price,
                fontFamily = EncodeSans,
                fontWeight = FontWeight.ExtraLight,
                fontSize = 14.sp,
                color = Neutral300
            )
        }
        Box(
            modifier = Modifier
                .size(34.dp)
                .clickable { onClick() }
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = "Open",
                tint = Neutral900,
                modifier = Modifier.rotate(-45f)
            )
        }
    }
}

