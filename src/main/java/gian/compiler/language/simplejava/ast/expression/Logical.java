package gian.compiler.language.simplejava.ast.expression;


import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;

/**
 * Created by tingyun on 2018/7/20.
 */
public abstract class Logical extends Expr {

    public String operator;
    public Expr expr1, expr2;
    public VariableType returnType;

    public Logical(String operator, Expr x1, Expr x2){
        this.operator = operator;
        this.expr1 = x1;
        this.expr2 = x2;

        this.returnType = check(expr1.gen().getDeclType(), expr2.gen().getDeclType());
        if(this.returnType == null){
//            error("type error");
        }
    }

    public VariableType check(VariableType p1, VariableType p2){
        // TODO 补全类型转换教研
        if(p1 == VariableType.BOOLEAN && p2 == VariableType.BOOLEAN){
            return VariableType.BOOLEAN;
        }else{
            return null;
        }
    }

}
