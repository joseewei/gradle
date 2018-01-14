/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.internal.artifacts.ivyservice.resolveengine.graph.builder;

import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.artifacts.component.ComponentSelector;
import org.gradle.api.artifacts.result.ComponentSelectionReason;
import org.gradle.api.internal.artifacts.ComponentSelectorConverter;
import org.gradle.internal.component.model.DependencyMetadata;

class DependencyState {
    private final DependencyMetadata dependencyMetadata;
    private final ComponentSelectorConverter componentSelectorConverter;
    private final ComponentSelector originalSelector;
    private final ComponentSelectionReason selectionReason;

    private ModuleIdentifier moduleIdentifier;

    DependencyState(DependencyMetadata dependencyMetadata, ComponentSelectorConverter componentSelectorConverter) {
        this(dependencyMetadata, componentSelectorConverter, dependencyMetadata.getSelector(), null);
    }

    public DependencyState(DependencyMetadata dependencyMetadata, ComponentSelectorConverter componentSelectorConverter,
                           ComponentSelector originalSelector, ComponentSelectionReason reason) {
        this.dependencyMetadata = dependencyMetadata;
        this.componentSelectorConverter = componentSelectorConverter;
        this.originalSelector = originalSelector;
        this.selectionReason = reason;
    }

    public DependencyMetadata getDependencyMetadata() {
        return dependencyMetadata;
    }

    public ComponentSelector getOriginalSelector() {
        return originalSelector;
    }

    public ModuleIdentifier getModuleIdentifier() {
        if (moduleIdentifier == null) {
            moduleIdentifier = componentSelectorConverter.getModule(dependencyMetadata.getSelector());
        }
        return moduleIdentifier;
    }

    public DependencyState withTarget(ComponentSelector target, ComponentSelectionReason componentSelectionReason) {
        DependencyMetadata targeted = dependencyMetadata.withTarget(target);
        return new DependencyState(targeted, componentSelectorConverter, originalSelector, componentSelectionReason);
    }

    public ComponentSelectionReason getSelectionReason() {
        return selectionReason;
    }
}
