package gian.compiler.language.simplejava.ast.expression;

import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.bean.Variable;

/**
 * Created by gaojian on 2019/3/31.
 */
public abstract class Expr extends Stmt {

    /**
     * 执行表达式并返回执行结果引用
     * @return
     */
    public abstract Variable gen();

}