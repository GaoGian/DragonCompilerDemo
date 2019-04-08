package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.bean.VariableType;

/**
 * Created by tingyun on 2018/7/20.
 */
public abstract class Logical extends Expr {

    public String operator;
    public Expr expr1, expr2;

    public Logical(String operator, Expr x1, Expr x2){
        super(x1.getType());
        this.operator = operator;
        this.expr1 = x1;
        this.expr2 = x2;
        this.type = check(expr1.getType(), expr2.getType());
        if(this.type == null){
            error("type error");
        }
    }

    public VariableType check(VariableType p1, VariableType p2){
        // TODO 补全类型转换校验
        if(p1 == VariableType.BOOLEAN && p2 == VariableType.BOOLEAN){
            return VariableType.BOOLEAN;
        }else{
            return null;
        }
    }

    @Override
    public String code(){
        return this.expr1 + " " + this.operator + " " + this.expr2;
    }

}
