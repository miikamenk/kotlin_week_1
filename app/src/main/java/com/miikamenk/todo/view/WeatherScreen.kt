package com.miikamenk.todo.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.miikamenk.todo.viewmodel.WeatherViewModel
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.automirrored.filled.List

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel,
    navController: NavHostController
) {
    val uiState by weatherViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("Weather") },
            actions = {
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "Task List"
                    )
                }
                IconButton(onClick = { navController.navigate("calendar") }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Go to calendar"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.city,
            onValueChange = { weatherViewModel.updateCity(it) },
            label = { Text("Enter city name") },
            placeholder = { Text("e.g., Helsinki") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { weatherViewModel.fetchWeather() },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.error != null -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.error!!,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { weatherViewModel.clearError() }) {
                            Text("Dismiss")
                        }
                    }
                }
            }
            uiState.weather != null -> {
                WeatherResultSection(
                    cityName = uiState.weather!!.name,
                    temperature = uiState.weather!!.main.temp,
                    feelsLike = uiState.weather!!.main.feelsLike,
                    description = uiState.weather!!.weather.firstOrNull()?.description ?: "",
                    humidity = uiState.weather!!.main.humidity,
                    windSpeed = uiState.weather!!.wind.speed
                )
            }
        }
    }
}

@Composable
fun WeatherResultSection(
    cityName: String,
    temperature: Double,
    feelsLike: Double,
    description: String,
    humidity: Int,
    windSpeed: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cityName,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${temperature.toInt()}°C",
                style = MaterialTheme.typography.displayLarge
            )

            Text(
                text = description.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Feels like: ${feelsLike.toInt()}°C",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Humidity", style = MaterialTheme.typography.labelMedium)
                    Text(text = "$humidity%", style = MaterialTheme.typography.bodyLarge)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Wind", style = MaterialTheme.typography.labelMedium)
                    Text(text = "$windSpeed m/s", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
