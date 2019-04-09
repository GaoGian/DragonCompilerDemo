package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.ast.expression.Expr;

/**
 * Created by tingyun on 2018/7/20.
 */
public class IfElse extends Stmt {

    public Expr expr;
    public Stmt stmt1, stmt2;

    public IfElse(Expr x, Stmt s1, Stmt s2){
        expr = x;
        stmt1 = s1;
        stmt2 = s2;
        if(expr.getType() != VariableType.BOOLEAN){
            expr.error("boolean required in if");
        }
    }

    @Override
    public void gen(String before, String after){
        String ifBefore = newlabel();
        Variable result = this.expr.gen();
        if(this.stmt2 == null){
            // 说明是单个if判断
            this.emit("<if> " + result.getName() + " <eq> <false> <goto> " + after);
            this.stmt1.gen(ifBefore, after);
        }else{
            // 说明后面接着另外的条件语句
            String elseBefore = newlabel();
            this.emit("<if> " + result.getName() + " <eq> <false> <goto> " + elseBefore);
            this.stmt1.gen(ifBefore, after);
            this.stmt2.gen(elseBefore, after);
        }
        emit("<goto> " + after);
    }

}
