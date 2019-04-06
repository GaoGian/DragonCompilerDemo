package gian.compiler.language.simplejava.ast.ref;

import gian.compiler.language.simplejava.bean.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/4/4.
 * 方法引用节点
 */
public class MethodRefNode extends RefNode {

    public String callName;
    public List<Variable> paramList = new ArrayList<>();

    public MethodRefNode(String callName, List<Variable> paramList){
        this.callName = callName;
        this.paramList = paramList;
    }

    @Override
    public String toString(){
        return this.caller + "." + this.callName + "(" + this.paramList.toString() + ")" + nextRef.toString();
    }

}