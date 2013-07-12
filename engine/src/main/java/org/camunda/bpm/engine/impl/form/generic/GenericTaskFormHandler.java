package org.camunda.bpm.engine.impl.form.generic;

import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.TaskFormHandler;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public class GenericTaskFormHandler extends GenericFormHandler implements TaskFormHandler {

    public GenericTaskFormHandler() {
    }

    @Override
    public TaskFormData createTaskForm(TaskEntity task) {
        GenericTaskForm genericTaskForm = new GenericTaskForm(task);
        genericTaskForm.setDeploymentId(this.deploymentId);
        
        
        initializeFormGroups(genericTaskForm, task.getExecution());
        

        if (formKey != null) {
            Object parsedformKey = formKey.getValue(task.getExecution());
            if (parsedformKey != null) {
                genericTaskForm.setFormKey(parsedformKey.toString());
            }
        }
        return genericTaskForm;
    }
}
