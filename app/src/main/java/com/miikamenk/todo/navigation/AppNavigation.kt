package com.miikamenk.todo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.miikamenk.todo.view.CalendarScreen
import com.miikamenk.todo.view.HomeScreen
import com.miikamenk.todo.viewmodel.TaskViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: TaskViewModel) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_HOME
    ) {
        composable(ROUTE_HOME) {
            HomeScreen(
                taskViewModel = viewModel,
                navController = navController
            )
        }
        composable(ROUTE_CALENDAR) {
            CalendarScreen(
                taskViewModel = viewModel,
                navController = navController
            )
        }
    }
}