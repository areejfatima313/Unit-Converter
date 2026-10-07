package com.example.unitconverter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =========================================================
// Colors
// =========================================================

val LabelDark = Color(0xFF424242)         // From / To labels
val ValueLabelPurple = Color(0xFF7E57C2)  // Value label
val ResultGreen = Color(0xFF4527A0)       // Results text

val DividerPurple =     Color(0xFF7E57C2)  // Text field underline

// =========================================================
//  MAIN SCREEN
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureConverterScreen(onBack: () -> Unit) {

    // ---- STATE ----
    var fromUnit by remember { mutableStateOf("Fahrenheit") }
    var toUnit by remember { mutableStateOf("Fahrenheit") }
    var inputValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val units = listOf("Celsius", "Fahrenheit", "Kelvin")

    // ---- UI ----
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                TopBarLightPurple1,
                                TopBarDarkPurple1
                            )
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
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
                        text = "Unit Converter",
                        color = Color.White,
                        fontSize = 20.sp,
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
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {

            // ---- FROM ROW ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "From",
                    color = LabelDark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(80.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                TempDropdownField(
                    selected = fromUnit,
                    options = units,
                    onSelect = { fromUnit = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ---- TO ROW ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "To",
                    color = LabelDark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(80.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                TempDropdownField(
                    selected = toUnit,
                    options = units,
                    onSelect = { toUnit = it }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ---- VALUE ROW ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Value",
                    color = ValueLabelPurple,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(80.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                            inputValue = newValue
                        }
                    },
                    placeholder = {
                        Text("Enter a Value", color = Color.Gray, fontSize = 14.sp)
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DividerPurple,
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = DividerPurple,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ---- CONVERT BUTTON ----
            // ---- CONVERT BUTTON ----
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {

                val convertButtonColor = if (inputValue.isEmpty()) {
                    ButtonLightPurple1
                } else {
                    ButtonDarkPurple1
                }

                Button(
                    onClick = {
                        result = convertTemperature(inputValue, fromUnit, toUnit)
                    },
                    shape = RoundedCornerShape(8.dp),              // 6.dp rounded
                    colors = ButtonDefaults.buttonColors(
                        containerColor = convertButtonColor,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Text(
                        text = "CONVERT",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // ---- RESULTS ----
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Results",
                    color = ResultGreen,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Result value (agar calculate ho chuka hai)
            if (result.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = result,
                    color = LabelDark,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

// =========================================================
// DROPDOWN FIELD
// =========================================================
@Composable
fun TempDropdownField(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected,
                color = Color(0xFF424242),
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = Color(0xFF424242)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = if (option == selected) Color(0xFF4527A0) else Color(0xFF424242)
                        )
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
// =========================================================
//  CONVERSION LOGIC (Temperature Example)
// =========================================================
fun convertTemperature(input: String, from: String, to: String): String {
    val value = input.toDoubleOrNull() ?: return ""
    if (from == to) return "${formatNumbers(value)} $to"

    // FIRST OF ALL CONVERT TO CENSUS
    val inCelsius = when (from) {
        "Celsius" -> value
        "Fahrenheit" -> (value - 32) * 5 / 9
        "Kelvin" -> value - 273.15
        else -> value
    }

    // CONVERT CENSUS TO TARGET UNIT
    val result = when (to) {
        "Celsius" -> inCelsius
        "Fahrenheit" -> inCelsius * 9 / 5 + 32
        "Kelvin" -> inCelsius + 273.15
        else -> inCelsius
    }

    return "${formatNumbers(result)} $to"
}

fun formatNumbers(value: Double): String {
    return if (value == value.toLong().toDouble()) {
        value.toLong().toString()
    } else {
        String.format("%.2f", value)
    }
}

// =========================================================
//  PREVIEW
// =========================================================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TemperatureConverterPreview() {
    MaterialTheme {
        TemperatureConverterScreen(onBack = {})
    }
}