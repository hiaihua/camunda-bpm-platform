package org.camunda.bpm.engine.impl.form.generic;

import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
class GenericFormFieldConfigurationConfigHandler {
    private String name;
    private String config;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public GenericFormFieldConfigurationConfig initializeGenericFormFieldConfigurationConfig(final ProcessDefinitionEntity processDefinition, final VariableScope variableScope) {
        GenericFormFieldConfigurationConfig configurationConfig = new GenericFormFieldConfigurationConfig();

        configurationConfig.setConfig(this.config);
        configurationConfig.setName(this.name);

        return configurationConfig;
    }
}
