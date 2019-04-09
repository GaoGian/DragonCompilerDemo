package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.ast.expression.Expr;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Do extends Stmt {

    public Expr expr;
    public Stmt stmt;

    public Do(){
        expr = null;
        stmt = null;
    }

    public void init(Stmt s, Expr x){
        expr = x;
        stmt = s;
        // TODO 暂时取消类型校验，等到动态加载连接功能完成后再启用
//        if(expr.getType() != VariableType.BOOLEAN){
//            expr.error("boolean required in do");
//        }
    }

    @Override
    public void gen(String before, String after){
        String doStart = newlabel();
        this.current = doStart;
        this.after = after;
        emitlabel(doStart);
        stmt.gen(doStart, after);
        Variable result = this.expr.gen();
        emit("<if> " + result.getName() + " <eq> <true> <goto> " + doStart);
    }

}
