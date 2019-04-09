package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Break extends Stmt {

    public Stmt stmt;

    public Break(){
        if(JavaDirectGlobalProperty.cycleEnclosingStack.top() == null){
            error("unenclosed break");
        }
        stmt = JavaDirectGlobalProperty.cycleEnclosingStack.top();
    }

    @Override
    public void gen(String before, String after){
        emit("<goto> " + stmt.after);
    }

}
