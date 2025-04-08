package com.example.basiccalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.basiccalculator.ui.theme.BasicCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }

    fun calculate(operation: (Double, Double) -> Double) {
        val num1 = number1.toDoubleOrNull()
        val num2 = number2.toDoubleOrNull()

        if (num1 != null && num2 != null) {
            try {
                result = operation(num1, num2).let {
                    if (it == it.toInt().toDouble()) it.toInt().toString() else it.toString()
                }
            } catch (e: Exception) {
                result = if (e is ArithmeticException) "Error: Div by zero" else "Error"
            }
        } else {
            result = "Invalid Input"
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = "Basic Calculator - 5025221005",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Result display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = result,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 36.sp,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Input fields
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Number 1" to number1, "Number 2" to number2).forEach { (label, value) ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { newValue ->
                        when (label) {
                            "Number 1" -> number1 = newValue.filter { it.isDigit() || it == '.' }
                            else -> number2 = newValue.filter { it.isDigit() || it == '.' }
                        }
                    },
                    label = { Text(label) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        }

        // Operation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(
                Triple("+", { a: Double, b: Double -> a + b }, Color(0xFF4CAF50)),
                Triple("-", { a: Double, b: Double -> a - b }, Color(0xFF2196F3)),
                Triple("×", { a: Double, b: Double -> a * b }, Color(0xFFFF9800)),
                Triple("÷", { a: Double, b: Double ->
                    if (b == 0.0) throw ArithmeticException() else a / b
                }, Color(0xFFF44336))
            ).forEach { (text, operation, color) ->
                Button(
                    onClick = { calculate(operation) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(width = 80.dp, height = 60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = color,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = text, fontSize = 24.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BasicCalculatorTheme {
        CalculatorScreen()
    }
}