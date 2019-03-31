package gian.compiler.front.language.java.simple.inter.expression;

import gian.compiler.front.language.java.simple.bean.VariableType;
import gian.compiler.front.language.java.simple.inter.AstNode;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gaojian on 2019/3/31.
 */
public class Expr extends AstNode {

    protected VariableType type;

    public Expr(Integer lexline, VariableType type){
        super(lexline);
        this.type = type;
    }

    public Expr gen(){
        return this;
    }

    public Expr reduce(){
        return this;
    }

    public void jumping(int t, int f){
        emitjumps(toString(), t, f);
    }

    public void emitjumps(String test, int t, int f){
        if(t != 0 && f != 0){
            emit("if " + test + " goto L" + t);
            emit("goto L" + f);
        }else if(t != 0){
            emit("if " + test + " goto L" + t);
        }else if(f != 0){
            emit("ifFalse " + test + " goto L" + f);
        }else{

        }
    }

//    public String toString(){
//        return op.toString();
//    }

    public VariableType getType() {
        return type;
    }

    public void setType(VariableType type) {
        this.type = type;
    }
}