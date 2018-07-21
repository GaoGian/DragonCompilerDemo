package inter.statement;

import inter.Expr;
import inter.statement.Stmt;
import symbols.Type;

/**
 * Created by tingyun on 2018/7/20.
 */
public class If extends Stmt {

    Expr expr;
    Stmt stmt;

    public If(Expr x, Stmt s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool){
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
