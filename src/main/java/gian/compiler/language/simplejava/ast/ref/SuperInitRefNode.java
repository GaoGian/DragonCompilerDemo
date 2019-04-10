package gian.compiler.language.simplejava.ast.ref;

import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/4/4.
 */
public class SuperInitRefNode extends RefNode {

    public List<Expr> paramList = new ArrayList<>();

    public SuperInitRefNode(VariableType type, List<Expr> paramList){
        super(type);
        this.callName = "super";
        this.paramList = paramList;
    }

    public Variable execute(Variable preResult){
        emit("<super> " + this.code());
        return null;
    }

    @Override
    public String code(){
        return "(" + this.paramList.toString() + ")";
    }

}