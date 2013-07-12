package org.camunda.bpm.engine.impl.form.generic;

import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public class GenericTaskForm extends GenericForm implements TaskFormData {

    protected Task task;

    public GenericTaskForm(Task task) {
        this.task = task;
    }

    @Override
    public Task getTask() {
        return this.task;
    }
}
