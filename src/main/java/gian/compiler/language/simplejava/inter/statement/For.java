package gian.compiler.language.simplejava.inter.statement;

import gian.compiler.language.simplejava.inter.expression.Expr;

/**
 * Created by gaojian on 2019/4/1.
 */
public class For extends Stmt {

    public Stmt init;
    public Expr control;
    public Stmt update;

    public For(Stmt init, Expr control, Stmt update) {
        this.init = init;
        this.control = control;
        this.update = update;
    }

    @Override
    public void gen(int b, int a){
        // FIXME 调整控制逻辑
        after = a;
        int label = newlabel();
        init.gen(b, label);
        emitlabel(label);
        label = newlabel();
        update.gen(b ,label);
        control.jumping(b, 0);
    }


}