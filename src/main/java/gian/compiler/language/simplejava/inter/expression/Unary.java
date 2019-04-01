package gian.compiler.language.simplejava.inter.expression;

import gian.compiler.language.simplejava.bean.VariableType;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Unary extends Op {

    public Expr expr;

    public Unary(String tok, Expr x){
        super(tok, null);
        expr = x;
        type = VariableType.max(VariableType.INT, expr.type);
        if(type == null){
            error("type error");
        }
    }

    public Expr gen(){
        return new Unary(op, expr.reduce());
    }

    public String toString(){
        return op.toString() + " " + expr.toString();
    }

}
