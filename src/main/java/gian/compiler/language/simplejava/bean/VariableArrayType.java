package gian.compiler.language.simplejava.bean;

/**
 * Created by tingyun on 2018/7/20.
 */
public class VariableArrayType extends VariableType {

    public VariableType baseVariableType;
    public int size;

    public VariableArrayType(int sz, VariableType baseVariableType){
        super("[]", sz * baseVariableType.width);
        this.size = sz;
        this.baseVariableType = baseVariableType;
    }

    public VariableType getBaseVariableType() {
        return baseVariableType;
    }

    public void setBaseVariableType(VariableType baseVariableType) {
        this.baseVariableType = baseVariableType;
    }

    @Override
    public String toString(){
        return "[" + size + "]" + baseVariableType.toString();
    }

}
