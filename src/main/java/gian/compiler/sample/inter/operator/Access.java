package gian.compiler.sample.inter.operator;

import gian.compiler.sample.inter.expression.Expr;
import gian.compiler.sample.inter.element.Id;
import gian.compiler.sample.inter.expression.Op;
import gian.compiler.sample.lexer.Tag;
import gian.compiler.sample.lexer.Word;
import gian.compiler.sample.symbols.Type;

/**
 * 数组节点
 * Created by tingyun on 2018/7/20.
 */
public class Access extends Op {

    public Id array;
    public Expr index;

    public Access(Id a, Expr i, Type p){
        super(new Word("[]", Tag.INDEX), p);
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
