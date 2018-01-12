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

package org.gradle.internal.component.external.model;

import com.google.common.collect.ImmutableList;
import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentSelector;
import org.gradle.api.internal.attributes.AttributesSchemaInternal;
import org.gradle.api.internal.attributes.ImmutableAttributes;
import org.gradle.internal.component.model.AttributeConfigurationSelector;
import org.gradle.internal.component.model.ComponentResolveMetadata;
import org.gradle.internal.component.model.ConfigurationMetadata;
import org.gradle.internal.component.model.ExcludeMetadata;
import org.gradle.internal.component.model.IvyArtifactName;

import java.util.Collection;
import java.util.List;

/**
 * Represents dependency information as stored in an external descriptor file.
 * This information is able to be transformed into a `ModuleDependencyMetadata` instance.
 */
public abstract class ExternalDependencyDescriptor {

    public abstract ModuleComponentSelector getSelector();

    public abstract boolean isOptional();

    public abstract boolean isChanging();

    public abstract boolean isTransitive();

    protected abstract ExternalDependencyDescriptor withRequested(ModuleComponentSelector newRequested);

    public List<ConfigurationMetadata> getMetadataForConfigurations(ImmutableAttributes consumerAttributes, AttributesSchemaInternal consumerSchema, ComponentIdentifier fromComponent, ConfigurationMetadata fromConfiguration, ComponentResolveMetadata targetComponent) {
        if (targetComponent.requiresAttributeMatching()) {
            // This condition shouldn't be here, and attribute matching should always be applied when the target has variants
            // however, the schemas and metadata implementations are not yet set up for this, so skip this unless:
            // - the consumer has asked for something specific (by providing attributes), as the other metadata types are broken for the 'use defaults' case
            // - or the target is a component from a Maven/Ivy repo as we can assume this is well behaved
            if (!consumerAttributes.isEmpty() || targetComponent instanceof ModuleComponentResolveMetadata) {
                return ImmutableList.of(AttributeConfigurationSelector.selectConfigurationUsingAttributeMatching(consumerAttributes, targetComponent, consumerSchema));
            }
        }
        return selectLegacyConfigurations(fromComponent, fromConfiguration, targetComponent);
    }

    protected abstract List<ConfigurationMetadata> selectLegacyConfigurations(ComponentIdentifier fromComponent, ConfigurationMetadata fromConfiguration, ComponentResolveMetadata targetComponent);

    public abstract List<ExcludeMetadata> getConfigurationExcludes(Collection<String> configurations);

    public abstract List<IvyArtifactName> getConfigurationArtifacts(ConfigurationMetadata fromConfiguration);
}
