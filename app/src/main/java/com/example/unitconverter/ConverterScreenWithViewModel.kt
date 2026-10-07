package com.example.unitconverter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

// =========================================================
// COLORS Dark Theme
// =========================================================
val AppBackground = Color(0xFF1C1B1F)
val PrimaryPurple = Color(0xFF7E57C2)
val LightPurple = Color(0xFFB39DDB)
val DarkPurple = Color(0xFF5E35B1)
val TextDark = Color(0xFFE6E1E5)
val TextGray = Color(0xFF9E9E9E)
val BorderGray = Color(0xFF49454F)
val ResultBg = Color(0xFF2B2930)
val FieldBg = Color(0xFF2B2930)

// =========================================================
// VIEW MODEL
// =========================================================
class UnitConverterViewModel : ViewModel() {

    var selectedCategory by mutableStateOf("Volume")
    var inputValue by mutableStateOf("")
    var fromUnit by mutableStateOf("Cubic Kilometer")
    var toUnit by mutableStateOf("Cubic Meter")
    var result by mutableStateOf("0")

    fun getUnits(category: String): List<String> {
        return when (category) {
            "Length" -> listOf("Meter", "Kilometer", "Centimeter", "Inch", "Foot", "Mile")
            "Volume" -> listOf("Cubic Meter", "Cubic Kilometer", "Liter", "Milliliter", "Gallon")
            "Mass" -> listOf("Kilogram", "Gram", "Pound", "Ounce", "Ton")
            "Speed" -> listOf("m/s", "km/h", "mph", "Knot")
            "Angle" -> listOf("Degree", "Radian", "Gradian")
            else -> listOf("Unit 1", "Unit 2")
        }
    }

    fun onCategoryChange(newCategory: String) {
        selectedCategory = newCategory
        val units = getUnits(newCategory)
        fromUnit = units.firstOrNull() ?: ""
        toUnit = units.getOrNull(1) ?: units.firstOrNull() ?: ""
        result = "0"
    }

    fun swapUnits() {
        val temp = fromUnit
        fromUnit = toUnit
        toUnit = temp
    }

    fun convert() {
        val value = inputValue.toDoubleOrNull() ?: 0.0
        if (value == 0.0) {
            result = "0"
            return
        }

        val converted = when (selectedCategory) {
            "Volume" -> convertVolume(value, fromUnit, toUnit)
            "Length" -> convertLength(value, fromUnit, toUnit)
            "Mass" -> convertMass(value, fromUnit, toUnit)
            "Speed" -> convertSpeed(value, fromUnit, toUnit)
            "Angle" -> convertAngle(value, fromUnit, toUnit)
            else -> value
        }

        result = formatScientific(converted) + " $toUnit"
    }

    private fun convertVolume(value: Double, from: String, to: String): Double {
        val inCubicMeter = when (from) {
            "Cubic Meter" -> value
            "Cubic Kilometer" -> value * 1_000_000_000
            "Liter" -> value / 1000
            "Milliliter" -> value / 1_000_000
            "Gallon" -> value * 0.00378541
            else -> value
        }
        return when (to) {
            "Cubic Meter" -> inCubicMeter
            "Cubic Kilometer" -> inCubicMeter / 1_000_000_000
            "Liter" -> inCubicMeter * 1000
            "Milliliter" -> inCubicMeter * 1_000_000
            "Gallon" -> inCubicMeter / 0.00378541
            else -> inCubicMeter
        }
    }

    private fun convertLength(value: Double, from: String, to: String): Double {
        val inMeter = when (from) {
            "Meter" -> value
            "Kilometer" -> value * 1000
            "Centimeter" -> value / 100
            "Inch" -> value * 0.0254
            "Foot" -> value * 0.3048
            "Mile" -> value * 1609.34
            else -> value
        }
        return when (to) {
            "Meter" -> inMeter
            "Kilometer" -> inMeter / 1000
            "Centimeter" -> inMeter * 100
            "Inch" -> inMeter / 0.0254
            "Foot" -> inMeter / 0.3048
            "Mile" -> inMeter / 1609.34
            else -> inMeter
        }
    }

