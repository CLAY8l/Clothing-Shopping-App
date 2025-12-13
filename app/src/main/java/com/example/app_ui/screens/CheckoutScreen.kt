package com.example.app_ui.screens

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import com.example.app_ui.R
import com.example.app_ui.data.entity.OrderItem
import com.example.app_ui.data.entity.ProductCart
import com.example.app_ui.service.OrderItemData
import com.example.app_ui.service.SendOrderService
import com.example.app_ui.utils.getCartViewModelFactory
import com.example.app_ui.utils.getOrderViewModelFactory
import com.example.app_ui.viewmodel.CartViewModel
import com.example.app_ui.viewmodel.OrderViewModel
import com.google.gson.Gson
import java.util.Locale

private val Olive = Color(0xFF434819)
private val Neutral = Color(0xFF1A1A1A)
private val Grey = Color(0xFF999999)
private val LightGrey = Color(0xFFB3B3B3)

private fun formatCardNumber(cardNumber: String): String {
    return if (cardNumber.length > 4) {
        "**** **** **** ${cardNumber.takeLast(4)}"
    } else {
        cardNumber
    }
}

@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onPlaceOrder: () -> Unit,
    modifier: Modifier = Modifier,
    onNotification: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel(
        factory = getCartViewModelFactory(LocalContext.current.applicationContext as Application)
    ),
    orderViewModel: OrderViewModel = viewModel(
        factory = getOrderViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val context = LocalContext.current
    val cartItems by cartViewModel.allCartItems.collectAsState(initial = emptyList())
    val cartTotal by cartViewModel.cartTotal.collectAsState(initial = 0.0)
    val orderState by orderViewModel.uiState.collectAsState()
    
    val shippingFee = 80.0
    val vat = 0.0
    val subtotal = cartTotal ?: 0.0
    val total = subtotal + shippingFee + vat
    
    var selectedPaymentMethod by remember { mutableStateOf("Card") }
    var promoCode by remember { mutableStateOf("") }
    var isPromoApplied by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf("925 S Chugach St #APT 10, Alaska 99645") }
    var cardNumber by remember { mutableStateOf("2512") }
    var isEditingAddress by remember { mutableStateOf(false) }
    var isEditingCard by remember { mutableStateOf(false) }
    
    // Handle order creation success
    LaunchedEffect(orderState.lastCreatedOrderId) {
        if (orderState.lastCreatedOrderId != null) {
            // Clear cart after successful order
            cartItems.forEach { item ->
                cartViewModel.removeCartItem(item)
            }
            // Navigate to order accepted screen
            onPlaceOrder()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            CheckoutTopBar(onBack, onNotification)
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Olive.copy(alpha = 0.4f))
            Spacer(Modifier.height(20.dp))

            DeliveryAddressSection(
                address = address,
                onAddressChange = { address = it },
                isEditing = isEditingAddress,
                onEditToggle = { isEditingAddress = !isEditingAddress }
            )
            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = Olive.copy(alpha = 0.4f))
            Spacer(Modifier.height(20.dp))

            PaymentMethodSection(
                selectedMethod = selectedPaymentMethod,
                onMethodSelected = { selectedPaymentMethod = it },
                cardNumber = cardNumber,
                onCardNumberChange = { cardNumber = it },
                isEditingCard = isEditingCard,
                onEditCardToggle = { isEditingCard = !isEditingCard }
            )
            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = Olive.copy(alpha = 0.4f))
            Spacer(Modifier.height(20.dp))

            OrderSummarySection(
                subtotal = subtotal,
                vat = vat,
                shippingFee = shippingFee,
                total = total
            )
            Spacer(Modifier.height(24.dp))

            PromoCodeSection(
                promoCode = promoCode,
                onPromoCodeChange = { promoCode = it },
                onApplyPromo = {
                    if (promoCode.isNotBlank()) {
                        isPromoApplied = true
                    }
                },
                isPromoApplied = isPromoApplied
            )
            Spacer(Modifier.height(24.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = {
                    // Convert cart items to order items for Room database
                    val orderItems = cartItems.map { cartItem ->
                        OrderItem(
                            orderId = 0, // Will be set when order is created
                            productId = cartItem.productId,
                            name = cartItem.name,
                            description = cartItem.description,
                            price = cartItem.price,
                            quantity = cartItem.quantity,
                            imageUrl = cartItem.imageUrl
                        )
                    }
                    
                    // Convert cart items to OrderItemData for service
                    val serviceItems = cartItems.map { cartItem ->
                        OrderItemData(
                            productId = cartItem.productId,
                            name = cartItem.name,
                            description = cartItem.description,
                            price = cartItem.price,
                            quantity = cartItem.quantity,
                            imageUrl = cartItem.imageUrl
                        )
                    }
                    
                    // Start background service to save order to SharedPreferences
                    val serviceIntent = Intent(context, SendOrderService::class.java).apply {
                        val gson = Gson()
                        putExtra(SendOrderService.EXTRA_ITEMS, gson.toJson(serviceItems))
                        putExtra(SendOrderService.EXTRA_TOTAL, total)
                        putExtra(SendOrderService.EXTRA_ITEM_COUNT, cartItems.sumOf { it.quantity })
                    }
                    
                    try {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            context.startForegroundService(serviceIntent)
                        } else {
                            context.startService(serviceIntent)
                        }
                        // Show toast to confirm service started
                        Toast.makeText(context, "Processing your order...", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error starting order service: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                    
                    // Also create order in Room database
                    orderViewModel.createOrder(
                        userId = null, // Can be linked to logged-in user later
                        deliveryAddress = address,
                        paymentMethod = selectedPaymentMethod,
                        cardNumber = if (selectedPaymentMethod == "Card") cardNumber.takeLast(4) else null,
                        promoCode = if (isPromoApplied) promoCode else null,
                        subtotal = subtotal,
                        vat = vat,
                        shippingFee = shippingFee,
                        total = total,
                        items = orderItems
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .background(
                        color = Color(0xCC434819),
                        shape = RoundedCornerShape(10.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(10.dp),
                enabled = !orderState.isLoading && cartItems.isNotEmpty()
            ) {
                Text(
                    if (orderState.isLoading) "Placing Order..." else "Place Order",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Show error message if any
            orderState.errorMessage?.let { error ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CheckoutTopBar(onBack: () -> Unit, onNotification: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Olive,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            "Checkout",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Olive
        )
        IconButton(
            onClick = onNotification,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = Olive,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun DeliveryAddressSection(
    address: String,
    onAddressChange: (String) -> Unit,
    isEditing: Boolean,
    onEditToggle: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Delivery Address",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Neutral
            )
            Text(
                if (isEditing) "Save" else "Change",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Olive,
                modifier = Modifier.clickable { onEditToggle() }
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = "Location",
                tint = Grey,
                modifier = Modifier.size(24.dp)
            )
            if (isEditing) {
                OutlinedTextField(
                    value = address,
                    onValueChange = onAddressChange,
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    placeholder = {
                        Text(
                            "Enter your address",
                            fontSize = 14.sp,
                            color = Grey
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Neutral,
                        unfocusedTextColor = Neutral,
                        focusedIndicatorColor = Olive,
                        unfocusedIndicatorColor = Olive.copy(alpha = 0.6f),
                        cursorColor = Olive,
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = false,
                    maxLines = 3
                )
            } else {
                Column {
                    Text(
                        "Home",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Neutral
                    )
                    Text(
                        address,
                        fontSize = 14.sp,
                        color = Grey
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodSection(
    selectedMethod: String,
    onMethodSelected: (String) -> Unit,
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    isEditingCard: Boolean,
    onEditCardToggle: () -> Unit
) {
    Column {
        Text(
            "Payment Method",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Neutral
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentMethodButton(
                label = "Card",
                iconRes = R.drawable.ic_card,
                selected = selectedMethod == "Card",
                onClick = { onMethodSelected("Card") }
            )
            PaymentMethodButton(
                label = "Cash",
                iconRes = R.drawable.ic_cash,
                selected = selectedMethod == "Cash",
                onClick = { onMethodSelected("Cash") }
            )
            PaymentMethodButton(
                label = "Pay",
                iconRes = R.drawable.ic_apple_pay,
                selected = selectedMethod == "Pay",
                onClick = { onMethodSelected("Pay") }
            )
        }
        Spacer(Modifier.height(16.dp))
        if (selectedMethod == "Card") {
            CardDetailsField(
                cardNumber = cardNumber,
                onCardNumberChange = onCardNumberChange,
                isEditing = isEditingCard,
                onEditToggle = onEditCardToggle
            )
        }
    }
}

@Composable
private fun PaymentMethodButton(
    label: String,
    iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Color(0xCC434819) else Color.Transparent,
        border = if (selected) null else BorderStroke(1.dp, Olive)
    ) {
        Row(
            modifier = Modifier
                .height(36.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = if (selected) Color.White else Neutral,
                modifier = Modifier.size(24.dp)
            )
            Text(
                label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (selected) Color.White else Neutral
            )
        }
    }
}

@Composable
private fun CardDetailsField(
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    isEditing: Boolean,
    onEditToggle: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Olive),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_visa),
                    contentDescription = "Visa",
                    modifier = Modifier.size(44.dp, 14.dp)
                )
                Text(
                    if (isEditing) "Edit Card Number" else "Card Number",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Neutral,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = if (isEditing) "Save" else "Edit",
                    tint = Olive,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onEditToggle() }
                )
            }
            Spacer(Modifier.height(12.dp))
            if (isEditing) {
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { newValue: String ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 16) {
                            onCardNumberChange(newValue)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Enter card number",
                            fontSize = 14.sp,
                            color = Grey
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Neutral,
                        unfocusedTextColor = Neutral,
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

            } else {
                Text(
                    formatCardNumber(cardNumber),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Neutral
                )
            }
        }
    }
}

@Composable
private fun OrderSummarySection(
    subtotal: Double,
    vat: Double,
    shippingFee: Double,
    total: Double
) {
    Column {
        Text(
            "Order Summary",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Neutral
        )
        Spacer(Modifier.height(20.dp))
        SummaryRow(label = "Sub-total", value = subtotal)
        Spacer(Modifier.height(12.dp))
        SummaryRow(label = "VAT (%)", value = vat)
        Spacer(Modifier.height(12.dp))
        SummaryRow(label = "Shipping fee", value = shippingFee)
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(color = Olive.copy(alpha = 0.4f))
        Spacer(Modifier.height(16.dp))
        SummaryRow(label = "Total", value = total, bold = true)
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
            text = "$${String.format(Locale.getDefault(), "%.2f", value)}",
            fontSize = 16.sp,
            fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Medium,
            color = Neutral
        )
    }
}

@Composable
private fun PromoCodeSection(
    promoCode: String,
    onPromoCodeChange: (String) -> Unit,
    onApplyPromo: () -> Unit,
    isPromoApplied: Boolean
) {
    Column {
        Text(
            "Promo Code",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Neutral
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = promoCode,
                onValueChange = onPromoCodeChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Enter promo code",
                        fontSize = 16.sp,
                        color = Grey
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_discount),
                        contentDescription = "Discount",
                        tint = if (promoCode.isNotEmpty()) Olive else LightGrey
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Neutral,
                    unfocusedTextColor = Neutral,
                    focusedIndicatorColor = Olive,
                    unfocusedIndicatorColor = Olive.copy(alpha = 0.6f),
                    cursorColor = Olive,
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            // Fixed: Applied button keeps the same size as Apply button
            Box(
                modifier = Modifier
                    .width(84.dp)
                    .height(56.dp)
                    .background(
                        color = if (isPromoApplied) Color(0xFF2E7D32) else Color(0xCC434819),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(
                        enabled = !isPromoApplied && promoCode.isNotEmpty(),
                        onClick = onApplyPromo
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isPromoApplied) "Applied" else "Apply",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (isPromoApplied) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Promo code applied successfully!",
                fontSize = 14.sp,
                color = Olive,
                fontWeight = FontWeight.Medium
            )
        }
    }
}