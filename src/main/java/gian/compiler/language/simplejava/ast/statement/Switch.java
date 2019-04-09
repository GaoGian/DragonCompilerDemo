package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.ast.expression.Expr;

/**
 * Created by gaojian on 2019/4/1.
 */
public class Switch extends Stmt {

    public Expr expr;
    public Stmt stmt;

    public Switch(){}

    public Switch(Expr expr, Stmt stmt){
        this.expr = expr;
        this.stmt = stmt;
    }

    public void init(Expr expr, Stmt stmt) {
        this.expr = expr;
        this.stmt = stmt;
    }

    @Override
    public void gen(String before, String after){
        String label = newlabel();
        expr.gen();
        stmt.gen(before, label);
        emitlabel(label);
    }

}