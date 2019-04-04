package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Temp extends Variable {

    public int number = 0;

    public Temp(VariableType p){
        super(JavaConstants.CODE_TEMP_STR, p, null);
        this.number = JavaDirectGlobalProperty.tempCout.getAndIncrement();
    }

    @Override
    public String toString(){
        return JavaConstants.CODE_TEMP_STR + number;
    }

}
