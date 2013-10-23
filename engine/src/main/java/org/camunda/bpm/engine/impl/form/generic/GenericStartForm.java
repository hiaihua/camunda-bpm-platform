package org.camunda.bpm.engine.impl.form.generic;

import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;

/**
 *
 * @author Michael
 */
public class GenericStartForm extends GenericForm implements StartFormData {

    protected ProcessDefinition processDefinition;

    public GenericStartForm(ProcessDefinitionEntity processDefinition) {
        this.processDefinition = processDefinition;
    }

    @Override
    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }
}
