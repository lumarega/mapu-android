package com.viajacalu.mapu.core.data.guides

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

// Reads the real guides.json, so a broken file fails here instead of in the app
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class LocalGuidesDataSourceTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun `reads the guides from guides json`() = runTest {
        val guides = dataSource().getGuides()

        assertTrue(guides.isNotEmpty())
    }

    // The test dispatcher runs the file reading on the test thread, so results are predictable
    private fun TestScope.dataSource() =
        LocalGuidesDataSource(context, StandardTestDispatcher(testScheduler))
}