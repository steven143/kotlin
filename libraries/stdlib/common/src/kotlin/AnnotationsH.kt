/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package kotlin

import kotlin.annotation.AnnotationTarget.*

@Experimental
@Target(
    CLASS,
    ANNOTATION_CLASS,
    PROPERTY,
    FIELD,
    LOCAL_VARIABLE,
    VALUE_PARAMETER,
    CONSTRUCTOR,
    FUNCTION,
    PROPERTY_GETTER,
    PROPERTY_SETTER,
    TYPEALIAS
)
@Retention(AnnotationRetention.BINARY)
annotation class ExperimentalMultiplatform

/**
 * This annotation is only applicable to `expect` annotation classes in multi-platform projects and marks that class as "optional".
 * Optional expected class is allowed to have no corresponding actual class on the platform. Optional annotations can only be used
 * to annotate something, not as types in signatures. If an optional annotation has no corresponding actual class on a platform,
 * the annotation entries where it's used are simply erased when compiling code on that platform.
 */
@Target(ANNOTATION_CLASS)
@Retention(AnnotationRetention.BINARY)
@ExperimentalMultiplatform
annotation class OptionalExpectation
