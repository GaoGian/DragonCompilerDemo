package gian.compiler.language.simplejava.bean;

import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.exception.JavaDirectException;

/**
 * 声明的参数
 * Created by gaojian on 2019/4/4.
 */
public class Param extends Expr {

    public String name;
    // 参数声明类型
    public VariableType declType;

    public Param(String fieldName, VariableType declType) {
        super(declType);
        this.declType = declType;
        this.name = fieldName;
    }

    @Override
    public Variable gen(){
        throw new JavaDirectException("该对象是参数，不用于生成中间码");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VariableType getDeclType() {
        return declType;
    }

    public void setDeclType(VariableType declType) {
        this.declType = declType;
    }

    @Override
    public String code(){
        return "param: type_" + declType.getName() + "-name_" + this.name;
    }

}