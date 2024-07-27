package com.goazzi.mycompose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goazzi.mycompose.model.BusinessesServiceClass
import com.goazzi.mycompose.model.SearchBusiness
import com.goazzi.mycompose.repository.Repository
import com.goazzi.mycompose.repository.local.entity.LoginEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {


    fun insertLogin(loginEntity: LoginEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLogin(loginEntity = loginEntity)
        }
    }

    /*private val _apiState = MutableStateFlow<ApiState<BusinessesServiceClass>>(ApiState.Loading)
    val apiState: StateFlow<ApiState<BusinessesServiceClass>> = _apiState*/
//    var unlockApiState: UnlockAPIState by mutableStateOf(UnlockAPIState.Loading)
//        private set
    var businessAPiState: ApiState<BusinessesServiceClass> by mutableStateOf(ApiState.Loading)
        private set

    fun getBusinesses(searchBusiness: SearchBusiness) {
        viewModelScope.launch {
            businessAPiState = ApiState.Loading
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getBusinesses(searchBusiness = searchBusiness)
                }

                Timber.tag(TAG).d(message = "getBusinesses: message: ${result.message()}")

                val body = result.body()!!

                body.message = result.message()
                body.httpCode = result.code()

                Timber.tag(TAG).d(message = "getBusinesses: result.body(): ${body}")
                businessAPiState = ApiState.Success(result.body()!!)
            } catch (e: Exception) {
                Timber.tag(TAG).e(message = "getBusinesses: e: $e")
                businessAPiState = ApiState.Error(e)
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}

sealed class ApiState<out T> {
    data object Loading : ApiState<Nothing>()
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val exception: Throwable) : ApiState<Nothing>()
}