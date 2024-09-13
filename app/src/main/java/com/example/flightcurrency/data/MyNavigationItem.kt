package com.example.flightcurrency.data

import com.example.flightcurrency.R

sealed class BottomNavigationItem(var route: String, var icon: Int, var title: String) {
    data object Departure: BottomNavigationItem("起飛班機", R.drawable.baseline_flight_takeoff_24, "起飛班機")
    data object Arrival: BottomNavigationItem("抵達班機", R.drawable.baseline_flight_land_24, "抵達班機")
    data object Currency: BottomNavigationItem("匯率", R.drawable.baseline_currency_exchange_24, "匯率")
}
