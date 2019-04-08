package gian.compiler.language.simplejava.ast.ref;

import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.ast.expression.Temp;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

/**
 * Created by gaojian on 2019/4/4.
 */
public abstract class RefNode extends Expr {

    // 调用名称
    public String callName;

    public RefNode preRef;
    public RefNode nextRef;

    public RefNode(VariableType type){
        super(type);
    }

    @Override
    public Variable gen(){
        RefNode nextRef = this;
        Variable temp = null;
        while(nextRef != null){
            temp = nextRef.execute(temp);
            nextRef = this.nextRef;
        }
        return temp;
    }

    public abstract Variable execute(Variable preResult);

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public RefNode getPreRef() {
        return preRef;
    }

    public void setPreRef(RefNode preRef) {
        this.preRef = preRef;
    }

    public RefNode getNextRef() {
        return nextRef;
    }

    public void setNextRef(RefNode nextRef) {
        this.nextRef = nextRef;
    }

}