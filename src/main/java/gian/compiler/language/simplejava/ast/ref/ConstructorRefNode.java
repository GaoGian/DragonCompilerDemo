package gian.compiler.language.simplejava.ast.ref;


import gian.compiler.language.simplejava.bean.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/4/4.
 */
public class ConstructorRefNode extends RefNode {

    public String newClassName;
    public List<Variable> paramList = new ArrayList<>();

    public ConstructorRefNode(String newClassName, List<Variable> paramList){
        this.newClassName = newClassName;
        this.paramList = paramList;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder(this.caller + "." + this.callName + "(" + this.paramList.toString() + ")");
        str.append(nextRef.toString());

        return str.toString();
    }


}