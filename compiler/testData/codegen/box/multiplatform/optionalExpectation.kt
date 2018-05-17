// !LANGUAGE: +MultiPlatformProjects
// !USE_EXPERIMENTAL: kotlin.ExperimentalMultiplatform
// TARGET_BACKEND: JVM
// WITH_RUNTIME
// FILE: common.kt

@OptionalExpectation
expect annotation class Anno(val s: String)

// FILE: jvm.kt

import kotlin.test.assertEquals

class Foo {
    @Anno("o_O")
    fun bar() {}
}

fun box(): String {
    val method = Foo::class.java.declaredMethods.single { it.name == "bar" }
    assertEquals("", method.annotations.joinToString())
    return "OK"
}
