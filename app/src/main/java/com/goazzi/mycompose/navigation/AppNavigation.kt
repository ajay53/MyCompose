package com.goazzi.mycompose.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

enum class AllScreens {
//    SPLASH,
    YELP,
    PIXABAY,
    ACCOUNT
}

enum class YelpScreens {
    ROOT
}

enum class PixabayScreens {
    ROOT,
    MEDIA_DETAIL
}

enum class AccountScreens {
    ROOT
}

sealed class NavigationItem(val route: String) {
//    data object Splash : NavigationItem(AllScreens.SPLASH.name)


    data object Yelp : NavigationItem(AllScreens.YELP.name) {
        data object Root : NavigationItem(YelpScreens.ROOT.name)
    }
    data object Pixabay : NavigationItem(AllScreens.PIXABAY.name) {
        data object Root : NavigationItem(PixabayScreens.ROOT.name)
        data object MediaDetail : NavigationItem(PixabayScreens.MEDIA_DETAIL.name)
    }
    data object Account : NavigationItem(AllScreens.ACCOUNT.name) {
        data object Root : NavigationItem(AccountScreens.ROOT.name)
    }
}

@SuppressLint("RestrictedApi")
sealed interface ScreenParams {
    val route: String
    val routeParam: String
    val isAppBarVisible: Boolean
    val navigationIcon: ImageVector?
    val navigationIconContentDescription: String?
    val onNavigationIconClick: (() -> Unit)?
    val title: String
    val navigationActions: @Composable (RowScope.() -> Unit)
}

data object YelpScreenParams : ScreenParams {
    sealed interface NavBarActionState {
        data object Settings : NavBarActionState
    }

    private val _actionButtons = MutableSharedFlow<NavBarActionState>(extraBufferCapacity = 1)
    val actionButtons: Flow<NavBarActionState> = _actionButtons.asSharedFlow()

    override val route: String
        get() = YelpScreens.ROOT.name
    override val routeParam: String
        get() = ""
    override val isAppBarVisible: Boolean
        get() = true
    override val navigationIcon: ImageVector?
        get() = Icons.Filled.Home
    override val navigationIconContentDescription: String?
        get() = "Home"
    override val onNavigationIconClick: (() -> Unit)?
        get() = null
    override val title: String
        get() = "Restaurants"
    override val navigationActions: @Composable (RowScope.() -> Unit)
        get() = {
            IconButton(onClick = {
                _actionButtons.tryEmit(NavBarActionState.Settings)
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Localized description"
                )
            }
        }
}

data object MediaDetailScreenParams : ScreenParams {
    sealed interface NavBarActionState {
        data object Settings : NavBarActionState
    }

    private val _actionButtons = MutableSharedFlow<NavBarActionState>(extraBufferCapacity = 1)
    val actionButtons: Flow<NavBarActionState> = _actionButtons.asSharedFlow()

    override val route: String
        get() = PixabayScreens.ROOT.name
    override val routeParam: String
        get() = ""
    override val isAppBarVisible: Boolean
        get() = true
    override val navigationIcon: ImageVector?
        get() = Icons.Filled.Home
    override val navigationIconContentDescription: String?
        get() = "Home"
    override val onNavigationIconClick: (() -> Unit)?
        get() = null
    override val title: String
        get() = "Images"
    override val navigationActions: @Composable (RowScope.() -> Unit)
        get() = {
            IconButton(onClick = {
                _actionButtons.tryEmit(NavBarActionState.Settings)
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Localized description"
                )
            }
        }
}

data object AccountScreenParams : ScreenParams {
    sealed interface NavBarActionState {
        data object Settings : NavBarActionState
    }

    private val _actionButtons = MutableSharedFlow<NavBarActionState>(extraBufferCapacity = 1)
    val actionButtons: Flow<NavBarActionState> = _actionButtons.asSharedFlow()

    override val route: String
        get() = AccountScreens.ROOT.name
    override val routeParam: String
        get() = ""
    override val isAppBarVisible: Boolean
        get() = true
    override val navigationIcon: ImageVector?
        get() = Icons.Filled.Home
    override val navigationIconContentDescription: String?
        get() = "Home"
    override val onNavigationIconClick: (() -> Unit)?
        get() = null
    override val title: String
        get() = "Account"
    override val navigationActions: @Composable (RowScope.() -> Unit)
        get() = {
            IconButton(onClick = {
                _actionButtons.tryEmit(NavBarActionState.Settings)
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Localized description"
                )
            }
        }
}