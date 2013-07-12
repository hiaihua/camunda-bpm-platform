package org.camunda.bpm.engine.rest.dto.task;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public class GenericFormGroupDto {

    protected String id;
    protected String name;
    protected List<GenericFormFieldDto> formFields = new ArrayList<GenericFormFieldDto>();

    public GenericFormGroupDto() {
    }

    void setId(String id) {
        this.id = id;
    }

    void setName(String name) {
        this.name = name;
    }

    public List<GenericFormFieldDto> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<GenericFormFieldDto> formFields) {
        this.formFields = formFields;
    }
    
    public void addFormFields(GenericFormFieldDto formFields) {
        this.formFields.add(formFields);
    }
}
