package org.camunda.bpm.engine.impl.form.generic;

/**
 *
 * @author Michael
 */
public class GenericFormValidationResult {
    boolean success;
    String reason;
    String name;
    Object value;

    public GenericFormValidationResult(boolean success, String reason, String name, Object value) {
        this.success = success;
        this.reason = reason;
        this.name = name;
        this.value = value;
    }

    public GenericFormValidationResult(boolean success) {
        if (success == false) {
            throw new RuntimeException("No reason on validation and success == " + false);
        } else {
            this.success = success;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
