package com.viajacalu.mapu.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.viajacalu.mapu.R
import com.viajacalu.mapu.feature.guides.GuidesDestination
import com.viajacalu.mapu.feature.guides.navigateToGuides
import com.viajacalu.mapu.feature.profile.ProfileDestination
import com.viajacalu.mapu.feature.profile.navigateToProfile
import kotlin.reflect.KClass

// The tabs in the bottom navigation bar
enum class TopLevelDestination(
    val route: KClass<*>,
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int
) {
    Guides(GuidesDestination::class, R.drawable.ic_map, R.string.tab_guides),
    Profile(ProfileDestination::class, R.drawable.ic_person, R.string.tab_profile)
}

fun NavController.navigateToTab(tab: TopLevelDestination) {
    val tabNavOptions = navOptions {
        // Guides is the first tab, so back from any other tab goes to Guides and then leaves the app.
        // Saving and restoring state keeps each tab's scroll position when switching between them.
        popUpTo<GuidesDestination> { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
    when (tab) {
        TopLevelDestination.Guides -> navigateToGuides(tabNavOptions)
        TopLevelDestination.Profile -> navigateToProfile(tabNavOptions)
    }
}
