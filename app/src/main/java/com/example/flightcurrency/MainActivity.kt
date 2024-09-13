package com.example.flightcurrency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flightcurrency.data.BottomNavigationItem
import com.example.flightcurrency.model.CurrencyModel
import com.example.flightcurrency.model.FlightModel
import com.example.flightcurrency.ui.theme.FlightCurrencyTheme
import com.example.flightcurrency.views.ArrivalFlightScreen
import com.example.flightcurrency.views.BottomNavigationBar
import com.example.flightcurrency.views.CurrencyScreen
import com.example.flightcurrency.views.DepartureFlightScreen

class MainActivity : ComponentActivity() {
    private val currencyModel : CurrencyModel by viewModels()
    private val flightModel : FlightModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlightCurrencyTheme {
                FlightCurrency(flightModel, currencyModel)
            }
        }
    }
}

@Composable
private fun FlightCurrency(flightModel: FlightModel, currencyModel: CurrencyModel) {
    val navController = rememberNavController()
    val navItems = listOf(
        BottomNavigationItem.Departure,
        BottomNavigationItem.Arrival,
        BottomNavigationItem.Currency
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, items = navItems)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavigationItem.Departure.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavigationItem.Departure.route) {
                DepartureFlightScreen(flightModel)
            }

            composable(BottomNavigationItem.Arrival.route) {
                ArrivalFlightScreen(flightModel)
            }

            composable(BottomNavigationItem.Currency.route) {
                CurrencyScreen(currencyModel)
            }
        }
    }

}