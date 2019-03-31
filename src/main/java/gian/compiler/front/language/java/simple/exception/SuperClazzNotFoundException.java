package gian.compiler.front.language.java.simple.exception;

/**
 * Created by gaojian on 2019/3/31.
 */
public class SuperClazzNotFoundException extends RuntimeException {

    public SuperClazzNotFoundException(String msg){
        super(msg);
    }

}