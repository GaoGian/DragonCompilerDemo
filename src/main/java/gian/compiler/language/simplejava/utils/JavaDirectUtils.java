package gian.compiler.language.simplejava.utils;

import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.exception.JavaDirectException;
import gian.compiler.language.simplejava.inter.expression.Expr;
import gian.compiler.front.lexical.parser.Token;

/**
 * Created by gaojian on 2019/3/31.
 */
public class JavaDirectUtils {

    public static Expr factor(Token token){
        Expr expr = null;
//        switch (token.getType().getType()) {
//            case JavaConstants.VARIABLE_TYPE_CHAR:
//                expr = new Constant(look, Type.Int);
//                return expr;
//            case Tag.REAL:
//                expr = new Constant(look, Type.Float);
//                return expr;
//            case Tag.TRUE:
//                expr = Constant.True;
//                return expr;
//            case Tag.FALSE:
//                expr = Constant.False;
//                return expr;
//            case Tag.ID:
//                String s = look.toString();
//                Id id = top.get(look);
//                if(id == null){
//                    error(look.toString() + " undeclared");
//                }
//                if(look.tag != '['){
//                    return id;
//                }else{
//                    return offset(id);
//                }
//            default:
//                error("syntax error");
//                return expr;
//        }

        return expr;
    }

    public static void error(String s){
        throw new JavaDirectException("near line " + JavaDirectGlobalProperty.line + ": " + s);
    }

}