/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.jps.build

import org.jetbrains.jps.ModuleChunk
import org.jetbrains.jps.builders.DirtyFilesHolder
import org.jetbrains.jps.builders.java.JavaBuilderUtil
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor
import org.jetbrains.jps.incremental.CompileContext
import org.jetbrains.jps.incremental.ModuleBuildTarget
import org.jetbrains.jps.incremental.ModuleLevelBuilder
import org.jetbrains.kotlin.jps.platforms.kotlinBuildTargets

class KotlinChunkBuildRound(
    val context: CompileContext,
    val chunk: ModuleChunk,
    val jpsDirtyFilesHolder: DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget>,
    val outputConsumer: ModuleLevelBuilder.OutputConsumer
) {
    val kotlinRepresentativeTarget = context.kotlinBuildTargets[chunk.representativeTarget()]

    val messageCollector = MessageCollectorAdapter(context, kotlinRepresentativeTarget)
    val fsOperations = FSOperationsHelper(context, chunk, KotlinBuilder.LOG)

    val dirtyFilesHolder = KotlinChunkDirtySourceFilesHolder(chunk, context)
    val projectDescriptor = context.projectDescriptor
    val dataManager = projectDescriptor.dataManager
    val targets = chunk.targets

    val hasKotlin = HasKotlinMarker(dataManager)
    val rebuildAfterCacheVersionChanged = RebuildAfterCacheVersionChangeMarker(dataManager)
    val isChunkRebuilding = JavaBuilderUtil.isForcedRecompilationAllJavaModules(context)
            || targets.any { rebuildAfterCacheVersionChanged[it] == true }
}