package com.splendo.kaluga.cucumber

expect class UserInfo
expect val userInfoDefault: UserInfo

expect fun disableCucumberishExceptionHandling(userInfo: UserInfo)
expect fun enableCucumberishExceptionHandling(userInfo: UserInfo)