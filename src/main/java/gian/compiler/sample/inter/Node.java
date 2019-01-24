package gian.compiler.sample.inter;

import gian.compiler.sample.lexer.Lexer;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Node {

    int lexline = 0;
    public static int lables = 0;

    public Node(){
        lexline = Lexer.line;
    }

    public void error(String s){
        throw new Error("near line" + lexline + ": " + s);
    }

    public int newlabel(){
        return ++lables;
    }

    public void emitlabel(int i){
        System.out.print("L" + i + ":");
    }

    public void emit(String s){
        System.out.println("\t" + s);
    }

}
