package com.splendo.kaluga.cucumber.example

import com.splendo.kaluga.cucumber.CucumberDefinition
import com.splendo.kaluga.cucumber.Definition
import com.splendo.kaluga.cucumber.EXPECT_VALUE_STRING
import com.splendo.kaluga.cucumber.GherkinTestCase
import com.splendo.kaluga.uitest.TestCase

sealed class CounterTestCase : GherkinTestCase<Definition>, TestCase.Platform {

    companion object {
        val allCases: List<CounterTestCase> = listOf(
            GivenAppLaunched,
            WhenTapIncrement,
            ThenCounterShows,
        )
    }

    data object GivenAppLaunched : CounterTestCase() {
        override val definition = CucumberDefinition.Step.Given("the app is launched")
    }

    data object WhenTapIncrement : CounterTestCase() {
        override val definition = CucumberDefinition.Step.When("I tap the increment button")
    }

    data object ThenCounterShows : CounterTestCase() {
        override val definition = CucumberDefinition.Step.Then("the counter shows $EXPECT_VALUE_STRING")
    }
}
