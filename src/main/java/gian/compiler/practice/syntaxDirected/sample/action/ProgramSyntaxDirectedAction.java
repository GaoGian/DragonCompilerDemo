package gian.compiler.practice.syntaxDirected.sample.action;

import gian.compiler.practice.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.practice.syntaxDirected.SyntaxDirectedListener;

/**
 * 匹配 program 非终结符
 * TODO 示例代码
 * Created by gaojian on 2019/3/23.
 */
public class ProgramSyntaxDirectedAction extends SyntaxDirectedListener {

    public ProgramSyntaxDirectedAction(){
        super("", 0, "", false);
    }

    @Override
    public String enterSyntaxSymbol(SyntaxDirectedContext context) {
        return "";
    }

    @Override
    public String exitSyntaxSymbol(SyntaxDirectedContext context) {
        return "";
    }

}