package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.ast.AstNode;

/**
 * Created by gaojian on 2019/3/31.
 */
public class Expr extends AstNode {

    protected VariableType type;

    public Expr(VariableType type){
        this.type = type;
    }

    public Expr gen(){
        return this;
    }

    public VariableType getType() {
        return type;
    }

    public void setType(VariableType type) {
        this.type = type;
    }

}