package gian.compiler.language.simplejava.inter.statement;

import gian.compiler.language.simplejava.inter.AstNode;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Stmt extends AstNode {

    public static Stmt Null = new Stmt();
    public static Stmt Enclosing = Stmt.Null;

    protected Integer after = 0;

    public Stmt(){}

    public void gen(int b, int a){}

    public Integer getAfter() {
        return after;
    }

    public void setAfter(Integer after) {
        this.after = after;
    }

}
