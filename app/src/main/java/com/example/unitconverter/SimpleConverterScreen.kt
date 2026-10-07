package com.example.unitconverter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// =========================================================
// COLORS
// =========================================================
val TopBarLightPurple1 = Color(0xFF9575CD)   // Light purple
val TopBarDarkPurple1 = Color(0xFF4527A0)    // Dark purple
val ScreenWhite1 = Color(0xFFFFFFFF)
val TextBlack = Color(0xFF000000)
val PrimaryPurple1 = Color(0xFF7E57C2)        // Labels + Dark button
val TextGray1 = Color(0xFF757575)
val FocusedPurple = Color(0xFFB39DDB)        // Light primary purple (focus)
val UnfocusedGray = Color(0xFFBDBDBD)        // Border grey
val ButtonLightPurple1 = Color(0xFFB39DDB)    // Button normal
val ButtonDarkPurple1 = Color(0xFF5E35B1)     // Button pressed/active

// =========================================================
//  MAIN SCREEN
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleConverterScreen(onBack: () -> Unit) {

    // ---- STATE ----
    var inputValue by remember { mutableStateOf("42") }
    var resultValue by remember { mutableStateOf("3.4999986") }
    var fromUnit by remember { mutableStateOf("Inches") }
    var toUnit by remember { mutableStateOf("Foot") }

    var isClearPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(TopBarLightPurple1, TopBarDarkPurple1)
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Simple Converter",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ScreenWhite1)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // =====================================================
            //  INCHES (INPUT)
            // =====================================================
            Text(
                text = fromUnit,
                color = PrimaryPurple1,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = inputValue,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        inputValue = newValue
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 20.sp,
                    color = TextBlack
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FocusedPurple,
                    unfocusedBorderColor = UnfocusedGray,
                    cursorColor = FocusedPurple,
                    focusedTextColor = TextBlack,
                    unfocusedTextColor = TextBlack,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // =====================================================
            //  FOOT (RESULT)
            // =====================================================
            Text(
                text = toUnit,
                color = PrimaryPurple1,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = resultValue,
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 20.sp,
                    color = TextGray1
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = UnfocusedGray,
                    unfocusedBorderColor = UnfocusedGray,
                    disabledBorderColor = UnfocusedGray,
                    focusedTextColor = TextGray1,
                    unfocusedTextColor = TextGray1,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // =====================================================
            //  CONVERT + CLEAR Buttons
            // =====================================================
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ---- CONVERT Button ----
                val convertColor = if (inputValue.isEmpty()) {
                    ButtonLightPurple1
                } else {
                    ButtonDarkPurple1
                }

                Button(
                    onClick = {
                        val v = inputValue.toDoubleOrNull() ?: 0.0
                        val inchesToFoot = v / 12.0
                        resultValue = formatResult1(inchesToFoot)
                    },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = convertColor,
                        contentColor = Color.White          // 🆕 Text white
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp),
                    modifier = Modifier
                        .width(110.dp)
                        .height(36.dp)
                ) {
                    Text(
                        "CONVERT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White                 // 🆕 Text white
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ---- CLEAR Button ----
                val clearColor = if (isClearPressed) {
                    ButtonDarkPurple1
                } else {
                    ButtonLightPurple1
                }

                Button(
                    onClick = {
                        // Value clear karo
                        inputValue = ""
                        resultValue = ""


                        scope.launch {
                            isClearPressed = true
                            delay(1000L)
                            isClearPressed = false
                        }
                    },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = clearColor,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp),
                    modifier = Modifier
                        .width(90.dp)
                        .height(36.dp)
                ) {
                    Text(
                        "CLEAR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

// =========================================================
// HELPER: Format Result
// =========================================================
fun formatResult1(value: Double): String {
    return if (value == value.toLong().toDouble()) {
        value.toLong().toString()
    } else {
        value.toString()
    }
}

// =========================================================
// PREVIEW
// =========================================================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SimpleConverterPreview() {
    MaterialTheme {
        SimpleConverterScreen(onBack = {})
    }
}