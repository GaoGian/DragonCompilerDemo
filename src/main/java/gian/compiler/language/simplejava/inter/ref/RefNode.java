package gian.compiler.language.simplejava.inter.ref;

import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.inter.AstNode;

/**
 * Created by gaojian on 2019/4/4.
 */
public class RefNode extends AstNode {

    // 调用者
    public Variable caller;
    // 调用名称
    public String callName;

    public Variable resultVariable;
    public VariableType resultType;

    public RefNode next;


    public Variable getCaller() {
        return caller;
    }

    public void setCaller(Variable caller) {
        this.caller = caller;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public Variable getResultVariable() {
        return resultVariable;
    }

    public void setResultVariable(Variable resultVariable) {
        this.resultVariable = resultVariable;
    }

    public VariableType getResultType() {
        return resultType;
    }

    public void setResultType(VariableType resultType) {
        this.resultType = resultType;
    }

    public RefNode getNext() {
        return next;
    }

    public void setNext(RefNode next) {
        this.next = next;
    }
}