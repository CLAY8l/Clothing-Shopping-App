package com.example.app_ui.screens

import android.app.Application
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app_ui.R
import com.example.app_ui.data.entity.FavoriteProduct
import com.example.app_ui.utils.getFavoriteViewModelFactory
import com.example.app_ui.viewmodel.FavoriteViewModel

private val Olive = Color(0xFF434819)
private val Neutral900 = Color(0xFF1A1A1A)
private val Neutral400 = Color(0xFF999999)
private val Grey100 = Color(0xFFE6E6E6)
private val PriceGrey = Color(0xFF454343).copy(alpha = 0.69f)
private val White = Color.White
private val HeartRed = Color(0xFFED1010)

private val GeneralSans = FontFamily(
    Font(R.font.general_sans_regular, FontWeight.Normal),
    Font(R.font.general_sans_medium, FontWeight.Medium),
    Font(R.font.general_sans_semibold, FontWeight.SemiBold)
)

@Composable
fun SavedScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNotification: () -> Unit = {},
    onHome: () -> Unit = {},
    onSearch: () -> Unit = {},
    onSaved: () -> Unit = {},
    onCart: () -> Unit = {},
    onAccount: () -> Unit = {},
    onProductClick: (String) -> Unit = {}, // productId for navigation
    viewModel: FavoriteViewModel = viewModel(
        factory = getFavoriteViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val favorites by viewModel.allFavorites.collectAsState(initial = emptyList())
    val favoriteCount by viewModel.favoriteCount.collectAsState(initial = 0)

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
            TopBar(
                onBack = onBack,
                onNotification = onNotification
            )
            Spacer(modifier = Modifier.height(28.dp))
            
            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No saved items yet",
                            fontFamily = GeneralSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            color = Neutral400
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Start saving your favorite products!",
                            fontFamily = GeneralSans,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Neutral400
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(
                        start = 24.dp,
                        top = 0.dp,
                        end = 24.dp,
                        bottom = 80.dp
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = favorites,
                        key = { it.id }
                    ) { favorite ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            SavedItemCard(
                                favorite = favorite,
                                onRemoveFavorite = {
                                    viewModel.removeFavorite(favorite)
                                },
                                onClick = {
                                    onProductClick(favorite.productId)
                                }
                            )
                        }
                    }
                }
            }
            
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
private fun TopBar(
    onBack: () -> Unit,
    onNotification: () -> Unit
) {
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
            text = "Saved Items",
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
/**
 * Get the product image resource based on product ID for local products
 * This ensures we always use the correct image even if the saved resource ID is wrong
 */
private fun getProductImageByProductId(productId: String): Int {
    return when (productId.toIntOrNull()) {
        1 -> R.drawable.fit_tshirt_rmbg // Regular Fit T-shirt
        2 -> R.drawable.denim_rmbg // Regular Hem Denim Trousers
        3 -> R.drawable.crew_tshirt_rmbg // Fit Crew Neck Printed T-Shirt
        4 -> R.drawable.img_product_lv_sneaker // LV Trainer Sneaker
        else -> R.drawable.img_product_lv_sneaker // Default
    }
}

@Composable
private fun SavedItemCard(
    favorite: FavoriteProduct,
    onRemoveFavorite: () -> Unit,
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Grey100),
            contentAlignment = Alignment.Center
        ) {
            // Image - handle both resource ID and URL
            when {
                favorite.imageUrl != null && favorite.imageUrl.startsWith("http") -> {
                    // API product with HTTP URL
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(favorite.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = favorite.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 6.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                favorite.imageUrl != null -> {
                    // Local product - try to parse as resource ID first
                    val imageRes = favorite.imageUrl.trim().toIntOrNull()
                    if (imageRes != null && imageRes > 0) {
                        // Use the parsed resource ID
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = favorite.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 6.dp)
                        )
                    } else {
                        // If parsing fails, use product ID to get the correct image
                        val productImageRes = getProductImageByProductId(favorite.productId)
                        Image(
                            painter = painterResource(id = productImageRes),
                            contentDescription = favorite.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 6.dp)
                        )
                    }
                }
                else -> {
                    // No image URL provided, use product ID to get the correct image
                    val productImageRes = getProductImageByProductId(favorite.productId)
                    Image(
                        painter = painterResource(id = productImageRes),
                        contentDescription = favorite.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 6.dp)
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                IconButton(onClick = onRemoveFavorite) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Remove from favorites",
                        tint = HeartRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        Text(
            text = favorite.name,
            fontFamily = GeneralSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color(0xFF121111),
            maxLines = 1
        )
        if (favorite.description != null) {
            Text(
                text = favorite.description,
                fontFamily = GeneralSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Neutral400,
                maxLines = 1
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "$${String.format("%.2f", favorite.price)}",
                fontFamily = GeneralSans,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = PriceGrey
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD33C),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "5.0",
                    fontFamily = GeneralSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    color = Color(0xFF121111)
                )
            }
        }
    }
}

@Composable
private fun BottomNav(
    onHome: () -> Unit,
    onSearch: () -> Unit,
    onSaved: () -> Unit,
    onCart: () -> Unit,
    onAccount: () -> Unit
) {
    Column {
        Divider(color = Grey100, thickness = 1.dp)
        NavigationBar(
            containerColor = White,
            tonalElevation = 0.dp
        ) {
            NavigationBarItem(
                selected = false,
                onClick = onHome,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_tab_home),
                        contentDescription = "Home",
                        tint = Neutral400
                    )
                },
                label = {
                    Text(
                        text = "Home",
                        fontFamily = GeneralSans,
                        fontSize = 12.sp,
                        color = Neutral400
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = false,
                onClick = onSearch,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_tab_search),
                        contentDescription = "Search",
                        tint = Neutral400
                    )
                },
                label = {
                    Text(
                        text = "Search",
                        fontFamily = GeneralSans,
                        fontSize = 12.sp,
                        color = Neutral400
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = true,
                onClick = onSaved,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_tab_saved),
                        contentDescription = "Saved",
                        tint = Neutral900
                    )
                },
                label = {
                    Text(
                        text = "Saved",
                        fontFamily = GeneralSans,
                        fontSize = 12.sp,
                        color = Neutral900
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = false,
                onClick = onCart,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_tab_cart),
                        contentDescription = "Cart",
                        tint = Neutral400
                    )
                },
                label = {
                    Text(
                        text = "Cart",
                        fontFamily = GeneralSans,
                        fontSize = 12.sp,
                        color = Neutral400
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = false,
                onClick = onAccount,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_tab_account),
                        contentDescription = "Account",
                        tint = Neutral400
                    )
                },
                label = {
                    Text(
                        text = "Account",
                        fontFamily = GeneralSans,
                        fontSize = 12.sp,
                        color = Neutral400
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
