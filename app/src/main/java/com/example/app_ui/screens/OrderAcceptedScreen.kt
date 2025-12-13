package com.example.app_ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_ui.R

private val SuccessGreen = Color(0xFF53B175)
private val Neutral = Color(0xFF1A1A1A)
private val Grey = Color(0xFF7C7C7C)
private val DarkText = Color(0xFF181725)

@Composable
fun OrderAcceptedScreen(
    onBackToHome: () -> Unit,
    onTrackOrder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Spacer(Modifier.weight(1f))

        // Success Icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(228.dp),
                contentAlignment = Alignment.Center
            ) {
                // Green circle background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SuccessGreen, CircleShape)
                )

                // Shopping bag image (with checkmark already included)
                Image(
                    painter = painterResource(id = R.drawable.img_shoppping_bag),
                    contentDescription = "Order accepted",
                    modifier = Modifier.size(250.dp)
                )
            }
        }

        Spacer(Modifier.height(60.dp))

        // Title
        Text(
            text = "Your Order has been\naccepted",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Description
        Text(
            text = "Your items has been placed and is on\nit's way to being processed",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Grey,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Track Order Button (now shows saved orders)
            Button(
                onClick = onTrackOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp)
                    .background(
                        color = SuccessGreen,
                        shape = RoundedCornerShape(19.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    "View My Orders",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Back to Home Button
            Surface(
                onClick = onBackToHome,
                shape = RoundedCornerShape(19.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, Grey.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Back to home",
                        color = DarkText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}
