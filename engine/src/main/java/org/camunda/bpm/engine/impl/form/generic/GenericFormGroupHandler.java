package org.camunda.bpm.engine.impl.form.generic;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public class GenericFormGroupHandler {
    protected String id;
    protected String name;
    protected List<GenericFormFieldHandler> formFieldHandlers = new ArrayList<GenericFormFieldHandler>();

    public GenericFormGroupHandler() {

    }

    void setId(String id) {
        this.id = id;
    }

    void setName(String name) {
        this.name = name;
    }

    public GenericFormGroup initializeGenericFormGroup(ProcessDefinitionEntity processDefinition, VariableScope variableScope) {
        GenericFormGroup genericFormGroup = new GenericFormGroup();
        genericFormGroup.setId(id);
        genericFormGroup.setName(name);

        for (GenericFormFieldHandler formFieldHandler : formFieldHandlers) {
            genericFormGroup.addFormFields(formFieldHandler.initializeGenericFormField(processDefinition, variableScope));
        }

        return genericFormGroup;
    }

    void addFormField(GenericFormFieldHandler formField) {
        this.formFieldHandlers.add(formField);
    }

    protected List<GenericFormFieldHandler> getFormFieldHandlers() {
        return formFieldHandlers;
    }
}
