package gian.compiler.language.simplejava.ast.statement;

import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.ast.ref.RefNode;
import gian.compiler.language.simplejava.bean.Variable;

/**
 * Created by gaojian on 2019/4/9.
 */
public class SetRef extends Stmt {

    public RefNode refNode;
    public Expr expr;

    public SetRef(RefNode refNode, Expr expr){
        this.refNode = refNode;
        this.expr = expr;
        // TODO 暂时取消类型校验，等到动态加载连接功能完成后再启用
//        if(check(id.getType(), expr.getType()) == null){
//            error("type error");
//        }
    }

    @Override
    public void gen(String before, String after){
        String lable = newlabel();
        emitlabel(lable);
        Variable refVariable = this.refNode.gen();
        String lable2 = newlabel();
        emitlabel(lable2);
        Variable result = this.expr.gen();
        emit("<getField> " + refVariable.getName() + " <assign> " + result.getName());
    }


}