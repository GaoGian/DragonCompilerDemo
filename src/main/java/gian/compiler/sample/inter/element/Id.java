package gian.compiler.sample.inter.element;

import gian.compiler.sample.inter.expression.Expr;
import gian.compiler.sample.lexer.Word;
import gian.compiler.sample.symbols.Type;

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
