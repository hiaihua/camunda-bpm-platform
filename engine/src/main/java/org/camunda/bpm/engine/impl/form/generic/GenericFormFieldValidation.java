package org.camunda.bpm.engine.impl.form.generic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Siebers
 */
public class GenericFormFieldValidation {
    private List<GenericFormFieldValidationConstraint> constraints = new ArrayList<GenericFormFieldValidationConstraint>();
    
    public void addConstraint(GenericFormFieldValidationConstraint constraint) {
        this.constraints.add(constraint);
    }
    
    public List<GenericFormFieldValidationConstraint> getConstraints() {
        return this.constraints;
    }
}
