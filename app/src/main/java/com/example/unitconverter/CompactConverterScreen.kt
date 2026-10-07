package com.example.unitconverter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =========================================================
// MAIN SCREEN
// =========================================================
@Composable
fun CompactConverterScreen(onBack: () -> Unit) {
    // ---- STATE ----
    var selectedCategory by remember { mutableStateOf("Length") }
    var inputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("Meter") }
    var outputUnit by remember { mutableStateOf("Kilometre") }
    var outputValue by remember { mutableStateOf("") }

    val categories = listOf("Length", "Area", "Mass", "Speed", "Angle")
    val units = getUnitsForCategory(selectedCategory)

    // Jab category change ho, units reset karo
    LaunchedEffect(selectedCategory) {
        inputUnit = units.firstOrNull() ?: ""
        outputUnit = units.getOrNull(1) ?: units.firstOrNull() ?: ""
        outputValue = ""
    }

    // Real-time conversion
    LaunchedEffect(inputValue, inputUnit, outputUnit) {
        outputValue = if (inputValue.isNotEmpty()) {
            convertValue(inputValue, inputUnit, outputUnit, selectedCategory)
        } else ""
    }

    // ---- UI ----
    Scaffold(
        topBar = { CustomTopBar(onBack) },
        containerColor = Color(0xFF121212)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Category Tabs
            CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Input Section
            UnitInputSection(
                label = inputUnit,
                value = inputValue,
                onValueChange = { inputValue = it },
                units = units,
                onUnitChange = { inputUnit = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Output Section
            UnitOutputSection(
                label = outputUnit,
                value = outputValue,
                units = units,
                onUnitChange = { outputUnit = it }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// =========================================================
// TOP BAR (Icons EQUAL SPACE mein)
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF121212))
            .padding(top = 60.dp, bottom = 20.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Icon 1 - BACK ARROW
        IconButton(
            onClick = onBack,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // Icon 2 - Language (Selected)
        IconButton(
            onClick = { },
            modifier = Modifier.weight(1f)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language",
                    tint = Color(0xFF7986CB)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .height(2.dp)
                        .background(Color(0xFF7986CB), RoundedCornerShape(1.dp))
                )
            }
        }

        // Icon 3 - Currency
        IconButton(
            onClick = { },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.AttachMoney,
                contentDescription = "Currency",
                tint = Color.White
            )
        }

        // Icon 4 - Menu
        IconButton(
            onClick = { },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu",
                tint = Color.White
            )
        }
    }
}

// =========================================================
// CATEGORY TABS
// =========================================================
@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory

            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) Color(0xFF2C2C2C) else Color(0xFF1E1E1E),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color(0xFF3A3A3A) else Color(0xFF2A2A2A),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFFB39DDB),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = category,
                        color = if (isSelected) Color(0xFFB39DDB) else Color.Gray,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }
    }
}

// =========================================================
// INPUT SECTION
// =========================================================
@Composable
fun UnitInputSection(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    units: List<String>,
    onUnitChange: (String) -> Unit
) {
    Column {
        UnitDropdown(
            selectedUnit = label,
            units = units,
            onUnitSelected = onUnitChange
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 28.sp,
                color = Color.White,
                textAlign = TextAlign.End
            ),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF5C6BC0),
                unfocusedBorderColor = Color(0xFF3A3A3A),
                cursorColor = Color(0xFF5C6BC0)
            )
        )
    }
}

// =========================================================
// OUTPUT SECTION
// =========================================================
@Composable
fun UnitOutputSection(
    label: String,
    value: String,
    units: List<String>,
    onUnitChange: (String) -> Unit
) {
    Column {
        UnitDropdown(
            selectedUnit = label,
            units = units,
            onUnitSelected = onUnitChange
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = { },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 28.sp,
                color = Color.Gray,
                textAlign = TextAlign.End
            ),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF7986CB),
                unfocusedBorderColor = Color(0xFF3A3A3A),
                disabledBorderColor = Color(0xFF3A3A3A)
            )
        )
    }
}

// =========================================================
// UNIT DROPDOWN
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDropdown(
    selectedUnit: String,
    units: List<String>,
    onUnitSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedUnit,
                color = Color.Gray,
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = Color.Gray
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color(0xFF1E1E1E)
        ) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = unit,
                            color = if (unit == selectedUnit) Color(0xFF5C6BC0) else Color.White
                        )
                    },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}

