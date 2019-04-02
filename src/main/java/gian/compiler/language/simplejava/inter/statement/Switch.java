package gian.compiler.language.simplejava.inter.statement;

import gian.compiler.language.simplejava.inter.expression.Expr;

/**
 * Created by gaojian on 2019/4/1.
 */
public class Switch extends Stmt {

    public Expr expr;
    public Stmt stmt;

    public Switch(Expr expr, Stmt stmt) {
        this.expr = expr;
        this.stmt = stmt;
    }

    @Override
    public void gen(int b, int a){
        // FIXME
        after = a;
        int label = newlabel();
        expr.gen();
        stmt.gen(b, label);
        emitlabel(label);
    }

}