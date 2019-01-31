package gian.compiler.practice.lexical.transform.regex;

import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.lexical.transform.MyStack;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 将正则表达式拆分成不同的子部分( [] 级别)，方便转换成逆波兰
 * Created by gaojian on 2019/1/31.
 */
public class LexSimplePattern {

    public static Map<Character, Character> bracketsMap = new HashMap();

    static {
        bracketsMap.put('}', '{');
        bracketsMap.put(')', '(');
        bracketsMap.put(']', '[');
    }

    /**
     * 拆分成子表达式树
     * @param pattern
     * @return
     */
    public static List<Metacharacter> compile(String pattern){

        MyStack<Character> inputStack = new MyStack<>();
        MyStack<Metacharacter> metaStack = new MyStack<>();
        // 是否是方括号元字符
        MyStack<Character> bracketStack = new MyStack<>();

        char[] chars = pattern.toCharArray();
        for(int i=0; i<chars.length; i++){
            char input = chars[i];
            switch (input) {
                case '|':
                case '*':
                case '+':
                case '?':{
                    metaStack.push(new Metacharacter(String.valueOf(input), false));
                    break;
                }
                case '(':{
                    // 用来识别左边界
                    metaStack.push(new Metacharacter(String.valueOf(input), false));
                    break;
                }
                case ')':{
                    Metacharacter parenthesisMeta = matchParenthesis(metaStack, bracketsMap.get(input));
                    metaStack.pop();
                    if(parenthesisMeta != null) {
                        metaStack.push(parenthesisMeta);
                    }
                    break;
                }
                case '[':{
                    inputStack.push(input);
                    bracketStack.push(input);
                    break;
                }
                case ']':{
                    inputStack.push(input);
                    Metacharacter bracketMeta = matchBracket(inputStack, bracketsMap.get(input));
                    if(bracketMeta != null) {
                        metaStack.push(bracketMeta);
                    }
                    bracketStack.pop();
                    break;
                }
                case '\\':{
                    inputStack.push(input);
                    break;
                }
                default:{
                    if(bracketStack.top() != null){
                        // 说明是 [] 元字符
                        inputStack.push(input);
                    }else if(inputStack.top() == '\\'){
                        // 识别转义字符
                        Character pop = inputStack.pop();
                        metaStack.push(new Metacharacter((String.valueOf(pop) + input), true));
                    }else{
                        // 识别单个字符
                        Character pop = inputStack.pop();
                        metaStack.push(new Metacharacter(String.valueOf(pop), true));
                    }
                    break;
                }

            }

        }

        List<Metacharacter> metaList = new ArrayList<>();
        while(metaStack.top() != null){
            metaList.add(metaStack.pop());
        }
        Collections.reverse(metaList);

        return metaList;
    }

    /**
     * 将子表达式进行逆波兰转换
     * @param originMetas
     * @return
     */
    public static List<Metacharacter> postfix(List<Metacharacter> originMetas){
        //设定表达式的最后一个符号式“ε”，而其“ε”一开始先放在栈s的栈底
        originMetas.add(new Metacharacter(LexConstants.EOF_STR, false));

        // 表达式拆分后，只需要对非标识符进行编排
        MyStack<Metacharacter> metaStack = new MyStack<>();
        for(Metacharacter meta : originMetas){
            if(meta.getChildMetas().size() > 0){
                meta.setChildMetas(postfix(meta.getChildMetas()));
            }

            if(metaStack.size() == 0){
                metaStack.push(meta);
            }else{
                Metacharacter preMeta = metaStack.top();
                if(priority(preMeta) < priority(meta)){
                    preMeta = metaStack.pop();
                    metaStack.push(meta);
                    metaStack.push(preMeta);
                }else{
                    metaStack.push(meta);
                }
            }
        }

        List<Metacharacter> postfixMetas = new ArrayList<>();
        while(metaStack.top() != null){
            postfixMetas.add(metaStack.pop());
        }
        Collections.reverse(postfixMetas);

        // 经过上述
        return postfixMetas;
    }

    public static List<Metacharacter> postfix(String pattern){
        List<Metacharacter> originMetas = compile(pattern);
        List<Metacharacter> postfixMetas = postfix(originMetas);
        return postfixMetas;
    }

    /**
     * 匹配圆括号，处理成集合
     * @param metaStack
     * @param left
     * @return
     */
    private static Metacharacter matchParenthesis(MyStack<Metacharacter> metaStack, Character left){

        MyStack<Metacharacter> temp = new MyStack<>();
        while(metaStack.top() != null
                && (metaStack.top().isMetaList() || !metaStack.top().getMeta().equals(String.valueOf(left)))){
            temp.push(metaStack.pop());
        }

        if(temp.size() > 0) {
            List<Metacharacter> childMetas = new ArrayList<>();
            while (temp.top() != null) {
                childMetas.add(temp.pop());
            }
            return new Metacharacter(LexConstants.METE_LIST, childMetas, true);
        }else{
            return null;
        }

    }

    /**
     * 识别元字符，直接识别成字符串
     * @param inputStack
     * @param left
     * @return
     */
    public static Metacharacter matchBracket(MyStack<Character> inputStack, Character left){
        MyStack<Character> temp = new MyStack<>();
        while(inputStack.top() != left){
            temp.push(inputStack.pop());
        }
        temp.push(inputStack.pop());

        if(temp.size() > 0) {
            StringBuilder str = new StringBuilder();
            while (temp.top() != null) {
                str.append(temp.pop());
            }
            return new Metacharacter(str.toString(), true);
        }else{
            return null;
        }

    }


    /*
     优先级表：
          \0	(	*	|	+   ?	)
     priority  0	    1	7	5	7   7	8
     icp  0	    8	6	4	6   6	1
    */
    // 优先级 in stack priority
    public static int priority(Metacharacter meta) {
        String pattern = meta.getMeta();
        switch (pattern) {
            case LexConstants.EOF_STR:
                return 0;
            case LexConstants.METE_LIST:
            case "*":
            case "+":
            case "?":
                return 7;
            case "|":
                return 5;
            default:
                return 100;
        }
    }
    // 优先级 in coming priority
    public static int icp(Metacharacter meta) {
        String pattern = meta.getMeta();
        switch (pattern) {
            case LexConstants.EOF_STR:
                return 0;
            case "*":
            case "+":
            case "?":
                return 6;
            case "|":
                return 4;
            default:
                return 100;
        }
    }

    public static class Metacharacter{

        private String meta;
        private boolean isLetter;
        private boolean isMetaList;
        private List<Metacharacter> childMetas = new ArrayList<>();

        public Metacharacter(){

        }

        public Metacharacter(String pattern, boolean isLetter) {
            this.meta = pattern;
            this.isLetter = isLetter;
        }

        public Metacharacter(String pattern, List<Metacharacter> childMetas, boolean isLetter){
            this.meta = pattern;
            this.childMetas = childMetas;
            this.isLetter = isLetter;
            this.isMetaList = true;
        }

        public boolean match(String target){
            return Pattern.matches(meta, target);
        }

        public String getMeta() {
            return meta;
        }

        public void setMeta(String meta) {
            this.meta = meta;
        }

        public boolean isLetter() {
            return isLetter;
        }

        public void setLetter(boolean letter) {
            isLetter = letter;
        }

        public List<Metacharacter> getChildMetas() {
            return childMetas;
        }

        public void setChildMetas(List<Metacharacter> childMetas) {
            this.childMetas = childMetas;
        }

        public boolean isMetaList() {
            return isMetaList;
        }

        public void setMetaList(boolean metaList) {
            isMetaList = metaList;
        }
    }

}