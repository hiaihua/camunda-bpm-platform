package org.camunda.bpm.engine.impl.cmd;

import java.io.Serializable;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.db.DbSqlSession;
import org.camunda.bpm.engine.impl.form.TaskFormHandler;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class CompleteGenericFormTaskCmd implements Command<Object>, Serializable {

  private static final long serialVersionUID = 1L;
  
  protected String taskId;
  protected Map<String, Object> properties;
  
  public CompleteGenericFormTaskCmd(String taskId, Map<String, Object> properties) {
    this.taskId = taskId;
    this.properties = properties;
  }
  
  public Object execute(CommandContext commandContext) {
    if(taskId == null) {
      throw new ProcessEngineException("taskId is null");
    }
    
    TaskEntity task = Context
      .getCommandContext()
      .getTaskManager()
      .findTaskById(taskId);
    
    if (task == null) {
      throw new ProcessEngineException("Cannot find task with id " + taskId);
    }
    
    int historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
    ExecutionEntity execution = task.getExecution();
    if (historyLevel>=ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT && execution != null) {
      DbSqlSession dbSqlSession = commandContext.getSession(DbSqlSession.class);
      for (String propertyId: properties.keySet()) {
        Object propertyValue = properties.get(propertyId);
        HistoricFormPropertyEntity historicFormProperty = new HistoricFormPropertyEntity(execution, propertyId, propertyValue, taskId);
        dbSqlSession.insert(historicFormProperty);
      }
    }
    
    TaskFormHandler taskFormHandler = task.getTaskDefinition().getTaskFormHandler();
    taskFormHandler.submitFormProperties(properties, task.getExecution());

    if (properties!=null) {
        task.setExecutionVariables(properties);
    }

    completeTask(task);

    return null;
  }
  
  protected void completeTask(TaskEntity task) {
    task.complete();
  }
}
