package sample.book.inter.statement;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Break extends Stmt {

    public Stmt stmt;

    public Break(){
        if(Stmt.Enclosing == Stmt.Null){
            error("unenclosed break");
        }
        stmt = Stmt.Enclosing;
    }

    public void gen(int b, int a){
        emit("goto L" + stmt.after);
    }

}
