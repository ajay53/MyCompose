package com.goazzi.mycompose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goazzi.mycompose.model.Business
import com.goazzi.mycompose.model.BusinessesServiceClass
import com.goazzi.mycompose.model.Category
import com.goazzi.mycompose.model.Coordinates
import com.goazzi.mycompose.model.Location
import com.goazzi.mycompose.model.SearchBusiness
import com.goazzi.mycompose.repository.Repository
import com.goazzi.mycompose.repository.local.entity.LoginEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    val dummyBusiness = Business(
        id = "MlH54XwiHAlUxzi2uzJKgA",
        alias= "on-same-day-delivery-long-island-city-2",
        name = "On Same Day Delivery",
        imageURL = "https://s3-media3.fl.yelpcdn.com/bphoto/qndeHgUFZ0Gc4qOAmHjv_A/o.jpg",
        isClosed = false,
        url = "https://www.yelp.com/biz/on-same-day-delivery-long-island-city-2?adjust_creative=NeKJyZmrzDFQsEkaI3emZA&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=NeKJyZmrzDFQsEkaI3emZA",
        reviewCount = 51,
        categories = listOf(Category(alias = "couriers", title = "Couriers & Delivery Services")),
        rating = 4.8,
        coordinates = Coordinates(latitude = 40.7329304, longitude = -73.9370068),
        transactions = emptyList(),
        location = Location(
            address1 = "4700 Northern Blvd",
            address2 = null,
            address3 = null,
            city = "Long Island City",
            zipCode = "11101",
            country = "US",
            state = "NY",
            displayAddress = listOf("4700 Northern Blvd", "Long Island City, NY 11101")
        ),
        phone = "+17187060700",
        displayPhone = "(718) 706-0700",
        distance = 297.8002213757102
    )

    companion object {
        private const val TAG = "MainViewModel"
    }
}

sealed class ApiState<out T> {
    data object Loading : ApiState<Nothing>()
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val exception: Throwable) : ApiState<Nothing>()
}