package sample.book.inter.operator;

import sample.book.inter.expression.Expr;
import sample.book.inter.element.Id;
import sample.book.inter.expression.Op;
import sample.book.lexer.Tag;
import sample.book.lexer.Word;
import sample.book.symbols.Type;

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
