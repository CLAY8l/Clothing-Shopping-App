package com.example.app_ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_ui.R
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.Alignment

val Inter = FontFamily(
    Font(R.font.inter_black, FontWeight.Black),
)

@Composable
fun OnBordingScreen(
    onContinue: () -> Unit,
    onGetStartedClicked: () -> Unit = {} // Add click handler parameter
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF434819))
    ) {
        Column(
            modifier = Modifier.offset(x = 70.dp, y = 45.dp)
        ) {
            Text(
                text = "Let's",
                color = Color(0xFFEBE3C3),
                fontFamily = Inter,
                fontWeight = FontWeight.Black,
                fontSize = 120.sp,
                lineHeight = 100.sp
            )

            Text(
                text = "Do It.",
                color = Color(0xFFEBE3C3),
                fontFamily = Inter,
                fontWeight = FontWeight.Black,
                fontSize = 120.sp,
                lineHeight = 100.sp
            )
        }

        Image(
            painter = painterResource(id = R.drawable.onbording),
            contentDescription = "onbordingImage",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .offset(0.dp, 300.dp)
                .fillMaxWidth()
                .height(800.dp)
                .clip(RoundedCornerShape(topStart = 200.dp, topEnd = 200.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
        )

        Button(
            onClick = onGetStartedClicked, // Use the passed click handler
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF434819)),
            shape = RoundedCornerShape(52.dp),
            modifier = Modifier
                .width(356.dp)
                .height(79.dp)
                .offset(x = 30.dp, y = 780.dp)
                .alpha(0.78f),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 12.dp),
            ) {
                Text(
                    text = "Get Started",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Center)
                )

                Box(
                    modifier = Modifier
                        .size(59.dp)
                        .align(Alignment.CenterEnd)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Arrow",
                        tint = Color(0xFF434819),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}