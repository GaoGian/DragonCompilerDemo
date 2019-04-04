package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.ast.AstNode;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Stmt extends AstNode {

    public static Stmt Null = new Stmt();

    protected Integer current = 0;
    protected Integer after = 0;

    public Stmt(){}

    public void gen(int b, int a){}

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getAfter() {
        return after;
    }

    public void setAfter(Integer after) {
        this.after = after;
    }

}
