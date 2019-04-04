package gian.compiler.language.simplejava.ast;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.ast.expression.Expr;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Constant extends Expr {

    public static final Constant True = new Constant(JavaConstants.JAVA_KEYWORD_TRUE, VariableType.BOOLEAN);
    public static final Constant False = new Constant(JavaConstants.JAVA_KEYWORD_FALSE, VariableType.BOOLEAN);
    // 自增、自减操作
    public static final Constant DIGIT_ONE = new Constant("1", VariableType.INT);

    public Constant(String tok, VariableType p){
        super(tok, p);
    }

    public Constant(int i){
        super(String.valueOf(i), VariableType.INT);
    }

    public void jumping(int t, int f){
        if(this == True && t != 0){
            emit("goto L" + t);
        }else if(this == False && f != 0){
            emit("goto L" + f);
        }
    }

}
