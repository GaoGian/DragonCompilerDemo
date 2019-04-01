package gian.compiler.language.simplejava.inter.statement;


import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.inter.expression.Expr;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Set extends Stmt {

    public Variable id;
    public Expr expr;

    public Set(Variable i, Expr x){
        id = i;
        expr = x;
        if(check(id.getVariableType(), expr.getType()) == null){
            error("type error");
        }
    }

    public VariableType check(VariableType p1, VariableType p2){
        if(VariableType.numeric(p1) && VariableType.numeric(p2)){
            return p2;
        }else if(p1 == VariableType.BOOLEAN && p2 == VariableType.BOOLEAN){
            return p2;
        }else{
            return null;
        }
    }

    public void gen(int b, int a){
        emit(id.toString() + " = " + expr.gen().toString());
    }

}
