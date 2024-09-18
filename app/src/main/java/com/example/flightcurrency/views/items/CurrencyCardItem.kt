package com.example.flightcurrency.views.items

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightcurrency.data.CurrencyData
import com.example.flightcurrency.model.CurrencyModel
import java.util.Locale

@Composable
fun CurrencyCardItem(currencyData: CurrencyData, int: Int, selectedIndex: Int, onSelect: (Int) -> Unit, currencyModel: CurrencyModel = viewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var inputText by remember { mutableStateOf("") }
    val focus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val currencyList by currencyModel.currencyList.collectAsState()
    val updatedCurrencyData = currencyList.find { it.currencyName == currencyData.currencyName }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .focusRequester(focus)
            .focusable()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        onClick = {
            onSelect(int)  // Update the selected index when clicked
            inputText = String.format(Locale.TAIWAN, "%.2f", currencyData.currencyValue)
            focus.requestFocus()  // Request focus for the BasicTextField
            //keyboardController?.show()  // Show the keyboard when the card is clicked
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .heightIn(min = 60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = updatedCurrencyData?.currencyName ?: currencyData.currencyName,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.width(8.dp))
            if (selectedIndex == int) {
                LaunchedEffect(Unit) {
                    // Ensure focus is requested when this composable becomes active
                    focus.requestFocus()
                }
                BasicTextField(
                    value = inputText,
                    onValueChange = { it ->
                        if (it == "" || it.toDoubleOrNull() == 0.0) {
                            inputText = "0"
                            currencyModel.reCalculateCurrencyValue(
                                currencyData.currencyName, 1.0)
                        }  else {
                            inputText = it
                            currencyModel.reCalculateCurrencyValue(
                                currencyData.currencyName,
                                it.toDoubleOrNull() ?: 0.0
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .focusRequester(focus)
                        .focusable(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }),
                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.End
                    )
                )
            } else {

                Text(
                    text = String.format(Locale.TAIWAN, "%.2f", updatedCurrencyData?.currencyValue ?: currencyData.currencyValue),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.End
                )
            }

        }
    }
}