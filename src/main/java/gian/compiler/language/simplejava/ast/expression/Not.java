package gian.compiler.language.simplejava.ast.expression;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Not extends Logical {

    public Not(String tok, Expr x2){
        super(tok, x2, x2);
    }

    @Override
    public void jumping(int t, int f){
        expr2.jumping(f, t);
    }

    @Override
    public String toString(){
        return op.toString() + " " + expr2.toString();
    }

}
