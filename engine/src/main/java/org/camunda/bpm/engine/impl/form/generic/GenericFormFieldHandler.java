package org.camunda.bpm.engine.impl.form.generic;

import java.util.Map;

import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
class GenericFormFieldHandler {

    protected String id;
    protected String name;
    protected String type;
    protected Expression defaultValue;
    protected GenericFormFieldValidationHandler validation = new GenericFormFieldValidationHandler();
    protected GenericFormFieldConfigurationHandler configuration = new GenericFormFieldConfigurationHandler();

    public GenericFormFieldHandler() {
    }
    
    public void validate(Map<String, Object> properties, final ExecutionEntity execution) {
        Object value = properties.get(id);
        
        for(GenericFormFieldValidationConstraintHandler constraint : validation.getConstraints()) {
            constraint.validate(value, execution);
        }
    }

    public <T> GenericFormField initializeGenericFormField(final ExecutionEntity execution) {
        GenericFormField genericFormField = new GenericFormField();

        genericFormField.setId(id);
        genericFormField.setName(name);
        genericFormField.setType(type);

        if (defaultValue != null) {
            genericFormField.setDefaultValue(ApplicationSwitchUtil.getValue(defaultValue, execution));
        }

        genericFormField.setValidation(validation.initializeGenericFormFieldValidation(execution));
        genericFormField.setConfiguration(configuration.initializeGenericFormFieldConfiguration(execution));

        return genericFormField;
    }

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

    public Expression getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Expression defaultValue) {
        this.defaultValue = defaultValue;
    }

    public GenericFormFieldValidationHandler getValidation() {
        return validation;
    }

    public void setValidation(GenericFormFieldValidationHandler validation) {
        this.validation = validation;
    }

    public GenericFormFieldConfigurationHandler getConfiguration() {
        return configuration;
    }

    public void setConfiguration(GenericFormFieldConfigurationHandler configuration) {
        this.configuration = configuration;
    }
}
