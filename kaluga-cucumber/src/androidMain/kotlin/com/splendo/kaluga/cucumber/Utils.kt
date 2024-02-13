package com.splendo.kaluga.cucumber

actual typealias UserInfo = Unit
actual val userInfoDefault = Unit

actual fun disableCucumberishExceptionHandling(userInfo: UserInfo) = Unit
actual fun enableCucumberishExceptionHandling(userInfo: UserInfo) = Unit
