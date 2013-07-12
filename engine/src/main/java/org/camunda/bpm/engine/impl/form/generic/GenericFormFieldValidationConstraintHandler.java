package org.camunda.bpm.engine.impl.form.generic;

import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;

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

    GenericFormFieldValidationConstraint initializeGenericFormFieldValidationConstraint(ExecutionEntity execution) {
        GenericFormFieldValidationConstraint constraint = new GenericFormFieldValidationConstraint();
        
        constraint.setName(name);
        
        if (config != null) {
            Object value = config.getValue(execution);

            if (value != null) {
                constraint.setConfig(value.toString());
            }
        }
        
        return constraint;
    }
}
