package org.camunda.bpm.engine.impl.form.generic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public class GenericFormFieldConfiguration {
    private ArrayList<GenericFormFieldConfigurationConfig> configs = new ArrayList<GenericFormFieldConfigurationConfig>();
    
    public void addConfig(GenericFormFieldConfigurationConfig config) {
        this.configs.add(config);
    }
    
    public List<GenericFormFieldConfigurationConfig> getConfigs() {
        return this.configs;
    }
}
