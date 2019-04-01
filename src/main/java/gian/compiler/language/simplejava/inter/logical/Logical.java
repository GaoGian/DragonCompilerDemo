package gian.compiler.language.simplejava.inter.logical;


import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.inter.expression.Expr;
import gian.compiler.language.simplejava.inter.expression.Temp;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Logical extends Expr {

    public String op;
    public Expr expr1, expr2;

    public Logical(Integer lexline, VariableType type, String op, Expr x1, Expr x2){
        super(lexline, type);
        this.op = op;
        expr1 = x1;
        expr2 = x2;
        type = check(expr1.getType(), expr2.getType());
        if(type == null){
            error("type error");
        }
    }

    public VariableType check(VariableType p1, VariableType p2){
        if(p1 == VariableType.BOOLEAN && p2 == VariableType.BOOLEAN){
            return VariableType.BOOLEAN;
        }else{
            return null;
        }
    }

    public Expr gen(){
        int f = newlabel();
        int a = newlabel();
        Temp temp = new Temp(this.lexline, type);
        this.jumping(0, f);
        emit(temp.toString() + " = true");
        emit("goto L" + a);
        emitlabel(f);
        emit(temp.toString() + " = false");
        emitlabel(a);
        return temp;
    }

    @Override
    public String toString(){
        return expr1.toString() + " " + op.toString() + " " + expr2.toString();
    }

}
