package com.example.app_ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_ui.R
import com.example.app_ui.service.SavedOrder
import com.example.app_ui.utils.OrderReader
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen showing saved orders from SharedPreferences
 * This demonstrates the exact code snippet to READ saved orders
 */
private val Olive = Color(0xFF434819)
private val Neutral900 = Color(0xFF1A1A1A)
private val Neutral400 = Color(0xFF999999)
private val White = Color.White
private val LightGray = Color(0xFFF5F5F5) // Light gray for cards

@Composable
fun SavedOrdersScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var orders by remember { mutableStateOf<List<SavedOrder>>(emptyList()) }
    
    // Function to refresh orders list
    fun refreshOrders() {
        orders = OrderReader.getOrdersSortedByDate(context)
    }
    
    // Read all orders when screen loads and refresh when resumed
    LaunchedEffect(Unit) {
        refreshOrders()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Header - matching other screens' padding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Orders (${orders.size})",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Neutral900
            )
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = "Back",
                    tint = Olive,
                    modifier = Modifier.rotate(180f)
                )
            }
        }
        
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No orders found.\nPlace an order to see it here!",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Neutral400
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                items(
                    items = orders,
                    key = { it.orderNumber }
                ) { order ->
                    SwipeableOrderCard(
                        order = order,
                        onDelete = {
                            OrderReader.deleteOrder(context, order.orderNumber)
                            refreshOrders()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableOrderCard(
    order: SavedOrder,
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
        OrderItemCard(order = order)
    }
}

@Composable
private fun OrderItemCard(order: SavedOrder) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(order.date))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = LightGray) // Contrasting color
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Order #${order.orderNumber}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedDate,
                        fontSize = 14.sp,
                        color = Neutral400
                    )
                }
                Text(
                    text = order.status,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF53B175)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${order.itemCount} items",
                    fontSize = 14.sp,
                    color = Neutral400
                )
                Text(
                    text = "$${String.format("%.2f", order.total)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Neutral900
                )
            }
        }
    }
}

