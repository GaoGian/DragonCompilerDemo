package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;

/**
 * 用于表示数值等常亮
 * Created by tingyun on 2018/7/20.
 */
public class Constant extends Variable {

    public static final Constant True = new Constant(JavaConstants.JAVA_KEYWORD_TRUE, VariableType.BOOLEAN);
    public static final Constant False = new Constant(JavaConstants.JAVA_KEYWORD_FALSE, VariableType.BOOLEAN);
    // 自增、自减操作
    public static final Constant DIGIT_ONE = new Constant("1", VariableType.INT);

    public Constant(String constant, VariableType type){
        super(constant , type, null);
    }

    @Override
    public String toString(){
        return this.getName();
    }

}
