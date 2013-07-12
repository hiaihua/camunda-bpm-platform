package org.camunda.bpm.engine.impl.form.generic;

import java.util.concurrent.Callable;

import org.camunda.bpm.application.ProcessApplicationReference;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.impl.application.ProcessApplicationManager;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
class GenericFormFieldHandler {

    protected String id;
    protected String name;
    protected String type;
    protected Expression defaultValue;
    protected GenericFormFieldValidationHandler validation = new GenericFormFieldValidationHandler();
    protected GenericFormFieldConfigurationHandler configuration = new GenericFormFieldConfigurationHandler();

    public GenericFormFieldHandler() {
    }

    public <T> GenericFormField initializeGenericFormField(final ExecutionEntity execution) {
        GenericFormField genericFormField = new GenericFormField();

        genericFormField.setId(id);
        genericFormField.setName(name);
        genericFormField.setType(type);

        if (defaultValue != null) {
            
            String deploymentId = execution.getProcessDefinition().getDeploymentId();
            
            ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
            ProcessApplicationManager processApplicationManager = processEngineConfiguration.getProcessApplicationManager();
            
            ProcessApplicationReference pa = processApplicationManager.getProcessApplicationForDeployment(deploymentId);
            
            Object value = null;
            
            if(pa!=null && !pa.getName().equals(Context.getCurrentProcessApplication())) {
            	value = Context.executeWithinProcessApplication(new Callable<Object>() {
					public Object call() throws Exception {
						return defaultValue.getValue(execution);
					}
				}, pa);
            } else {
            	value = defaultValue.getValue(execution);
            }
            
            genericFormField.setDefaultValue(value);
        }

        genericFormField.setValidation(validation.initializeGenericFormFieldValidation(execution));
        genericFormField.setConfiguration(configuration.initializeGenericFormFieldConfiguration(execution));

        return genericFormField;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Expression defaultValue) {
        this.defaultValue = defaultValue;
    }

    public GenericFormFieldValidationHandler getValidation() {
        return validation;
    }

    public void setValidation(GenericFormFieldValidationHandler validation) {
        this.validation = validation;
    }

    public GenericFormFieldConfigurationHandler getConfiguration() {
        return configuration;
    }

    public void setConfiguration(GenericFormFieldConfigurationHandler configuration) {
        this.configuration = configuration;
    }
}
