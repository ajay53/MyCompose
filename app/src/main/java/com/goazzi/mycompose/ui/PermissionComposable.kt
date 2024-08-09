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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.goazzi.mycompose.R
import com.goazzi.mycompose.util.PermissionEnum
import com.goazzi.mycompose.util.PrefKeys
import com.goazzi.mycompose.util.SharedPreferencesCustom
import com.goazzi.mycompose.util.Util
import com.goazzi.mycompose.util.d
import com.goazzi.mycompose.util.toast
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import timber.log.Timber

private const val TAG = "PermissionComposable"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermission(
    permissionEnum: PermissionEnum,
    onPermissionGranted: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
//    var showRationale by remember { mutableStateOf(true) }
//    var showRationaleGPS by remember { mutableStateOf(true) }

    val showLocPermReq = remember {
        SharedPreferencesCustom.getBoolean(context = context, key = PrefKeys.SHOW_LOC_PERM_REQ)
    }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ), onPermissionsResult = { statusMap: Map<String, Boolean> ->

            if (!statusMap.values.contains(false)) {
                TAG.d(message = "All perm Granted")
                onPermissionGranted()
            }

            TAG.d(message = "All perm not Granted")
        }
    )

    /*var neverAskAgain by remember {
        *//*val showLocPermReq =
            SharedPreferencesCustom.getBoolean(context = context, key = PrefKeys.SHOW_LOC_PERM_REQ)*//*
        if (showLocPermReq && !permissionsState.shouldShowRationale) {
            //never ask again situation
            mutableStateOf(true)
        } else {
            mutableStateOf(false)
        }
    }.apply {
        Timber.tag(TAG).d("neverAskAgain: $value")
    }*/

    showLocPermReq.run {
        Timber.tag(TAG).d(message = "showLocPermReq: $this")
    }

    permissionsState.run {
        Timber.tag(TAG).d(message = "permissionsState: ${!this.shouldShowRationale}")
    }

    val neverAskAgain by remember {
        Timber.tag(TAG)
            .d(message = "showLocPermReq: $showLocPermReq || !permissionsState.shouldShowRationale: ${!permissionsState.shouldShowRationale}")
        derivedStateOf {
            showLocPermReq && !permissionsState.shouldShowRationale
        }
    }.apply {
        Timber.tag(TAG)
            .d(message = "showLocPermReq: $showLocPermReq || !permissionsState.shouldShowRationale: ${!permissionsState.shouldShowRationale}")

        Timber.tag(TAG).d("neverAskAgain: $value")
    }

    //to launch settings when either prompts are exhausted or prompt doesn't exist
    val permissionSettingLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        when (permissionEnum) {
            PermissionEnum.GPS -> {
                if (Util.isGpsEnabled(context)) {
//                    shouldShowGpsRationale = false
                    Timber.tag(TAG).d(message = "GPS from Settings Enabled")
//                    showDialogLocal = false
                    onPermissionGranted()
                }
            }

            PermissionEnum.LOCATION -> {
                if (Util.hasLocationPermission(context)) {
//                    shouldShowPermissionRationale = false
                    Timber.tag(TAG).d(message = "Location from Settings Enabled")
//                    showDialogLocal = false
                    onPermissionGranted()
                }
            }
        }
    }

    /**
     * There is one work around which uses shouldShowRequestPermissionRationale.
     * Create a SharedPreference with default value false and store value returned by shouldShowRequestPermissionRationale in it.
     * Before updating the value, check if the value set was true. If it was true then don't update it.
     *
     * Whenever you want to check for permission, get the value from SharedPreference and current value returned by shouldShowRequestPermissionRationale.
     * If shouldShowRequestPermissionRationale returns false but value from SharedPreference is true,
     * you can deduce that Never ask again was selected by user.
     */
    LaunchedEffect(key1 = permissionsState.shouldShowRationale) {
        /*val showLocPermReq =
            SharedPreferencesCustom.getBoolean(context = context, key = PrefKeys.SHOW_LOC_PERM_REQ)*/

        if (!showLocPermReq && permissionsState.shouldShowRationale) {
            SharedPreferencesCustom.putBoolean(
                context = context,
                key = PrefKeys.SHOW_LOC_PERM_REQ,
                value = true
            )
        }
    }

    /*when (permissionEnum) {
        PermissionEnum.GPS -> {
            PermissionDialog(
                permissionEnum = permissionEnum,
                onAgreeClick = {},
                onDeclineClick = {})
        }

        PermissionEnum.LOCATION -> {
            when {
                showRationale -> {
//            Toast.makeText(context, "Denied", Toast.LENGTH_SHORT).show()
                    TAG.d(message = "when: showRationale")
                }

                neverAskAgain -> {
                    TAG.d(message = "when: neverAskAgain")
                    neverAskAgain = true
                    val intent =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts(
                                "package",
                                context.packageName,
                                null
                            )
                        }
                    permissionSettingLauncher.launch(input = intent)
                }
            }
        }
    }*/

    /*LaunchedEffect(key1 = permissionsState.shouldShowRationale) {
        TAG.d(message = "shouldShowRationale: ${permissionsState.shouldShowRationale}")

    }*/

//    TAG.d(message = "shouldShowRationale: ${permissionsState.shouldShowRationale}")

    PermissionDialog(
        permissionEnum = permissionEnum,
        isNeverAskAgain = neverAskAgain,
        onAgreeClick = {
            when (permissionEnum) {
                PermissionEnum.GPS -> {
                    permissionSettingLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

                PermissionEnum.LOCATION -> {
                    if (neverAskAgain) {
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts(
                                    "package",
                                    context.packageName,
                                    null
                                )
                            }
                        permissionSettingLauncher.launch(input = intent)
                    } else {
                        permissionsState.launchMultiplePermissionRequest()
                    }
                }
            }
        },
        onDeclineClick = {
            Toast.makeText(context, "Need Location Permission for API", Toast.LENGTH_SHORT).show()
        })

    /*PermissionScreen(permissionEnum = PermissionEnum.LOCATION,
        title = "Location permissions are required to use this feature. Please grant the permissions.",
        onYesClick = {

            val showLocPermReq =
                SharedPreferencesCustom.getBoolean(
                    context = context,
                    key = PrefKeys.SHOW_LOC_PERM_REQ
                )

            if (showLocPermReq && !permissionsState.shouldShowRationale) {
                //never ask again situation
                neverAskAgain = true
            } else {
                permissionsState.launchMultiplePermissionRequest()
            }
        },
        onNoClick = {
            context.toast(message = "Location de re baba!")
            onBack()
        })*/
}

@Composable
fun PermissionDialog(
    permissionEnum: PermissionEnum,
    isNeverAskAgain: Boolean = false,
    onAgreeClick: () -> Unit,
    onDeclineClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialogLocal by remember {
        mutableStateOf(true)
    }

    val title = when (permissionEnum) {
        PermissionEnum.GPS -> {
            "Give GPS pls"
        }

        PermissionEnum.LOCATION -> {
            if (isNeverAskAgain) "Give loc from Settings" else "pls grant location perm"
        }
    }

    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ), onDismissRequest = {

//                showDialogLocal = false
//            resetShowDialog()
        }) {

        PermissionScreen(
            title = title,
            onYesClick = onAgreeClick,
            onNoClick = onDeclineClick
        )
    }
}

@Composable
fun PermissionScreen(
    title: String,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
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
