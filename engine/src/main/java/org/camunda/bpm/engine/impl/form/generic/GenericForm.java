package org.camunda.bpm.engine.impl.form.generic;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.FormProperty;

/**
 *
 * @author Michael Siebers
 */
public abstract class GenericForm implements FormData {
    
  
    protected String deploymentId;
    protected ArrayList<GenericFormGroup> formGroups;
    protected String formKey;
    
    public GenericForm() {
        formGroups = new ArrayList<GenericFormGroup>();
    }

    public ArrayList<GenericFormGroup> getFormGroups() {
        return formGroups;
    }

    public void addFormGroup(GenericFormGroup formField) {
        this.formGroups.add(formField);
    }

    

    @Override
    public String getFormKey() {
        return this.formKey;
    }
    
    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    @Override
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    @Override
    public List<FormProperty> getFormProperties() {
        // @todo
        return new ArrayList<FormProperty>();
    }
}
