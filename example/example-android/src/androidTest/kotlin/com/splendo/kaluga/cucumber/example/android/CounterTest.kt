package com.splendo.kaluga.cucumber.example.android

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.splendo.kaluga.cucumber.example.assertCounterShows
import com.splendo.kaluga.cucumber.example.launchApp
import com.splendo.kaluga.cucumber.example.tapIncrementButton
import com.splendo.kaluga.uitest.DefaultApplicationAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CounterTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun runOnAdapter(block: DefaultApplicationAdapter.() -> Unit) {
        DefaultApplicationAdapter(composeRule).apply(block)
    }

    @Test
    fun counter_starts_at_zero() = runOnAdapter {
        assertCounterShows(this, "0")
    }

    @Test
    fun increment_counter() = runOnAdapter {
        tapIncrementButton(this)
        assertCounterShows(this, "1")
    }

    @Test
    fun multiple_increments() = runOnAdapter {
        tapIncrementButton(this)
        tapIncrementButton(this)
        tapIncrementButton(this)
        assertCounterShows(this, "3")
    }
}
