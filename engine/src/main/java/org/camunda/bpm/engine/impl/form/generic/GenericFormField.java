package org.camunda.bpm.engine.impl.form.generic;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public class GenericFormField {
    private String id;
    private String name;
    private String type;
    private Object defaultValue;
    
    private GenericFormFieldValidation validation = new GenericFormFieldValidation();
    private GenericFormFieldConfiguration configuration = new GenericFormFieldConfiguration();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public boolean enterType(String type) {
        type = type.toLowerCase();
        
        // @todo check here
        
        return false;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public GenericFormFieldValidation getValidation() {
        return validation;
    }

    public void setValidation(GenericFormFieldValidation validation) {
        this.validation = validation;
    }

    public GenericFormFieldConfiguration getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(GenericFormFieldConfiguration configuration) {
        this.configuration = configuration;
    }
}
