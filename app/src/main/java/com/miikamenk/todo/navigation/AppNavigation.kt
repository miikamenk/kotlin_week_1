package com.miikamenk.todo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.miikamenk.todo.view.WeatherScreen
import com.miikamenk.todo.view.CalendarScreen
import com.miikamenk.todo.view.HomeScreen
import com.miikamenk.todo.viewmodel.TaskViewModel
import com.miikamenk.todo.viewmodel.WeatherViewModel

@Composable
fun AppNavigation(navController: NavHostController, taskViewModel: TaskViewModel, weatherViewModel: WeatherViewModel) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_HOME
    ) {
        composable(ROUTE_HOME) {
            HomeScreen(
                taskViewModel = taskViewModel,
                navController = navController
            )
        }
        composable(ROUTE_CALENDAR) {
            CalendarScreen(
                taskViewModel = taskViewModel,
                navController = navController
            )
        }
        composable(ROUTE_WEATHER) {
            WeatherScreen(
                weatherViewModel = weatherViewModel,
                navController = navController
            )
        }
    }
}