package org.camunda.bpm.engine.impl.form.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParser;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.form.AbstractFormType;
import org.camunda.bpm.engine.impl.form.FormPropertyHandler;
import org.camunda.bpm.engine.impl.form.FormTypes;
import org.camunda.bpm.engine.impl.form.StartFormHandler;
import org.camunda.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.util.xml.Element;

/**
 *
 * @author Michael Siebers
 */
public class GenericFormHandler implements StartFormHandler {

    protected Expression formKey;
    protected String deploymentId;
    protected List<GenericFormGroupHandler> formGroupHandlers = new ArrayList<GenericFormGroupHandler>();
    protected List<FormPropertyHandler> formPropertyHandlers = new ArrayList<FormPropertyHandler>();

    @Override
    public void parseConfiguration(Element activityElement, DeploymentEntity deployment, ProcessDefinitionEntity processDefinition, BpmnParse bpmnParse) {
        this.deploymentId = deployment.getId();

        ExpressionManager expressionManager = Context
                .getProcessEngineConfiguration()
                .getExpressionManager();

        String formKeyAttribute = activityElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formKey");

        if (formKeyAttribute != null) {
            this.formKey = expressionManager.createExpression(formKeyAttribute);
        }

        Element extensionElement = activityElement.element("extensionElements");
        if (extensionElement != null) {
            List<Element> formPropertyElements = extensionElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formProperty");
            parseFormPropertyElements(formPropertyElements, bpmnParse, expressionManager);

            List<Element> formGroupElements = extensionElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formGroup");

            if (formGroupElements != null) {
                for (Element formGroupElement : formGroupElements) {
                    GenericFormGroupHandler formGroupHandler = new GenericFormGroupHandler();

                    formGroupHandler.setId(formGroupElement.attribute("id"));
                    formGroupHandler.setName(formGroupElement.attribute("name"));

                    List<Element> formFieldElements = formGroupElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formField");

                    if (formFieldElements != null) {
                        for (Element formFieldElement : formFieldElements) {
                            GenericFormFieldHandler formField = new GenericFormFieldHandler();

                            formField.setId(formFieldElement.attribute("id"));
                            formField.setName(formFieldElement.attribute("name"));
                            formField.setType(formFieldElement.attribute("type"));

                            String defaultValue = formFieldElement.attribute("defaultValue");

                            if (defaultValue != null) {
                                formField.setDefaultValue(expressionManager.createExpression(defaultValue));
                            }



                            List<Element> validationElements = formFieldElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "validation");
                            GenericFormFieldValidationHandler validationHandler = new GenericFormFieldValidationHandler();

                            for (Element validationElement : validationElements) {
                                List<Element> validationConstraintElements = validationElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "constraint");
                                for (Element validationConstraintElement : validationConstraintElements) {
                                    GenericFormFieldValidationConstraintHandler constraintHandler = new GenericFormFieldValidationConstraintHandler();

                                    constraintHandler.setName(validationConstraintElement.attribute("name"));

                                    String config = validationConstraintElement.attribute("config");

                                    if (config != null) {
                                        constraintHandler.setConfig(expressionManager.createExpression(config));
                                    }

                                    validationHandler.addConstraint(constraintHandler);
                                }
                            }

                            if (!validationHandler.getConstraints().isEmpty()) {
                                formField.setValidation(validationHandler);
                            }




                            List<Element> configurationElements = formFieldElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "configuration");
                            GenericFormFieldConfigurationHandler configurationHandler = new GenericFormFieldConfigurationHandler();

                            for (Element configurationElement : configurationElements) {
                                List<Element> configurationConfigElements = configurationElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "config");
                                for (Element configurationConfigElement : configurationConfigElements) {
                                    GenericFormFieldConfigurationConfigHandler configHandler = new GenericFormFieldConfigurationConfigHandler();

                                    configHandler.setName(configurationConfigElement.attribute("name"));
                                    configHandler.setConfig(configurationConfigElement.attribute("config"));

                                    configurationHandler.addConfig(configHandler);
                                }
                            }


                            if (!configurationHandler.getConfigs().isEmpty()) {
                                formField.setConfiguration(configurationHandler);
                            }

                            formGroupHandler.addFormField(formField);
                        }
                    }
                    formGroupHandlers.add(formGroupHandler);
                }
            }
        }
    }

    protected void initializeFormGroups(GenericForm genericFormData, ExecutionEntity execution) {
        for (GenericFormGroupHandler formGroupHandler : formGroupHandlers) {

            GenericFormGroup formGroup = formGroupHandler.initializeGenericFormGroup(execution);

            genericFormData.addFormGroup(formGroup);
        }

        if (formPropertyHandlers != null && !formPropertyHandlers.isEmpty()) {
            GenericFormGroup formGroup = new GenericFormGroup();
            for (FormPropertyHandler formPropertyHandler : formPropertyHandlers) {

                GenericFormField formField = formPropertyHandler.initializeAndMapGenericFormField(execution);

                formGroup.addFormFields(formField);
            }
            genericFormData.addFormGroup(formGroup);
        }
    }

    @Override
    public void submitFormProperties(Map<String, Object> properties, ExecutionEntity execution) {
        for (GenericFormGroupHandler formGroup : formGroupHandlers) {
            for (GenericFormFieldHandler formField : formGroup.getFormFieldHandlers()) {
                formField.validate(properties, execution);
            }
        }
    }

    @Override
    public StartFormData createStartFormData(ProcessDefinitionEntity processDefinition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void parseFormPropertyElements(List<Element> formPropertyElements, BpmnParse bpmnParse, ExpressionManager expressionManager) {
        FormTypes formTypes = Context
                .getProcessEngineConfiguration()
                .getFormTypes();

        for (Element formPropertyElement : formPropertyElements) {
            FormPropertyHandler formPropertyHandler = new FormPropertyHandler();

            String id = formPropertyElement.attribute("id");
            if (id == null) {
                bpmnParse.addError("attribute 'id' is required", formPropertyElement);
            }
            formPropertyHandler.setId(id);

            String name = formPropertyElement.attribute("name");
            formPropertyHandler.setName(name);

            AbstractFormType type = formTypes.parseFormPropertyType(formPropertyElement, bpmnParse);
            formPropertyHandler.setType(type);

            String requiredText = formPropertyElement.attribute("required", "false");
            Boolean required = bpmnParse.parseBooleanAttribute(requiredText);
            if (required != null) {
                formPropertyHandler.setRequired(required);
            } else {
                bpmnParse.addError("attribute 'required' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
            }

            String readableText = formPropertyElement.attribute("readable", "true");
            Boolean readable = bpmnParse.parseBooleanAttribute(readableText);
            if (readable != null) {
                formPropertyHandler.setReadable(readable);
            } else {
                bpmnParse.addError("attribute 'readable' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
            }

            String writableText = formPropertyElement.attribute("writable", "true");
            Boolean writable = bpmnParse.parseBooleanAttribute(writableText);
            if (writable != null) {
                formPropertyHandler.setWritable(writable);
            } else {
                bpmnParse.addError("attribute 'writable' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
            }

            String variableName = formPropertyElement.attribute("variable");
            formPropertyHandler.setVariableName(variableName);

            String expressionText = formPropertyElement.attribute("expression");
            if (expressionText != null) {
                Expression expression = expressionManager.createExpression(expressionText);
                formPropertyHandler.setVariableExpression(expression);
            }

            String defaultExpressionText = formPropertyElement.attribute("default");
            if (defaultExpressionText != null) {
                Expression defaultExpression = expressionManager.createExpression(defaultExpressionText);
                formPropertyHandler.setDefaultExpression(defaultExpression);
            }

            formPropertyHandlers.add(formPropertyHandler);
        }
    }
}
