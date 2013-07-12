package org.camunda.bpm.engine.impl.form.generic;

/**
 *
 * @author Michael Siebers
 */
public class GenericFormFieldValidationConstraint {
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
