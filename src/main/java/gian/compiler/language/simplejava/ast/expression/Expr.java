package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

/**
 * Created by gaojian on 2019/3/31.
 */
public abstract class Expr extends Stmt {

    protected Variable result;
    protected VariableType type;

    public Expr(VariableType type){
        this.type = type;
    }

    /**
     * 执行表达式并返回执行结果引用
     * @return
     */
    public Variable gen(){
        if(this.result == null) {
            this.result = JavaDirectUtils.temp(type);
            emit(this.result.getName() + " <assign> " + this.code());
        }

        return this.result;
    }

    /**
     * 返回中间码
     * @return
     */
    public abstract String code();

    public VariableType getType() {
        return type;
    }

    public void setType(VariableType type) {
        this.type = type;
    }
}