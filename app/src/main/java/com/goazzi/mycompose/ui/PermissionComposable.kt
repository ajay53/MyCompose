package com.goazzi.mycompose.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.goazzi.mycompose.R
import com.goazzi.mycompose.util.PermissionEnum
import com.goazzi.mycompose.util.Util
import com.goazzi.mycompose.util.toast
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import timber.log.Timber

private const val TAG = "PermissionComposable"

@OptIn(ExperimentalPermissionsApi::class)
//@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RequestPermission(permissionEnum: PermissionEnum) {
    val context = LocalContext.current

    //permission states
    var shouldShowPermissionRationale by remember { mutableStateOf(false) }
    var shouldShowGpsRationale by remember { mutableStateOf(true) }
    var isGpsEnabled by remember { mutableStateOf(isGpsEnabled(context)) }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    /**
     * this covers both cases where permission is denied or permission prompt is exhausted
     * Effect gets executed again as the state is updated
     */
    if (permissionEnum == PermissionEnum.LOCATION) {
        LaunchedEffect(permissionsState.allPermissionsGranted) {
            if (!permissionsState.allPermissionsGranted) {
                shouldShowPermissionRationale =
                    permissionsState.revokedPermissions.any { it.status.shouldShowRationale }
            }
            if (!shouldShowGpsRationale) {
                Timber.tag(TAG).d(message = "Location Permission Granted")
            }
        }
    }


    /**
     * Checking whether GPS is enabled, to set shouldShowGpsRationale
     *
     * While coroutines are non-blocking,
     * the absence of delay in a continuously running loop can still lead to high CPU usage, rapid battery drain, resource contention, and overall reduced system performance.
     *  delay ensures that the coroutine performs the check at reasonable intervals, maintaining app responsiveness and efficient resource usage.
     */
    if (permissionEnum == PermissionEnum.GPS) {
        LaunchedEffect(Unit) {
            while (true) {
                isGpsEnabled = isGpsEnabled(context)

                if (isGpsEnabled) {
                    shouldShowGpsRationale = false
                    break
                }

                delay(timeMillis = 1000)
            }
        }
    }

    //to launch settings when either prompts are exhausted or prompt doesn't exist
    val permissionSettingLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        val enabled: Boolean = when (permissionEnum) {
            PermissionEnum.GPS -> {
                Util.isGpsEnabled(context = context)
            }

            PermissionEnum.LOCATION -> {
                Util.hasLocationPermission(context = context)
            }
        }
        if (enabled) {
            when (permissionEnum) {
                PermissionEnum.GPS -> {
                    shouldShowGpsRationale = false
                    Timber.tag(TAG).d(message = "Location from Settings Enabled")
                }

                PermissionEnum.LOCATION -> {
                    shouldShowPermissionRationale = false
                    Timber.tag(TAG).d(message = "GPS from Settings Enabled")
                }
            }
        }
    }

    //for location permission prompt
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val allGranted = permissions.entries.all { it.value }
            shouldShowPermissionRationale = allGranted
            if (!allGranted) {
//                shouldShowPermissionRationale = permissions.entries.any { !it.value }
            }
        }
    )


    /*LaunchedEffect(Unit) {
        if (!permissionsState.allPermissionsGranted) {
            shouldShowPermissionRationale =
                permissionsState.revokedPermissions.any { it.status.shouldShowRationale }
        }

        if (!isGpsEnabled) {
            shouldShowGpsRationale = true
        }
    }*/

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            /*permissionsState.allPermissionsGranted && isGpsEnabled -> {
                Text("All permissions granted and GPS is enabled.")
                // Launch your actual code here
            }*/

            shouldShowPermissionRationale -> {

                PermissionScreen(permissionEnum = PermissionEnum.LOCATION,
                    title = "Location permissions are required to use this feature. Please grant the permissions.",
                    onYesClick = {
                        permissionsState.launchMultiplePermissionRequest()
                    },
                    onNoClick = {
                        context.toast(message = "Location de re baba!")
                    })


                /*Text("Location permissions are required to use this feature. Please grant the permissions.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                    Text("Request Permissions")
                }*/
            }

            !permissionsState.allPermissionsGranted -> {

                Timber.tag(TAG).d(message = "permanently denied the location permissions")
                PermissionScreen(permissionEnum = PermissionEnum.LOCATION,
                    title = "You have permanently denied the location permissions. Please enable them in the app settings.",
                    onYesClick = {
//                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        permissionSettingLauncher.launch(input = intent)
                    },
                    onNoClick = {
                        context.toast(message = "GO to App settings")
                    })

                /*Text("You have permanently denied the location permissions. Please enable them in the app settings.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    context.startActivity(intent)
                }) {
                    Text("Open Settings")
                }*/
            }

            shouldShowGpsRationale -> {

                PermissionScreen(permissionEnum = PermissionEnum.LOCATION,
                    title = "GPS is required to use this feature. Please enable GPS.",
                    onYesClick = {
                        permissionSettingLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                        permissionsState.launchMultiplePermissionRequest()
                    },
                    onNoClick = {
                        context.toast(message = "GPS is necessary")
                    })

                /*Text("GPS is required to use this feature. Please enable GPS.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                }) {
                    Text("Enable GPS")
                }*/
            }
        }
    }


    /*if (permissionEnum == PermissionEnum.GPS) {
        LaunchedEffect(Unit) {
            while (true) {
                isGpsEnabled = isGpsEnabled(context)
                if (isGpsEnabled) {
                    shouldShowGpsRationale = false
                    break
                }
                kotlinx.coroutines.delay(1000)
            }
        }
    }*/
}

@Composable
fun PermissionScreen(
    permissionEnum: PermissionEnum = PermissionEnum.LOCATION,
    title: String,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onYesClick) {
            Text("Yes")
        }
        Button(onClick = onNoClick) {
            Text("No")
        }
    }
}

fun isGpsEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

/*val title: String
val desc: String
when (permission) {
    PermissionEnum.GPS -> {
        title = stringResource(id = R.string.gps_permission_title)
        desc = stringResource(id = R.string.gps_permission_desc)
    }

    PermissionEnum.LOCATION -> {
        title = stringResource(id = R.string.location_permission_title)
        desc = stringResource(id = R.string.location_permission_desc)
    }
}*/
