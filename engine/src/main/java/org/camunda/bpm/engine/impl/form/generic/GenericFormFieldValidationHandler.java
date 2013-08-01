package org.camunda.bpm.engine.impl.form.generic;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
class GenericFormFieldValidationHandler {

    private List<GenericFormFieldValidationConstraintHandler> constraints = new ArrayList<GenericFormFieldValidationConstraintHandler>();

    public void addConstraint(GenericFormFieldValidationConstraintHandler constraint) {
        this.constraints.add(constraint);
    }

    public List<GenericFormFieldValidationConstraintHandler> getConstraints() {
        return this.constraints;
    }

    public GenericFormFieldValidation initializeGenericFormFieldValidation(final ProcessDefinitionEntity processDefinition, final VariableScope variableScope) {
        GenericFormFieldValidation validation = new GenericFormFieldValidation();

        for (GenericFormFieldValidationConstraintHandler constraint : constraints) {
            if (!constraint.getName().equals("validator")) {
                validation.addConstraint(constraint.initializeGenericFormFieldValidationConstraint(processDefinition, variableScope));
            }
        }

        return validation;
    }
}
