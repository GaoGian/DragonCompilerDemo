package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.ast.expression.Temp;

/**
 * Created by gaojian on 2019/4/2.
 */
public class Return extends Expr {

    public Expr expr;

    public Return(Expr expr){
        super(expr.getOp(), expr.getType());
        // TODO 需要加入返回类型校验
        if(JavaDirectGlobalProperty.methodVariableType == null){
            error("undeclaret return type");
        }else if(!this.getType().equals(JavaDirectGlobalProperty.methodVariableType)){
            error("no match return type");
        }
        this.expr = expr;
    }

    @Override
    public Expr reduce(){
        Expr x = this.expr.gen();
        Temp t = new Temp(type);
        emit(t.toString() + " = " + x.toString());
        return t;
    }

}