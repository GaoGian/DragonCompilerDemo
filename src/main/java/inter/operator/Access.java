package inter.operator;

import inter.expression.Expr;
import inter.element.Id;
import inter.expression.Op;
import lexer.Tag;
import lexer.Word;
import symbols.Type;

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
