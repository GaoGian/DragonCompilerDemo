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
 */
public class ConstructorRefNode extends RefNode {

    public String newClassName;
    public List<Expr> paramList = new ArrayList<>();

    public ConstructorRefNode(String newClassName, List<Expr> paramList){
        // TODO 需要携带类型信息
        super(null);
        this.newClassName = newClassName;
        this.paramList = paramList;
    }

    @Override
    public Variable execute(Variable preResult){
        Temp temp = JavaDirectUtils.temp(this.type);
        emit(temp.getName() + " = " + " <new> " + this.code());

        return temp;
    }

    @Override
    public String code(){
        return this.newClassName + "(" + this.paramList.toString() + ")";
    }


}