package gian.compiler.language.simplejava.bean;

/**
 * Created by tingyun on 2018/7/20.
 */
public class VariableArrayType extends VariableType {

    public VariableType of;
    public int size = 1;

    public VariableArrayType(int sz, VariableArrayType p){
        super("[]", false);
        size = sz;
        of = p;
    }

    public String toString(){
        return "[" + size + "]" + of.toString();
    }

}
