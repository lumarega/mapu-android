package com.viajacalu.mapu.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavController
import com.viajacalu.mapu.R

// The tabs in the bottom navigation bar
enum class TopLevelDestination(
    val route: MapuDestination,
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int
) {
    Guides(MapuDestination.Guides, R.drawable.ic_map, R.string.tab_guides),
    Profile(MapuDestination.Profile, R.drawable.ic_person, R.string.tab_profile)
}

fun NavController.navigateToTab(tab: TopLevelDestination) {
    navigate(tab.route) {
        // Guides is the first tab, so back from any other tab goes to Guides and then leaves the app.
        // Saving and restoring state keeps each tab's scroll position when switching between them.
        popUpTo<MapuDestination.Guides> { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
