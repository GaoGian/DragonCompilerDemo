package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.bean.VariableType;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Arith extends Op {

    public Expr expr1, expr2;

    public Arith(String tok, Expr x1, Expr x2){
        super(tok, null);
        expr1 = x1;
        expr2 = x2;
        type = VariableType.max(expr1.type, expr2.type);
        if(type == null){
            error("type error");
        }
    }

    @Override
    public Expr gen(){
        return new Arith("", expr1, expr2);
    }

    @Override
    public String toString(){
        return expr1.toString() + " " + expr2.toString();
    }

}
