package gian.compiler.language.simplejava.ast.ref;

/**
 * Created by gaojian on 2019/4/4.
 * 变量引用节点
 */
public class FieldRefNode extends RefNode {

    @Override
    public String toString(){
        return this.caller + "." + this.callName + next.toString();
    }

}