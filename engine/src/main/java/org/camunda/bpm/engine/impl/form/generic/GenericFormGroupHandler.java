package org.camunda.bpm.engine.impl.form.generic;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;

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
    
    public GenericFormGroup initializeGenericFormGroup(ExecutionEntity execution) {
        GenericFormGroup genericFormGroup = new GenericFormGroup();
        genericFormGroup.setId(id);
        genericFormGroup.setName(name);
        
        for (GenericFormFieldHandler formFieldHandler : formFieldHandlers) {
            genericFormGroup.addFormFields(formFieldHandler.initializeGenericFormField(execution));
        }
        
        return genericFormGroup;
    }

    void addFormField(GenericFormFieldHandler formField) {
        this.formFieldHandlers.add(formField);
    }
}
