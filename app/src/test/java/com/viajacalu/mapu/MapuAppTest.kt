package com.viajacalu.mapu

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.viajacalu.mapu.core.designsystem.theme.MapuTheme
import com.viajacalu.mapu.navigation.MapuDestination
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapuAppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            MapuTheme {
                MapuApp(startDestination = MapuDestination.Guides, navController = navController)
            }
        }
    }

    @Test
    fun `on Guides the bottom bar is shown with Guides selected`() {
        tab(R.string.tab_guides).assertIsDisplayed().assertIsSelected()
        tab(R.string.tab_profile).assertIsDisplayed().assertIsNotSelected()
        screenTitle(R.string.guides_title).assertIsDisplayed()
    }

    @Test
    fun `tapping Profile shows the Profile screen with Profile selected`() {
        tab(R.string.tab_profile).performClick()

        screenTitle(R.string.profile_title).assertIsDisplayed()
        tab(R.string.tab_profile).assertIsSelected()
        tab(R.string.tab_guides).assertIsNotSelected()
    }

    @Test
    fun `back from Profile goes to Guides`() {
        tab(R.string.tab_profile).performClick()

        pressBack()

        screenTitle(R.string.guides_title).assertIsDisplayed()
        tab(R.string.tab_guides).assertIsSelected()
    }

    @Test
    fun `switching tabs keeps one copy of each tab in the back stack`() {
        tab(R.string.tab_profile).performClick()
        tab(R.string.tab_guides).performClick()
        tab(R.string.tab_profile).performClick()

        composeTestRule.runOnIdle {
            assertEquals(1, navController.backStack.count { it.destination.hasRoute<MapuDestination.Guides>() })
            assertEquals(1, navController.backStack.count { it.destination.hasRoute<MapuDestination.Profile>() })
        }
    }

    // Tabs are the only selectable items, which tells them apart from screen titles with the same text
    private fun tab(@StringRes labelRes: Int): SemanticsNodeInteraction =
        composeTestRule.onNode(hasText(string(labelRes)) and isSelectable())

    private fun screenTitle(@StringRes titleRes: Int): SemanticsNodeInteraction =
        composeTestRule.onNode(hasText(string(titleRes)) and isHeading())

    private fun string(@StringRes resId: Int): String = composeTestRule.activity.getString(resId)

    private fun pressBack() {
        // Lets the previous navigation settle first, as back is ignored while NavHost is still animating
        composeTestRule.waitForIdle()
        composeTestRule.runOnUiThread {
            composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
        }
    }
}
