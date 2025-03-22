package com.goazzi.mycompose.navigation

import timber.log.Timber

object UtilNavigation {

    fun getRouteName(navigationItem: NavigationItem): String {
        var route = ""
        when (navigationItem) {



            /*NavigationItem.Splash -> {
                route = NavigationItem.Splash.route
            }

            NavigationItem.Root -> {
                route = NavigationItem.Root.route

                if (DoorLockRootParams.routeParam.isNotBlank()) {
                    route += "/{${DoorLockRootParams.routeParam}}"
                }
                Timber.tag("getRouteName").d("route: $route")

//            return route
//            "${NavigationItem.Root.route}/{${DoorLockRootParams.routeParam}}"
            }

            NavigationItem.LockSite -> {
                Timber.tag("getRouteName")
                    .d("LockSiteScreenParams.routeParam: ${LockSiteScreenParams.routeParam}")
                route = NavigationItem.LockSite.route

                if (LockSiteScreenParams.routeParam.isNotBlank()) {
                    route += "/{${LockSiteScreenParams.routeParam}}"
                }

                Timber.tag("getRouteName").d("route: $route")

//            return route

//            "${NavigationItem.LockSite.route}/{${LockSiteScreenParams.routeParam}}"
            }

            NavigationItem.Settings -> {
                route = NavigationItem.Settings.route
            }

            NavigationItem.Support -> {
                route = NavigationItem.Support.route
            }

            NavigationItem.Settings.Root -> {
                route = NavigationItem.Settings.Root.route
            }

            NavigationItem.Settings.KeyDetails -> {
                route = NavigationItem.Settings.KeyDetails.route

                if (SettingsKeyDetailsParams.routeParam.isNotBlank()) {
                    route += "/{${SettingsKeyDetailsParams.routeParam}}"
                }
                Timber.tag(TAG).d("getRouteName: route: $route")
            }

            NavigationItem.Settings.Widgets -> {
                route = NavigationItem.Settings.Widgets.route
            }

            NavigationItem.Settings.InstallKey -> {
                route = NavigationItem.Settings.InstallKey.route
            }*/
            NavigationItem.Account -> {
                route = NavigationItem.Account.route
            }
            NavigationItem.Account.Root -> {
                route = NavigationItem.Account.Root.route
            }
            NavigationItem.Pixabay -> {
                route = NavigationItem.Pixabay.route
            }
            NavigationItem.Pixabay.Root -> {
                route = NavigationItem.Pixabay.Root.route
            }
            NavigationItem.Yelp ->  {
                route = NavigationItem.Yelp.route
            }
            NavigationItem.Yelp.Root -> {
                route = NavigationItem.Yelp.Root.route
            }

            NavigationItem.Pixabay.MediaDetail -> {
                route = NavigationItem.Pixabay.MediaDetail.route
            }
        }
        return route
    }
}