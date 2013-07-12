package org.camunda.bpm.engine.rest.dto.task;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public class GenericFormFieldDto {
    private String id;
    private String name;
    private String type;
    private Object defaultValue;
    
    private GenericFormFieldConfigurationDto configuration;
    private GenericFormFieldValidationDto validation;
   
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

    public GenericFormFieldConfigurationDto getConfiguration() {
        return configuration;
    }

    public void setConfiguration(GenericFormFieldConfigurationDto configuration) {
        this.configuration = configuration;
    }

    public GenericFormFieldValidationDto getValidation() {
        return validation;
    }

    public void setValidation(GenericFormFieldValidationDto validation) {
        this.validation = validation;
    }
}
