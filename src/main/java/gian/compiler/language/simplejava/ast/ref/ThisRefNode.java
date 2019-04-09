package gian.compiler.language.simplejava.ast.ref;

import gian.compiler.language.simplejava.ast.expression.Temp;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

/**
 * Created by Gian on 2019/4/6.
 */
public class ThisRefNode extends RefNode {

    public ThisRefNode(VariableType type){
        super(type);
    }

    public Variable execute(Variable preResult){
        Temp temp = JavaDirectUtils.temp(this.type);
        emit(temp.getName() + " = <this>");

        return temp;
    }

    @Override
    public String code(){
        return "this";
    }

}
