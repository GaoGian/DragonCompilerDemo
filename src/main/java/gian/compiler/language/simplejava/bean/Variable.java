package gian.compiler.language.simplejava.bean;

import gian.compiler.language.simplejava.inter.expression.Expr;

/**
 * Created by gaojian on 2019/3/27.
 */
public class Variable extends Expr {

    protected VariableType variableType;
    protected String fieldName;
    protected String address;
    protected VariableInitInfo variableInitInfo;

    public Variable(String fieldName, VariableType variableType) {
        super(fieldName, variableType);
        this.variableType = variableType;
        this.fieldName = fieldName;
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