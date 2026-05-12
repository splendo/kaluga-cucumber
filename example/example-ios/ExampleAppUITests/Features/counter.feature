Feature: Counter

  Scenario: Counter starts at zero
    Given the app is launched
    Then the counter shows "0"

  Scenario: Increment counter
    Given the app is launched
    When I tap the increment button
    Then the counter shows "1"

  Scenario: Multiple increments
    Given the app is launched
    When I tap the increment button
    And I tap the increment button
    And I tap the increment button
    Then the counter shows "3"
