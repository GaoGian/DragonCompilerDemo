package gian.compiler.front.language.java.simple.bean;

/**
 * Created by gaojian on 2019/3/30.
 */
public class VariableInitInfo {

    // FIXME 初始化动作code的行号
    protected Integer initCodeIndex;
    protected VariableType initVariableType;

    public Integer getInitCodeIndex() {
        return initCodeIndex;
    }

    public void setInitCodeIndex(Integer initCodeIndex) {
        this.initCodeIndex = initCodeIndex;
    }

    public VariableType getInitVariableType() {
        return initVariableType;
    }

    public void setInitVariableType(VariableType initVariableType) {
        this.initVariableType = initVariableType;
    }
}