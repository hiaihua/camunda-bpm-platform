package org.camunda.bpm.engine.rest.dto.runtime;

/**
 * @author: Michael Siebers
 */
public class GenericFormVariableValueDto {
  Object value;
  String type;

  public GenericFormVariableValueDto() {
  }
  
  public GenericFormVariableValueDto(Object value, String type) {
    this.value = value;
    this.type = type;
  }

  public Object getValue() {
    return value;
  }

  public String getType() {
    return type;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public void setType(String type) {
    this.type = type;
  }
}
