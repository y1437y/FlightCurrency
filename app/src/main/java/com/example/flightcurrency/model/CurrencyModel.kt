package com.example.flightcurrency.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightcurrency.data.CurrencyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

open class CurrencyModel: ViewModel() {
    internal val list = MutableStateFlow<List<CurrencyData>>(emptyList())
    val currencyList: StateFlow<List<CurrencyData>> = list

    fun getCurrencyLatest(currency: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_mRI3CdztVQtKFELwofhNGlSvZzsUASGbr2wisx7I&currencies=EUR%2CUSD%2CCAD%2CJPY%2CCNY%2CKRW%2CHKD%2CGBP&base_currency=$currency")
                .build()

            try {
                val response = client.newCall(request).execute()
                val result = response.body?.string() ?: ""
                val json = JSONObject(result)
                val dataObject = json.getJSONObject("data")

                val dataList = mutableListOf<CurrencyData>()

                val keys = dataObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val value = dataObject.getDouble(key)
                    dataList.add(CurrencyData(key, value))
                }
                launch(Dispatchers.Main) {
                    list.value = dataList
                }
            } catch (e: IOException) {
                Log.e("Error", e.toString())
            }
        }
    }

    fun reCalculateCurrencyValue(currency: String, value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedCurrencyList = currencyList.value.map {
                if (it.currencyName == currency) {
                    CurrencyData(it.currencyName, value)
                } else {
                    it
                }
            }

            val baseCurrency = currencyList.value.find { it.currencyName == currency }
            if (baseCurrency != null) {
                val newList = updatedCurrencyList.map { currencyData ->
                    if (currencyData.currencyName != currency) {
                        val newValue = currencyData.currencyValue * (value / baseCurrency.currencyValue)
                        CurrencyData(currencyData.currencyName, newValue)
                    } else {
                        currencyData
                    }
                }

                launch(Dispatchers.Main) {
                    list.value = newList
                }
            }
        }
    }
}