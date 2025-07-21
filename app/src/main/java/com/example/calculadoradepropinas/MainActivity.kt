package com.example.calculadoradepropinas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoradepropinas.ui.theme.CalculadoraDePropinasTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraDePropinasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    val opcionesPropina = listOf("10%", "15%", "20%")
    var seleccionActual by remember { mutableStateOf(opcionesPropina[0]) }
    val tipPercent = seleccionActual.removeSuffix("%").toDoubleOrNull() ?: 10.0


    var amountInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent)
    val rawTip = tipPercent / 100 * amount
    val total = NumberFormat.getCurrencyInstance().format(amount + rawTip)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth())
        TipSelector(
            value = opcionesPropina,
            selectedValue = seleccionActual,
            onValueChange = { seleccionActual = it }
        )

        Text(
            text = stringResource(R.string.tip_amount, tip),
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.total_amount, total),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            amountInput = ""
            seleccionActual = opcionesPropina[0]
        }) {
            Text("Limpiar")
        }
    }
}

private fun calculateTip(amount: Double, tipPercent: Double): String {
    val tip = tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)
}


@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    CalculadoraDePropinasTheme{
        TipTimeLayout()
    }
}

@Composable
fun EditNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier){
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                stringResource(R.string.bill_amount)
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

@Composable
fun TipSelector(
    value: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text("Selecciona una propina:", style = MaterialTheme.typography.titleMedium)
        value.forEach { opcion ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onValueChange(opcion) }
            ) {
                RadioButton(
                    selected = (opcion == selectedValue),
                    onClick = { onValueChange(opcion) }
                )
                Text(text = opcion)
            }
        }
    }
}
