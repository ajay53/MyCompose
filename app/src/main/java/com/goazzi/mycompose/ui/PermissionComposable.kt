package com.goazzi.mycompose.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.goazzi.mycompose.R
import com.goazzi.mycompose.util.PermissionEnum
import com.goazzi.mycompose.util.d
import com.goazzi.mycompose.util.getShowLockPermReq
import com.goazzi.mycompose.util.hasLocationPermission
import com.goazzi.mycompose.util.isGpsEnabled
import com.goazzi.mycompose.util.rursusFamily
import com.goazzi.mycompose.util.setShowLockPermReq
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import timber.log.Timber

private const val TAG = "PermissionComposable"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionDialogStateful(
    shouldShowDialog: Boolean = true,
    onPermissionGranted: () -> Unit,
) {
    val context = LocalContext.current

    //region setting request type and observe permission state & dismiss permission dialog
    var currRequest by remember { mutableStateOf(PermissionEnum.GPS) }

    var isGpsEnabled by remember { mutableStateOf(isGpsEnabled(context = context)) }

    var hasLocationPermission by remember { mutableStateOf(hasLocationPermission(context = context)) }

    //This code block also acts as a list for all the necessary permissions
    // Update `currRequest` when relevant states change
    LaunchedEffect(isGpsEnabled, hasLocationPermission) {
        currRequest = when {
            !isGpsEnabled -> PermissionEnum.GPS
            else -> PermissionEnum.LOCATION
//            !hasLocationPermission -> PermissionEnum.LOCATION
        }
    }

    val isPermissionGranted by remember {
        derivedStateOf {
            isGpsEnabled && hasLocationPermission
        }
    }

    if (isPermissionGranted) {
        onPermissionGranted()
    }
    //endregion

    val showLocPermReq by context.getShowLockPermReq().collectAsState(initial = false)

    showLocPermReq.run {
        Timber.tag(TAG).d(message = "showLocPermReqPref: $this")
    }

    val locationPermState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ), onPermissionsResult = { statusMap: Map<String, Boolean> ->

            if (!statusMap.values.contains(false)) {
                TAG.d(message = "All perm Granted")
                hasLocationPermission = true
//                onPermissionGranted()
            }

            TAG.d(message = "All perm not Granted")
        }
    )

    //just logs for debugging
    /*showLocPermReq.run {
        Timber.tag(TAG).d(message = "showLocPermReq: $this")
    }

    locationPermState.run {
        Timber.tag(TAG).d(message = "permissionsState: ${!this.shouldShowRationale}")
    }*/

    val neverAskAgain by remember {
        Timber.tag(TAG)
            .d(message = "showLocPermReq: $showLocPermReq || !permissionsState.shouldShowRationale: ${!locationPermState.shouldShowRationale}")
        derivedStateOf {
            showLocPermReq && !locationPermState.shouldShowRationale
        }
    }.apply {
        Timber.tag(TAG)
            .d(message = "showLocPermReq: $showLocPermReq || !permissionsState.shouldShowRationale: ${!locationPermState.shouldShowRationale}")

        Timber.tag(TAG).d("neverAskAgain: $value")
    }

    //to launch settings when either prompts are exhausted or prompt doesn't exist
    val permissionSettingLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        when (currRequest) {
            PermissionEnum.GPS -> {
                if (isGpsEnabled(context)) {
//                    shouldShowGpsRationale = false
                    Timber.tag(TAG).d(message = "GPS from Settings Enabled")
                    isGpsEnabled = true
//                    showDialogLocal = false
//                    onPermissionGranted()
                }
            }

            PermissionEnum.LOCATION -> {
                if (hasLocationPermission(context)) {
//                    shouldShowPermissionRationale = false
                    Timber.tag(TAG).d(message = "Location from Settings Enabled")
                    hasLocationPermission = true
//                    showDialogLocal = false
//                    onPermissionGranted()
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
    LaunchedEffect(key1 = locationPermState.shouldShowRationale) {
        if (!showLocPermReq && locationPermState.shouldShowRationale) {
            context.setShowLockPermReq(isEnabled = true)
        }
    }

    /**
     * Permission Dialog
     */
    PermissionDialogStateless(
        permissionEnum = currRequest,
        shouldShowDialog = shouldShowDialog,
        isNeverAskAgain = neverAskAgain,
        onAgreeClick = {
            when (currRequest) {
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
                        locationPermState.launchMultiplePermissionRequest()
                    }
                }
            }
        },
        onDeclineClick = {
            Toast.makeText(context, "Need Location Permission for API", Toast.LENGTH_SHORT).show()
        }, modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Min)
    )
}

