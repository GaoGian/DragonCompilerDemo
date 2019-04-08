package gian.compiler.language.simplejava.bean;

import gian.compiler.language.simplejava.ast.statement.Stmt;

/**
 * Created by gaojian on 2019/3/27.
 */
public class Variable extends Param {

    // TODO 实际引用的变量（可能是表达式临时变量，可以包括地址等信息）
    public Variable refVariable;
    public Stmt code;

    public Variable(String fieldName, VariableType variableType, Stmt code) {
        super(fieldName, variableType);
        this.code = code;
    }

    @Override
    public Variable gen(){
        return this;
    }

    public Variable getRefVariable() {
        return refVariable;
    }

    public void setRefVariable(Variable refVariable) {
        this.refVariable = refVariable;
    }

    public Stmt getCode() {
        return code;
    }

    public void setCode(Stmt code) {
        this.code = code;
    }

    @Override
    public String toString(){
        return "variable: type_" + declType.getName() + "-name_" + this.name;
    }

}