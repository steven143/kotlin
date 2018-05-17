/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.resolve.checkers

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.ClassifierDescriptor
import org.jetbrains.kotlin.diagnostics.Errors
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType
import org.jetbrains.kotlin.resolve.BindingContext

class OptionalExpectationChecker : ClassifierUsageChecker {
    override fun check(targetDescriptor: ClassifierDescriptor, element: PsiElement, context: ClassifierUsageCheckerContext) {
        if (!ExpectedActualDeclarationChecker.isOptionalAnnotationClass(targetDescriptor)) return

        val entry = element.getParentOfType<KtAnnotationEntry>(true)
        if (entry != null) {
            val type = context.trace.bindingContext.get(BindingContext.TYPE, entry.typeReference)
            if (type != null && type.constructor.declarationDescriptor == targetDescriptor) {
                // This is a usage in an annotation entry, no error should be reported
                return
            }
        }

        context.trace.report(Errors.OPTIONAL_DECLARATION_OUTSIDE_OF_ANNOTATION_ENTRY.on(element))
    }
}
