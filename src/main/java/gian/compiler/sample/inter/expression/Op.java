package gian.compiler.sample.inter.expression;

import gian.compiler.sample.inter.element.Temp;
import gian.compiler.sample.lexer.Token;
import gian.compiler.sample.symbols.Type;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Op extends Expr {

    public Op(Token tok, Type p){
        super(tok, p);
    }

    public Expr reduce(){
        Expr x = gen();
        Temp t = new Temp(type);
        emit(t.toString() + " = " + x.toString());
        return t;
    }

}