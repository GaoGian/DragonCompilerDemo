package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Arith extends Expr {

    public String operator;
    public Expr expr1;
    public Expr expr2;
    public VariableType returnType;

    public Arith(String tok, Expr expr1, Expr expr2){
        // TODO 暂时取消类型校验，等到动态加载连接功能完成后再启用
//        super(VariableType.max(expr1.getType(), expr2.getType()));
        super(null);
        this.operator = tok;
        this.expr1 = expr1;
        this.expr2 = expr2;
//        this.returnType = VariableType.max(this.expr1.getType(), this.expr2.getType());
//        if(this.returnType == null){
//            error("type error");
//        }
    }

    @Override
    public String code(){
        return expr1.toString() + " " + expr2.toString();
    }

}
