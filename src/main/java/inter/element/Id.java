package inter.element;

import inter.expression.Expr;
import lexer.Word;
import symbols.Type;

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
