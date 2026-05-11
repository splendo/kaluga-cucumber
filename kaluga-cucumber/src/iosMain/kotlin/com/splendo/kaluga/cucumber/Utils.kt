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

private const val USER_INFO_XCTESTCASE_KEY = "XCTestCase"

/**
 * Disables Cucumberish's custom exception throwing.
 * @param disable Represents whether the
 */
@OptIn(BetaInteropApi::class)
actual fun toggleCucumberishExceptionHandling(
    userInfo: UserInfo,
    disable: Boolean,
) {
    val testCaseCastedToNSObject = xcTestCaseAsNSObject(userInfo)
    val (originalSelector, targetSelector) = methodSelectors(disable = disable)

    swizzleMethods(
        actualImplementationSelector = originalSelector,
        newImplementationSelector = targetSelector,
        actualClass = testCaseCastedToNSObject.`class`()!!,
        newImplementationClass = testCaseCastedToNSObject.`class`()!!,
        actualImplementationIsClassMethod = false,
        newImplementationIsClassMethod = false,
    )
}

private typealias OriginalSelector = COpaquePointer?
private typealias TargetSelector = COpaquePointer?
private typealias XCTestCaseClass = NSObject

/**
 * For each step, Cucumberish passes this NSDictionary that contains the specific XCTestCase running
 * for that step.
 * "XCTestCase" is the key used to store the `XCTestCase`.
 */
private fun xcTestCaseAsNSObject(userInfo: UserInfo): XCTestCaseClass {
    val testCase = userInfo.objectForKey(USER_INFO_XCTESTCASE_KEY)
    val testCaseCastedToNSObject = (testCase as NSObject)
    return testCaseCastedToNSObject
}

/**
 * Create and returns two pointers for two methods with the passed name.
 * Eventually these pointers are passed to some API that takes also a class type and can identify
 * what methods the pointers are referring to.
 */
private fun methodSelectors(disable: Boolean): Pair<OriginalSelector, TargetSelector> {
    val originalSelector =
        sel_registerName(
            if (disable) {
                "recordFailureWithDescription:inFile:atLine:expected:"
            } else {
                "cucumberish_recordFailureWithDescription:inFile:atLine:expected:"
            },
        )

    val targetSelector =
        sel_registerName(
            if (disable) {
                "cucumberish_recordFailureWithDescription:inFile:atLine:expected:"
            } else {
                "recordFailureWithDescription:inFile:atLine:expected:"
            },
        )
    return Pair(originalSelector, targetSelector)
}

/**
 * Takes care of swapping the implementation of two methods in two classes or on the same one.
 */
@OptIn(BetaInteropApi::class)
private fun swizzleMethods(
    actualImplementationSelector: COpaquePointer?,
    newImplementationSelector: COpaquePointer?,
    actualClass: ObjCClass,
    newImplementationClass: ObjCClass,
    actualImplementationIsClassMethod: Boolean,
    newImplementationIsClassMethod: Boolean,
) {
    // Depending whether the class we want to update has the method as a class method or as a member method, we call 2 different API.
    val originalMethod =
        if (actualImplementationIsClassMethod) {
            class_getClassMethod(actualClass, actualImplementationSelector)
        } else {
            class_getInstanceMethod(actualClass, actualImplementationSelector)
        }

    // Depending whether the class we want to replace with the actualClass has the method as a class method or as a member method, we call 2 different API.
    val swizzledMethod =
        if (newImplementationIsClassMethod) {
            class_getClassMethod(newImplementationClass, newImplementationSelector)
        } else {
            class_getInstanceMethod(newImplementationClass, newImplementationSelector)
        }

    // Call this API to check if the method from the new implementation
    // is added to the old class with the signature of the old class' method.
    val didAddMethod =
        class_addMethod(
            actualClass,
            actualImplementationSelector,
            method_getImplementation(swizzledMethod),
            method_getTypeEncoding(swizzledMethod)!!.pointed.value.toString(),
        )
    if (didAddMethod) {
        // Swaps the old method implementation into the newImplementationSelector
        class_replaceMethod(
            actualClass,
            newImplementationSelector,
            method_getImplementation(originalMethod),
            method_getTypeEncoding(originalMethod)!!.pointed.value.toString(),
        )
    } else {
        // Swaps originalMethod with swizzledMethod and vice versa without referring to a specific class.
        method_exchangeImplementations(originalMethod, swizzledMethod)
    }
}
