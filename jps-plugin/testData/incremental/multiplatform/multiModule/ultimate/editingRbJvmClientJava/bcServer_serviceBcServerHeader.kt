//
// DON'T EDIT! This file is GENERATED by `MppJpsIncTestsGenerator` (called in generateTests)
// from `incremental/multiplatform/multiModule/ultimate/dependencies.txt`
//

expect fun bcServer_platformDependentBcServer(): String
fun bcServer_platformIndependentBcServer() = "common"

fun TestBcServer() {
  bcServer_platformIndependentBcServer()
  bcServer_platformDependentBcServer()
  bc_platformIndependentBc()
  bc_platformDependentBc()
  ac_platformIndependentAc()
  ac_platformDependentAc()
  acServer_platformIndependentAcServer()
  acServer_platformDependentAcServer()
}
