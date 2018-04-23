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
import org.jetbrains.jps.model.java.JpsJavaClasspathKind
import org.jetbrains.jps.model.java.JpsJavaExtensionService
import org.jetbrains.jps.model.module.JpsModule
import org.jetbrains.kotlin.jps.model.kotlinFacet

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

        val kotlinFacetExtension = module.kotlinFacet
        val implementedModuleNames = kotlinFacetExtension?.settings?.implementedModuleNames ?: return listOf()
        if (implementedModuleNames.isEmpty()) return listOf()

        val result = mutableListOf<JavaSourceRootDescriptor>()
        JpsJavaExtensionService.dependencies(module)
            .exportedOnly()
            .includedIn(JpsJavaClasspathKind.compile(moduleBuildTarget.isTests))
            .processModules {
                if (it.name in implementedModuleNames) {
                    if (moduleBuildTarget.isTests) result.addSourceRoots(it, JavaSourceRootType.TEST_SOURCE, target)

                    // Note, production sources should be added for both production and tests targets
                    result.addSourceRoots(it, JavaSourceRootType.SOURCE, target)
                }
            }

        return result
    }

    private fun MutableList<JavaSourceRootDescriptor>.addSourceRoots(
        it: JpsModule,
        sourceRootType: JavaSourceRootType,
        target: ModuleBuildTarget
    ) {
        it.getSourceRoots(sourceRootType).map {
            add(
                JavaSourceRootDescriptor(
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