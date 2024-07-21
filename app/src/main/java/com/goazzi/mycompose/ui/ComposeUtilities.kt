package com.goazzi.mycompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SeparatorSpacer(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .height(height = 1.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.outlineVariant
            )
    )
}