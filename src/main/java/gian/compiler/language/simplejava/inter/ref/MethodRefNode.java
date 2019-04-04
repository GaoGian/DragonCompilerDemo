package gian.compiler.language.simplejava.inter.ref;

import gian.compiler.language.simplejava.bean.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/4/4.
 * 方法引用节点
 */
public class MethodRefNode extends RefNode {

    public List<Variable> paramList = new ArrayList<>();

    @Override
    public String toString(){
        return this.caller + "." + this.callName + "(" + this.paramList.toString() + ")";
    }

}