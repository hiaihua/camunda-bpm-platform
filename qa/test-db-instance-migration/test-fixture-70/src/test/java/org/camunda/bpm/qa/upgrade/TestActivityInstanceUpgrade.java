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
package org.camunda.bpm.qa.upgrade;

import static org.camunda.bpm.qa.upgrade.util.ActivityInstanceAssert.assertThat;
import static org.camunda.bpm.qa.upgrade.util.ActivityInstanceAssert.describeActivityInstanceTree;

import java.util.List;

import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Daniel Meyer
 *
 */
public class TestActivityInstanceUpgrade extends AbstractDbUpgradeTestCase {
  
  @Test
  public void testSingleTaskProcess() {
    String processDefinitionKey = "TestFixture62.singleTaskProcess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
         describeActivityInstanceTree()
           .createActivity("waitHere")
         .done());
    
    // assert that the process instance can be completed:
    Task task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();
    
    taskService.complete(task.getId());
  }
  
  @Test
  public void testNestedSingleTaskProcess() {
    String processDefinitionKey = "TestFixture62.nestedSingleTaskProcess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
        .processDefinitionKey(processDefinitionKey)
        .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .createActivity("waitHere")
          .endActivity()
        .done());
    
    // assert that the process instance can be completed:
    Task task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();
    
    taskService.complete(task.getId());
  }
  
  @Test
  public void testConcurrentTaskProcess() {
    String processDefinitionKey = "TestFixture62.concurrentTaskProcess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .createActivity("task1")
          .createActivity("task2")
        .done());
    
    // complete first task
    Task firstTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("task1")
      .singleResult();  
    taskService.complete(firstTask.getId());
    
    // task removed from tree
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .createActivity("task2")
        .done());
        
    // complete second task
    taskService.complete(taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).singleResult().getId());    
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testNestedConcurrentTaskProcess() {
    String processDefinitionKey = "TestFixture62.nestedConcurrentTaskProcess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .createActivity("task1")
            .createActivity("task2")
          .endActivity()
        .done());
    
    // complete first task
    Task firstTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("task1")
      .singleResult();  
    taskService.complete(firstTask.getId());
    
    // task removed from tree
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .createActivity("task2")
          .endActivity()
        .done());
        
    // complete second task
    taskService.complete(taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).singleResult().getId());    
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testJoinOneExecutionProcess() {
    String processDefinitionKey = "TestFixture62.joinOneExecutionProcess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .createActivity("task1")
          .createActivity("join")
        .done());
    
    // complete first task
    Task firstTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();  
    taskService.complete(firstTask.getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testNestedJoinOneExecutionProcess() {
    String processDefinitionKey = "TestFixture62.nestedJoinOneExecutionProcess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .createActivity("task1")
            .createActivity("join")
          .endActivity()
        .done());
    
    // complete first task
    Task firstTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();  
    taskService.complete(firstTask.getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testJoinTwoExecutionsProcess() {
    String processDefinitionKey = "TestFixture62.joinTwoExecutionsProcess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .createActivity("task1")
          .createActivity("join")
          .createActivity("join")
        .done());
    
    // complete first task
    Task firstTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();  
    taskService.complete(firstTask.getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testNestedJoinTwoExecutionsProcess() {
    String processDefinitionKey = "TestFixture62.nestedJoinTwoExecutionsProcess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .createActivity("task1")
            .createActivity("join")
            .createActivity("join")
          .endActivity()
        .done());
    
    // complete first task
    Task firstTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();  
    taskService.complete(firstTask.getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
      
  @Test
  public void testSingleEmbeddedSubprocess() {
    String processDefinitionKey = "TestFixture62.singleEmbeddedSubprocess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope1")
            .createActivity("waitInside1")
          .endActivity()
        .done());
    
    // assert that the process instance can be completed:
    List<Task> tasks = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .list();    
    // complete first task
    taskService.complete(tasks.get(0).getId());    
    
    // process ended
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  
  @Test
  public void testNestedSingleEmbeddedSubprocess() {
    String processDefinitionKey = "TestFixture62.nestedSingleEmbeddedSubprocess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .beginActivity("scope1")
              .createActivity("waitInside1")
            .endActivity()
          .endActivity()
        .done());
    
    // assert that the process instance can be completed:
    List<Task> tasks = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .list();    
    // complete first task
    taskService.complete(tasks.get(0).getId());    
    
    // process ended
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testConcurrentEmbeddedSubprocess() {
    String processDefinitionKey = "TestFixture62.concurrentEmbeddedSubprocess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope1")
            .createActivity("waitInside1")
          .endActivity()
          .beginActivity("scope2")
            .createActivity("waitInside2")
          .endActivity()
        .done());
   
    // complete first task
    Task firstTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside1")
      .singleResult();  
    taskService.complete(firstTask.getId());
    
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope2")
            .createActivity("waitInside2")
          .endActivity()
        .done());
    
    // complete second task
    Task secondTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();  
    taskService.complete(secondTask.getId());
    
    // process ended
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testNestedConcurrentEmbeddedSubprocess() {
    String processDefinitionKey = "TestFixture62.nestedConcurrentEmbeddedSubprocess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .beginActivity("scope1")
              .createActivity("waitInside1")
            .endActivity()
            .beginActivity("scope2")
              .createActivity("waitInside2")
            .endActivity()
          .endActivity()
        .done());
   
    // complete first task
    Task firstTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside1")
      .singleResult();  
    taskService.complete(firstTask.getId());
    
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .beginActivity("scope2")
              .createActivity("waitInside2")
            .endActivity()
          .endActivity()
        .done());
    
    // complete second task
    Task secondTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();  
    taskService.complete(secondTask.getId());
    
    // process ended
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testMultiInstanceSequentialTask() {
    String processDefinitionKey = "TestFixture62.multiInstanceSequentialTask";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .createActivity("waitHere")
        .done());
   
    // complete first task
    Task task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();  
    taskService.complete(task.getId());
    
    // validate tree
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .createActivity("waitHere")
        .done());
    
    // complete second task
    task = taskService
        .createTaskQuery()
        .processDefinitionKey(processDefinitionKey)
        .singleResult();  
    taskService.complete(task.getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testNestedMultiInstanceSequentialTask() {
    String processDefinitionKey = "TestFixture62.nestedMultiInstanceSequentialTask";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .createActivity("waitHere")
          .endActivity()
        .done());
   
    // complete first task
    Task task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();  
    taskService.complete(task.getId());
    
    // validate tree
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .createActivity("waitHere")
          .endActivity()
        .done());
    
    // complete second task
    task = taskService
        .createTaskQuery()
        .processDefinitionKey(processDefinitionKey)
        .singleResult();  
    taskService.complete(task.getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testMultiInstanceSequentialSubprocess() {
    String processDefinitionKey = "TestFixture62.multiInstanceSequentialSubprocess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope1")
            .createActivity("waitInside1")
          .endActivity()
        .done());
   
    // complete first task
    Task task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();  
    taskService.complete(task.getId());
    
    // validate tree
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope1")
            .createActivity("waitInside1")
          .endActivity()
        .done());
    
    // complete second task
    task = taskService
        .createTaskQuery()
        .processDefinitionKey(processDefinitionKey)
        .singleResult();  
    taskService.complete(task.getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testNestedMultiInstanceSequentialSubprocess() {
    String processDefinitionKey = "TestFixture62.nestedMultiInstanceSequentialSubprocess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .beginActivity("scope1")
              .createActivity("waitInside1")
            .endActivity()
          .endActivity()
        .done());
   
    // complete first task
    Task task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();  
    taskService.complete(task.getId());
    
    // validate tree
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
      describeActivityInstanceTree()
        .beginActivity("outerProcess")
          .beginActivity("scope1")
            .createActivity("waitInside1")
          .endActivity()
        .endActivity()
      .done());
    
    // complete second task
    task = taskService
        .createTaskQuery()
        .processDefinitionKey(processDefinitionKey)
        .singleResult();  
    taskService.complete(task.getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testMultiInstanceSequentialSubprocessConcurrent() {
    String processDefinitionKey = "TestFixture62.multiInstanceSequentialSubprocessConcurrent";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope1")
            .createActivity("waitInside1")
            .createActivity("waitInside2")
          .endActivity()
        .done());
   
    // complete first task
    Task task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside1")
      .singleResult();  
    taskService.complete(task.getId());
    
    // validate tree
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope1")
            .createActivity("waitInside2")
          .endActivity()
        .done());
    
    // complete second task
    task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside2")
      .singleResult();  
    taskService.complete(task.getId());
    
    // second instance created:
    actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope1")
            .createActivity("waitInside1")
            .createActivity("waitInside2")
          .endActivity()
        .done());
    
    // complete first task
    task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside1")
      .singleResult();  
    taskService.complete(task.getId());
    
    // validate tree
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope1")
            .createActivity("waitInside2")
          .endActivity()
        .done());
    
    // complete second task
    task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside2")
      .singleResult();  
    taskService.complete(task.getId());
            
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  /** this is still failing */
  @Test
  @Ignore
  public void testNestedMultiInstanceSequentialSubprocessConcurrent() {
    String processDefinitionKey = "TestFixture62.nestedMultiInstanceSequentialSubprocessConcurrent";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    // validate tree
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .beginActivity("scope1")
              .createActivity("waitInside1")
              .createActivity("waitInside2")
            .endActivity()
          .endActivity()
        .done());
   
    // complete first task
    Task task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside1")
      .singleResult();  
    taskService.complete(task.getId());
    
    // validate tree
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .beginActivity("scope1")
              .createActivity("waitInside2")
            .endActivity()
          .endActivity()
        .done());
    
    // complete second task
    task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside2")
      .singleResult();  
    taskService.complete(task.getId());
    
    // second instance created
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .beginActivity("scope1")
              .createActivity("waitInside1")
              .createActivity("waitInside2")
            .endActivity()
          .endActivity()
        .done());
        
    // complete first task
    task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside1")
      .singleResult();  
    taskService.complete(task.getId());
    
    actualTree = runtimeService.getActivityInstance(processInstance.getId());    
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .beginActivity("scope1")
              .createActivity("waitInside2")
            .endActivity()
          .endActivity()
        .done());
        
    // complete second task
    task = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside2")
      .singleResult();  
    taskService.complete(task.getId());
            
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testMultiInstanceParallelTask() {
    String processDefinitionKey = "TestFixture62.multiInstanceParallelTask";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .createActivity("waitHere")
          .createActivity("waitHere")          
        .done());
        
    // assert that the process instance can be completed:
    List<Task> tasks = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .list();    
    // complete first task
    taskService.complete(tasks.get(0).getId());
    
    actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .createActivity("waitHere")
        .done());
        
    // assert that the process instance can be completed:
    tasks = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .list();
    
    // complete second task
    taskService.complete(tasks.get(0).getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testNestedMultiInstanceParallelTask() {
    String processDefinitionKey = "TestFixture62.nestedMultiInstanceParallelTask";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .createActivity("waitHere")
            .createActivity("waitHere")
          .endActivity()
        .done());
        
    // assert that the process instance can be completed:
    List<Task> tasks = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .list();    
    // complete first task
    taskService.complete(tasks.get(0).getId());
    
    actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .createActivity("waitHere")
          .endActivity()
        .done());
        
    // assert that the process instance can be completed:
    tasks = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .list();    
    // complete second task
    taskService.complete(tasks.get(0).getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testMultiInstanceParallelSubprocess() {
    String processDefinitionKey = "TestFixture62.multiInstanceParallelSubprocess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope1")
            .createActivity("waitInside1")
          .endActivity()
          .beginActivity("scope1")
            .createActivity("waitInside1")
          .endActivity()
        .done());
        
    List<Task> tasks = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .list();    
    // complete first task
    taskService.complete(tasks.get(0).getId());
    
    // validate tree
    actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()          
          .beginActivity("scope1")
            .createActivity("waitInside1")
          .endActivity()
        .done());
    
    tasks = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .list();    
    // complete next task
    taskService.complete(tasks.get(0).getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testNestedMultiInstanceParallelSubprocess() {
    String processDefinitionKey = "TestFixture62.nestedMultiInstanceParallelSubprocess";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .beginActivity("scope1")
              .createActivity("waitInside1")
            .endActivity()
            .beginActivity("scope1")
              .createActivity("waitInside1")
            .endActivity()
          .endActivity()
        .done());
        
    List<Task> tasks = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .list();    
    // complete first task
    taskService.complete(tasks.get(0).getId());
    
    // validate tree
    actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()      
          .beginActivity("outerProcess")
            .beginActivity("scope1")
              .createActivity("waitInside1")
            .endActivity()
          .endActivity()
        .done());
    
    tasks = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .list();    
    // complete next task
    taskService.complete(tasks.get(0).getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testMultiInstanceParallelSubprocessConcurrent() {
    
    String processDefinitionKey = "TestFixture62.multiInstanceParallelSubprocessConcurrent";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope1")
            .createActivity("waitInside1")
            .createActivity("waitInside2")
          .endActivity()
          .beginActivity("scope1")
            .createActivity("waitInside1")
            .createActivity("waitInside2")
          .endActivity()
        .done());
    
    List<Task> waitInside1 = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside1")
      .listPage(0, 2);
    taskService.complete(waitInside1.get(0).getId());
    taskService.complete(waitInside1.get(1).getId());
    
    List<Task> waitInside2 = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside2")
      .listPage(0, 1);
    
    // this completes the first subprocess instance
    taskService.complete(waitInside2.get(0).getId());
    
    actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("scope1")
            .createActivity("waitInside2")
          .endActivity()          
        .done());
    
    // assert that the process instance can be completed:
    Task lastTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    // complete first task
    taskService.complete(lastTask.getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }
  
  @Test
  public void testNestedMultiInstanceParallelSubprocessConcurrent() {
    
    String processDefinitionKey = "TestFixture62.nestedMultiInstanceParallelSubprocessConcurrent";
    
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    Assert.assertNotNull(processInstance);
    
    ActivityInstance actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .beginActivity("scope1")
              .createActivity("waitInside1")
              .createActivity("waitInside2")
            .endActivity()
            .beginActivity("scope1")
              .createActivity("waitInside1")
              .createActivity("waitInside2")
            .endActivity()
          .endActivity()
        .done());
    
    List<Task> waitInside1 = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside1")
      .listPage(0, 2);
    taskService.complete(waitInside1.get(0).getId());
    taskService.complete(waitInside1.get(1).getId());
    
    List<Task> waitInside2 = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .taskDefinitionKey("waitInside2")
      .listPage(0, 1);
    
    // this completes the first subprocess instance
    taskService.complete(waitInside2.get(0).getId());
    
    actualTree = runtimeService.getActivityInstance(processInstance.getId());
    assertThat(actualTree).hasStructure(
        describeActivityInstanceTree()
          .beginActivity("outerProcess")
            .beginActivity("scope1")
              .createActivity("waitInside2")
            .endActivity()          
          .endActivity()
        .done());
    
    // assert that the process instance can be completed:
    Task lastTask = taskService
      .createTaskQuery()
      .processDefinitionKey(processDefinitionKey)
      .singleResult();    
    // complete first task
    taskService.complete(lastTask.getId());
        
    Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count());
  }

}
