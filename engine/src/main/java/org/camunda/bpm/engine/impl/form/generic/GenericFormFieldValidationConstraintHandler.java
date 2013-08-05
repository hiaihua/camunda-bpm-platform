package org.camunda.bpm.engine.impl.form.generic;

import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
class GenericFormFieldValidationConstraintHandler {

    private String name;
    private Expression config;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Expression getConfig() {
        return config;
    }

    public void setConfig(Expression config) {
        this.config = config;
    }

    GenericFormFieldValidationConstraint initializeGenericFormFieldValidationConstraint(final ProcessDefinitionEntity processDefinition, final VariableScope variableScope) {
        GenericFormFieldValidationConstraint constraint = new GenericFormFieldValidationConstraint();

        constraint.setName(name);

        if (config != null) {
            constraint.setConfig(ApplicationSwitchUtil.getValue(config, processDefinition, variableScope));
        }

        return constraint;
    }

    public GenericFormValidationResult validate(Object value, ExecutionEntity execution) {
        if (name.equals("validator")) {
            GenericFormFieldValidator validator = (GenericFormFieldValidator) ApplicationSwitchUtil.getValue(config, (ProcessDefinitionEntity) execution.getProcessDefinition(), execution);

            if (validator != null && value != null && execution != null) {
                return validator.validate(value, execution);
            }

        } else if (name.equals("max-length")) {
            if (value != null) {
                if (value.toString().length() >= Integer.parseInt(config.toString())) {
                    return new GenericFormValidationResult(false, "is to long (max-length = " + config.toString() + ")", name, value);
                } else {
                    return new GenericFormValidationResult(true);
                }
            }
        } else if (name.equals("min-length")) {
            if (value == null || value.toString().length() < Integer.parseInt(config.toString())) {
                return new GenericFormValidationResult(false, "is to short (min-length = " + config.toString() + ")", name, value);
            } else {
                return new GenericFormValidationResult(true);
            }
        } else if (name.equals("required")) {
            if (value == null || value.toString().length() == 0) {
                return new GenericFormValidationResult(false, "is required", name, value);
            } else {
                return new GenericFormValidationResult(true);
            }
        } else if (name.equals("readable")) {
        } else if (name.equals("writable")) {
        }

        return new GenericFormValidationResult(true);
    }
}
