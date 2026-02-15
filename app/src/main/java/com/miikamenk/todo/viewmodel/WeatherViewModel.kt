package com.miikamenk.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.miikamenk.todo.BuildConfig
import com.miikamenk.todo.data.model.WeatherResponse
import com.miikamenk.todo.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val isLoading: Boolean = false,
    val weather: WeatherResponse? = null,
    val error: String? = null,
    val city: String = ""
)

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val api = RetrofitInstance.weatherApi
    private val apiKey = BuildConfig.WEATHER_API_KEY

    init {
        Log.d("WeatherViewModel", "API Key: $apiKey")
    }

    fun updateCity(city: String) {
        _uiState.value = _uiState.value.copy(city = city)
    }

    fun fetchWeather(city: String = _uiState.value.city) {
        if (city.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter a city name")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = api.getWeather(city = city, apiKey = apiKey)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    weather = response,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to fetch weather data"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
