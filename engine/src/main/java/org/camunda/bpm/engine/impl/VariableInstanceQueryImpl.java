/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.impl;

import java.io.Serializable;
import java.util.List;

import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.camunda.bpm.engine.impl.variable.SerializableType;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.runtime.VariableInstanceQuery;

/**
 * @author roman.smirnov
 */
public class VariableInstanceQueryImpl extends AbstractVariableQueryImpl<VariableInstanceQuery, VariableInstance> implements VariableInstanceQuery, Serializable {

  private static final long serialVersionUID = 1L;
  
  protected String variableName;
  protected String variableNameLike;
  protected String[] executionIds;
  protected String[] processInstanceIds;
  protected String[] taskIds;
  protected String[] activityInstanceIds;

  public VariableInstanceQueryImpl() { }
  
  public VariableInstanceQueryImpl(CommandExecutor commandExecutor) {
    super(commandExecutor);
  }

  public VariableInstanceQuery variableName(String variableName) {
    this.variableName = variableName;
    return this;
  }

  public VariableInstanceQuery variableNameLike(String variableNameLike) {
    this.variableNameLike = variableNameLike;
    return this;
  }

  public VariableInstanceQuery executionIdIn(String... executionIds) {
    this.executionIds = executionIds;
    return this;
  }

  public VariableInstanceQuery processInstanceIdIn(String... processInstanceIds) {
    this.processInstanceIds = processInstanceIds;
    return this;
  }

  public VariableInstanceQuery taskIdIn(String... taskIds) {
    this.taskIds = taskIds;
    return this;
  }

  public VariableInstanceQuery activityInstanceIdIn(String... activityInstanceIds) {
    this.activityInstanceIds = activityInstanceIds;
    return this;
  }

  // ordering ////////////////////////////////////////////////////
  
  public VariableInstanceQuery orderByVariableName() {
    orderBy(VariableInstanceQueryProperty.VARIABLE_NAME);
    return this;
  }

  public VariableInstanceQuery orderByVariableType() {
    orderBy(VariableInstanceQueryProperty.VARIABLE_TYPE);
    return this;
  }

  public VariableInstanceQuery orderByActivityInstanceId() {
    orderBy(VariableInstanceQueryProperty.ACTIVITY_INSTANCE_ID);
    return this;
  }

  // results ////////////////////////////////////////////////////  
  
  public long executeCount(CommandContext commandContext) {
    checkQueryOk();
    ensureVariablesInitialized();
    return commandContext
      .getVariableInstanceManager()
      .findVariableInstanceCountByQueryCriteria(this);
  }

  public List<VariableInstance> executeList(CommandContext commandContext, Page page) {
    checkQueryOk();
    ensureVariablesInitialized();
    List<VariableInstance> result = commandContext
      .getVariableInstanceManager()
      .findVariableInstanceByQueryCriteria(this, page);
    
    if (result == null) {
      return result;
    }
    
    // iterate over the result array to initialize the value of the value
    for (VariableInstance variableInstance : result) {
      if (variableInstance instanceof VariableInstanceEntity) {
        VariableInstanceEntity variableInstanceEntity = (VariableInstanceEntity) variableInstance;
        // skip variable instances from type serializable
        if (!variableInstanceEntity.getType().getTypeName().equals(SerializableType.TYPE_NAME)) {
          variableInstanceEntity.getValue();
        }
      }      
    }
    
    return result;
  }

  // getters ////////////////////////////////////////////////////
  
  public String getVariableName() {
    return variableName;
  }

  public String getVariableNameLike() {
    return variableNameLike;
  }

  public String[] getExecutionIds() {
    return executionIds;
  }

  public String[] getProcessInstanceIds() {
    return processInstanceIds;
  }

  public String[] getTaskIds() {
    return taskIds;
  }

  public String[] getActivityInstanceIds() {
    return activityInstanceIds;
  }
  
}
