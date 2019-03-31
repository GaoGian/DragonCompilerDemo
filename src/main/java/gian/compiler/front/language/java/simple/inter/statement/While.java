package gian.compiler.front.language.java.simple.inter.statement;


import gian.compiler.front.language.java.simple.bean.VariableType;
import gian.compiler.front.language.java.simple.inter.expression.Expr;

/**
 * Created by tingyun on 2018/7/20.
 */
public class While extends Stmt {

    public Expr expr;
    public Stmt stmt;

    public While(Integer lexline){
        super(lexline);
        expr = null;
        stmt = null;
    }

    public void init(Expr x, Stmt s){
        expr = x;
        stmt = s;
        if(expr.getType() != VariableType.BOOLEAN){
            expr.error("boolean required in while");
        }
    }

    public void gen(int b, int a){
        after = a;
        expr.jumping(0, a);
        int label = newlabel();
        emitlabel(label);
        stmt.gen(label, b);
        emit("goto L" + b);
    }

}
