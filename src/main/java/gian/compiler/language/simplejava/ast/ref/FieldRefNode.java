package gian.compiler.language.simplejava.ast.ref;

import gian.compiler.language.simplejava.ast.expression.Temp;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

/**
 * Created by gaojian on 2019/4/4.
 * 变量引用节点
 */
public class FieldRefNode extends RefNode {

    public String fieldName;

    public FieldRefNode(String fieldName) {
        // TODO 需要携带类型信息
        super(null);
        this.callName = fieldName;
        this.fieldName = fieldName;
    }

    @Override
    public Variable execute(Variable preResult){
        Temp temp = JavaDirectUtils.temp(this.type);
        if(preResult != null) {
            emit(temp.getName() + " = " + preResult.getName() + " <getField> " + this.code());
        }else{
            emit(temp.getName() + " = " + " <getVariable> " + this.code());
        }

        return temp;
    }

    @Override
    public String code(){
        return this.fieldName;
    }

}