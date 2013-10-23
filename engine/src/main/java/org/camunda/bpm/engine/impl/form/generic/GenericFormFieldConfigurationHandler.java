package org.camunda.bpm.engine.impl.form.generic;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
class GenericFormFieldConfigurationHandler {
private ArrayList<GenericFormFieldConfigurationConfigHandler> configs = new ArrayList<GenericFormFieldConfigurationConfigHandler>();

    public void addConfig(GenericFormFieldConfigurationConfigHandler config) {
        this.configs.add(config);
    }

    public List<GenericFormFieldConfigurationConfigHandler> getConfigs() {
        return this.configs;
    }

    public GenericFormFieldConfiguration initializeGenericFormFieldConfiguration(final ProcessDefinitionEntity processDefinition, final VariableScope variableScope) {
        GenericFormFieldConfiguration configuration = new GenericFormFieldConfiguration();

        for (GenericFormFieldConfigurationConfigHandler config : this.configs) {
            configuration.addConfig(config.initializeGenericFormFieldConfigurationConfig(processDefinition, variableScope));
        }

        return configuration;
    }
}
