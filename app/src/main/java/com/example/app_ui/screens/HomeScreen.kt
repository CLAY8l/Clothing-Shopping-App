package com.example.app_ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_ui.utils.getProductViewModelFactory
import com.example.app_ui.viewmodel.ProductViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app_ui.R

private val Olive = Color(0xFF434819)
private val Neutral900 = Color(0xFF1A1A1A)
private val Neutral700 = Color(0xFF292526)
private val Neutral500 = Color(0xFF787676)
private val Neutral200 = Color(0xFFE6E6E6)
private val ChipBg = Color(0xFF292526)
private val White = Color.White
private val HeartRed = Color(0xFFFF3B30)

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

@Immutable
data class ProductUi(
    val id: Int,
    val title: String,
    val subtitle: String,
    val price: String,
    val rating: String,
    val imageRes: Int,
    val imageUrl: String? = null, // Optional URL for API images
    val isWishlisted: Boolean = false
)

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    products: List<ProductUi> = sampleProducts,
    onSearchClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onBellClick: () -> Unit = {},
    onProductClick: (ProductUi) -> Unit = {},
    onToggleWishlist: (ProductUi) -> Unit = {},
    onTabSelected: (BottomTab) -> Unit = {},
    selectedTab: BottomTab = BottomTab.Home,
    selectedCategory: String = "All Items",
    onCategorySelected: (String) -> Unit = {},
    filteredProducts: List<ProductUi> = products
) {
    var searchQuery by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Header(onBellClick)
            Spacer(modifier = Modifier.height(24.dp))
            SearchRow(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearchClick = onSearchClick,
                onFilterClick = onFilterClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            CategoryRow(
                products = products,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )
            Spacer(modifier = Modifier.height(20.dp))
            ProductGrid(
                products = filteredProducts,
                onProductClick = onProductClick,
                onWishlistClick = onToggleWishlist,
                modifier = Modifier.weight(1f)
            )
            BottomNavigationBar(
                selected = selectedTab,
                onTabSelected = onTabSelected,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Header(onBellClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Discover",
            fontFamily = GeneralSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp,
            color = Olive,
            letterSpacing = (-1.6).sp
        )
        IconButton(onClick = onBellClick) {
            Icon(
                painter = painterResource(R.drawable.ic_bell),
                contentDescription = "Notifications",
                tint = Olive
            )
        }
    }
}

@Composable
private fun SearchRow(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            placeholder = {
                Text(
                    text = "Search for clothes...",
                    fontFamily = GeneralSans,
                    fontSize = 16.sp,
                    color = Olive
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = Olive
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_mic),
                    contentDescription = "Voice search",
                    tint = Olive
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Olive,
                unfocusedBorderColor = Olive,
                cursorColor = Olive,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = GeneralSans,
                fontSize = 16.sp,
                color = Neutral900
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Olive)
                .clickable { onFilterClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_filter),
                contentDescription = "Filter",
                tint = White
            )
        }
    }
}

@Composable
private fun CategoryRow(
    products: List<ProductUi>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    // Extract unique categories from products
    val categories = remember(products) {
        val uniqueCategories = mutableSetOf<String>()
        products.forEach { product ->
            val category = product.subtitle
            if (category.isNotBlank()) {
                uniqueCategories.add(category)
            }
        }
        // Create list with "All Items" first, then sorted categories
        val categoryList = mutableListOf("All Items")
        categoryList.addAll(uniqueCategories.sorted())
        categoryList
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Chip(
                text = category,
                filled = isSelected,
                onClick = { onCategorySelected(category) },
                leadingIcon = if (category == "All Items" && isSelected) {
                    {
                        Icon(
                            painterResource(R.drawable.ic_all_item),
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null
            )
        }
    }
}

@Composable
private fun Chip(
    text: String,
    filled: Boolean,
    onClick: () -> Unit = {},
    leadingIcon: (@Composable () -> Unit)? = null
) {
    val background = if (filled) ChipBg else Color.Transparent
    val borderColor = if (filled) Color.Transparent else Olive
    val textColor = if (filled) White else Neutral900

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        leadingIcon?.invoke()
        Text(
            text = text,
            fontFamily = GeneralSans,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = textColor
        )
    }
}

@Composable
private fun ProductGrid(
    products: List<ProductUi>,
    onProductClick: (ProductUi) -> Unit,
    onWishlistClick: (ProductUi) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            ProductCard(
                product = product,
                onClick = onProductClick,
                onWishlistClick = onWishlistClick
            )
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductUi,
    onClick: (ProductUi) -> Unit,
    onWishlistClick: (ProductUi) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(product) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF7F7F7)),
            contentAlignment = Alignment.Center
        ) {
            // Display image - use URL if available, otherwise use drawable resource
            if (product.imageUrl != null && product.imageUrl.startsWith("http")) {
                // API product with URL
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(product.imageUrl)
                        .size(400, 400)
                        .crossfade(false)
                        .build(),
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Local product with drawable resource
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(White)
                    .clickable { onWishlistClick(product) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        if (product.isWishlisted) R.drawable.ic_red_heart else R.drawable.ic_heart_outline
                    ),
                    contentDescription = if (product.isWishlisted) "Remove from wishlist" else "Add to wishlist",
                    tint = if (product.isWishlisted) HeartRed else Neutral900,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        ProductDetails(product)
    }
}

