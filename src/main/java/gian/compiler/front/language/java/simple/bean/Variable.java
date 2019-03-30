package gian.compiler.front.language.java.simple.bean;

/**
 * Created by gaojian on 2019/3/27.
 */
public class Variable {

    protected VariableType variableType;
    protected String fieldName;
    protected String address;
    protected VariableInitInfo variableInitInfo;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public VariableInitInfo getVariableInitInfo() {
        return variableInitInfo;
    }

    public void setVariableInitInfo(VariableInitInfo variableInitInfo) {
        this.variableInitInfo = variableInitInfo;
    }
}