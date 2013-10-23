package org.camunda.bpm.engine.impl.form.generic;

import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.impl.el.StartProcessVariableScope;
import org.camunda.bpm.engine.impl.form.StartFormHandler;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

/**
 *
 * @author Michael
 */
public class GenericStartFormHandler extends GenericFormHandler implements StartFormHandler {

    @Override
    public StartFormData createStartFormData(ProcessDefinitionEntity processDefinition) {
        GenericStartForm genericStartForm = new GenericStartForm(processDefinition);
        genericStartForm.setDeploymentId(this.deploymentId);

        initializeFormGroups(genericStartForm, processDefinition, StartProcessVariableScope.getSharedInstance());

        if (formKey != null) {
            Object parsedformKey = ApplicationSwitchUtil.getValue(formKey, processDefinition, StartProcessVariableScope.getSharedInstance());
            if (parsedformKey != null) {
                genericStartForm.setFormKey(parsedformKey.toString());
            }
        }
        return genericStartForm;
    }
}
