package com.goazzi.mycompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.goazzi.mycompose.repository.Repository
import com.goazzi.mycompose.repository.local.entity.LoginEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.contracts.contract

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {


    fun insertLogin(loginEntity: LoginEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLogin(loginEntity = loginEntity)
        }
    }
}