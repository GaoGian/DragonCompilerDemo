package gian.compiler.language.simplejava.ast;

import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.exception.JavaDirectException;

/**
 * 抽象语法树节点
 * Created by gaojian on 2019/3/31.
 */
public class AstNode {

    public int lexline = 0;
    public int lexindex = 0;

    public AstNode(){
        this.lexline = JavaDirectGlobalProperty.lexline;
        this.lexindex = JavaDirectGlobalProperty.lexindex;
    }

    public void error(String s){
        throw new JavaDirectException("near line: " + lexline + ", lexindex: " + lexindex + ", msg: " + s);
    }

    public String newlabel(){
        // TODO 需要改成返回对象，并且记录行号标记，方便后续生成真实韩浩
        return "L" + JavaDirectGlobalProperty.lable.getAndIncrement();
    }

    public void emitlabel(String lable){
        System.out.println(lable + ":");
    }

    public void emit(String s){
        System.out.println("\t" + s);
    }

}