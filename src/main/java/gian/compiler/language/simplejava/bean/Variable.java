package gian.compiler.language.simplejava.bean;

import gian.compiler.language.simplejava.ast.AstNode;

/**
 * Created by gaojian on 2019/3/27.
 */
public class Variable extends Param {

    // TODO 存储地址，暂时用不到
    protected String address;
    public AstNode code;

    public Variable(String fieldName, VariableType variableType, AstNode code) {
        super(fieldName, variableType);
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AstNode getCode() {
        return code;
    }

    public void setCode(AstNode code) {
        this.code = code;
    }

}