package gian.compiler.practice.lexical.transform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by gaojian on 2019/1/28.
 */
public class LexUtils {

    /**
     * 将正则表达式进行逆波兰转化（转后缀）
     * @param regular_expression
     * @return
     */
    public static String RNP(String regular_expression){

        // 添加“+”符号，方便转换成后缀表达式
        // TODO 需要能够识别正则表达式元字符，例如：[^a-z]，使用类分装元字符，使用分析树存储位置信息
        regular_expression = LexUtils.add_join_symbol(regular_expression);
        // 中缀转后缀        FIXME 方便计算机按照顺序识别正则表达式词法单元
        regular_expression = LexUtils.postfix(regular_expression);

        return regular_expression;
    }

    // 添加交操作符“+”，便于中缀转后缀表达式，例如 abb->a+b+b
    public static String add_join_symbol(String addStr) {
        int length = addStr.length();
        int return_string_length = 0;
        //转换后的表达式长度最多是原来的两倍
        char[] return_string = new char[2 * length];

        Character first = null;
        Character second = null;
        for (int i = 0; i < length - 1; i++) {
            first = addStr.charAt(i);
            second = addStr.charAt(i + 1);

            // TODO 需要优化
            return_string[return_string_length++] = first;
            // 若第二个是字母、第一个不是 '(' '|' 都要添加
            if (first != '(' && first != '|' && isLetter(second)) {
                return_string[return_string_length++] = '+';
            }
            // 若第二个是'(', 第一个不是'|'、'(' 也要添加
            else if (second == '(' && first != '|' && first != '(') {
                return_string[return_string_length++] = '+';
            }

        }

        // 将最后一个字符写入
        return_string[return_string_length++] = second;
        return_string[return_string_length] = '\0';

        return_string = Arrays.copyOf(return_string, return_string_length);

        String dealStr = new String(return_string);
        System.out.println("加'+'后的表达式：" + dealStr);
        return dealStr;

    }

    // 中缀转后缀
    // TODO 解析出语法分析树
    public static String postfix(String expression) {
        //设定e的最后一个符号式“ε”，而其“ε”一开始先放在栈s的栈底
        expression = expression + LexConstants.EPSILON;

        MyStack<Character> stack = new MyStack<Character>();
        char ch = LexConstants.EPSILON, ch1, op;
        stack.push(ch);
        //读一个字符
        String out_string = "";
        int read_location = 0;
        ch = expression.charAt(read_location++);
        while (!stack.empty()) {
            if (isLetter(ch)) {
                out_string = out_string + ch;
                //cout<<ch;
                ch = expression.charAt(read_location++);
            } else {
                //cout<<"输出操作符："<<ch<<endl;
                ch1 = stack.top();
                if (isp(ch1) < icp(ch)) {
                    stack.push(ch);
                    //cout<<"压栈"<<ch<<"  读取下一个"<<endl;
                    ch = expression.charAt(read_location++);
                } else if (isp(ch1) > icp(ch)) {
                    op = stack.top();
                    stack.pop();
                    //cout<<"退栈"<<op<<" 添加到输出字符串"<<endl;
                    out_string = out_string + op;
                    //cout<<op;
                } else {
                    op = stack.top();
                    stack.pop();
                    //cout<<"退栈"<<op<<"  但不添加到输入字符串"<<endl;

                    if (op == '(') {
                        ch = expression.charAt(read_location++);
                    }
                }
            }
        }
        //cout<<endl;
//        cout<<"后缀表达式："<<out_string<<endl;
        System.out.println("转化后的后缀表达式：" + out_string);
        return out_string;
    }

    // 检查输入的字符是否合适 () * | a~z A~Z
    boolean checkCharacter(String checkStr) {
        int length = checkStr.length();
        for (int i = 0; i < length; i++) {
            char check = checkStr.charAt(i);
            if (isLetter(check)) {
//                System.out.println("");
            } else if (check == '(' || check == ')' || check == '*' || check == '|') {
//                System.out.println("");
            } else {
                System.out.println("输入字符不合法");
                throw new RuntimeException("输入字符不合法");
            }
        }
        return true;
    }

    public static boolean isLetter(char check) {
        if (check >= 'a' && check <= 'z' || check >= 'A' && check <= 'Z' || check == LexConstants.EOF) {
            return true;
        }
        return false;
    }