    private fun convertMass(value: Double, from: String, to: String): Double {
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

    private fun convertSpeed(value: Double, from: String, to: String): Double {
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

    private fun convertAngle(value: Double, from: String, to: String): Double {
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

    private fun formatScientific(value: Double): String {
        if (value == 0.0) return "0"
        return if (value >= 1_000_000 || (value < 0.001 && value > 0)) {
            String.format("%.1E", value).replace("E+0", "E").replace("E+", "E")
        } else {
            if (value == value.toLong().toDouble()) value.toLong().toString()
            else String.format("%.2f", value)
        }
    }
}

// =========================================================
// MAIN SCREEN
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreenWithViewModel(
    viewModel: UnitConverterViewModel = viewModel(),
    onBack: () -> Unit
) {

    val categories = listOf("Length", "Volume", "Mass", "Speed", "Angle")
    val units = viewModel.getUnits(viewModel.selectedCategory)

    val convertButtonColor = if (viewModel.inputValue.isEmpty()) {
        LightPurple
    } else {
        DarkPurple
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Unit Converter",
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PrimaryPurple
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AppBackground
                )
            )
        },
        bottomBar = { CustomBottomBar() },
        containerColor = AppBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // ---- Category Dropdown ----
            Text("Select Category", fontSize = 12.sp, color = TextGray)
            Spacer(modifier = Modifier.height(4.dp))
            DropdownField(
                selected = viewModel.selectedCategory,
                options = categories,
                onSelect = { viewModel.onCategoryChange(it) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ---- Input Value ----
            Text("Enter Value", fontSize = 12.sp, color = TextGray)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = viewModel.inputValue,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                        viewModel.inputValue = newValue
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = BorderGray,
                    focusedContainerColor = FieldBg,
                    unfocusedContainerColor = FieldBg,
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark,
                    cursorColor = PrimaryPurple
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ---- From / To Row ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("From Unit", fontSize = 12.sp, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    DropdownField(
                        selected = viewModel.fromUnit,
                        options = units,
                        onSelect = { viewModel.fromUnit = it }
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(40.dp)
                        .background(LightPurple, CircleShape)
                        .clickable { viewModel.swapUnits() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Swap",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("To Unit", fontSize = 12.sp, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    DropdownField(
                        selected = viewModel.toUnit,
                        options = units,
                        onSelect = { viewModel.toUnit = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ---- Convert Button ----
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { viewModel.convert() },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = convertButtonColor,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(140.dp)
                        .height(44.dp)
                ) {
                    Text("Convert", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ---- Result Box ----
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ResultBg, RoundedCornerShape(8.dp))
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewModel.result,
                    fontSize = 16.sp,
                    color = TextDark,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// =========================================================
// DROPDOWN FIELD
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = TextGray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPurple,
                unfocusedBorderColor = BorderGray,
                focusedContainerColor = FieldBg,
                unfocusedContainerColor = FieldBg,
                focusedTextColor = TextDark,
                unfocusedTextColor = TextDark
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = FieldBg
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = if (option == selected) PrimaryPurple else TextDark
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
// 📱 BOTTOM NAVIGATION BAR
// =========================================================
@Composable
fun CustomBottomBar() {
    NavigationBar(
        containerColor = AppBackground,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.History, contentDescription = "History", tint = TextGray) },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "Favorites", tint = TextGray) },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Hexagon, contentDescription = "Shapes", tint = TextGray) },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ViewInAr, contentDescription = "3D", tint = TextGray) },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.MoreHoriz, contentDescription = "More", tint = TextGray) },
            selected = false,
            onClick = { }
        )
    }
}

// =========================================================
// PREVIEW
// =========================================================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConverterPreviewWithViewModel() {
    MaterialTheme {
        ConverterScreenWithViewModel(onBack = {})
    }
}