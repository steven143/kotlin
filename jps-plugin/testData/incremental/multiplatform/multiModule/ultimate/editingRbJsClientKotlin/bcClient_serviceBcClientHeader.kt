//
// DON'T EDIT! This file is GENERATED by `MppJpsIncTestsGenerator` (called in generateTests)
// from `incremental/multiplatform/multiModule/ultimate/dependencies.txt`
//

expect fun bcClient_platformDependentBcClient(): String
fun bcClient_platformIndependentBcClient() = "common"

fun TestBcClient() {
  bcClient_platformIndependentBcClient()
  bcClient_platformDependentBcClient()
  bc_platformIndependentBc()
  bc_platformDependentBc()
  ac_platformIndependentAc()
  ac_platformDependentAc()
  acClient_platformIndependentAcClient()
  acClient_platformDependentAcClient()
}