    /*
     优先级表：
          ε	(	*	|	+	)
     isp  0	    1	7	5	3	8
     icp  0	    8	6	4	2	1
    */
    // 优先级 in stack priority
    public static int isp(char c) {

        switch (c) {
            case LexConstants.EOF:
                return -1;
            case LexConstants.EPSILON:
                return 0;
            case '(':
                return 1;
            case '*':
                return 7;
            case '|':
                return 5;
            case '+':
                return 3;
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
            case LexConstants.EPSILON:
                return 0;
            case '(':
                return 8;
            case '*':
                return 6;
            case '|':
                return 4;
            case '+':
                return 2;
            case ')':
                return 1;
        }
        //若走到这一步，说明出错了
        System.out.println("icp优先级匹配错误");
        throw new RuntimeException("icp优先级匹配错误");
    }

//--------------------------------------------------------------------------------------------------//
//-------------------------------- 打印树结构 ------------------------------------------------------//

    public static void print(DFATransformer.LexNode root) {
        // 找到左边的最大偏移量
        int maxLeftOffset = findMaxOffset(root, 0, true);
        int maxRightOffset = findMaxOffset(root, 0, false);
        int offset = Math.max(maxLeftOffset, maxRightOffset);
        // 计算最大偏移量
        Map<Integer, LexPrintLine> lineMap = new HashMap();
        calculateLines(root, offset, lineMap, 0, true);
        Iterator<Integer> lineNumbers = lineMap.keySet().iterator();
        int maxLine = 0;
        while (lineNumbers.hasNext()) {
            int lineNumber = lineNumbers.next();
            if (lineNumber > maxLine) {
                maxLine = lineNumber;
            }
        }
        for (int i = 0; i <= maxLine; i++) {
            LexPrintLine line = lineMap.get(i);
            if (line != null) {
                System.out.println(line.getLineString());
            }
        }

    }

    private static void calculateLines(DFATransformer.LexNode parent, int offset, Map<Integer, LexPrintLine> lineMap, int level,
                                       boolean right) {
        if (parent == null) {
            return;
        }
        int nameoffset = parent.toString().length() / 2;
        LexPrintLine line = lineMap.get(level);
        if (line == null) {
            line = new LexPrintLine();
            lineMap.put(level, line);
        }
        line.putString(right ? offset : (offset - nameoffset), parent.toString());
        // 判断有没有下一级
        if (parent.getLeft() == null && parent.getRight() == null) {
            return;
        }
        // 如果有，添加分割线即/\
        LexPrintLine separateLine = lineMap.get(level + 1);
        if (separateLine == null) {
            separateLine = new LexPrintLine();
            lineMap.put(level + 1, separateLine);
        }
        if (parent.getLeft() != null) {
            separateLine.putString(offset - 1, "/");
            calculateLines(parent.getLeft(), offset - nameoffset - 1, lineMap, level + 2, false);
        }
        if (parent.getRight() != null) {
            separateLine.putString(offset + nameoffset + 1, "\\");
            calculateLines(parent.getRight(), offset + nameoffset + 1, lineMap, level + 2, true);
        }

    }

    /**
     * 需要打印的某一行
     *
     * @author zhuguohui
     *
     */
    private static class PrintLine {
        /**
         * 记录了offset和String的map
         */
        Map<Integer, String> printItemsMap = new HashMap<>();
        int maxOffset = 0;

        public void putString(int offset, String info) {
            printItemsMap.put(offset, info);
            if (offset > maxOffset) {
                maxOffset = offset;
            }
        }

        public String getLineString() {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i <= maxOffset; i++) {
                String info = printItemsMap.get(i);
                if (info == null) {
                    buffer.append(" ");
                } else {
                    buffer.append(info);
                    i += info.length();
                }
            }
            return buffer.toString();
        }

    }

    private static int findMaxOffset(DFATransformer.LexNode parent, int offset, boolean findLeft) {
        if (parent != null) {
            offset += parent.toString().length();
        }
        if (findLeft && parent.getLeft() != null) {
            offset += 1;
            return findMaxOffset(parent.getLeft(), offset, findLeft);
        }
        if (!findLeft && parent.getRight() != null) {
            return findMaxOffset(parent.getRight(), offset, findLeft);
        }
        return offset;
    }

    /**
     * 需要打印的某一行
     *
     * @author zhuguohui
     *
     */
    static class LexPrintLine {
        /**
         * 记录了offset和String的map
         */
        Map<Integer, String> printItemsMap = new HashMap<>();
        int maxOffset = 0;

        public void putString(int offset, String info) {
            printItemsMap.put(offset, info);
            if (offset > maxOffset) {
                maxOffset = offset;
            }
        }

        public String getLineString() {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i <= maxOffset; i++) {
                String info = printItemsMap.get(i);
                if (info == null) {
                    buffer.append(" ");
                } else {
                    buffer.append(info);
                    i += info.length();
                }
            }
            return buffer.toString();
        }

    }

}