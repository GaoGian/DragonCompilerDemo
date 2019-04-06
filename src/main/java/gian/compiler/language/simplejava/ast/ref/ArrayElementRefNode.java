package gian.compiler.language.simplejava.ast.ref;

import gian.compiler.language.simplejava.ast.expression.Expr;

import java.util.List;

/**
 * Created by Gian on 2019/4/5.
 */
public class ArrayElementRefNode extends RefNode {

    public String callName;
    public List<Expr> arrayIndex;

    public ArrayElementRefNode(String callName, List<Expr> arrayIndex) {
        this.callName = callName;
        this.arrayIndex = arrayIndex;
    }

    public List<Expr> getArrayIndex() {
        return arrayIndex;
    }

    public void setArrayIndex(List<Expr> arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    @Override
    public String toString(){
        StringBuffer str = new StringBuffer();
        for(Expr index : arrayIndex){
            str.append("[" + index + "]");
        }
        str.append(nextRef.toString());

        return str.toString();
    }

}
