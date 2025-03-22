package com.goazzi.mycompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.goazzi.mycompose.ui.BusinessStateful
import com.goazzi.mycompose.ui.account.AccountStateful
import com.goazzi.mycompose.ui.pixabay.MediaDetailStateful
import com.goazzi.mycompose.ui.pixabay.PixabayStateful
import com.goazzi.mycompose.viewmodel.MainViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = YelpScreenParams.route
) {
    val viewModel: MainViewModel = hiltViewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {

        navigation(
            route = UtilNavigation.getRouteName(NavigationItem.Yelp),
            startDestination = UtilNavigation.getRouteName(NavigationItem.Yelp.Root)
        ) {
            composable(route = UtilNavigation.getRouteName(NavigationItem.Yelp.Root)) {
                BusinessStateful(viewModel = viewModel)
            }
        }

        navigation(
            route = UtilNavigation.getRouteName(NavigationItem.Pixabay),
            startDestination = UtilNavigation.getRouteName(NavigationItem.Pixabay.Root)
        ) {
            composable(route = UtilNavigation.getRouteName(NavigationItem.Pixabay.Root)) {
//                BusinessStateful(viewModel = viewModel)
                PixabayStateful(
                    viewModel = viewModel,
                    onMediaClick = {
                        navController.navigate(UtilNavigation.getRouteName(NavigationItem.Pixabay.MediaDetail))
                    }
                )
            }
            composable(route = UtilNavigation.getRouteName(NavigationItem.Pixabay.MediaDetail)) {
                MediaDetailStateful(viewModel = viewModel)
            }
        }

        navigation(
            route = UtilNavigation.getRouteName(NavigationItem.Account),
            startDestination = UtilNavigation.getRouteName(NavigationItem.Account.Root)
        ) {
            composable(route = UtilNavigation.getRouteName(NavigationItem.Account.Root)) {
//                BusinessStateful(viewModel = viewModel)
                AccountStateful(viewModel = viewModel)
            }
        }

        /*composable(route = YelpScreenParams.route) {
            BusinessStateful(viewModel = viewModel)
        }*/
    }
}