package com.goazzi.mycompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.goazzi.mycompose.model.Business
import com.goazzi.mycompose.model.BusinessesServiceClass
import com.goazzi.mycompose.model.Category
import com.goazzi.mycompose.model.Coordinates
import com.goazzi.mycompose.model.Location
import com.goazzi.mycompose.model.SearchBusiness
import com.goazzi.mycompose.repository.Repository
import com.goazzi.mycompose.util.Constants
import com.goazzi.mycompose.util.SortByEnum
import com.goazzi.mycompose.util.d
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.concatWith
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {


    var page = 1
    val pageSize = 10

    private val _radius = MutableStateFlow(100f)
    val radius: StateFlow<Float> = _radius.asStateFlow()

    fun updateRadius(newRadius: Float) {
        _radius.value = newRadius
    }

    //    lat = 40.730610,
//    lon = -73.935242,
    private val _searchBusiness = MutableStateFlow(
        SearchBusiness(
            lat = 0.0,
            lon = 0.0,
            radius = radius.value.toInt(),
            sortBy = SortByEnum.BEST_MATCH.type,
            limit = Constants.PAGE_LIMIT,
            offset = 0
        )
    )

    //    val searchBusiness: StateFlow<SearchBusiness> = _searchBusiness.asStateFlow()
    val searchBusiness: StateFlow<SearchBusiness> = combine(
        _radius,
    ) { radiusValue ->
        SearchBusiness(
            lat = 40.70444381565246,
            lon = -73.99451834098926,
            radius = radiusValue[0].toInt(),
            sortBy = SortByEnum.BEST_MATCH.type,
            limit = Constants.PAGE_LIMIT,
            offset = 0
        )
    }.stateIn(
        viewModelScope, SharingStarted.Lazily, SearchBusiness(
            lat = 40.70444381565246,
            lon = -73.99451834098926,
            radius = radius.value.toInt(),
            sortBy = SortByEnum.BEST_MATCH.type,
            limit = Constants.PAGE_LIMIT,
            offset = 0
        )
    )


    // Function to update searchBusiness at runtime
    fun updateSearchParams(newSearchBusiness: SearchBusiness) {
        _searchBusiness.value = newSearchBusiness
    }

    private val _metadata = MutableStateFlow<BusinessesServiceClass?>(null)
    val metadata: StateFlow<BusinessesServiceClass?> = _metadata.asStateFlow()

    /*@OptIn(ExperimentalCoroutinesApi::class)
    val businessFlow: Flow<PagingData<Business>> = searchBusiness
        .flatMapLatest { searchParams ->
            Pager(
                config = PagingConfig(
                    pageSize = Constants.PAGE_LIMIT,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    BusinessPagingSource(
                        repository = repository,
                        searchBusiness = searchParams,
                        onMetadataReceived = { metadata -> _metadata.value = metadata }
                    )
                }
            ).flow
                .onStart { emit(PagingData.empty()) } // Clears list before loading
        }
        .cachedIn(viewModelScope)*/

    //OG code
    @OptIn(ExperimentalCoroutinesApi::class)
    val businessFlow: Flow<PagingData<Business>> = searchBusiness
        .flatMapLatest { searchParams ->
            Pager(
                config = PagingConfig(
                    pageSize = Constants.PAGE_LIMIT,
//                    prefetchDistance = 1,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    BusinessPagingSource(
                        repository = repository,
                        searchBusiness = searchParams, // Pass searchParams here
                        onMetadataReceived = { metadata ->
                            _metadata.value = metadata // Update metadata separately
                        }
                    )
                }
            ).flow
                .onStart { emit(PagingData.empty()) } // Clears list before loading
        }
        .cachedIn(viewModelScope)

    //    private val sourcesList = mutableListOf<BusinessPagingSource>()
    fun updateSearch(newSearchBusiness: SearchBusiness) {
        TAG.d("updateSearch called")
        _searchBusiness.value = newSearchBusiness
        /*sourcesList.forEach {
            TAG.d("updateSearch called: forEach: $it")
            it.updateSearchParameters(newSearchBusiness)
        }*/
    }
    /*fun insertLogin(loginEntity: LoginEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLogin(loginEntity = loginEntity)
        }
    }*/

    /*private val _apiState = MutableStateFlow<ApiState<BusinessesServiceClass>>(ApiState.Loading)
    val apiState: StateFlow<ApiState<BusinessesServiceClass>> = _apiState*/