// =========================================================
// HELPER: UNITS PER CATEGORY
// =========================================================
fun getUnitsForCategory(category: String): List<String> {
    return when (category) {
        "Length" -> listOf("Meter", "Kilometre", "Centimetre", "Inch", "Foot", "Mile")
        "Area" -> listOf("Square Meter", "Square Kilometre", "Square Foot", "Acre")
        "Mass" -> listOf("Kilogram", "Gram", "Pound", "Ounce", "Ton")
        "Speed" -> listOf("km/h", "m/s", "mph", "Knot")
        "Angle" -> listOf("Degree", "Radian", "Gradian")
        else -> listOf("Meter", "Kilometre")
    }
}

// =========================================================
// HELPER: CONVERSION LOGIC
// =========================================================
fun convertValue(input: String, from: String, to: String, category: String): String {
    val value = input.toDoubleOrNull() ?: return ""
    if (from == to) return formatResult1(value)

    val result = when (category) {
        "Length" -> convertLength(value, from, to)
        "Area" -> convertArea(value, from, to)
        "Mass" -> convertMass(value, from, to)
        "Speed" -> convertSpeed(value, from, to)
        "Angle" -> convertAngle(value, from, to)
        else -> value
    }
    return formatResult1(result)
}

fun formatResult(value: Double): String {
    return if (value == value.toLong().toDouble()) {
        value.toLong().toString()
    } else {
        String.format("%.4f", value).trimEnd('0').trimEnd('.')
    }
}

fun convertLength(value: Double, from: String, to: String): Double {
    val inMeter = when (from) {
        "Meter" -> value
        "Kilometre" -> value * 1000
        "Centimetre" -> value / 100
        "Inch" -> value * 0.0254
        "Foot" -> value * 0.3048
        "Mile" -> value * 1609.34
        else -> value
    }
    return when (to) {
        "Meter" -> inMeter
        "Kilometre" -> inMeter / 1000
        "Centimetre" -> inMeter * 100
        "Inch" -> inMeter / 0.0254
        "Foot" -> inMeter / 0.3048
        "Mile" -> inMeter / 1609.34
        else -> inMeter
    }
}

fun convertArea(value: Double, from: String, to: String): Double {
    val inSqMeter = when (from) {
        "Square Meter" -> value
        "Square Kilometre" -> value * 1_000_000
        "Square Foot" -> value * 0.092903
        "Acre" -> value * 4046.86
        else -> value
    }
    return when (to) {
        "Square Meter" -> inSqMeter
        "Square Kilometre" -> inSqMeter / 1_000_000
        "Square Foot" -> inSqMeter / 0.092903
        "Acre" -> inSqMeter / 4046.86
        else -> inSqMeter
    }
}

fun convertMass(value: Double, from: String, to: String): Double {
    val inKg = when (from) {
        "Kilogram" -> value
        "Gram" -> value / 1000
        "Pound" -> value * 0.453592
        "Ounce" -> value * 0.0283495
        "Ton" -> value * 1000
        else -> value
    }
    return when (to) {
        "Kilogram" -> inKg
        "Gram" -> inKg * 1000
        "Pound" -> inKg / 0.453592
        "Ounce" -> inKg / 0.0283495
        "Ton" -> inKg / 1000
        else -> inKg
    }
}

fun convertSpeed(value: Double, from: String, to: String): Double {
    val inMs = when (from) {
        "m/s" -> value
        "km/h" -> value / 3.6
        "mph" -> value * 0.44704
        "Knot" -> value * 0.514444
        else -> value
    }
    return when (to) {
        "m/s" -> inMs
        "km/h" -> inMs * 3.6
        "mph" -> inMs / 0.44704
        "Knot" -> inMs / 0.514444
        else -> inMs
    }
}

fun convertAngle(value: Double, from: String, to: String): Double {
    val inDegree = when (from) {
        "Degree" -> value
        "Radian" -> value * 57.2958
        "Gradian" -> value * 0.9
        else -> value
    }
    return when (to) {
        "Degree" -> inDegree
        "Radian" -> inDegree / 57.2958
        "Gradian" -> inDegree / 0.9
        else -> inDegree
    }
}

// =========================================================
// PREVIEW
// =========================================================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CompactConverterPreview() {
    MaterialTheme {
        CompactConverterScreen(onBack = {})
    }
}