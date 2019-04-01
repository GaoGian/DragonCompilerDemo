package gian.compiler.language.simplejava.inter.statement;


import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.inter.expression.Expr;

/**
 * Created by tingyun on 2018/7/20.
 */
public class If extends Stmt {

    Expr expr;
    Stmt stmt;

    public If(Expr x, Stmt s){
        expr = x;
        stmt = s;
        if(expr.getType() != VariableType.BOOLEAN){
            expr.error("boolean required in if");
        }
    }

    public void gen(int b, int a){
        int label = newlabel();
        expr.jumping(0, a);
        emitlabel(label);
        stmt.gen(label, a);
    }

}
