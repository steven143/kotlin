//
// DON'T EDIT! This file is GENERATED by `MppJpsIncTestsGenerator` (called in generateTests)
// from `incremental/multiplatform/multiModule/multiplatformDependsOnRegular/dependencies.txt`
//

actual fun c_platformDependentC(): String = "pJvm"
fun pJvm_platformOnly() = "pJvm"

fun TestPJvm() {
  pJvm_platformOnly()
  PJvmJavaClass().doStuff()
  r_platformOnly()
  RJavaClass().doStuff()
  c_platformIndependentC()
  c_platformDependentC()
}
