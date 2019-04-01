package gian.compiler.language.simplejava.inter.expression;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.VariableType;

/**
 * 数组节点
 * Created by tingyun on 2018/7/20.
 */
public class Access extends Op {

    public VariableType array;
    public Expr index;

    public Access(VariableType a, Expr i, VariableType p){
        super(JavaConstants.ARRAY_TAG, p);
        array = a;
        index = i;
    }

    public Expr gen(){
        return new Access(array, index.reduce(), type);
    }

    public void jumping(int t, int f){
        emitjumps(reduce().toString(), t, f);
    }

    public String toString(){
        return array.toString() + " [ " + index.toString() + " ]";
    }

}
