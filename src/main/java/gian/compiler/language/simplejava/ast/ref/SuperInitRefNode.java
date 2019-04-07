package gian.compiler.language.simplejava.ast.ref;

import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.bean.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/4/4.
 */
public class SuperInitRefNode extends RefNode {

    public List<Expr> paramList = new ArrayList<>();

    public SuperInitRefNode(List<Expr> paramList){
        this.paramList = paramList;
    }

    @Override
    public String toString(){
        return "super(" + this.paramList.toString() + ")";
    }

}