package gian.compiler.language.simplejava.ast.ref;

import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.ast.expression.Temp;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

import java.util.List;

/**
 * Created by Gian on 2019/4/5.
 */
public class ArrayElementRefNode extends RefNode {

    public String callName;
    public List<Expr> arrayIndex;

    public ArrayElementRefNode(String callName, List<Expr> arrayIndex) {
        // TODO 需要携带类型信息
        super(null);
        this.callName = callName;
        this.arrayIndex = arrayIndex;
    }

    @Override
    public Variable execute(Variable preResult){
        Temp temp = JavaDirectUtils.temp(this.type);
        if(preResult != null) {
            emit(temp.getName() + " = " + preResult.getName() + " <getField> " + this.callName + " <getArrayElement> " + this.code());
        }else{
            emit(temp.getName() + " = " + " <getVariable> " + this.callName + " <getArrayElement> " + this.code());
        }

        return temp;
    }

    @Override
    public String code(){
        StringBuffer str = new StringBuffer();
        for(Expr index : arrayIndex){
            str.append("[" + index + "]");
        }
        return str.toString();
    }

}
