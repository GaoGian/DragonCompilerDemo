package gian.compiler.language.simplejava.ast.ref;

/**
 * Created by gaojian on 2019/4/4.
 * 变量引用节点
 */
public class FieldRefNode extends RefNode {

    public String fieldName;

    public FieldRefNode(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString(){
        return this.caller + "." + this.callName + nextRef.toString();
    }

}