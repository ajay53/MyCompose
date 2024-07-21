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
    SPLASH,
    DASHBOARD,
    AUTH,
}

enum class AuthScreens {
    LOGIN,
    REGISTRATION
}

enum class DashboardScreens {
    CONTENT_LIST,
    CONTENT_DETAIL
}

sealed class NavigationItem(val route: String) {
    data object Splash : NavigationItem(AllScreens.SPLASH.name)
    data object Dashboard : NavigationItem(AllScreens.DASHBOARD.name) {
        data object ContentList : NavigationItem(DashboardScreens.CONTENT_LIST.name)
        data object ContentDetail : NavigationItem(DashboardScreens.CONTENT_DETAIL.name)
    }
    data object Auth : NavigationItem(AllScreens.AUTH.name) {
        data object Login : NavigationItem(AuthScreens.LOGIN.name)
        data object Registration : NavigationItem(AuthScreens.REGISTRATION.name)
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

data object DashboardListScreenParams : ScreenParams {
    sealed interface NavBarActionState {
        data object Settings : NavBarActionState
    }

    private val _actionButtons = MutableSharedFlow<NavBarActionState>(extraBufferCapacity = 1)
    val actionButtons: Flow<NavBarActionState> = _actionButtons.asSharedFlow()

    override val route: String
        get() = DashboardScreens.CONTENT_LIST.name
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