package gian.compiler.language.simplejava.inter.statement;

import gian.compiler.language.simplejava.inter.AstNode;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Stmt extends AstNode {

    public int after = 0;

    public Stmt(Integer lexline){
        super(lexline);
    }

    public void gen(int b, int a){}

}
