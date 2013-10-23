package org.camunda.bpm.engine.impl.cmd;

import java.util.Map;

import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;


/**
 * @author Michael Siebers
 */
public class ResolveGenericFormTaskCmd extends CompleteGenericFormTaskCmd {

  private static final long serialVersionUID = 1L;

  public ResolveGenericFormTaskCmd(String taskId, Map<String, Object> variables) {
    super(taskId, variables);
  }
  
  @Override
  protected void completeTask(TaskEntity task) {
    task.resolve();
  }
}
