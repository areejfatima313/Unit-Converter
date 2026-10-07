package com.example.unitconverter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =========================================================
//  MENU COLORS
// =========================================================
val MenuBg = Color(0xFFF5F5F5)
val MenuCard = Color(0xFFFFFFFF)
val MenuPrimary = Color(0xFF7E57C2)
val MenuLightPurple = Color(0xFF9575CD)
val MenuDarkPurple = Color(0xFF4527A0)

// =========================================================
//  APP NAVIGATOR - 5 SCREENS
// =========================================================
@Composable
fun AppNavigator() {
    var currentScreen by remember { mutableStateOf("menu") }

    when (currentScreen) {
        "menu"    -> MenuScreen(onNavigate = { currentScreen = it })
        "screen1" -> UnitConverterScreen(onBack = { currentScreen = "menu" })   // Cards wali
        "screen2" -> CompactConverterScreen(onBack = { currentScreen = "menu" })  // Categories wali
        "screen3" -> ConverterScreenWithViewModel(onBack = { currentScreen = "menu" })  // ViewModel wali
        "screen4" -> TemperatureConverterScreen(onBack = { currentScreen = "menu" })  // Temperature wali
        "screen5" -> SimpleConverterScreen(onBack = { currentScreen = "menu" }) // Inches wali
    }
}

// =========================================================
//  MENU SCREEN - 5 Cards
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(onNavigate: (String) -> Unit) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(MenuLightPurple, MenuDarkPurple)
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Text(
                    text = "Unit Converter",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = MenuBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Choose a Converter",
                color = MenuPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            MenuCardItem(
                title = "Cards Style",
                subtitle = "Category + Input cards",
                route = "screen1",
                onNavigate = onNavigate
            )

            MenuCardItem(
                title = "Categories",
                subtitle = "Multi-unit converter",
                route = "screen2",
                onNavigate = onNavigate
            )

            MenuCardItem(
                title = "Volume",
                subtitle = "ViewModel based converter",
                route = "screen3",
                onNavigate = onNavigate
            )

            MenuCardItem(
                title = "Temperature",
                subtitle = "Celsius • Fahrenheit • Kelvin",
                route = "screen4",
                onNavigate = onNavigate
            )

            MenuCardItem(
                title = "Inches to Foot",
                subtitle = "Simple length converter",
                route = "screen5",
                onNavigate = onNavigate
            )
        }
    }
}

// =========================================================
//  MENU CARD ITEM
// =========================================================
@Composable
fun MenuCardItem(
    title: String,
    subtitle: String,
    route: String,
    onNavigate: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate(route) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MenuCard),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = MenuDarkPurple,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MenuPrimary
            )
        }
    }
}