@Composable
private fun ProductDetails(product: ProductUi) {
    Column {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = product.title,
            fontFamily = EncodeSans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Neutral700,
            maxLines = 2
        )
        Text(
            text = product.subtitle,
            fontFamily = EncodeSans,
            fontSize = 10.sp,
            color = Neutral500,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = product.price,
                fontFamily = EncodeSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Neutral700
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = null,
                    tint = Color(0xFFFFD33C),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = product.rating,
                    fontFamily = EncodeSans,
                    fontSize = 12.sp,
                    color = Neutral700
                )
            }
        }
    }
}

enum class BottomTab { Home, Search, Saved, Cart, Account }

@Composable
private fun BottomNavigationBar(
    selected: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = White,
        modifier = modifier
    ) {
        BottomTab.values().forEach { tab ->
            val iconRes = when (tab) {
                BottomTab.Home -> R.drawable.ic_tab_home
                BottomTab.Search -> R.drawable.ic_tab_search
                BottomTab.Saved -> R.drawable.ic_tab_saved
                BottomTab.Cart -> R.drawable.ic_tab_cart
                BottomTab.Account -> R.drawable.ic_tab_account
            }
            NavigationBarItem(
                selected = tab == selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = tab.name,
                        tint = if (tab == selected) Neutral900 else Color(0xFF999999)
                    )
                },
                label = {
                    Text(
                        text = tab.name.lowercase().replaceFirstChar { it.uppercaseChar() },
                        fontFamily = GeneralSans,
                        fontSize = 12.sp
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Neutral900,
                    selectedTextColor = Neutral900,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Color(0xFF999999),
                    unselectedTextColor = Color(0xFF999999)
                )
            )
        }
    }
}

val sampleProducts = listOf(
    ProductUi(1, "Regular Fit T-shirt", "T-Shirt", "$212.99", "5.0", R.drawable.img_product_green_tee, "false"),
    ProductUi(2, "Regular Hem Denim Trousers", "Jeans", "$150", "5.0", R.drawable.img_product_denim, "false"),
    ProductUi(3, "Fit Crew Neck Printed T-Shirt", "T-Shirt", "$149.99", "4.5", R.drawable.img_product_black_tee, "false"),
    ProductUi(4, "LV Trainer Sneaker", "Shoes", "$1,330.00", "5.0", R.drawable.img_product_lv_sneaker, "false")
)

@Composable
fun DiscoverScreenWithState(
    onSneakerClick: (ProductUi) -> Unit,
    onCartClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSavedClick: () -> Unit = {},
    onAccountClick: () -> Unit = {},
    onBellClick: () -> Unit = {},
    productViewModel: ProductViewModel = viewModel(
        factory = getProductViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    val apiProducts by productViewModel.products.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    
    // Combine local products and API products
    val allProducts = remember(apiProducts) {
        // Combine both lists - local products first, then API products
        // API products have IDs offset by 1000 to avoid conflicts
        val combinedList = mutableListOf<ProductUi>()
        
        // Add local products first
        combinedList.addAll(sampleProducts)
        
        // Add API products (they have different IDs, so no conflict)
        combinedList.addAll(apiProducts)
        
        combinedList
    }
    
    var products by remember(allProducts) { mutableStateOf(allProducts) }
    var selectedCategory by remember { mutableStateOf("All Items") }
    
    // Filter products based on selected category
    val filteredProducts = remember(products, selectedCategory) {
        if (selectedCategory == "All Items") {
            products
        } else {
            products.filter { it.subtitle.equals(selectedCategory, ignoreCase = true) }
        }
    }
    
    // Update products when allProducts changes (when API products load)
    LaunchedEffect(allProducts) {
        products = allProducts
    }

    DiscoverScreen(
        products = products,
        selectedCategory = selectedCategory,
        onCategorySelected = { category ->
            selectedCategory = category
        },
        filteredProducts = filteredProducts,
        onToggleWishlist = { product ->
            products = products.map {
                if (it.id == product.id) it.copy(isWishlisted = !it.isWishlisted) else it
            } as MutableList<ProductUi>
        },
        onProductClick = onSneakerClick, // Use the passed parameter
        onSearchClick = {
            // Handle search click if needed
        },
        onFilterClick = {
            // Handle filter click
        },
        onBellClick = onBellClick,
        onTabSelected = { tab ->
            when (tab) {
                BottomTab.Cart -> onCartClick()
                BottomTab.Search -> onSearchClick()
                BottomTab.Saved -> onSavedClick()
                BottomTab.Account -> onAccountClick()
                else -> Unit
            }
        }
    )
}