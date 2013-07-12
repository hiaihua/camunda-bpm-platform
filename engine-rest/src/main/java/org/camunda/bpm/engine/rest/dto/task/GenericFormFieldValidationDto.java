package org.camunda.bpm.engine.rest.dto.task;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Siebers
 */
public class GenericFormFieldValidationDto {
    private List<GenericFormFieldValidationConstraintDto> constraints = new ArrayList<GenericFormFieldValidationConstraintDto>();
    
    public void addConstraint(GenericFormFieldValidationConstraintDto constraint) {
        this.constraints.add(constraint);
    }
    
    public List<GenericFormFieldValidationConstraintDto> getConstraints() {
        return this.constraints;
    }
}
