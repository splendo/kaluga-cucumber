package com.splendo.kaluga.cucumber.example

class Counter {
    var value: Int = 0
        private set

    fun increment() {
        value++
    }

    fun reset(to: Int) {
        value = to
    }
}
