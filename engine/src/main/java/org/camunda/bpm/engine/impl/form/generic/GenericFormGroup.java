package org.camunda.bpm.engine.impl.form.generic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public class GenericFormGroup {

    protected String id;
    protected String name;
    protected List<GenericFormField> formFields = new ArrayList<GenericFormField>();

    public GenericFormGroup() {
    }

    void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }

    void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    public List<GenericFormField> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<GenericFormField> formFields) {
        this.formFields = formFields;
    }
    
    public void addFormFields(GenericFormField formFields) {
        this.formFields.add(formFields);
    }
}
