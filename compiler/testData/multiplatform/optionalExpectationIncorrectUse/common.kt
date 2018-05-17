// WITH_RUNTIME
// ADDITIONAL_COMPILER_ARGUMENTS: -Xuse-experimental=kotlin.ExperimentalMultiplatform

@OptionalExpectation
expect annotation class A()

fun useInSignature(a: A) = a.toString()

@OptionalExpectation
expect class NotAnAnnotationClass
