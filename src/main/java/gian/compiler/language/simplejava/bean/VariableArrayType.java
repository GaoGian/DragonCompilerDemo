package gian.compiler.language.simplejava.bean;

/**
 * Created by tingyun on 2018/7/20.
 */
public class VariableArrayType extends VariableType {

    public VariableType elementType;
    // TODO 如果是0，说明是数组变量声明
    public int size;

    public VariableArrayType(int size, VariableType elementType){
        super("[]", size * elementType.width);
        this.size = size;
        this.elementType = elementType;
    }

    public VariableType getBaseVariableType() {
        return elementType;
    }

    public void setBaseVariableType(VariableType baseVariableType) {
        this.elementType = baseVariableType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString(){
        VariableType tempType = this.elementType;
        StringBuilder str = new StringBuilder();
        while(tempType instanceof VariableArrayType) {
            str.append("[]");
            tempType = ((VariableArrayType) this.elementType).getBaseVariableType();
        }

        return tempType.getName() + elementType.toString();
    }

}
