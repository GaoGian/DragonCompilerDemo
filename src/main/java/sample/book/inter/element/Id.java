package sample.book.inter.element;

import sample.book.inter.expression.Expr;
import sample.book.lexer.Word;
import sample.book.symbols.Type;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Id extends Expr {

    public int offset;

    public Id(Word id, Type p, int b){
        super(id, p);
        offset = b;
    }

}
