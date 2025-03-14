package com.goazzi.mycompose.ui.pixabay

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.goazzi.mycompose.viewmodel.MainViewModel

@Composable
fun PixabayStateful(viewModel: MainViewModel) {
    Text(text = "Pixabay", style = MaterialTheme.typography.titleMedium)
}