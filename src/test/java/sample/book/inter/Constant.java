package sample.book.inter;

import sample.book.inter.expression.Expr;
import sample.book.lexer.Num;
import sample.book.lexer.Token;
import sample.book.lexer.Word;
import sample.book.symbols.Type;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Constant extends Expr {

    public static final Constant
            True = new Constant(Word.True, Type.Bool),
            False = new Constant(Word.False, Type.Bool);

    public Constant(Token tok, Type p){
        super(tok, p);
    }

    public Constant(int i){
        super(new Num(i), Type.Int);
    }

    public void jumping(int t, int f){
        if(this == True && t != 0){
            emit("goto L" + t);
        }else if(this == False && f != 0){
            emit("goto L" + f);
        }
    }

}
