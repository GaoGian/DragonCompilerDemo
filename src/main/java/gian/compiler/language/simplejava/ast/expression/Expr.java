package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

/**
 * Created by gaojian on 2019/3/31.
 */
public abstract class Expr extends Stmt {

    public abstract Temp gen();

    public Temp newTemp(){
        return new Temp();
    }

}