/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.jps.build

import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.util.containers.MultiMap
import org.jetbrains.jps.ModuleChunk
import org.jetbrains.jps.builders.DirtyFilesHolder
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor
import org.jetbrains.jps.incremental.ModuleBuildTarget
import org.jetbrains.kotlin.jps.platforms.KotlinModuleBuilderTarget

import java.io.File

object KotlinSourceFileCollector {
    // For incremental compilation
    fun getDirtySourceFiles(
        dirtyFilesHolder: DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget>,
        chunk: org.jetbrains.jps.ModuleChunk,
        kotlinTarget: KotlinModuleBuilderTarget?
    ): MultiMap<ModuleBuildTarget, File> {
        val result = MultiMap<ModuleBuildTarget, File>()

//        dirtyFilesHolder.processDirtyFiles { target, file, root ->
//            if (isKotlinSourceFile(file)) {
//                result.putValue(target, file)
//            }
//            true
//        }

        // add files from common modules, whether or not they are in build scope
        addOutOfScopeCommonModuleFiles(kotlinTarget, chunk, result)

        return result
    }

    private fun addOutOfScopeCommonModuleFiles(
        kotlinTarget: KotlinModuleBuilderTarget?,
        chunk: ModuleChunk,
        result: MultiMap<ModuleBuildTarget, File>
    ) {
        if (kotlinTarget != null) {
            val context = kotlinTarget.context
            val fsState = context.projectDescriptor.fsState
            val scope = context.scope
            chunk.targets.forEach { target ->
                val delta = fsState.getEffectiveFilesDelta(context, target)
                delta.lockData()
                try {
                    for (entry in delta.sourcesToRecompile.entries) {
                        val isCommonRoot = entry.key is KotlinCommonModuleSourceRoot
                        entry.value.forEach { file ->
                            if (entry.key.target == target) {
                                if (scope.isAffected(target, file) || isCommonRoot) {
                                    if (isKotlinSourceFile(file)) {
                                        result.putValue(target, file)
                                    }
                                }
                            }
                        }
                    }
                } finally {
                    delta.unlockData()
                }
            }
        }
    }

    fun getRemovedKotlinFiles(
        dirtyFilesHolder: DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget>,
        target: ModuleBuildTarget
    ): List<File> =
        dirtyFilesHolder
            .getRemovedFiles(target)
            .mapNotNull { if (FileUtilRt.extensionEquals(it, "kt")) File(it) else null }

    internal fun isKotlinSourceFile(file: File): Boolean {
        return FileUtilRt.extensionEquals(file.name, "kt")
    }
}
