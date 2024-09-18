package com.example.flightcurrency.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightcurrency.model.CurrencyModel
import com.example.flightcurrency.views.items.CurrencyCardItem

@Composable
fun CurrencyScreen(currencyModel: CurrencyModel = viewModel()) {
    val currencies by currencyModel.currencyList.collectAsState()

    var selectedIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(Unit) { currencyModel.getCurrencyLatest("USD") }

    if (currencies.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        items(currencies.size) { index ->
            CurrencyCardItem(
                currencyData = currencies[index],
                int = index,
                selectedIndex = selectedIndex,
                onSelect = { selectedIndex = it },
                currencyModel = currencyModel
            )
        }
    }
}