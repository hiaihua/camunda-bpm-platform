package org.camunda.bpm.engine.impl.form.generic;

import java.util.Map;
import org.camunda.bpm.engine.delegate.DelegateExecution;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public interface GenericFormFieldValidator {

    public GenericFormValidationResult validate(Object value, DelegateExecution execution, Map<String, Object> properties);

}
