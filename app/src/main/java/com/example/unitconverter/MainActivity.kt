package com.example.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.unitconverter.ui.theme.UnitConverterTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Form()
                }
            }
        }
    }
}

@Composable
fun Form() {
    var length by remember { mutableStateOf("") }
    var selectedFrom by remember { mutableStateOf(LengthUnit.METERS) }
    var selectedTo by remember { mutableStateOf(LengthUnit.FEET) }

    var result by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Převodník jednotek", textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineLarge)
            OutlinedTextField(
                value = length,
                onValueChange = {
                    if (it.isDigitsOnly()) length = it
                },
                label = { Text("Zadejte hodnotu") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                LengthDropdown(selectedFrom, OnItemSelected = { newValue -> selectedFrom = newValue })
                LengthDropdown(selectedTo, OnItemSelected = { newValue -> selectedTo = newValue })
            }
            Button(onClick = {
                val convertedValue = convertLength(length.toInt(), selectedFrom, selectedTo)
                result = "${(convertedValue.toDouble() * 100.0).roundToInt() / 100.0} ${selectedTo.getAbbreviation()}"
            }) {
                Text("Převést")
            }

            Text(result.toString())
        }
    }
}

fun convertLength(length: Int, fromUnit: LengthUnit, toUnit: LengthUnit): String {
    val valueInMeters = when (fromUnit) {
        LengthUnit.METERS -> length.toDouble()
        LengthUnit.FEET -> length * 0.3048
        LengthUnit.INCHES -> length * 0.0254
        LengthUnit.KILOMETERS -> length * 1000.0
        LengthUnit.YARDS -> length / 1.094
        LengthUnit.MILES -> length * 1609.34
    }

    val result = when (toUnit) {
        LengthUnit.METERS -> valueInMeters.toString()
        LengthUnit.FEET -> (valueInMeters / 0.3048).toString()
        LengthUnit.INCHES -> (valueInMeters / 0.0254).toString()
        LengthUnit.KILOMETERS -> (valueInMeters / 1000).toString()
        LengthUnit.YARDS -> (valueInMeters * 1.094).toString()
        LengthUnit.MILES -> (valueInMeters / 1609.34).toString()
    }

    return result
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LengthDropdown(
    selectedValue: LengthUnit,
    OnItemSelected: (LengthUnit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.width(190.dp)
            .padding(8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedValue.getName(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                LengthUnit.entries.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.getName()) },
                        onClick = {
                            OnItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}