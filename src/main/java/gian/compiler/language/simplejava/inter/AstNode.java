package gian.compiler.language.simplejava.inter;

import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.exception.JavaDirectException;

/**
 * 抽象语法树节点
 * Created by gaojian on 2019/3/31.
 */
public class AstNode {

    protected Integer lexline = 0;

    public AstNode(Integer lexline){
        this.lexline = lexline;
    }

    public void error(String s){
        throw new JavaDirectException("near line" + lexline + ": " + s);
    }

    public int newlabel(){
        return JavaDirectGlobalProperty.lable.getAndIncrement();
    }

    public void emitlabel(int i){
        System.out.println("L" + i + ":");
    }

    public void emit(String s){
        System.out.println("\t" + s);
    }

}