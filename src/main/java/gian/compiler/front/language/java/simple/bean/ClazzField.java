package gian.compiler.front.language.java.simple.bean;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ClazzField extends Variable {

    private String permission;
    private VariableType variableType;
    private String fieldName;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public VariableType getVariableType() {
        return variableType;
    }

    public void setVariableType(VariableType variableType) {
        this.variableType = variableType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}