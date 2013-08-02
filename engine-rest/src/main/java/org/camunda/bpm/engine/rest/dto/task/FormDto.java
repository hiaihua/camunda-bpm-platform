package org.camunda.bpm.engine.rest.dto.task;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.impl.form.generic.GenericForm;
import org.camunda.bpm.engine.impl.form.generic.GenericFormField;
import org.camunda.bpm.engine.impl.form.generic.GenericFormFieldConfigurationConfig;
import org.camunda.bpm.engine.impl.form.generic.GenericFormFieldValidationConstraint;
import org.camunda.bpm.engine.impl.form.generic.GenericFormGroup;

/**
 *
 * @author Michael Siebers
 */
public class FormDto {

    protected String key;
    protected String deploymentId;
    protected List<GenericFormGroupDto> formGroups = new ArrayList<GenericFormGroupDto>();
    private String contextPath;

    public void setKey(String form) {
        this.key = form;
    }

    public String getKey() {
        return key;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getContextPath() {
        return contextPath;
    }

    public static FormDto fromFormData(GenericForm genericForm) {
        FormDto dto = new FormDto();

        if (genericForm != null) {
            dto.key = genericForm.getFormKey();


            dto.setDeploymentId(genericForm.getDeploymentId());

            for (GenericFormGroup genericFormGroup : genericForm.getFormGroups()) {
                GenericFormGroupDto genericFormGroupDto = new GenericFormGroupDto();

                genericFormGroupDto.setId(genericFormGroup.getId());
                genericFormGroupDto.setName(genericFormGroup.getName());


                for (GenericFormField genericFormField : genericFormGroup.getFormFields()) {
                    GenericFormFieldDto genericFormFieldDto = new GenericFormFieldDto();

                    genericFormFieldDto.setId(genericFormField.getId());
                    genericFormFieldDto.setName(genericFormField.getName());
                    genericFormFieldDto.setType(genericFormField.getType());
                    genericFormFieldDto.setDefaultValue(genericFormField.getDefaultValue());

                    GenericFormFieldConfigurationDto configurationDto = new GenericFormFieldConfigurationDto();

                    for (GenericFormFieldConfigurationConfig config : genericFormField.getConfiguration().getConfigs()) {
                        GenericFormFieldConfigurationConfigDto configDto = new GenericFormFieldConfigurationConfigDto();

                        configDto.setConfig(config.getConfig());
                        configDto.setName(config.getName());

                        configurationDto.addConfig(configDto);
                    }

                    genericFormFieldDto.setConfiguration(configurationDto);

                    GenericFormFieldValidationDto validationDto = new GenericFormFieldValidationDto();

                    for (GenericFormFieldValidationConstraint constraint : genericFormField.getValidation().getConstraints()) {
                        GenericFormFieldValidationConstraintDto constraintDto = new GenericFormFieldValidationConstraintDto();

                        constraintDto.setConfig(constraint.getConfig());
                        constraintDto.setName(constraint.getName());

                        validationDto.addConstraint(constraintDto);
                    }

                    genericFormFieldDto.setValidation(validationDto);
                    genericFormGroupDto.addFormFields(genericFormFieldDto);
                }

                dto.addFormGroup(genericFormGroupDto);
            }
        }
        return dto;
    }

    public String getDeploymentId() {
        return this.deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public List<GenericFormGroupDto> getFormGroups() {
        return formGroups;
    }

    public void addFormGroup(GenericFormGroupDto formField) {
        this.formGroups.add(formField);
    }
}
