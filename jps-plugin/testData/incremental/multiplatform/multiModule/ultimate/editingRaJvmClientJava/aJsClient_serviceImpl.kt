//
// DON'T EDIT! This file is GENERATED by `MppJpsIncTestsGenerator` (called in generateTests)
// from `incremental/multiplatform/multiModule/ultimate/dependencies.txt`
//

actual fun ac_platformDependentAc(): String = "aJsClient"
actual fun acClient_platformDependentAcClient(): String = "aJsClient"
fun aJsClient_platformOnly() = "aJsClient"

fun TestAJsClient() {
  aJsClient_platformOnly()
  ac_platformIndependentAc()
  ac_platformDependentAc()
  acClient_platformIndependentAcClient()
  acClient_platformDependentAcClient()
  raJsClient_platformOnly()
}
