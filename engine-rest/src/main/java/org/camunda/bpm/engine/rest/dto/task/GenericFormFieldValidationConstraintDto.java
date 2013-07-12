package org.camunda.bpm.engine.rest.dto.task;

/**
 *
 * @author Michael Siebers
 */
public class GenericFormFieldValidationConstraintDto {
    private String name;
    private Object config;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }
}
