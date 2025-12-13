package com.example.app_ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_ui.R

private val Olive = Color(0xFF434819)
private val Neutral900 = Color(0xFF1A1A1A)
private val Neutral400 = Color(0xFF999999)
private val Grey100 = Color(0xFFE6E6E6)
private val White = Color.White
private val ErrorRed = Color(0xFFED1010)

private val GeneralSans = FontFamily(
    Font(R.font.general_sans_regular, FontWeight.Normal),
    Font(R.font.general_sans_medium, FontWeight.Medium),
    Font(R.font.general_sans_semibold, FontWeight.SemiBold)
)

sealed class AccountMenuItem(
    val title: String,
    val icon: ImageVector? = null,
    val iconRes: Int? = null,
    val iconColor: Color = Neutral900,
    val textColor: Color = Neutral900
) {
    object MyOrders : AccountMenuItem("My Orders", Icons.Default.ShoppingCart)
    object MyDetails : AccountMenuItem("My Details", Icons.Default.Person)
    object AddressBook : AccountMenuItem("Address Book", Icons.Default.LocationOn)
    object PaymentMethods : AccountMenuItem("Payment Methods", iconRes = R.drawable.ic_card)
    object Notifications : AccountMenuItem("Notifications", Icons.Default.Notifications)
    object FAQs : AccountMenuItem("FAQs", Icons.Default.Info)
    object HelpCenter : AccountMenuItem("Help Center", Icons.Default.Phone)
    object Logout : AccountMenuItem("Logout", Icons.Default.ExitToApp, iconColor = ErrorRed, textColor = ErrorRed)
}

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNotification: () -> Unit = {},
    onHome: () -> Unit = {},
    onSearch: () -> Unit = {},
    onSaved: () -> Unit = {},
    onCart: () -> Unit = {},
    onAccount: () -> Unit = {},
    onMyOrders: () -> Unit = {},
    onMyDetails: () -> Unit = {},
    onAddressBook: () -> Unit = {},
    onPaymentMethods: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onFAQs: () -> Unit = {},
    onHelpCenter: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
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
            // Top Bar
            TopBar(
                onBack = onBack,
                onNotification = onNotification
            )
            
            // Divider
            Divider(color = Grey100, thickness = 1.dp)
            
            // Menu Items
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Section 1: My Orders
                MenuItem(
                    item = AccountMenuItem.MyOrders,
                    onClick = onMyOrders
                )
                
                Divider(
                    color = Grey100,
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 64.dp)
                )
                
                // Section 2: My Details
                MenuItem(
                    item = AccountMenuItem.MyDetails,
                    onClick = onMyDetails
                )
                
                Divider(
                    color = Grey100,
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 64.dp)
                )
                
                // Section 3: Address Book, Payment Methods
                MenuItem(
                    item = AccountMenuItem.AddressBook,
                    onClick = onAddressBook
                )
                
                Divider(
                    color = Grey100,
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 64.dp)
                )
                
                MenuItem(
                    item = AccountMenuItem.PaymentMethods,
                    onClick = onPaymentMethods
                )
                
                // Section divider
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Grey100)
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Section 4: Notifications
                MenuItem(
                    item = AccountMenuItem.Notifications,
                    onClick = onNotifications
                )
                
                // Section divider
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Grey100)
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Section 5: FAQs, Help Center
                MenuItem(
                    item = AccountMenuItem.FAQs,
                    onClick = onFAQs
                )
                
                Divider(
                    color = Grey100,
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 64.dp)
                )
                
                MenuItem(
                    item = AccountMenuItem.HelpCenter,
                    onClick = onHelpCenter
                )
                
                // Section divider
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Grey100)
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Section 6: Logout
                MenuItem(
                    item = AccountMenuItem.Logout,
                    onClick = onLogout
                )
            }
            
            // Bottom Navigation
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
            text = "Account",
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
private fun MenuItem(
    item: AccountMenuItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        if (item.icon != null) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = item.iconColor,
                modifier = Modifier.size(24.dp)
            )
        } else if (item.iconRes != null) {
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.title,
                tint = item.iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Title
        Text(
            text = item.title,
            fontFamily = GeneralSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = item.textColor,
            modifier = Modifier.weight(1f)
        )
        
        // Chevron
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = Color(0xFFB3B3B3),
            modifier = Modifier.size(24.dp)
        )
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
            modifier = Modifier.fillMaxWidth()
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
                selected = false,
                onClick = onSaved,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_tab_saved),
                        contentDescription = "Saved",
                        tint = Neutral400
                    )
                },
                label = {
                    Text(
                        text = "Saved",
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
                selected = true,
                onClick = onAccount,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_tab_account),
                        contentDescription = "Account",
                        tint = Neutral900
                    )
                },
                label = {
                    Text(
                        text = "Account",
                        fontFamily = GeneralSans,
                        fontSize = 12.sp,
                        color = Neutral900
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

