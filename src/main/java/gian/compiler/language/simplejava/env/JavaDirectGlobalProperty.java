package gian.compiler.language.simplejava.env;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gaojian on 2019/3/31.
 */
public class JavaDirectGlobalProperty {

    public static JavaEnvironment topEnv = new JavaEnvironment();
    public static Integer lexline;
    public static AtomicInteger lable = new AtomicInteger(0);
    public static AtomicInteger tempCout = new AtomicInteger(0);

}