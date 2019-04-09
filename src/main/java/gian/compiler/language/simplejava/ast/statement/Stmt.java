package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.ast.AstNode;
import gian.compiler.language.simplejava.bean.Variable;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Stmt extends AstNode {

    public static Stmt Null = new Stmt();

    protected String current;
    protected String after;

    public Stmt(){}

    @Override
    public Variable gen(){
        String before = newlabel();
        String after = newlabel();
        this.gen(before, after);

        return null;
    }

    public void gen(String before, String after){}

}
