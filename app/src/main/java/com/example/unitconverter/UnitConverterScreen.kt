package com.example.unitconverter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =========================================================
//  DATA MODELS
// =========================================================
data class UnitItem(
    val name: String,
    val factor: Double
)

data class Category(
    val name: String,
    val units: List<UnitItem>,
    val isTemperature: Boolean = false
)

// =========================================================
//  ALL CATEGORIES & UNITS
// =========================================================
val categories = listOf(

    Category(
        name = "Length",
        units = listOf(
            UnitItem("Meter",      1.0),
            UnitItem("Kilometer",  1000.0),
            UnitItem("Centimeter", 0.01),
            UnitItem("Millimeter", 0.001),
            UnitItem("Mile",       1609.34),
            UnitItem("Yard",       0.9144),
            UnitItem("Foot",       0.3048),
            UnitItem("Inch",       0.0254)
        )
    ),

    Category(
        name = "Weight",
        units = listOf(
            UnitItem("Kilogram",  1.0),
            UnitItem("Gram",      0.001),
            UnitItem("Milligram", 0.000001),
            UnitItem("Pound",     0.453592),
            UnitItem("Ounce",     0.0283495),
            UnitItem("Ton",       1000.0)
        )
    ),

    Category(
        name = "Temperature",
        units = listOf(
            UnitItem("Celsius",    1.0),
            UnitItem("Fahrenheit", 1.0),
            UnitItem("Kelvin",     1.0)
        ),
        isTemperature = true
    ),

    Category(
        name = "Area",
        units = listOf(
            UnitItem("Square Meter",      1.0),
            UnitItem("Square Kilometer",  1_000_000.0),
            UnitItem("Square Foot",       0.092903),
            UnitItem("Square Yard",       0.836127),
            UnitItem("Acre",              4046.86),
            UnitItem("Hectare",           10_000.0)
        )
    ),

    Category(
        name = "Volume",
        units = listOf(
            UnitItem("Liter",       1.0),
            UnitItem("Milliliter",  0.001),
            UnitItem("Cubic Meter", 1000.0),
            UnitItem("Gallon (US)", 3.78541),
            UnitItem("Cup",         0.236588),
            UnitItem("Pint",        0.473176)
        )
    ),

    Category(
        name = "Speed",
        units = listOf(
            UnitItem("Meter/sec",      1.0),
            UnitItem("Kilometer/hour", 0.277778),
            UnitItem("Mile/hour",      0.44704),
            UnitItem("Foot/sec",       0.3048),
            UnitItem("Knot",           0.514444)
        )
    )
)

// =========================================================
//  MAIN SCREEN
// =========================================================
@Composable
fun UnitConverterScreen(onBack: () -> Unit) {

    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var fromUnit by remember { mutableStateOf(categories[0].units[0]) }
    var toUnit   by remember { mutableStateOf(categories[0].units[1]) }
    var inputValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    LaunchedEffect(selectedCategory) {
        fromUnit = selectedCategory.units[0]
        toUnit   = selectedCategory.units.getOrNull(1) ?: selectedCategory.units[0]
        inputValue = ""
        result = ""
    }

    // ---------- AUTO CONVERT — NO formatNumbers ----------
    LaunchedEffect(inputValue, fromUnit, toUnit, selectedCategory) {
        val value = inputValue.toDoubleOrNull()
        result = when {
            value == null -> ""
            selectedCategory.isTemperature -> {
                convertTemperature(value, fromUnit.name, toUnit.name)
            }
            else -> {
                val base = value * fromUnit.factor
                val converted = base / toUnit.factor
                converted.toString()
            }
        }
    }

    Scaffold(
        topBar = { CustomTopAppBar(onBack) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            CategoryCard(
                selected = selectedCategory,
                onSelect = { selectedCategory = it }
            )

            Spacer(Modifier.height(16.dp))

            InputCard(
                inputValue = inputValue,
                onValueChange = { inputValue = it },
                fromUnit = fromUnit,
                onFromUnitChange = { fromUnit = it },
                availableUnits = selectedCategory.units
            )

            Spacer(Modifier.height(16.dp))

            ToUnitCard(
                toUnit = toUnit,
                onToUnitChange = { toUnit = it },
                availableUnits = selectedCategory.units
            )

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEDE7F6)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Result",
                        color = Color(0xFF7E57C2),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = if (result.isEmpty()) "0" else "$result ${toUnit.name}",
                        color = Color(0xFF5E35B1),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

// =========================================================
//  TEMPERATURE CONVERSION — Simple toString
// =========================================================
private fun convertTemperature(value: Double, from: String, to: String): String {
    val celsius = when (from) {
        "Celsius"    -> value
        "Fahrenheit" -> (value - 32) * 5.0 / 9.0
        "Kelvin"     -> value - 273.15
        else         -> value
    }

    val converted = when (to) {
        "Celsius"    -> celsius
        "Fahrenheit" -> celsius * 9.0 / 5.0 + 32
        "Kelvin"     -> celsius + 273.15
        else         -> celsius
    }

    return converted.toString()
}

// =========================================================
//  TOP APP BAR
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Unit Converter",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        modifier = Modifier.background(
            brush = Brush.horizontalGradient(
                colors = listOf(Color(0xFFB39DDB), Color(0xFF5E35B1))
            )
        ),
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.LightMode, "Theme", tint = Color.White)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.MoreVert, "Menu", tint = Color.White)
            }
        }
    )
}

// =========================================================
//  CATEGORY CARD
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    selected: Category,
    onSelect: (Category) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Select Category",
                color = Color(0xFF7E57C2),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(14.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selected.name,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7E57C2),
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                onSelect(category)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

// =========================================================
//  INPUT CARD
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputCard(
    inputValue: String,
    onValueChange: (String) -> Unit,
    fromUnit: UnitItem,
    onFromUnitChange: (UnitItem) -> Unit,
    availableUnits: List<UnitItem>
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Input",
                color = Color(0xFF7E57C2),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Text(text = "From Unit", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = fromUnit.name,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7E57C2),
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableUnits.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit.name) },
                            onClick = {
                                onFromUnitChange(unit)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = inputValue,
                onValueChange = { new ->
                    val pattern = if (availableUnits.any { it.name == "Celsius" })
                        Regex("^-?\\d*\\.?\\d*$")
                    else
                        Regex("^\\d*\\.?\\d*$")

                    if (new.isEmpty() || new.matches(pattern)) {
                        onValueChange(new)
                    }
                },
                placeholder = { Text("Enter value") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7E57C2),
                    unfocusedBorderColor = Color.LightGray
                )
            )
        }
    }
}

// =========================================================
//  TO UNIT CARD
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToUnitCard(
    toUnit: UnitItem,
    onToUnitChange: (UnitItem) -> Unit,
    availableUnits: List<UnitItem>
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Convert To",
                color = Color(0xFF7E57C2),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Text(text = "To Unit", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = toUnit.name,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7E57C2),
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableUnits.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit.name) },
                            onClick = {
                                onToUnitChange(unit)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

// =========================================================
//  PREVIEW
// =========================================================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UnitConverterPreview() {
    MaterialTheme {
        UnitConverterScreen(onBack = {})
    }
}