package com.viajacalu.mapu

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
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import com.viajacalu.mapu.core.auth.di.AuthModule
import com.viajacalu.mapu.core.auth.domain.AuthRepository
import com.viajacalu.mapu.core.designsystem.theme.MapuTheme
import com.viajacalu.mapu.core.testing.FakeAuthRepository
import com.viajacalu.mapu.core.testing.testUser
import com.viajacalu.mapu.navigation.MapuDestination
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

// Screens get their ViewModels from Hilt, so the real Firebase auth is swapped for a fake signed in user
@HiltAndroidTest
@UninstallModules(AuthModule::class)
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
class MapuAppTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @BindValue
    @JvmField
    val authRepository: AuthRepository = FakeAuthRepository(signedInUser = testUser)

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
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
