import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;
import org.junit.Test;

import javax.script.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by gaojian on 2019/1/24.
 */
public class MyTest {

    @Test
    public void test(){
        Scanner scan = new Scanner(System.in);
        System.out.println("input: " + scan.next());

    }

    @Test
    public void test1(){

        String content = "I am noob from runoob.com.";

        String pattern = ".*runoob.*";

        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("字符串中是否包含了 'runoob' 子字符串? " + isMatch);

    }

    @Test
    public void test2(){
        List<SyntaxSymbol> list1 = new ArrayList<>();
        list1.add(new SyntaxSymbol("if", true));
        list1.add(new SyntaxSymbol("expr", true));

        List<SyntaxSymbol> list2 = new ArrayList<>();
        list2.add(new SyntaxSymbol("if", true));
        list2.add(new SyntaxSymbol("expr", true));

        System.out.println(list1.equals(list2));

    }

    @Test
    public void test3(){
//        System.out.println(compareReleaseNote("2.10.1", "2.9"));
//        System.out.println(compareReleaseNote("2.10.1", "2.10"));
//        System.out.println(compareReleaseNote("2.10.1", "2.10.1"));
//        System.out.println(compareReleaseNote("2.10.1", "2.9.1"));
//        System.out.println(compareReleaseNote("2.10.1", "1.9.1"));
//        System.out.println(compareReleaseNote("2.10.1", "0.1.1"));
//
//        System.out.println(compareReleaseNote("2.10.1", "0"));
//        System.out.println(compareReleaseNote("2.10.1", "1"));

        System.out.println(compareReleaseNote("0.1.0", "0.2.0"));
//        System.out.println(compareReleaseNote("2.10", "2.10.1"));
//        System.out.println(compareReleaseNote("2.10.1", "2.10.1"));
//        System.out.println(compareReleaseNote("2.9.1", "2.10.1"));
//        System.out.println(compareReleaseNote("1.9.1", "2.10.1"));
//        System.out.println(compareReleaseNote("0.1.1", "2.10.1"));
    }

    // 数据库查询的版本号比较方法不准确
    private String compareReleaseNote(String version1, String version2){
        // 线上的版本号只有2.10.1形式，暂时没有其他分隔符
        String[] versionPart1 = version1.split("\\.");
        String[] versionPart2 = version2.split("\\.");

        // 按照版本号个部分进行比较
        int minLength = versionPart1.length < versionPart2.length ? versionPart1.length : versionPart2.length;
        for(int i=0; i<minLength; i++){
            if(Integer.valueOf(versionPart1[i]) > Integer.valueOf(versionPart2[i])){
                return version1;
            }else if(Integer.valueOf(versionPart1[i]) < Integer.valueOf(versionPart2[i])){
                return version2;
            }
        }

        // 如果走到这里说明大版本号相同，有小版本号的版本为高版本探针
        String maxVersion = versionPart1.length >= versionPart2.length ? version1 : version2;
        return maxVersion;
    }


/***********************************************************JSEngine***************************************************************/

    // 执行js代码
    @Test
    public void testJSEngine(){

        String str="function executeJS(){ print('js execute java') }";

        ScriptEngineManager manager = new ScriptEngineManager();

        ScriptEngine engine = manager.getEngineByName("nashorn");
        try {
            engine.eval(str);
            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine;
                //从脚本引擎中返回一个给定接口的实现
                try {
                    Object[] args = new Object[]{};
                    invocable.invokeFunction("executeJS", args);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
    }

    // 执行java对象方法   （无法执行）
    @Test
    public void testJSEngine1(){

        String str="function executeJS(stream){ stream.println('js execute java') }";

        ScriptEngineManager manager = new ScriptEngineManager();

        ScriptEngine engine = manager.getEngineByName("nashorn");
        try {
            engine.eval(str);
            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine;
                //从脚本引擎中返回一个给定接口的实现
                try {
                    Object[] args = new Object[]{System.out};
                    invocable.invokeFunction("executeJS", args);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
    }

    // 执行静态方法
    @Test
    public void testJSEngine2(){

        String str="function executeJS(){ var jsTest = Java.type('MyTest.JSTest'); jsTest.print(); }";

        ScriptEngineManager manager = new ScriptEngineManager();

        ScriptEngine engine = manager.getEngineByName("nashorn");
        try {
            engine.eval(str);
            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine;
                //从脚本引擎中返回一个给定接口的实现
                try {
                    Object[] args = new Object[]{};
                    invocable.invokeFunction("executeJS", args);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
    }

    // 执行对象方法 (无法执行)
    @Test
    public void testJSEngine21(){

        String str="function executeJS(instance){ var JSTest = Java.type('MyTest.JSTest'); var temp = new JSTest(); temp.prototype.showName.call(instance); }";

        ScriptEngineManager manager = new ScriptEngineManager();

        ScriptEngine engine = manager.getEngineByName("nashorn");
        try {
            engine.eval(str);
            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine;
                //从脚本引擎中返回一个给定接口的实现
                try {
                    Object[] args = new Object[]{new JSTest("JSTest one")};
                    invocable.invokeFunction("executeJS", args);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
    }

    // 通过绑定变量到全局进行执行
    @Test
    public void testJSEngine3(){

        ScriptEngineManager manager = new ScriptEngineManager();

        ScriptEngine engine = manager.getEngineByName("nashorn");
        try {
            // 绑定变量
            Integer valueIn = 10;
            SimpleBindings simpleBindings = new SimpleBindings();
            simpleBindings.put("valueIn", valueIn);

            // 将绑定的变量设置到js引擎中
            engine.eval("function executeJS(){ print(valueIn); }", simpleBindings);
//            engine.eval("function executeJS(){ print(globalValue); }");
//            engine.eval("print(globalValue)", simpleBindings);

            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine;
                //从脚本引擎中返回一个给定接口的实现
                try {
                    Object[] args = new Object[]{};
                    invocable.invokeFunction("executeJS", args);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
    }

    // 通过绑定变量到全局进行执行 (可行)
    @Test
    public void testJSEngine31(){

        ScriptEngineManager manager = new ScriptEngineManager();

        ScriptEngine engine = manager.getEngineByName("nashorn");
        try {
            // 绑定变量
            SimpleBindings simpleBindings = new SimpleBindings();
            simpleBindings.put("jsTest", new JSTest("JSTest one"));

            // 将绑定的变量设置到js引擎中
            engine.eval("print(jsTest.showName())", simpleBindings);
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
    }

    // 通过绑定变量到全局进行执行
    @Test
    public void testJSEngine4(){

        String str="function executeJS(){ systemOut.println('js execute java System.out'); }";

        ScriptEngineManager manager = new ScriptEngineManager();

        ScriptEngine engine = manager.getEngineByName("nashorn");
        try {
            // 绑定变量
            SimpleBindings simpleBindings = new SimpleBindings();
            simpleBindings.put("systemOut", System.out);

            // 将绑定的变量设置到js引擎中
            engine.eval(str, simpleBindings);
            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine;
                //从脚本引擎中返回一个给定接口的实现
                try {
                    Object[] args = new Object[]{};
                    invocable.invokeFunction("executeJS", args);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
    }

    public static class JSTest{

        private String name;

        public JSTest(){};

        public JSTest(String name) {
            this.name = name;
        }

        public void showName(){
            System.out.println("JSTest name: " + this.name);
        }

        public static void print(){
            System.out.println(JSTest.class.toString());
        }

    }


/***********************************************************JSEngine***************************************************************/


}



