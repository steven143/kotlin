/*
 * Copyright 2010-2012 JetBrains s.r.o.
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

package org.jetbrains.jet.lang.resolve.java.data;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.lang.resolve.java.DescriptorResolverUtils;
import org.jetbrains.jet.lang.resolve.name.FqName;

public abstract class ResolverScopeData extends ClassPsiDeclarationProviderBase {

    @Nullable
    private final PsiPackage psiPackage;

    public ResolverScopeData(
            @Nullable PsiClass psiClass,
            @Nullable PsiPackage psiPackage,
            @Nullable FqName fqName,
            boolean staticMembers
    ) {
        super(staticMembers, psiClass);
        DescriptorResolverUtils.checkPsiClassIsNotJet(psiClass);

        this.psiPackage = psiPackage;

        if (psiClass == null && psiPackage == null) {
            throw new IllegalStateException("both psiClass and psiPackage cannot be null");
        }

        //TODO: move check to remove fqName parameter
    }

    @NotNull
    public PsiPackage getPsiPackage() {
        return psiPackage;
    }
}
