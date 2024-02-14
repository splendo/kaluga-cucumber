package com.splendo.kaluga.cucumber

expect class UserInfo
expect val userInfoDefault: UserInfo

expect fun toggleCucumberishExceptionHandling(userInfo: UserInfo, disable: Boolean)
