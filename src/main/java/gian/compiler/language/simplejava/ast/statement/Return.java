package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.ast.expression.Temp;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.ast.expression.Expr;

/**
 * Created by gaojian on 2019/4/2.
 */
public class Return extends Stmt {

    public Expr expr;

    public Return(Expr expr){
        // TODO 需要加入返回类型校验
        if(JavaDirectGlobalProperty.methodVariableType == null){
            error("undeclaret return type");
        }else if(!expr.gen().getType().equals(JavaDirectGlobalProperty.methodVariableType)){
            error("no match return type");
        }
        this.expr = expr;
    }

    @Override
    public void gen(String before, String after){
        Variable result = expr.gen();
        String lable = newlabel();
        emitlabel(lable);
        emit("<return> " + result.getName());
    }

}