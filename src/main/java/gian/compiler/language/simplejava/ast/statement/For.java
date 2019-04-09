package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.bean.Variable;

/**
 * Created by gaojian on 2019/4/1.
 */
public class For extends Stmt {

    // TODO 这里需要改成表达式
    public Stmt init;
    public Expr control;
    public Stmt update;
    // TODO 这里需要改成表达式
    public Stmt blockStmt;

    public For(){}

    public For(Stmt init, Expr control, Stmt update, Stmt blockStmt) {
        this.init = init;
        this.control = control;
        this.update = update;
        this.blockStmt = blockStmt;
    }

    public void init(Stmt init, Expr control, Stmt update, Stmt blockStmt){
        this.init = init;
        this.control = control;
        this.update = update;
        this.blockStmt = blockStmt;
    }

    @Override
    public void gen(String before, String after){
        String initLable = newlabel();
        emitlabel(initLable);
        this.init.gen(before, after);

        String controlLable = newlabel();
        emitlabel(controlLable);
        this.current = controlLable;
        this.after = after;
        Variable result = this.control.gen();
        emit("<if> " + result.getName() + " <eq> <false> <goto> " + after);

        String blockLabel = newlabel();
        emitlabel(blockLabel);
        this.blockStmt.gen(controlLable, after);

        String updateLable = newlabel();
        emitlabel(updateLable);
        this.update.gen(controlLable, after);

    }


}