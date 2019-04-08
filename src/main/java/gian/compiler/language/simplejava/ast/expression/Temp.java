package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;

/**
 * 用于存储表达式返回的临时变量
 * Created by tingyun on 2018/7/20.
 */
public class Temp extends Variable {

    public Temp(VariableType type){
        super(JavaConstants.CODE_TEMP_STR + JavaDirectGlobalProperty.tempCout.getAndIncrement(), type, null);
    }

}
