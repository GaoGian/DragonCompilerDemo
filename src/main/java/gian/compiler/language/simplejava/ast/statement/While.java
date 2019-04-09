package gian.compiler.language.simplejava.ast.statement;


import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.ast.expression.Expr;

/**
 * Created by tingyun on 2018/7/20.
 */
public class While extends Stmt {

    public Expr expr;
    public Stmt stmt;

    public While(){
        expr = null;
        stmt = null;
    }

    public void init(Expr x, Stmt s){
        expr = x;
        stmt = s;
        // TODO 暂时取消类型校验，等到动态加载连接功能完成后再启用
//        if(expr.getType() != VariableType.BOOLEAN){
//            expr.error("boolean required in while");
//        }
    }

    @Override
    public void gen(String before, String after){
        String label = newlabel();
        emitlabel(label);
        this.current = label;
        this.after = after;
        Variable result = this.expr.gen();
        this.emit("<if> " + result.getName() + " <eq> <false> <goto> " + after);
        stmt.gen(label, after);
        emit("<goto> " + label);
    }

}
