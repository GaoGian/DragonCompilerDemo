package gian.compiler.language.simplejava.ast.statement;


import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.ast.expression.Expr;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Set extends Stmt {

    public Variable id;
    public Expr expr;

    public Set(Variable variable, Expr expr){
        id = variable;
        this.expr = expr;
        // TODO 暂时取消类型校验，等到动态加载连接功能完成后再启用
//        if(check(id.getType(), expr.getType()) == null){
//            error("type error");
//        }
    }

    public VariableType check(VariableType p1, VariableType p2){
        if(VariableType.numeric(p1) && VariableType.numeric(p2)){
            return p2;
        }else if(p1 == VariableType.BOOLEAN && p2 == VariableType.BOOLEAN){
            return p2;
        }else{
            return null;
        }
    }

    @Override
    public void gen(String before, String after){
        String lable = newlabel();
        emitlabel(lable);
        Variable result = this.expr.gen();
        emit("<getField> " + this.id.getName() + " <assign> " + result.getName());
    }

}
