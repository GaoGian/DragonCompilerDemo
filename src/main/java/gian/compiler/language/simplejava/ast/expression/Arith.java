package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Arith extends Expr {

    public String operator;
    public Expr expr1;
    public Expr expr2;
    public VariableType returnType;

    public Arith(String tok, Expr expr1, Expr expr2){
        this.operator = tok;
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.returnType = VariableType.max(this.expr1.execute().getDeclType(), this.expr2.execute().getDeclType());
        if(this.returnType == null){
            error("type error");
        }
    }

    @Override
    protected Variable gen(){
        // TODO
        return null;
    }

    @Override
    public String toString(){
        return expr1.toString() + " " + expr2.toString();
    }

}
