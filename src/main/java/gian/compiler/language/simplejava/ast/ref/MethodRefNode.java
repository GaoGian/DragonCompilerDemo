package gian.compiler.language.simplejava.ast.ref;

import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.ast.expression.Temp;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/4/4.
 * 方法引用节点
 */
public class MethodRefNode extends RefNode {

    public List<Expr> paramList = new ArrayList<>();

    public MethodRefNode(String callName, List<Expr> paramList){
        super(null);
        this.callName = callName;
        this.paramList = paramList;
    }

    public Variable execute(Variable preResult){
        Temp temp = JavaDirectUtils.temp(this.type);
        if(preResult != null) {
            emit(temp.getName() + " = " + preResult.getName() + " <invoke> " + this.code());
        }else{
            emit(temp.getName() + " = " + "<this> <invoke> " + this.code());
        }

        return temp;
    }

    @Override
    public String code(){
        return this.callName + "(" + this.paramList.toString() + ")";
    }

}