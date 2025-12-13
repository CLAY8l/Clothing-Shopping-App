package com.example.app_ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_ui.R

private val PrimaryOlive = Color(0xFF434819)
private val TextPrimary = Color(0xFF1A1A1A)
private val TextMuted = Color(0xFF808080)
private val TextHint = Color(0xFF999999)
private val DividerColor = Color(0xFFBDBDBD)
private val FacebookBlue = Color(0xFF1877F2)

private val GeneralSans = FontFamily(
    Font(R.font.general_sans_regular, FontWeight.Normal),
    Font(R.font.general_sans_medium, FontWeight.Medium),
    Font(R.font.general_sans_semibold, FontWeight.SemiBold)
)

@Immutable
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
)

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginUiState = LoginUiState(),
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onTogglePasswordVisibility: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onLogin: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
    onFacebookLogin: () -> Unit = {},
    onJoin: () -> Unit = {},
    errorMessage: String? = null,
    isLoading: Boolean = false
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection()
            Spacer(modifier = Modifier.height(32.dp))
            InputField(
                label = "Email",
                value = uiState.email,
                placeholder = "Enter your email address",
                onTextChange = onEmailChange
            )
            Spacer(modifier = Modifier.height(16.dp))
            InputField(
                label = "Password",
                value = uiState.password,
                placeholder = "Enter your password",
                onTextChange = onPasswordChange,
                isPassword = true,
                isPasswordVisible = uiState.isPasswordVisible,
                onToggleVisibility = onTogglePasswordVisibility
            )
            Spacer(modifier = Modifier.height(12.dp))
            ForgotPassword(onForgotPassword)
            
            // Error message
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    fontFamily = GeneralSans,
                    fontSize = 14.sp,
                    color = Color(0xFFED1010),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                onClick = onLogin,
                enabled = !isLoading,
                text = if (isLoading) "Logging in..." else "Login"
            )
            Spacer(modifier = Modifier.height(24.dp))
            DividerRow()
            Spacer(modifier = Modifier.height(24.dp))
            SocialButton(
                text = "Login with Google",
                logo = R.drawable.google_logo,
                backgroundColor = Color.Transparent,
                borderColor = DividerColor,
                textColor = TextPrimary,
                onClick = onGoogleLogin
            )
            Spacer(modifier = Modifier.height(16.dp))
            SocialButton(
                text = "Login with Facebook",
                logo = R.drawable.facebook_logo,
                backgroundColor = FacebookBlue,
                borderColor = Color.Transparent,
                textColor = Color.White,
                onClick = onFacebookLogin
            )

            // Removed the entered data display box
            Spacer(modifier = Modifier.weight(1f))
            Footer(onJoin = onJoin)
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Login to your account",
            fontFamily = GeneralSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            color = PrimaryOlive,
            letterSpacing = (-1.6).sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "It's great to see you again.",
            fontFamily = GeneralSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = TextPrimary
        )
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    placeholder: String,
    onTextChange: (String) -> Unit,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onToggleVisibility: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = GeneralSans,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    fontFamily = GeneralSans,
                    fontSize = 16.sp,
                    color = TextHint
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryOlive,
                unfocusedBorderColor = PrimaryOlive,
                cursorColor = PrimaryOlive,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontFamily = GeneralSans,
                fontSize = 16.sp,
                color = TextPrimary
            ),
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onToggleVisibility) {
                        Image(
                            painter = painterResource(
                                if (isPasswordVisible) R.drawable.eye_icon_visible else R.drawable.eye_icon
                            ),
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None
        )
    }
}

@Composable
private fun ForgotPassword(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Forgot your password? ",
            fontFamily = GeneralSans,
            fontSize = 14.sp,
            color = TextPrimary
        )
        Text(
            text = "Reset your password",
            fontFamily = GeneralSans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = TextPrimary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onClick() }
        )
    }
}

@Composable
private fun PrimaryButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    text: String = "Login"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOlive)
    ) {
        Text(
            text = text,
            fontFamily = GeneralSans,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
private fun DividerRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = PrimaryOlive,
            thickness = 1.dp
        )
        Text(
            text = "Or",
            modifier = Modifier.padding(horizontal = 12.dp),
            fontFamily = GeneralSans,
            fontSize = 14.sp,
            color = TextMuted
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = PrimaryOlive,
            thickness = 1.dp
        )
    }
}

@Composable
private fun SocialButton(
    text: String,
    logo: Int,
    backgroundColor: Color,
    borderColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(
                width = if (borderColor != Color.Transparent) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(logo),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
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
private fun Footer(onJoin: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Don't have an account? ",
            fontFamily = GeneralSans,
            fontSize = 16.sp,
            color = PrimaryOlive
        )
        Text(
            text = "Join",
            fontFamily = GeneralSans,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = TextPrimary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onJoin() }
        )
    }
}

// Usage example with state management
@Composable
fun LoginScreenWithState(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit
)
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    LoginScreen(
        uiState = LoginUiState(
            email = email,
            password = password,
            isPasswordVisible = isPasswordVisible
        ),
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
        onForgotPassword = { /* Handle forgot password */ },
        onLogin = { /* Handle login */ },
        onGoogleLogin = { /* Handle Google login */ },
        onFacebookLogin = { /* Handle Facebook login */ },
        onJoin = { /* Handle join */ }
    )
}