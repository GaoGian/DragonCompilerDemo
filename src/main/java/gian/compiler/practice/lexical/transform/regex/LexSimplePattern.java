package gian.compiler.practice.lexical.transform.regex;

import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.lexical.transform.MyStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * TODO 测试使用，只用于简单语句
 * 将正则表达式拆分成不同的子部分，方便转换成逆波兰
 * Created by gaojian on 2019/1/31.
 */
public class LexSimplePattern {

    public static Map<Character, Character> bracketsMap = new HashMap();

    static {
        bracketsMap.put('}', '{');
        bracketsMap.put(')', '(');
        bracketsMap.put(']', '[');
    }

    public static List<Metacharacter> compile(String pattern){

        List<Metacharacter> metaList = new ArrayList<>();

        MyStack<Character> inputStack = new MyStack<>();

        char[] chars = pattern.toCharArray();
        for(int i=0; i<chars.length; i++){
            char input = chars[i];
            switch (input) {
                case '|':
                case '*':
                case '+':
                case '?':{
                    metaList.add(new Metacharacter(String.valueOf(input), false));
                    break;
                }
                case '(':{
                    inputStack.push(input);
                    metaList.add(new Metacharacter(String.valueOf(input), false));
                    break;
                }
                case ')':{
                    Metacharacter meta = matchBrackets(inputStack, bracketsMap.get(input), input, false);
                    if(meta != null) {
                        metaList.add(meta);
                    }
                    metaList.add(new Metacharacter(String.valueOf(input), false));
                    break;
                }
                case '[':{
                    inputStack.push(input);
                    break;
                }
                case ']':{
                    metaList.add(matchBrackets(inputStack, bracketsMap.get(input), input, true));
                    break;
                }
                default:{
                    inputStack.push(input);
                    break;
                }

            }

        }

        return metaList;
    }

    public static List<Metacharacter> postfix(String pattern){
        List<Metacharacter> originMetas = compile(pattern);
        List<Metacharacter> postfixMetas = postfix(originMetas);
        return postfixMetas;
    }

    public static List<Metacharacter> postfix(List<Metacharacter> originMetas){
        MyStack<Metacharacter> stack = new MyStack<>();
        Metacharacter meta = new Metacharacter(String.valueOf(LexConstants.EOF), false), preMeta, op;
        stack.push(meta);

        //读一个字符
        List<Metacharacter> postfixMetas = new ArrayList<>();
        int read_location = 0;
        meta = originMetas.get(read_location++);
        while (!stack.empty()) {
            if (meta.isLetter()) {
                postfixMetas.add(meta);
                //cout<<ch;
                meta = originMetas.get(read_location++);
            } else {
                //cout<<"输出操作符："<<ch<<endl;
                preMeta = stack.top();
                if (isp(preMeta.getMeta().charAt(0)) < icp(meta.getMeta().charAt(0))) {
                    stack.push(meta);
                    //cout<<"压栈"<<ch<<"  读取下一个"<<endl;
                    meta = originMetas.get(read_location++);
                } else if (isp(preMeta.getMeta().charAt(0)) > icp(meta.getMeta().charAt(0))) {
                    op = stack.top();
                    stack.pop();
                    //cout<<"退栈"<<op<<" 添加到输出字符串"<<endl;
                    postfixMetas.add(op);
                    //cout<<op;
                } else {
                    op = stack.top();
                    stack.pop();
                    //cout<<"退栈"<<op<<"  但不添加到输入字符串"<<endl;

                    if (op.getMeta().charAt(0) == '(') {
                        meta = originMetas.get(read_location++);
                    }
                }
            }
        }

        return postfixMetas;
    }

    private static Metacharacter matchBrackets(MyStack<Character> stack, Character left, Character right, boolean appendBrackets){
        if(appendBrackets) {
            stack.push(right);
        }

        MyStack<Character> temp = new MyStack<>();
        while(stack.top() != left){
            temp.push(stack.pop());
        }

        if(appendBrackets) {
            temp.push(stack.pop());
        }

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
     isp  0	    1	7	5	7   7	8
     icp  0	    8	6	4	6   6	1
    */
    // 优先级 in stack priority
    public static int isp(char c) {

        switch (c) {
            case LexConstants.EOF:
                return -1;
            case '(':
                return 1;
            case '*':
            case '+':
            case '?':
                return 7;
            case '|':
                return 5;
            case ')':
                return 8;
        }
        //若走到这一步，说明出错了
        System.out.println("isp优先级匹配错误");
        throw new RuntimeException("优先级匹配错误");
    }

    // 优先级 in coming priority
    public static int icp(char c) {
        switch (c) {
            case LexConstants.EOF:
                return -1;
            case '(':
                return 8;
            case '*':
            case '+':
            case '?':
                return 6;
            case '|':
                return 4;
            case ')':
                return 1;
        }
        //若走到这一步，说明出错了
        System.out.println("icp优先级匹配错误");
        throw new RuntimeException("icp优先级匹配错误");
    }

    public static class Metacharacter{

        private String meta;
        private boolean isLetter;

        public Metacharacter(String pattern, boolean isLetter) {
            this.meta = pattern;
            this.isLetter = isLetter;
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
    }

}