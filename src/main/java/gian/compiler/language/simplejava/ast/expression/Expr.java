package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;

/**
 * Created by gaojian on 2019/3/31.
 */
public class Expr extends Stmt {

    public Variable variable;
    public VariableType type;

    public Expr(){}

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