package gian.compiler.language.simplejava.inter.expression;

import gian.compiler.language.simplejava.bean.VariableArrayType;
import gian.compiler.language.simplejava.bean.VariableType;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Rel extends Logical {

    public Rel(String tok, Expr x1, Expr x2){
        super(tok, x1, x2);
    }

    @Override
    public VariableType check(VariableType p1, VariableType p2){
        if(p1 instanceof VariableArrayType || p2 instanceof VariableArrayType){
            return null;
        }else if(p1 == p2){
            return VariableType.BOOLEAN;
        }else{
            return null;
        }
    }

    @Override
    public void jumping(int t, int f){
        Expr a = expr1.reduce();
        Expr b = expr2.reduce();
        String test = a.toString() + " " + op.toString() + " " + b.toString();
        emitjumps(test, t, f);
    }

}
