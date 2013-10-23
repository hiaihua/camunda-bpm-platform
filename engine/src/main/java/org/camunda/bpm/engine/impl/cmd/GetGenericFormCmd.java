package org.camunda.bpm.engine.impl.cmd;

import java.io.Serializable;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.form.TaskFormHandler;
import org.camunda.bpm.engine.impl.form.generic.GenericForm;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public class GetGenericFormCmd implements Command<GenericForm>, Serializable {

    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String formEngineName;

    public GetGenericFormCmd(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public GenericForm execute(CommandContext commandContext) {
        TaskEntity task = Context
                .getCommandContext()
                .getTaskManager()
                .findTaskById(taskId);
        
        if (task == null) {
            throw new ProcessEngineException("No task found for taskId '" + taskId + "'");
        }

        if (task.getTaskDefinition() != null) {            
            TaskFormHandler genericFormHandler = task.getTaskDefinition().getTaskFormHandler();
            
            if (genericFormHandler == null) {
                throw new ProcessEngineException("No genericFormHandler specified for task '" + taskId + "'");
            }
            
            return (GenericForm) genericFormHandler.createTaskForm(task);
        } else {
            // Standalone task, no TaskFormData available
            return null;
        }
    }
}