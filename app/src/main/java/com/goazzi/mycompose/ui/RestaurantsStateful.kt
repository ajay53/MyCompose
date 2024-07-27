package com.goazzi.mycompose.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.goazzi.mycompose.model.SearchBusiness
import com.goazzi.mycompose.repository.local.entity.LoginEntity
import com.goazzi.mycompose.util.Constants
import com.goazzi.mycompose.util.SortByEnum
import com.goazzi.mycompose.viewmodel.ApiState
import com.goazzi.mycompose.viewmodel.MainViewModel
import timber.log.Timber

private const val TAG = "RestaurantsStateful"

@Composable
fun RestaurantsStateful(viewModel: MainViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {

        Text("sdkjfkjvbkjdabvjfd")
//        viewModel.insertLogin(LoginEntity("dum dum", "email@gmail.com", "pass pass"))

        val searchBusiness = SearchBusiness(
            40.730610,
            -73.935242,
            300,
            SortByEnum.BEST_MATCH.type,
            Constants.PAGE_LIMIT,
            0
        )

        val businessState = viewModel.businessAPiState

        when (businessState) {
            is ApiState.Error -> {
                Timber.tag(TAG).e(businessState.exception)
            }

            ApiState.Loading -> {
                Timber.tag(TAG).d("loading")
            }

            is ApiState.Success -> {
                Timber.tag(TAG).d("businessState: ${businessState.data}")
            }
        }

        LaunchedEffect(key1 = Unit) {
            viewModel.getBusinesses(searchBusiness = searchBusiness)
        }
    }
}