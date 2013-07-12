package org.camunda.bpm.engine.rest.dto.task;

import java.util.Map;
import org.camunda.bpm.engine.rest.dto.runtime.GenericFormVariableValueDto;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public class SaveGenericFormDto {

    private Map<String, GenericFormVariableValueDto> variables;

    public Map<String, GenericFormVariableValueDto> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, GenericFormVariableValueDto> variables) {
        this.variables = variables;
    }
}


