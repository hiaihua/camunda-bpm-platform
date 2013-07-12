package org.camunda.bpm.engine.rest.dto.task;

/**
 *
 * @author msiebers <info@msiebers.de>
 */
public class GenericFormFieldConfigurationConfigDto {
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
}
