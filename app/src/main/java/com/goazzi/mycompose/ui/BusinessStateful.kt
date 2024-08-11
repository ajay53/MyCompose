package com.goazzi.mycompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.goazzi.mycompose.R
import com.goazzi.mycompose.model.Business
import com.goazzi.mycompose.model.Category
import com.goazzi.mycompose.model.Coordinates
import com.goazzi.mycompose.model.Location
import com.goazzi.mycompose.model.SearchBusiness
import com.goazzi.mycompose.util.Constants
import com.goazzi.mycompose.util.LocationEnum
import com.goazzi.mycompose.util.PermissionEnum
import com.goazzi.mycompose.util.SortByEnum
import com.goazzi.mycompose.util.d
import com.goazzi.mycompose.util.hasLocationPermission
import com.goazzi.mycompose.viewmodel.ApiState
import com.goazzi.mycompose.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import timber.log.Timber

private const val TAG = "BusinessStateful"

@Composable
fun BusinessStateful(viewModel: MainViewModel = hiltViewModel()) {

    val context = LocalContext.current

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback
    lateinit var locationRequest: LocationRequest
    var lat: Double = 0.0
    var lon: Double = 0.0

    var radius by remember { mutableFloatStateOf(value = 100f) }
    var locLocation by remember { mutableStateOf(value = LocationEnum.CURRENT) }
    var checked by remember { mutableStateOf(true) }
    var isPermissionGranted by remember {
        mutableStateOf(
            value = hasLocationPermission(context = context) && isGpsEnabled(
                context = context
            )
        )
    }

    val searchBusiness = SearchBusiness(
        40.730610,
        -73.935242,
        300,
        SortByEnum.BEST_MATCH.type,
        Constants.PAGE_LIMIT,
        0
    )

    val businessState = viewModel.businessAPiState




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(horizontal = 15.dp)
    ) {

//        Text("sdkjfkjvbkjdabvjfd")
//        viewModel.insertLogin(LoginEntity("dum dum", "email@gmail.com", "pass pass"))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Radius Selector")
            Text(text = radius.toString())
        }

        RadiusSlider {
            radius = it
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Location: $locLocation")
            Switch(checked = checked,
                onCheckedChange = {
                    checked = it
                })
        }

        SeparatorSpacer()

        var isButtonClicked by remember { mutableStateOf(false) }

        Button(onClick = {
            isButtonClicked = true
        }) {
            Text("Filled")
        }

        /*if (isButtonClicked) {
            RequestPermission(permissionEnum = PermissionEnum.LOCATION, onPermissionGranted = {
                if (Util.isGpsEnabled(context) && Util.hasLocationPermission(context)) {
                    isPermissionGranted = true
                }
            }, onBack = {
                isButtonClicked = false
            })
        }*/

        when (businessState) {
            is ApiState.Error -> {
                Timber.tag(TAG).e(businessState.exception)
            }

            ApiState.Loading -> {
                Timber.tag(TAG).d("loading")
            }

            is ApiState.Success -> {
                val businesses = businessState.data.businesses

                Timber.tag(TAG).d("businessState: $businesses")

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    itemsIndexed(
                        items = businessState.data.businesses,
                        key = { _, item -> item.id }
                    ) { _, item ->
                        BusinessListItem(
                            business = item,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }


    var shouldShowDialog by remember {
        mutableStateOf(true)
    }

    if (!isPermissionGranted) {
        RequestPermission(
//            permissionEnum = PermissionEnum.LOCATION,
            shouldShowDialog = shouldShowDialog,
            onPermissionGranted = {
                if (isGpsEnabled(context) && hasLocationPermission(context)) {
                    TAG.d(message = "All perm Granted")
                    shouldShowDialog = false
                    isPermissionGranted = true
                }
            },
            onBack = {})
        /*if (!Util.isGpsEnabled(context)) {
            RequestPermission(permissionEnum = PermissionEnum.GPS, onPermissionGranted = {
                if (Util.isGpsEnabled(context) && Util.hasLocationPermission(context)) {
                    isPermissionGranted = true
                }
            }, onBack = {})
        }
        if (!Util.hasLocationPermission(context)) {
            RequestPermission(permissionEnum = PermissionEnum.LOCATION, onPermissionGranted = {
                if (Util.isGpsEnabled(context) && Util.hasLocationPermission(context)) {
                    isPermissionGranted = true
                }
            }, onBack = {})
        }
*/

        /*RequestPermission(permissionEnum = PermissionEnum.GPS, onPermissionGranted = {
            if (Util.isGpsEnabled(context) && Util.hasLocationPermission(context)) {
                isPermissionGranted = true
            }
        })*/
    } else {
        LaunchedEffect(key1 = Unit) {
            viewModel.getBusinesses(searchBusiness = searchBusiness)
        }
    }


}

@Composable
fun BusinessListItem(business: Business, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
//            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentHeight().padding(5.dp)
//                .background(color = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(business.imageURL)
//                    .data("https://via.placeholder.com/150")
                    .crossfade(true)
                    .placeholder(R.drawable.ic_launcher_background)
                    .build(),
                error = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "business image",
                modifier = Modifier
//                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
            )

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.weight(1f)
                    .padding(horizontal = 5.dp)
            ) {
                Text(text = business.name, style = MaterialTheme.typography.labelSmall)
                Text(text = business.location.address1, style = MaterialTheme.typography.labelSmall)
                Text(
                    text = business.isClosed.toString(),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f) // Ensures the Box is a square
                    .clip(CircleShape)
                    .background(Color.Green)
            ) {
                Text(text = business.rating.toString(), color = Color.White)
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun BusinessListItemPreview() {
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
    BusinessListItem(business = dummyBusiness)
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun BusinessStatefulPreview() {
    BusinessStateful()
}

@Composable
fun RadiusSlider(onValueChangeFinished: (value: Float) -> Unit) {
    var sliderPosition by remember { mutableFloatStateOf(100f) }
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    Column {
        Slider(
            value = sliderPosition,
            onValueChange = {
                Timber.tag(TAG).d(message = "sliderPosition: $it")
                sliderPosition = if (it == 0f) 100f else it
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 19,
            valueRange = 0f..5000f,
            interactionSource = interactionSource,
            onValueChangeFinished = {
                Timber.tag(TAG).d(message = "onValueChangeFinished")
            },
//            modifier = Modifier.background(color = Color.Blue)
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "100 M")
            Text(text = "5 KM")
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun RadiusSliderPreview() {
    RadiusSlider(onValueChangeFinished = {})
}
