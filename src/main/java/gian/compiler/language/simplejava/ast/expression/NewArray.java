package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.ast.AstNode;
import gian.compiler.language.simplejava.bean.VariableArrayType;
import gian.compiler.language.simplejava.bean.VariableType;

/**
 * Created by gaojian on 2019/4/4.
 */
public class NewArray extends AstNode {

    public VariableType baseType;
    public VariableArrayType variableArrayType;

    public NewArray(VariableType baseType, VariableArrayType variableArrayType){
        this.baseType = baseType;
        this.variableArrayType = variableArrayType;
    }

    @Override
    public String toString(){
        return "new " + this.baseType.getName() + this.variableArrayType.toString();
    }

}