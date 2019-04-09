package gian.compiler.language.simplejava.ast.statement;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Seq extends Stmt {

    public Stmt stmt1, stmt2;

    public Seq(Stmt s1, Stmt s2){
        stmt1 = s1;
        stmt2 = s2;
    }

    @Override
    public void gen(String before, String after){
        if(stmt1 == Stmt.Null){
            stmt2.gen(before, after);
        }else if(stmt2 == Stmt.Null){
            stmt1.gen(before, after);
        }else{
            String label = newlabel();
            stmt1.gen(before, label);
            emitlabel(label);
            stmt2.gen(label, after);
        }
    }

}
