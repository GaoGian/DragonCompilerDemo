package gian.compiler.language.simplejava.bean;

/**
 * Created by gaojian on 2019/3/27.
 */
public class Variable extends Param {

    // TODO 存储地址，暂时用不到
    protected String address;

    public Variable(String fieldName, VariableType variableType) {
        super(fieldName, variableType);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}