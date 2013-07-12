package org.camunda.bpm.engine.impl.form.generic;

import org.camunda.bpm.engine.delegate.DelegateExecution;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public interface GenericFormFieldValidator {

    public void validate(Object value, DelegateExecution execution);
    
}
