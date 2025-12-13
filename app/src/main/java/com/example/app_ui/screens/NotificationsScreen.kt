package com.example.app_ui.screens

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_ui.R
import com.example.app_ui.data.entity.Notification
import com.example.app_ui.utils.getNotificationViewModelFactory
import com.example.app_ui.viewmodel.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.*

private val Olive = Color(0xFF434819)
private val Neutral900 = Color(0xFF1A1A1A)
private val Neutral400 = Color(0xFF999999)
private val Neutral500 = Color(0xFF808080)
private val Grey100 = Color(0xFFE6E6E6)
private val White = Color.White

private val GeneralSans = FontFamily(
    Font(R.font.general_sans_regular, FontWeight.Normal),
    Font(R.font.general_sans_medium, FontWeight.Medium),
    Font(R.font.general_sans_semibold, FontWeight.SemiBold)
)

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onHome: () -> Unit = {},
    onSearch: () -> Unit = {},
    onSaved: () -> Unit = {},
    onCart: () -> Unit = {},
    onAccount: () -> Unit = {},
    viewModel: NotificationViewModel = viewModel(
        factory = getNotificationViewModelFactory(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val notifications by viewModel.allNotifications.collectAsState(initial = emptyList())
    
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
            TopBar(onBack = onBack)
            Divider(color = Grey100, thickness = 1.dp)
            
            if (notifications.isEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                EmptyState()
                Spacer(modifier = Modifier.weight(1.2f))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = notifications,
                        key = { it.id }
                    ) { notification ->
                        SwipeableNotificationCard(
                            notification = notification,
                            onDelete = {
                                viewModel.deleteNotification(notification)
                            }
                        )
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
private fun TopBar(onBack: () -> Unit) {
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
            text = "Notifications",
            fontFamily = GeneralSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = Olive,
            modifier = Modifier.align(Alignment.Center)
        )
        
        // Bell icon
        IconButton(
            onClick = { /* Already on notifications screen */ },
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
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large bell icon (duotone style)
        Image(
            painter = painterResource(R.drawable.ic_bell),
            contentDescription = "Bell",
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 24.dp)
        )
        
        // Title text
        Text(
            text = "You haven't gotten any notifications yet!",
            fontFamily = GeneralSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Neutral900,
            modifier = Modifier.padding(bottom = 16.dp),
            lineHeight = 24.sp
        )
        
        // Subtitle text
        Text(
            text = "We'll alert you when something cool happens.",
            fontFamily = GeneralSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Neutral500,
            lineHeight = 22.4.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableNotificationCard(
    notification: Notification,
    onDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )
    
    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier.fillMaxWidth(),
        backgroundContent = {
            // Delete background shown when swiping
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFED1010), RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) {
        NotificationItemCard(notification = notification)
    }
}

@Composable
private fun NotificationItemCard(notification: Notification) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(notification.timestamp))
    
    // Determine icon color based on notification type
    val iconColor = when (notification.type) {
        "confirmation" -> Color(0xFF53B175)
        "cart_added" -> Color(0xFF347EFB)
        else -> Olive
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Grey100)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Notification icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = iconColor,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bell),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            // Notification content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = notification.title,
                    fontFamily = GeneralSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Neutral900
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    fontFamily = GeneralSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Neutral500
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedDate,
                    fontFamily = GeneralSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Neutral400
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

