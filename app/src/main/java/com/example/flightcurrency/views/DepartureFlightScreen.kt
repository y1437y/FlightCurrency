package com.example.flightcurrency.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightcurrency.model.FlightModel
import com.example.flightcurrency.views.items.DepartureFlightCard

@Composable
fun DepartureFlightScreen(flightModel: FlightModel = viewModel()) {
    val flights by flightModel.departureFlightList.collectAsState()


    LaunchedEffect(Unit) {
        flightModel.getFlightDataRefreshing("1", "1", true)
    }

    DisposableEffect(Unit) {
        onDispose {
            flightModel.stopDepartureFlightDataRefreshing()
        }
    }

    if (flights.isEmpty()) {
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
            .padding(16.dp)
    ) {
        items(flights.size) { index ->
            DepartureFlightCard(flightData = flights[index])
        }
    }

}