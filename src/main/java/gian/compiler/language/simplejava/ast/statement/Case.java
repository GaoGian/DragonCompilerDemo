package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.ast.expression.Expr;

/**
 * Created by Gian on 2019/4/6.
 */
public class Case extends Stmt {

    public Expr expr;
    public Stmt stmt;

    public Case(Expr expr, Stmt stmt){
        this.expr = expr;
        this.stmt = stmt;
    }

    @Override
    public void gen(int b, int a){
        // FIXME 仿照 IF
        after = a;
        int label = newlabel();
//        expr.jumping();
        emitlabel(label);
        label = newlabel();
        stmt.gen(b, label);
    }

}
