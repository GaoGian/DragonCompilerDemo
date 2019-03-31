package gian.compiler.front.language.java.simple.inter.statement;

import gian.compiler.front.language.java.simple.inter.AstNode;

import java.util.concurrent.atomic.AtomicInteger;

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
