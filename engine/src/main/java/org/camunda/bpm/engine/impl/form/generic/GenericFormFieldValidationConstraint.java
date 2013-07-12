package org.camunda.bpm.engine.impl.form.generic;

/**
 *
 * @author Michael Siebers
 */
public class GenericFormFieldValidationConstraint {
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
