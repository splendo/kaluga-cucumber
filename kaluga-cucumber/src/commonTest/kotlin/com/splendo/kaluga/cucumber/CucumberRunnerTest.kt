package com.splendo.kaluga.cucumber

import kotlin.test.Ignore
import kotlin.test.Test

sealed class CucumberDefinitionMock {
    class GivenMock(
        override val definitionString: String,
    ) : CucumberDefinitionMock(),
        Definition.Step.Given

    class ThenSingleMock(
        override val definitionString: String,
        mockParameter: String,
    ) : CucumberDefinitionMock(),
        Definition.Step.Then
}

sealed class StubTestCase : GherkinTestCase<Definition> {
    class TestGivenCase : StubTestCase() {
        override val definition: CucumberDefinitionMock.GivenMock = CucumberDefinitionMock.GivenMock("Test Given with GherkinLambdaMock0")
    }

    class TestThenCaseWithSingleParameter : StubTestCase() {
        companion object {
            const val EXPECTED_VALUE = "TEST_THEN"
        }

        override val definition: CucumberDefinitionMock.ThenSingleMock =
            CucumberDefinitionMock.ThenSingleMock(
                "Test Then with GherkinLambdaMock1",
                mockParameter = EXPECTED_VALUE,
            )
    }
}

class CucumberRunnerTest {
    @Ignore
    @Test
    fun test_given_case() {
        TODO("Implement once there is a common implementation for Gherkin Definitions")
    }

    @Ignore
    @Test
    fun test_then_case_with_parameter() {
        TODO("Implement once there is a common implementation for Gherkin Definitions")
    }
}
