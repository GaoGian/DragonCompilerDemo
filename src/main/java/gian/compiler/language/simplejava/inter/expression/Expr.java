package gian.compiler.language.simplejava.inter.expression;

import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.inter.AstNode;

/**
 * Created by gaojian on 2019/3/31.
 */
public class Expr extends AstNode {

    protected String op;
    protected VariableType type;

    public Expr(String op, VariableType type){
        this.op = op;
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

    @Override
    public String toString(){
        return op.toString();
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public VariableType getType() {
        return type;
    }

    public void setType(VariableType type) {
        this.type = type;
    }

}