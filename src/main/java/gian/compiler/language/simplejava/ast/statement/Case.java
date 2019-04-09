package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.bean.Variable;

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
    public void gen(String before, String after){
        String lable = newlabel();
        this.emitlabel(lable);
        Variable result = this.expr.gen();
        this.emit("<if> " + result.getName() + " <eq> <false> <goto> " + after);

        String lable2 = newlabel();
        this.emitlabel(lable2);
        this.stmt.gen(before, after);

    }

}
