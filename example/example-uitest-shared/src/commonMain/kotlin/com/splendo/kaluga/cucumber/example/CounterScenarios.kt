package com.splendo.kaluga.cucumber.example

import com.splendo.kaluga.uitest.ApplicationAdapter

fun launchApp(adapter: ApplicationAdapter) {
    adapter.launch(null, emptyMap())
}

fun tapIncrementButton(adapter: ApplicationAdapter) {
    adapter.findViewByTag(Tags.INCREMENT_BUTTON).tap()
}

@Throws(AssertionError::class)
fun assertCounterShows(adapter: ApplicationAdapter, expected: String) {
    adapter.assert(adapter.findViewByTag(Tags.COUNTER_VALUE).isTextEqualTo(expected, contains = false))
}
