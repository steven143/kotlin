// !LANGUAGE: +MultiPlatformProjects
// !USE_EXPERIMENTAL: kotlin.ExperimentalMultiplatform
// TARGET_BACKEND: JVM
// WITH_RUNTIME

@OptionalExpectation
expect annotation class Anno(val s: String)

class Foo {
    @Anno("o_O")
    fun bar() {}
}