@Composable
fun PermissionDialogStateless(
    permissionEnum: PermissionEnum,
    shouldShowDialog: Boolean = true,
    isNeverAskAgain: Boolean = false,
    onAgreeClick: () -> Unit,
    onDeclineClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val title = when (permissionEnum) {
        PermissionEnum.GPS -> {
            "Give GPS pls"
        }

        PermissionEnum.LOCATION -> {
            if (isNeverAskAgain) "Give loc from Settings" else "pls grant location perm"
        }
    }

    if (shouldShowDialog) {
        Dialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ), onDismissRequest = {
                TAG.d(message = "onDismissRequest")
//                showDialogLocal = false
//            resetShowDialog()
            }) {
            PermissionDialogUI(
                title = title,
                onYesClick = onAgreeClick,
                onNoClick = onDeclineClick,
                modifier = modifier
            )
        }
    }
}

@Composable
fun PermissionDialogUI(
    title: String,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        border = BorderStroke(
            width = 1.dp, color = MaterialTheme.colorScheme.outline
        ),
        shape = MaterialTheme.shapes.medium,
//        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontFamily = rursusFamily,
//            fontSize = 18.sp,
                style = MaterialTheme.typography.titleLarge,
//            fontSize = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(100.dp)
                    .padding(5.dp)
//                    .background(color = MaterialTheme.colorScheme.secondary)
            )
            Text(
                text = "Please grant location description bla bla bla",
//            fontSize = 18.sp,
                fontFamily = rursusFamily,
                style = MaterialTheme.typography.bodyMedium,
//            fontSize = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 15.dp)
//                    .align(alignment = Alignment.CenterHorizontally)
            )
            SeparatorSpacer(modifier = Modifier.padding(top = 10.dp))
            Surface(
                onClick = { onYesClick() },
                modifier = Modifier
//                    .background(color = MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    text = stringResource(id = R.string.allow),
//            fontSize = 18.sp,
                    fontFamily = rursusFamily,
                    style = MaterialTheme.typography.bodyLarge,
//            fontSize = MaterialTheme.typography.titleMedium,
                    color = colorResource(id = R.color.allow),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(all = 10.dp)
                )
            }

            SeparatorSpacer()
            Surface(
                onClick = { onNoClick() },
                modifier = Modifier
//                    .background(color = MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    text = stringResource(id = R.string.decline),
//            fontSize = 18.sp,
                    fontFamily = rursusFamily,
                    style = MaterialTheme.typography.bodyLarge,
//            fontSize = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(all = 10.dp)
                )
            }
            /*Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onYesClick) {
                Text("Yes")
            }
            Button(onClick = onNoClick) {
                Text("No")
            }*/
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PermissionDialogStatelessPreview() {
    PermissionDialogStateless(
        shouldShowDialog = true,
        onAgreeClick = {},
        onDeclineClick = {},
        permissionEnum = PermissionEnum.LOCATION,
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Min)
            .padding(vertical = 100.dp, horizontal = 30.dp)
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PermissionDialogUIPreview() {
    PermissionDialogUI(title = "Give me Permission", onYesClick = {}, onNoClick = {})
}

/*fun isGpsEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}*/

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
