package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableArrayType;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.ast.expression.Access;
import gian.compiler.language.simplejava.ast.expression.Expr;

/**
 * Created by tingyun on 2018/7/20.
 */
public class SetElem extends Stmt {

    public Variable array;
    public Expr index;
    public Expr expr;

    public SetElem(Access x, Expr y){
        array = x.array;
        index = x.index;
        expr = y;
        if(check(x.getType(), expr.getType()) == null){
            error("type error");
        }
    }

    public VariableType check(VariableType p1, VariableType p2){
        if(p1 instanceof VariableArrayType || p2 instanceof VariableArrayType){
            return null;
        }else if(p1 == p2){
            return p2;
        }else if(VariableType.numeric(p1) && VariableType.numeric(p2)){
            return p2;
        }else{
            return null;
        }
    }

    @Override
    public void gen(int b, int a){
//        String s1 = index.reduce().toString();
//        String s2 = expr.reduce().toString();
//        emit(array.toString() + " [ " + s1 + " ] = " + s2);
    }


}
