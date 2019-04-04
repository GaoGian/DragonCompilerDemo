package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.bean.VariableType;


/**
 * Created by tingyun on 2018/7/20.
 */
public class Op extends Expr {

    public Op(String tok, VariableType p){
        super(p);
    }

    public Expr reduce(){
        Expr x = gen();
        Temp t = new Temp(type);
        emit(t.toString() + " = " + x.toString());
        return t;
    }

}
