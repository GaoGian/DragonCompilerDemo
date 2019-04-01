package gian.compiler.language.simplejava.inter.expression;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Not extends Logical {

    public Not(String tok, Expr x2){
        super(tok, x2, x2);
    }

    public void jumping(int t, int f){
        expr2.jumping(f, t);
    }

    public String toString(){
        return op.toString() + " " + expr2.toString();
    }

}
