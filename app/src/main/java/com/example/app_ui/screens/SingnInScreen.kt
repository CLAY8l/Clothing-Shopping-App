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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.app_ui.R

private val PrimaryOlive = Color(0xFF434819)
private val TextPrimary = Color(0xFF1A1A1A)
private val TextMuted = Color(0xFF808080)
private val HintColor = Color(0xFF999999)
private val DividerColor = Color(0xFFE0E0E0)
private val FacebookBlue = Color(0xFF1877F2)

private val GeneralSans = FontFamily(
    Font(R.font.general_sans_regular, FontWeight.Normal),
    Font(R.font.general_sans_medium, FontWeight.Medium),
    Font(R.font.general_sans_semibold, FontWeight.SemiBold)
)

@Immutable
data class SignUpUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
)

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState = SignUpUiState(),
    onFullNameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onTogglePasswordVisibility: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onCookieClick: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    onGoogleSignUp: () -> Unit = {},
    onFacebookSignUp: () -> Unit = {},
    onLogin: () -> Unit = {},
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
            SignUpFields(
                uiState = uiState,
                onFullNameChange = onFullNameChange,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onTogglePasswordVisibility = onTogglePasswordVisibility
            )
            Spacer(modifier = Modifier.height(16.dp))
            TermsText(
                onTermsClick = onTermsClick,
                onPrivacyClick = onPrivacyClick,
                onCookieClick = onCookieClick
            )
            
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
                text = if (isLoading) "Creating Account..." else "Create an Account",
                onClick = onCreateAccount,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(24.dp))
            DividerRow()
            Spacer(modifier = Modifier.height(24.dp))
            SocialButton(
                text = "Sign Up with Google",
                logoRes = R.drawable.google_logo,
                backgroundColor = Color.Transparent,
                borderColor = DividerColor,
                textColor = TextPrimary,
                onClick = onGoogleSignUp
            )
            Spacer(modifier = Modifier.height(16.dp))
            SocialButton(
                text = "Sign Up with Facebook",
                logoRes = R.drawable.facebook_logo,
                backgroundColor = FacebookBlue,
                borderColor = Color.Transparent,
                textColor = Color.White,
                onClick = onFacebookSignUp
            )
            Spacer(modifier = Modifier.weight(1f))
            Footer(onLogin = onLogin)
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Create an account",
            fontFamily = GeneralSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            color = PrimaryOlive,
            letterSpacing = (-1.6).sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Let's create your account.",
            fontFamily = GeneralSans,
            fontSize = 16.sp,
            color = TextPrimary
        )
    }
}

@Composable
private fun SignUpFields(
    uiState: SignUpUiState,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        InputField(
            label = "Full Name",
            value = uiState.fullName,
            placeholder = "Enter your full name",
            onTextChange = onFullNameChange
        )
        Spacer(modifier = Modifier.height(16.dp))
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
                    color = HintColor
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(
                fontFamily = GeneralSans,
                fontSize = 16.sp,
                color = TextPrimary
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryOlive,
                unfocusedBorderColor = PrimaryOlive,
                cursorColor = PrimaryOlive,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
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
            visualTransformation = if (isPassword && !isPasswordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }
        )
    }
}

@Composable
private fun TermsText(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onCookieClick: () -> Unit
) {
    Text(
        text = buildAnnotatedString {
            // Regular text
            append("By signing up you agree to our ")

            // Bold black text for "Terms"
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            ) {
                append("Terms")
            }

            // Regular text
            append(", ")

            // Bold black text for "Privacy Policy"
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            ) {
                append("Privacy Policy")
            }

            // Regular text
            append(", and ")

            // Bold black text for "Cookie Use"
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            ) {
                append("Cookie Use")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTermsClick() },
        fontFamily = GeneralSans,
        fontSize = 14.sp,
        color = TextMuted, // Regular text color
        textAlign = TextAlign.Start
    )
}

@Composable
private fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
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
        Divider(modifier = Modifier.weight(1f), color = PrimaryOlive, thickness = 1.dp)
        Text(
            text = "Or",
            modifier = Modifier.padding(horizontal = 12.dp),
            fontFamily = GeneralSans,
            fontSize = 14.sp,
            color = PrimaryOlive
        )
        Divider(modifier = Modifier.weight(1f), color = PrimaryOlive, thickness = 1.dp)
    }
}

@Composable
private fun SocialButton(
    text: String,
    logoRes: Int,
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
            painter = painterResource(logoRes),
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
private fun Footer(onLogin: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Already have an account? ",
            fontFamily = GeneralSans,
            fontSize = 16.sp,
            color = PrimaryOlive
        )
        Text(
            text = "Log In",
            fontFamily = GeneralSans,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = TextPrimary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onLogin() }
        )
    }
}


@Composable
fun SignUpScreenWithState(
    onSignUpClick: () -> Unit,
    onSignUpSuccess: () -> Unit,
    onBack: () -> Unit
)
{
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    SignUpScreen(
        uiState = SignUpUiState(
            fullName = fullName,
            email = email,
            password = password,
            isPasswordVisible = isPasswordVisible
        ),
        onFullNameChange = { fullName = it },
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
        onTermsClick = { /* Handle terms click */ },
        onPrivacyClick = { /* Handle privacy policy click */ },
        onCookieClick = { /* Handle cookie policy click */ },
        onCreateAccount = { /* Handle account creation */ },
        onGoogleSignUp = { /* Handle Google sign up */ },
        onFacebookSignUp = { /* Handle Facebook sign up */ },
        onLogin = { /* Handle login navigation */ }
    )
}
