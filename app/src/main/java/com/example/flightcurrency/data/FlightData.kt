package com.example.flightcurrency.data

data class DepartureFlightData (
    val expectTime: String,
    val realTime: String,
    val airLineName: String,
    val airLineCode: String,
    val airLineLogo: String,
    val airLineNum: String,
    val goalAirportCode: String,
    val goalAirportName: String,
    val airBoardingGate: String,
    val airFlyStatus: String,
    val airFlyDelayCause: String
)

data class ArrivalFlightData (
    val expectTime: String,
    val realTime: String,
    val airLineName: String,
    val airLineCode: String,
    val airLineLogo: String,
    val airLineNum: String,
    val goalAirportCode: String,
    val goalAirportName: String,
    val airBoardingGate: String,
    val airFlyStatus: String,
    val airFlyDelayCause: String
)