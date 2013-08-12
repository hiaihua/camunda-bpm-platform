package org.camunda.bpm.engine.impl.form.generic;

import java.util.Map;
import org.camunda.bpm.engine.ProcessEngineException;

import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

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
            GenericFormValidationResult result = constraint.validate(value, execution, properties);
            if (result.success == false) {
                throw new ProcessEngineException("Exception on validation on field: " + name + ", with constraint name: " + result.name + ", with value: " + (result.value == null ? null : result.value.toString()) + ", Reason: " + result.reason);
            }
        }
    }

    public <T> GenericFormField initializeGenericFormField(final ProcessDefinitionEntity processDefinition, final VariableScope variableScope) {
        GenericFormField genericFormField = new GenericFormField();

        genericFormField.setId(id);
        genericFormField.setName(name);
        genericFormField.setType(type);

        if (defaultValue != null) {
            genericFormField.setDefaultValue(ApplicationSwitchUtil.getValue(defaultValue, processDefinition, variableScope));
        }

        genericFormField.setValidation(validation.initializeGenericFormFieldValidation(processDefinition, variableScope));
        genericFormField.setConfiguration(configuration.initializeGenericFormFieldConfiguration(processDefinition, variableScope));

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
