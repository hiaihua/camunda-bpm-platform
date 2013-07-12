package org.camunda.bpm.engine.impl.form.generic;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;

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

    public GenericFormFieldConfiguration initializeGenericFormFieldConfiguration(ExecutionEntity execution) {
        GenericFormFieldConfiguration configuration = new GenericFormFieldConfiguration();
        
        for (GenericFormFieldConfigurationConfigHandler config : this.configs) {
            configuration.addConfig(config.initializeGenericFormFieldConfigurationConfig(execution));
        }
        
        return configuration;
    }
}
