package com.goazzi.mycompose.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.goazzi.mycompose.repository.local.entity.LoginEntity
import com.goazzi.mycompose.viewmodel.MainViewModel

@Composable
fun RestaurantsStateful(viewModel: MainViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {

        Text("sdkjfkjvbkjdabvjfd")
        viewModel.insertLogin(LoginEntity("dum dum", "email@gmail.com", "pass pass"))
    }
}