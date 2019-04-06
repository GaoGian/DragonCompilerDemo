package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.bean.VariableType;

/**
 * Created by Gian on 2019/4/6.
 */
public class StringJoin extends Expr {

    public Expr expr1;
    public Expr expr2;

    public StringJoin(Expr expr1, Expr expr2){
        super(VariableType.STRING);
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public String toString(){
        return this.expr1 + " <join> " + this.expr2;
    }

}