//    var unlockApiState: UnlockAPIState by mutableStateOf(UnlockAPIState.Loading)
//        private set
    private val _allBusinesses = MutableStateFlow<List<Business>>(emptyList())
    val allBusinesses: StateFlow<List<Business>> = _allBusinesses.asStateFlow()

    private val _businessAPiState =
        MutableStateFlow<ApiState<BusinessesServiceClass>>(ApiState.Idle)
    val businessAPiState: StateFlow<ApiState<BusinessesServiceClass>> = _businessAPiState
        .asStateFlow()

    fun getBusinesses(searchBusiness: SearchBusiness) {
        viewModelScope.launch {
            TAG.d(message = "getBusinesses: called: $searchBusiness")

            _businessAPiState.value = ApiState.Loading
            try {
                delay(2000)
                val result = withContext(Dispatchers.IO) {
                    repository.getBusinesses(searchBusiness = searchBusiness)
                }

                Timber.tag(TAG).d(message = "getBusinesses: message: ${result.message()}")

                val body = result.body()!!

                body.message = result.message()
                body.httpCode = result.code()

                Timber.tag(TAG).d(message = "getBusinesses: result.body(): $body")
                _businessAPiState.value = ApiState.Success(result.body()!!)
            } catch (e: Exception) {
                Timber.tag(TAG).e(message = "getBusinesses: e: $e")
                _businessAPiState.value = ApiState.Error(e)
            }
        }
    }

    /*fun loadNextPage() {
        _businessAPiState.value =
            if (allItems.isEmpty()) ListState.Loading else ListState.LoadingMore(allItems)
        // Simulate data fetching
        CoroutineScope(Dispatchers.Default).launch {
            delay(1000) // Simulate network delay
            val newData = (page * pageSize until (page + 1) * pageSize).map { Item(it, "Item $it") }
            allItems.addAll(newData)
            _listState.value = ListState.Success(allItems)
            page++
        }

    }*/

    val dummyBusiness = Business(
        id = "MlH54XwiHAlUxzi2uzJKgA",
        alias = "on-same-day-delivery-long-island-city-2",
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

/*sealed class ApiState<T> {
    data object Idle : ApiState<Nothing>()
    data object Loading : ApiState<Nothing>()
    data class LoadingMoreBusinesses(val currentList: List<Business>) : ApiState<Any?>()
//    data class LoadingMoreReviews(val currentList: List<com.google.android.libraries.places.api.model.Review>) : ApiState()
//    data class SuccessBusinesses(val data: BusinessesServiceClass) : ApiState()
//    data class SuccessReviews(val data: List<com.google.android.libraries.places.api.model.Review>) : ApiState()
    data class Error(val exception: Throwable) : ApiState<Nothing>()
}*/
sealed class ApiState<out T> {
    data object Idle : ApiState<Nothing>()
    data object Loading : ApiState<Nothing>()
    data class LoadingMore<out T>(val data: T) : ApiState<T>()
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val exception: Throwable) : ApiState<Nothing>()
}

class BusinessPagingSource(
    private val repository: Repository,
    private val searchBusiness: SearchBusiness,
    private val onMetadataReceived: (BusinessesServiceClass) -> Unit
) : PagingSource<Int, Business>() {

    override fun getRefreshKey(state: PagingState<Int, Business>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Business> {
        val pageIndex = params.key ?: 1

        val lat = searchBusiness.lat
        val lon = searchBusiness.lon
        return try {
            if (lat == 0.0 || lon == 0.0) {
                LoadResult.Error(Exception("Default location not set"))
            } else {
                val updatedSearch =
                    searchBusiness.copy(offset = (pageIndex - 1) * Constants.PAGE_LIMIT)
                TAG.d(message = "updated Search: $updatedSearch")

                val response = repository.getBusinesses(updatedSearch)
                val body = response.body()

                body?.message = response.message()
                body?.httpCode = response.code()

                if (body != null) {
                    onMetadataReceived(body)
                    LoadResult.Page(
                        data = body.businesses,
                        prevKey = if (pageIndex == 1) null else pageIndex - 1,
                        nextKey = if (body.businesses.isEmpty()) null else pageIndex + 1
                    )
                } else {
                    LoadResult.Error(Exception("Empty response body"))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val TAG = "BusinessPagingSource"
    }
}

/*class BusinessPagingSource(
    private val repository: Repository,
    private val initialSearchBusiness: SearchBusiness,
    private val onMetadataReceived: (BusinessesServiceClass) -> Unit // Callback for metadata
) : PagingSource<Int, Business>() {

    private var currentSearchBusiness: SearchBusiness = initialSearchBusiness

    fun updateSearchParameters(newSearchBusiness: SearchBusiness) {
        TAG.d("updateSearchParameters in BusinessPagingSource called")
        currentSearchBusiness = newSearchBusiness
    }

    override fun getRefreshKey(state: PagingState<Int, Business>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Business> {
        val pageIndex = params.key ?: 1
        return try {
            // Use the currentSearchBusiness parameters here
            val lat = currentSearchBusiness.lat
            val lon = currentSearchBusiness.lon

            if (lat == 0.0 || lon == 0.0) {
                LoadResult.Error(Exception("Default"))

            } else {
                val updatedSearch =
                    currentSearchBusiness.copy(offset = (pageIndex - 1) * Constants.PAGE_LIMIT)

//            delay(2000)

                TAG.d(message = "updated Search: $updatedSearch")

                val response = repository.getBusinesses(updatedSearch)
                val body = response.body()
//            val body = result.body()!!

                body?.message = response.message()
                body?.httpCode = response.code()

                if (body != null) {
                    // Pass metadata to ViewModel
                    onMetadataReceived(body)

                    LoadResult.Page(
                        data = body.businesses, // Only businesses for pagination
                        prevKey = if (pageIndex == 1) null else pageIndex - 1,
                        nextKey = if (body.businesses.isEmpty()) null else pageIndex + 1
                    )
                } else {
                    LoadResult.Error(Exception("Empty response body"))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val TAG = "BusinessPagingSource"
    }
}*/


/*class BusinessPagingSource(
    private val repository: Repository,
    private val searchBusiness: SearchBusiness
) : PagingSource<Int, Business>() {

    override fun getRefreshKey(state: PagingState<Int, Business>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Business> {
        val pageIndex = params.key ?: 1
        return try {
            val pageSize = params.loadSize
            val updatedSearchBusiness =
                searchBusiness.copy(offset = (pageIndex - 1) * Constants.PAGE_LIMIT)

            val response = repository.getBusinesses(searchBusiness = updatedSearchBusiness)
            val body = response.body()

            if (body != null) {
                val nextKey = if (body.businesses.isEmpty()) null else pageIndex + 1
                val prevKey = if (pageIndex == 1) null else pageIndex - 1

                LoadResult.Page(
                    data = body.businesses,
//                    data = mutableListOf(),
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(Exception("Empty response body"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}*/
