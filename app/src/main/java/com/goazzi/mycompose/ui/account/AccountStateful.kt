package com.goazzi.mycompose.ui.account

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.goazzi.mycompose.viewmodel.MainViewModel

@Composable
fun AccountStateful(viewModel: MainViewModel) {
    Text(text = "Account", style = MaterialTheme.typography.titleMedium)
}