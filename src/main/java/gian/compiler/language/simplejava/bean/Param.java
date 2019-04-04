package gian.compiler.language.simplejava.bean;

import gian.compiler.language.simplejava.inter.expression.Expr;

/**
 * Created by gaojian on 2019/4/4.
 */
public class Param extends Expr {

    public String name;
    public VariableType type;

    public Param(String fieldName, VariableType variableType) {
        super(fieldName, variableType);
        this.type = variableType;
        this.name = fieldName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public VariableType getType() {
        return type;
    }

    @Override
    public void setType(VariableType type) {
        this.type = type;
    }
}