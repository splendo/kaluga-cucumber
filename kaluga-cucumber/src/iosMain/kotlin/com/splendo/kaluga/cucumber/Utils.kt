package com.splendo.kaluga.cucumber

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.pointed
import kotlinx.cinterop.value
import platform.Foundation.NSDictionary
import platform.darwin.NSObject
import platform.objc.class_addMethod
import platform.objc.class_getClassMethod
import platform.objc.class_getInstanceMethod
import platform.objc.class_replaceMethod
import platform.objc.method_exchangeImplementations
import platform.objc.method_getImplementation
import platform.objc.method_getTypeEncoding
import platform.objc.sel_registerName

actual typealias UserInfo = NSDictionary
actual val userInfoDefault = NSDictionary()

@OptIn(BetaInteropApi::class)
actual fun disableCucumberishExceptionHandling(userInfo: UserInfo) {
    val (testCaseCastedToNSObject, originalSelector, targetSelector) = selectorsWithUserInfo(
        userInfo,
        disable = true
    )

    swizzleMethods(
        actualImplementationSelector = originalSelector,
        newImplementationSelector = targetSelector,
        actualClass = testCaseCastedToNSObject.`class`()!!,
        newImplementationClass = testCaseCastedToNSObject.`class`()!!,
        actualImplementationIsClassMethod = false,
        newImplementationIsClassMethod = false
    )
}

@OptIn(BetaInteropApi::class)
actual fun enableCucumberishExceptionHandling(userInfo: UserInfo) {
    val (testCaseCastedToNSObject, originalSelector, targetSelector) = selectorsWithUserInfo(
        userInfo,
        disable = false
    )

    swizzleMethods(
        actualImplementationSelector = originalSelector,
        newImplementationSelector = targetSelector,
        actualClass = testCaseCastedToNSObject.`class`()!!,
        newImplementationClass = testCaseCastedToNSObject.`class`()!!,
        actualImplementationIsClassMethod = false,
        newImplementationIsClassMethod = false
    )
}

private typealias OriginalSelector = COpaquePointer?
private typealias TargetSelector = COpaquePointer?
private typealias XCTestCaseClass = NSObject

private typealias SwizzleSelectors = Triple<XCTestCaseClass, OriginalSelector, TargetSelector>

private fun selectorsWithUserInfo(
    userInfo: UserInfo,
    disable: Boolean
): SwizzleSelectors {
    val testCase = userInfo.objectForKey("XCTestCase")
    val testCaseCastedToNSObject = (testCase as NSObject)

    val originalSelector = sel_registerName(
        if (disable)
            "recordFailureWithDescription:inFile:atLine:expected:"
        else
            "cucumberish_recordFailureWithDescription:inFile:atLine:expected:"
    )

    val targetSelector = sel_registerName(
        if (disable)
            "cucumberish_recordFailureWithDescription:inFile:atLine:expected:"
        else
            "recordFailureWithDescription:inFile:atLine:expected:"
    )

    return Triple(testCaseCastedToNSObject, originalSelector, targetSelector)
}

/**
 * A copied version of the swizzling method inside the Cucumberish ObjC framework.
 *
 */
@OptIn(BetaInteropApi::class)
fun swizzleMethods(
    actualImplementationSelector: COpaquePointer?,
    newImplementationSelector: COpaquePointer?,
    actualClass: ObjCClass,
    newImplementationClass: ObjCClass,
    actualImplementationIsClassMethod: Boolean,
    newImplementationIsClassMethod: Boolean
) {
    // Depending whether the class we want to update has the method as a class method or as a member method, we call 2 different API.
    val originalMethod = if (actualImplementationIsClassMethod) {
        class_getClassMethod(actualClass, actualImplementationSelector)
    } else {
        class_getInstanceMethod(actualClass, actualImplementationSelector)
    }

    // Depending whether the class we want to replace with the actualClass has the method as a class method or as a member method, we call 2 different API.
    val swizzledMethod = if (newImplementationIsClassMethod) {
        class_getClassMethod(newImplementationClass, newImplementationSelector)
    } else {
        class_getInstanceMethod(newImplementationClass, newImplementationSelector)
    }

    // Call this API to check if the method from the new implementation is added to the old class with the signature of the old class' method
    val didAddMethod = class_addMethod(
        actualClass,
        actualImplementationSelector,
        method_getImplementation(swizzledMethod),
        method_getTypeEncoding(swizzledMethod)!!.pointed.value.toString()
    )
    if (didAddMethod) {
        class_replaceMethod(
            actualClass,
            newImplementationSelector,
            method_getImplementation(originalMethod),
            method_getTypeEncoding(originalMethod)!!.pointed.value.toString()
        )
    } else {
        method_exchangeImplementations(originalMethod, swizzledMethod)
    }
}