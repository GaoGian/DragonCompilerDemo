package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.ast.expression.Expr;

/**
 * Created by gaojian on 2019/4/1.
 */
public class For extends Stmt {

    public Stmt init;
    public Expr control;
    public Stmt update;
    public Stmt blockStmt;

    public For(Stmt init, Expr control, Stmt update, Stmt blockStmt) {
        this.init = init;
        this.control = control;
        this.update = update;
        this.blockStmt = blockStmt;
    }

    @Override
    public void gen(int b, int a){
        // FIXME 调整控制逻辑
        after = a;
        int label = newlabel();
        init.gen(b, label);
        emitlabel(label);
        label = newlabel();
//        control.jumping(b, 0);
        label = newlabel();
        blockStmt.gen(b, label);
        label = newlabel();
        update.gen(b ,label);
    }


}