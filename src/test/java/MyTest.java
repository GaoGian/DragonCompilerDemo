import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxSymbol;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.ScriptObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.assertj.core.util.Lists;
import org.junit.Test;

import javax.script.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
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
    public void test21(){
        String content = "\"testSimpleJavaMath\"";
        String pattern = "\"(\\S|\\s)*\"";
        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println(isMatch);
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
    public void test23(){
        String syntax = "parExpression → expressionTerm ◀&#124;&#124;▶ expressionTerm | expressionTerm ◀&&▶ expressionTerm | expressionTerm";
        String head = syntax.split("→")[0];
        String bodys = syntax.split("→")[1];

        String[] products = bodys.split("\\|");
        for(String body : products) {
            String[] symbols = body.trim().split(" ");
            for (String symbol : symbols) {
                System.out.println(symbol.replace(LexConstants.MONTANT_UNICODE, LexConstants.MONTANT_STRING));
            }

        }
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

    // 通过绑定变量到全局进行执行 (可行)
    @Test
    public void testJSEngine3(){

        ScriptEngineManager manager = new ScriptEngineManager();

        ScriptEngine engine = manager.getEngineByName("nashorn");
        try {
            // 绑定变量
            Integer valueIn = 10;
            Constructor constructor =  ScriptObjectMirror.class.getDeclaredConstructor(ScriptObject.class, Global.class);
            constructor.setAccessible(true);
            ScriptObjectMirror simpleBindings = (ScriptObjectMirror) constructor.newInstance((ScriptObject) getField(engine, "global"), (Global) getField(engine, "global"));
            Map<String, JSTest> map = new HashMap<>();
            JSTest jsTest = new JSTest("jsTest_value");
            map.put("jsTest_key", jsTest);
            List<Map<String, JSTest>> stack = new ArrayList<>();
            stack.add(map);
            simpleBindings.put("jsTest", stack);

            // 将绑定的变量设置到js引擎中
            engine.eval("function executeJS(){ " +
                    // 判断是否获取到java对象
                    "print(jsTest); " +
                    // 判断是否可以操作list、map
                    "print(jsTest[0].jsTest_key.name); " +
                    // 判断是否可以调用实例方法
                    "print(jsTest[0].jsTest_key.showName()); " +
                    // 判断是否可以赋值
                    "jsTest[0].jsTest_key.name='test'; print(jsTest[0].jsTest_key.name); " +
                    // 创建java对象
                    "var JSTest = Java.type('MyTest.JSTest'); var newJSTest = new JSTest('newJSTest'); jsTest[0].newJSTest=newJSTest; print(jsTest[0].newJSTest.name); " +
                    // 调用call方法
//                    "var showName = jsTest[0].newJSTest.showName; print(showName.call(jsTest[0].jsTest_key)); " +
                    " }",
                    simpleBindings);

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

            System.out.println(jsTest.name);
            System.out.println(map.get("newJSTest").name);
        } catch (Exception ex) {
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

    public Object getField(Object target, String fieldName){
        Object result = null;
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            result = field.get(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class JSTest{

        public String name;

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


    public static void main(String[] args){
        String helloWorld = "hello world";
        int a = 1;
        int b = 2;
        int result = a - b;
        boolean b_result = (a >= result);
        System.out.println(helloWorld + " " + result + " " + b_result);
    }

    @Test
    public void test111(){

        // int类型关键字正则表达式
        String type_regex = "int";
        // identifier变量名正则表达式
        String identifier_regex = "identifier";

        // 目标解析代码
        String program = "int identifier = 1 + 2;";

        // 当前需要识别的字符串
        String input = "";
        // 读入字符流
        for(char c : program.toCharArray()){
            if(c == ' '){continue;}
            // 拼接字符串
            input += String.valueOf(c);
            // 识别字符串
            if(Pattern.matches(type_regex, input)){
                System.out.println("识别出int类型关键字: " + input);
                input = "";
            }else if(Pattern.matches(identifier_regex, input)){
                System.out.println("识别出identifier变量名: " + input);
                input = "";
            }
        }

    }

    @Test
    public void testRegexParse(){
        List<Metacharacter> metaLists = Lists.newArrayList();   // 存放解析后的正则表达式元字符
        String identifier_regex = "[abc]\\w*";               // identifier变量名正则表达式
        char[] targetInput = identifier_regex.toCharArray();
        /** 递归解析所有正则表达式元字符 **/
        for(int index=0; index<targetInput.length; index++){
            Metacharacter meta = genMetacharacter(targetInput, index);
            index += meta.getMeta().length();
            metaLists.add(meta);
        }
    }
    private Metacharacter genMetacharacter(char[] targetInput, int index){
        // 识别普通单个字符,例如 a、b、c、d
        if(targetInput[index] == 'a' || targetInput[index] == 'b' || targetInput[index] == 'c'){
            CharMetacharacter charMeta = genCharMetacharacter(targetInput, index);
            return charMeta;
        }
        // 识别元字符，例如 [ABC]
        else if(targetInput[index] == '['){
            UnitMetacharacter unitMeta = genUnitMetacharacter(targetInput, index);
            return unitMeta;
        }
        // 识别转义字符，例如 \w  \n
        else if(targetInput[index] == '\\'){
            TransformMetacharacter transformMeta = genTransformMetacharacter(targetInput, index);
            return transformMeta;
        }
        // 识别控制流字符，例如 ? * + |
        else if(targetInput[index] == '*' || targetInput[index] == '+' || targetInput[index] == '|'){
            ControlMetacharacter controlMeta = genControlMetacharacter(targetInput, index);
            return controlMeta;
        }
        throw new RuntimeException("genMetacharacter error, match regex fail");
    }

    // 识别单个普通字符
    private CharMetacharacter genCharMetacharacter(char[] targetInput, int index){
        char c = targetInput[index];
        return new CharMetacharacter(String.valueOf(c));
    }
    // 识别元字符
    private UnitMetacharacter genUnitMetacharacter(char[] targetInput, int index){
        List<Metacharacter> childMetas = Lists.newArrayList();
        for(int i=index; i<targetInput.length; i++){
            if(targetInput[i] == '['){
                continue;
            }else if(targetInput[i] == ']'){
                // 说明元字符识别完成
                break;
            }
            /** 可能包含其他正则表达式，需要循环递归处理 **/
            Metacharacter chileMeta = genMetacharacter(targetInput, index);
            i += chileMeta.getMeta().length();
            childMetas.add(chileMeta);
        }
        return new UnitMetacharacter(childMetas);
    }
    // 识别转义字符
    private TransformMetacharacter genTransformMetacharacter(char[] targetInput, int index){
        String meta = String.valueOf(targetInput[index]) + String.valueOf(targetInput[index+1]);
        return new TransformMetacharacter(meta);
    }
    // 识别控制流字符
    private ControlMetacharacter genControlMetacharacter(char[] targetInput, int index){
        return new ControlMetacharacter(String.valueOf(targetInput[index]));
    }

    abstract class Metacharacter{
        abstract String getMeta();
        abstract boolean match(char input);
    }
    @Data @AllArgsConstructor
    class CharMetacharacter extends Metacharacter{
        // 匹配单个普通字符,例如 a、b、c、d
        String meta;
        public boolean match(char input){
            return meta.equals(String.valueOf(input));
        }
    }
    @Data @AllArgsConstructor
    class UnitMetacharacter extends Metacharacter{
        // 元字符[....]，例如 [ABC]
        List<Metacharacter> childMetas = Lists.newArrayList();
        public boolean match(char input){
            for(Metacharacter childMeta : childMetas){
                // 只要有一个匹配就可以
                if(childMeta.match(input)){
                    return true;
                }
            }
            return false;
        }
        public String getMeta(){
            String meta = "";
            for(Metacharacter childMeta : childMetas){
                meta += childMeta.getMeta();
            }
            return meta;
        }
    }
    @Data @AllArgsConstructor
    class TransformMetacharacter extends Metacharacter{
        // 转义字符，例如 \w  \n
        String meta;
        public boolean match(char input){
            // 各种转义处理
            if(meta.equals("\\w")){
                return (input >= 65 && input <= 90) || (input >= 97 && input <= 122);
            }else{
                //其他转义处理.........
            }
            return false;
        }
    }
    @Data @AllArgsConstructor
    class ControlMetacharacter extends Metacharacter{
        // 控制流字符，例如 ? * + |
        String meta;
        public boolean match(char input){
            return meta.equals(String.valueOf(input));
        }
    }

    // 优先级
    public static int priority(Metacharacter meta) {
        String pattern = meta.getMeta();
        switch (pattern) {
            case "*":
            case "+":
            case "?":
                return 7;
            case "|":
                return 3;
            default:
                return 7;
        }
    }


    @Test
    public void testLexParse(){
        // 已经构建好的自动机（状态转换图）
        List<TranCell> tranCells = new ArrayList<>();
        // 目标解析代码
        String content = ".........";
        char[] inputArray = content.toCharArray();
        // 识别出得词法单元
        List<Token> tokens = Lists.newArrayList();
        // 上一次匹配位置
        int lastIndex = 0;
        // 根据当前输入可以匹配的状态机
        List<TranCell> tranAableCell = Lists.newArrayList(tranCells);
        for(int i=0; i<inputArray.length; i++){
            char input = inputArray[i];
            // 遍历所有自动机，排除不匹配的选项
            for(TranCell tranCell : tranAableCell){
                // 根据当前输入进行状态迁移，如果不能迁移则移除
                if(!tranCell.tranState(input)){
                    tranAableCell.remove(tranCell);
                }
            }
            // 判断是否识别成功
            if(tranAableCell.size() == 1
                    && tranAableCell.get(0).matchComplete()){
                // 识别的词法单元
                Token token = new Token(content.substring(lastIndex, i), tranAableCell.get(0).getType());
                // 重新
                tranAableCell = Lists.newArrayList(tranCells);
                lastIndex = i;
            }
        }

    }


    class TranCell{
        boolean tranState(char input){
            return true;
        }

        boolean matchComplete(){
            return true;
        }

        String getType(){
            return "";
        }
    }

    @AllArgsConstructor
    class Token{
        private String content;
        private String type;
    }

}



