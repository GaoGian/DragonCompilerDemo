package inter.operator;

import inter.Expr;
import lexer.Token;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Or extends Logical {

    public Or(Token tok, Expr x1, Expr x2){
        super(tok, x1, x2);
    }

    public void jumping(int t, int f){
        int label = t != 0 ? t : newlabel();
        expr1.jumping(label, 0);
        expr2.jumping(t, f);
        if(t == 0){
            emitlabel(label);
        }
    }

}
