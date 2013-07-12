package org.camunda.bpm.engine.test.api.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.form.FormProperty;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;

public class FormPropertyDefaultValueTest extends PluggableProcessEngineTestCase {

    @Deployment
    public void testGenericFormHandler() {
        System.out.println("----------------- Start testGenericFormHandler -------------");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("FormPropertyDefaultValueTest.testGenericFormHandler");
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        /*TaskFormData formData = formService.getTaskFormData(task.getId());
        List<FormProperty> formProperties = formData.getFormProperties();
        for (FormProperty prop : formProperties) {
            System.out.println("-->" + prop.getId());
        }*/
        
        Object genericFormData = formService.getGenericForm(task.getId());
        
        System.out.println("----------------- End testGenericFormHandler -------------");
    }

    @Deployment
    public void testDefaultValue() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("FormPropertyDefaultValueTest.testDefaultValue");
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        TaskFormData formData = formService.getTaskFormData(task.getId());
        List<FormProperty> formProperties = formData.getFormProperties();
        assertEquals(4, formProperties.size());

        for (FormProperty prop : formProperties) {
            if ("booleanProperty".equals(prop.getId())) {
                assertEquals("true", prop.getValue());
            } else if ("stringProperty".equals(prop.getId())) {
                assertEquals("someString", prop.getValue());
            } else if ("longProperty".equals(prop.getId())) {
                assertEquals("42", prop.getValue());
            } else if ("longExpressionProperty".equals(prop.getId())) {
                assertEquals("23", prop.getValue());
            } else {
                assertTrue("Invalid form property: " + prop.getId(), false);
            }
        }

        Map<String, String> formDataUpdate = new HashMap<String, String>();
        formDataUpdate.put("longExpressionProperty", "1");
        formDataUpdate.put("booleanProperty", "false");
        formService.submitTaskFormData(task.getId(), formDataUpdate);

        assertEquals(false, runtimeService.getVariable(processInstance.getId(), "booleanProperty"));
        assertEquals("someString", runtimeService.getVariable(processInstance.getId(), "stringProperty"));
        assertEquals(42L, runtimeService.getVariable(processInstance.getId(), "longProperty"));
        assertEquals(1L, runtimeService.getVariable(processInstance.getId(), "longExpressionProperty"));
    }

    @Deployment
    public void testStartFormDefaultValue() throws Exception {
        String processDefinitionId = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("FormPropertyDefaultValueTest.testDefaultValue")
                .latestVersion()
                .singleResult()
                .getId();

        StartFormData startForm = formService.getStartFormData(processDefinitionId);


        List<FormProperty> formProperties = startForm.getFormProperties();
        assertEquals(4, formProperties.size());

        for (FormProperty prop : formProperties) {
            if ("booleanProperty".equals(prop.getId())) {
                assertEquals("true", prop.getValue());
            } else if ("stringProperty".equals(prop.getId())) {
                assertEquals("someString", prop.getValue());
            } else if ("longProperty".equals(prop.getId())) {
                assertEquals("42", prop.getValue());
            } else if ("longExpressionProperty".equals(prop.getId())) {
                assertEquals("23", prop.getValue());
            } else {
                assertTrue("Invalid form property: " + prop.getId(), false);
            }
        }

        // Override 2 properties. The others should pe posted as the default-value
        Map<String, String> formDataUpdate = new HashMap<String, String>();
        formDataUpdate.put("longExpressionProperty", "1");
        formDataUpdate.put("booleanProperty", "false");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinitionId, formDataUpdate);

        assertEquals(false, runtimeService.getVariable(processInstance.getId(), "booleanProperty"));
        assertEquals("someString", runtimeService.getVariable(processInstance.getId(), "stringProperty"));
        assertEquals(42L, runtimeService.getVariable(processInstance.getId(), "longProperty"));
        assertEquals(1L, runtimeService.getVariable(processInstance.getId(), "longExpressionProperty"));
    }
}
