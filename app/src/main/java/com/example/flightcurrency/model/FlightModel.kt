package com.example.flightcurrency.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightcurrency.data.ArrivalFlightData
import com.example.flightcurrency.data.DepartureFlightData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class FlightModel: ViewModel() {
    private val departureList = MutableStateFlow<List<DepartureFlightData>>(emptyList())
    val departureFlightList: StateFlow<List<DepartureFlightData>> = departureList

    private val arrivalList = MutableStateFlow<List<ArrivalFlightData>>(emptyList())
    val arrivalFlightList: StateFlow<List<ArrivalFlightData>> = arrivalList

    private var departureJob: Job? = null
    private var arrivalJob: Job? = null

    fun getFlightDataRefreshing(airFlyLine: String, airFlyIO: String, isDeparture: Boolean) {
        if (isDeparture) {
            departureJob = viewModelScope.launch(Dispatchers.IO) {
                while (isActive) {
                    getFlightData(airFlyLine, airFlyIO, true)
                    delay(10_000L)
                }
            }
        } else {
            arrivalJob = viewModelScope.launch(Dispatchers.IO) {
                while (isActive) {
                    getFlightData(airFlyLine, airFlyIO, false)
                    delay(10_000L)
                }
            }
        }

    }

    fun stopDepartureFlightDataRefreshing() {
        departureJob?.cancel()
    }

    fun stopArrivalFlightDataRefreshing() {
        arrivalJob?.cancel()
    }

    fun getFlightData(airFlyLine: String, airFlyIO: String, isDeparture: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("Check Data", "values are $airFlyLine, $airFlyIO, and $isDeparture")
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://www.kia.gov.tw/API/InstantSchedule.ashx?AirFlyLine=$airFlyLine&AirFlyIO=$airFlyIO")
                .build()

            try {
                val response = client.newCall(request).execute()
                val result = response.body?.string() ?: ""
                val json = JSONObject(result)
                val dataArray = json.getJSONArray("InstantSchedule")
                val depList = mutableListOf<DepartureFlightData>()
                val arrList = mutableListOf<ArrivalFlightData>()

                for (i in 0 until dataArray.length()) {
                    val eachObject = dataArray.getJSONObject(i)
                    if (isDeparture) {
                        val data = DepartureFlightData(
                            expectTime = eachObject.getString("expectTime"),
                            realTime = eachObject.getString("realTime"),
                            airLineName = eachObject.getString("airLineName"),
                            airLineCode = eachObject.getString("airLineCode"),
                            airLineLogo = eachObject.getString("airLineLogo"),
                            airLineNum = eachObject.getString("airLineNum"),
                            goalAirportCode = eachObject.getString("goalAirportCode"),
                            goalAirportName = eachObject.getString("goalAirportName"),
                            airBoardingGate = eachObject.getString("airBoardingGate"),
                            airFlyStatus = eachObject.getString("airFlyStatus"),
                            airFlyDelayCause = eachObject.getString("airFlyDelayCause")
                        )
                        depList.add(data)
                    } else {
                        val data = ArrivalFlightData(
                            expectTime = eachObject.getString("expectTime"),
                            realTime = eachObject.getString("realTime"),
                            airLineName = eachObject.getString("airLineName"),
                            airLineCode = eachObject.getString("airLineCode"),
                            airLineLogo = eachObject.getString("airLineLogo"),
                            airLineNum = eachObject.getString("airLineNum"),
                            goalAirportCode = eachObject.getString("upAirportCode"),
                            goalAirportName = eachObject.getString("upAirportName"),
                            airBoardingGate = eachObject.getString("airBoardingGate"),
                            airFlyStatus = eachObject.getString("airFlyStatus"),
                            airFlyDelayCause = eachObject.getString("airFlyDelayCause")
                        )
                        arrList.add(data)
                    }
                }
                launch(Dispatchers.Main) {
                    if (isDeparture) {
                        departureList.value = depList
                    } else {
                        arrivalList.value = arrList
                    }
                }
            } catch (e: IOException) {
                Log.e("Error", e.toString())
            }
        }
    }
}