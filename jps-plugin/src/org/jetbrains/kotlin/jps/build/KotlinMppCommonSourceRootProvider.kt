/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.jps.build

import org.jetbrains.jps.builders.AdditionalRootsProviderService
import org.jetbrains.jps.builders.BuildTarget
import org.jetbrains.jps.builders.java.JavaModuleBuildTargetType
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor
import org.jetbrains.jps.builders.storage.BuildDataPaths
import org.jetbrains.jps.incremental.ModuleBuildTarget
import org.jetbrains.jps.model.java.JavaSourceRootType
import org.jetbrains.jps.model.module.JpsModule
import org.jetbrains.kotlin.jps.model.expectedByModules
import java.io.File

/**
 * Required for Multiplatform Projects.
 *
 * Adds all the source roots of the expectedBy modules to the platform modules.
 */
class KotlinMppCommonSourceRootProvider : AdditionalRootsProviderService<JavaSourceRootDescriptor>(JavaModuleBuildTargetType.ALL_TYPES) {
    override fun getAdditionalRoots(
        target: BuildTarget<JavaSourceRootDescriptor>,
        dataPaths: BuildDataPaths?
    ): List<JavaSourceRootDescriptor> {
        val moduleBuildTarget = target as? ModuleBuildTarget ?: return listOf()
        val module = moduleBuildTarget.module

        val result = mutableListOf<JavaSourceRootDescriptor>()

        module.expectedByModules.forEach { module ->
            if (moduleBuildTarget.isTests) {
                result.addSourceRoots(module, JavaSourceRootType.TEST_SOURCE, target)
            } else {
                result.addSourceRoots(module, JavaSourceRootType.SOURCE, target)
            }
        }

        return result
    }

    private fun MutableList<JavaSourceRootDescriptor>.addSourceRoots(
        module: JpsModule,
        sourceRootType: JavaSourceRootType,
        target: ModuleBuildTarget
    ) {
        module.getSourceRoots(sourceRootType).map {
            add(
                KotlinCommonModuleSourceRoot(
                    module,
                    it.file,
                    target,
                    it.properties.isForGeneratedSources,
                    false,
                    it.properties.packagePrefix,
                    setOf()
                )
            )
        }
    }
}

class KotlinCommonModuleSourceRoot(
    val commonModule: JpsModule,
    root: File,
    target: ModuleBuildTarget,
    isGenerated: Boolean,
    isTemp: Boolean,
    packagePrefix: String,
    excludes: Set<File>
) : JavaSourceRootDescriptor(root, target, isGenerated, isTemp, packagePrefix, excludes)