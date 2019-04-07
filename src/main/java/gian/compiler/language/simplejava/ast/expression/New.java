package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gian on 2019/4/5.
 */
public class New extends Expr {

    public VariableType variableType;
    public List<Variable> paramList = new ArrayList<>();

    public New(VariableType variableType, List<Variable> paramList){
        this.variableType = variableType;
        this.paramList = paramList;
    }

    @Override
    public String toString(){
        return "new " + this.variableType.getName() + "(" + this.paramList.toString() + ")";
    }

}
