package com.example.unitconverter

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {

    // ---------- Lottie ----------
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash_animation)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 1f,
        restartOnPlay = true
    )

    // ---------- Text Animations ----------
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(40f) }

    LaunchedEffect(Unit) {
        textAlpha.animateTo(1f, tween(900))
        textOffsetY.animateTo(0f, tween(900, easing = EaseOutCubic))
    }

    // Pulsing glow
    val infinite = rememberInfiniteTransition(label = "glow")
    val glow by infinite.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // ---------- Splash timer ----------
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(5000L)
        showSplash = false
    }

    // ---------- Screen Switch ----------
    if (showSplash) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEDE7F6),   // very light lavender (top)
                            Color(0xFFD1C4E9),   // light purple
                            Color(0xFFB39DDB)    // soft purple (bottom)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            // ============================================
            //  Decorative glow circles (subtle)
            // ============================================
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                // Top-left glow (soft white-purple)
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .offset(x = (-90).dp, y = (-80).dp)
                        .blur(90.dp)
                        .background(
                            Color(0xFFFFFFFF).copy(alpha = 0.4f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
                // Bottom-right glow (deeper purple hint)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(240.dp)
                        .offset(x = 110.dp, y = 90.dp)
                        .blur(100.dp)
                        .background(
                            Color(0xFF7E57C2).copy(alpha = 0.25f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
            }

            // ============================================
            //  LOTTIE ANIMATION (center)
            // ============================================
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(280.dp)
                    .offset(y = (-60).dp),
                contentScale = ContentScale.Fit
            )

            // ============================================
            //  STYLISH TEXT (bottom) — Dark purple for contrast
            // ============================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
                    .alpha(textAlpha.value)
                    .graphicsLayer(translationY = textOffsetY.value),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {

                // ---------- Main Title with GLOW ----------
                Box(contentAlignment = Alignment.Center) {

                    // Glow layer
                    Text(
                        text = "Unit Converter",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.SansSerif,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF7E57C2),
                                    Color(0xFF5E35B1),
                                    Color(0xFF9575CD)
                                )
                            ),
                            shadow = Shadow(
                                color = Color(0xFF7E57C2).copy(alpha = glow * 0.5f),
                                offset = androidx.compose.ui.geometry.Offset(0f, 0f),
                                blurRadius = 30f
                            )
                        )
                    )

                    // Main visible text (dark purple)
                    Text(
                        text = "Unit Converter",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.SansSerif,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF5E35B1),
                                    Color(0xFF7E57C2),
                                    Color(0xFF8E24AA)
                                )
                            ),
                            shadow = Shadow(
                                color = Color.White.copy(alpha = 0.6f),
                                offset = androidx.compose.ui.geometry.Offset(0f, 2f),
                                blurRadius = 8f
                            )
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ---------- Tagline with purple accent ----------
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF7E57C2).copy(alpha = 0.18f),
                                    Color(0xFF5E35B1).copy(alpha = 0.18f)
                                )
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(50)
                        )
                        .padding(horizontal = 22.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "CONVERT ANYTHING · ANYWHERE",
                        color = Color(0xFF5E35B1),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    } else {
        AppNavigator()
    }
}