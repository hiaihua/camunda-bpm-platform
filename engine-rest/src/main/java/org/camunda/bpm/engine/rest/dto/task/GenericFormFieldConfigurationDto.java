package org.camunda.bpm.engine.rest.dto.task;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
class GenericFormFieldConfigurationDto {
    private ArrayList<GenericFormFieldConfigurationConfigDto> configs = new ArrayList<GenericFormFieldConfigurationConfigDto>();
    
    public void addConfig(GenericFormFieldConfigurationConfigDto config) {
        this.configs.add(config);
    }
    
    public List<GenericFormFieldConfigurationConfigDto> getConfigs() {
        return this.configs;
    }
}
