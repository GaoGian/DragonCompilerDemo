package gian.compiler.language.simplejava.ast.ref;


import gian.compiler.language.simplejava.bean.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/4/4.
 */
public class ConstructorRefNode extends RefNode {

    public List<Variable> paramList = new ArrayList<>();

    public ConstructorRefNode(List<Variable> paramList){
        this.paramList = paramList;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder(this.caller + "." + this.callName + "(" + this.paramList.toString() + ")");
        str.append(next.toString());

        return str.toString();
    }


}