package com.goazzi.mycompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.goazzi.mycompose.ui.RestaurantsStateful
import com.goazzi.mycompose.viewmodel.MainViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = DashboardListScreenParams.route
) {
    val viewModel: MainViewModel = hiltViewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {


        composable(route = DashboardListScreenParams.route) {
            RestaurantsStateful(viewModel = viewModel)
        }
    }
}