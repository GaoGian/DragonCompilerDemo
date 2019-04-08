package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gian on 2019/4/5.
 */
public class New extends Expr {

    public List<Variable> paramList = new ArrayList<>();

    public New(VariableType variableType, List<Variable> paramList){
        super(variableType);
        this.paramList = paramList;
    }


    @Override
    public String code(){
        return "new " + this.type.getName() + "(" + this.paramList.toString() + ")";
    }

}
