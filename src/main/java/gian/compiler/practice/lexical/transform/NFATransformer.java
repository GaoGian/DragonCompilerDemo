package gian.compiler.practice.lexical.transform;


import java.util.Arrays;
import java.util.List;

/**
 * Thompson算法，正则表达式转换成NFA
 * Created by gaojian on 2019/1/25.
 */
public class NFATransformer {

    int EDGE_NUM = 0;
    int STATE_NUM = 0;
    int CELL_NUM = 0;

    void input(String regularExpression) {
        if (!checkLegal(regularExpression)) {
            throw new RuntimeException("正则表达式不合法");
        }
    }

    boolean checkLegal(String checkStr) {
        if (checkCharacter(checkStr) && checkParentThesis(checkStr)) {
            return true;
        }
        return false;
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

    // 先检查括号是否匹配
    boolean checkParentThesis(String checkStr) {
        int length = checkStr.length();
        char[] check = checkStr.toCharArray();

        MyStack<Integer> stack = new MyStack<Integer>();
        for (int i = 0; i < length; i++) {
            if (check[i] == '(') {
                stack.push(i);
            } else if (check[i] == ')') {
                if (stack.empty()) {
                    // 暂时不记录位置信息
                    System.out.println("括号不匹配");
                    throw new RuntimeException("括号不匹配");
                } else {
                    stack.pop();
                }
            }
        }
        if (!stack.isEmpty()) {
            // 暂时不记录位置信息
            System.out.println("括号不匹配");
            throw new RuntimeException("括号不匹配");
        }

        return true;
    }

    boolean isLetter(char check) {
        if (check >= 'a' && check <= 'z' || check >= 'A' && check <= 'Z') {
            return true;
        }
        return false;
    }

    // 添加交操作符“+”，便于中缀转后缀表达式，例如 abb->a+b+b
    String add_join_symbol(String addStr) {
        int length = addStr.length();
        int return_string_length = 0;
        //转换后的表达式长度最多是原来的两倍
        char[] return_string = new char[2 * length];

        Character first = null;
        Character second = null;
        for (int i = 0; i < length - 1; i++) {
            first = addStr.charAt(i);
            second = addStr.charAt(i + 1);

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
    String postfix(String expression) {
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

                    if (op == '(')
                        ch = expression.charAt(read_location++);
                }
            }
        }
        //cout<<endl;
//        cout<<"后缀表达式："<<out_string<<endl;
        System.out.println("转化后的后缀表达式：" + out_string);
        return out_string;
    }

    /*
     优先级表：
          ε	(	*	|	+	)
     isp  0	1	7	5	3	8
     icp  0	8	6	4	2	1
    */
    // 优先级 in stack priority
    int isp(char c) {

        switch (c) {
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
    int icp(char c) {
        switch (c) {
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

    // 表达式转NFA
    Cell express_2_NFA(String express) {
        int length = express.length();
        char element;
        Cell cell, left, right;
        MyStack<Cell> stack = new MyStack<Cell>();

        for (int i = 0; i < length; i++) {
            element = express.charAt(i);
            switch (element) {
                case '|': {
                    right = stack.top();
                    stack.pop();
                    left = stack.top();
                    stack.pop();
                    cell = do_Unite(left, right);
                    stack.push(cell);
                    break;
                }
                case '*': {
                    left = stack.top();
                    stack.pop();
                    cell = do_Start(left);
                    stack.push(cell);
                    break;
                }
                case '+': {
                    right = stack.top();
                    stack.pop();
                    left = stack.top();
                    stack.pop();
                    cell = do_Join(left, right);
                    stack.push(cell);
                    break;
                }
                default: {
                    cell = do_Cell(element);
                    stack.push(cell);
                }
            }
        }

        System.out.println("处理完毕");

        cell = stack.top();
        stack.pop();

        return cell;

    }

    // 处理 a|b
    Cell do_Unite(Cell left, Cell right) {
        Cell newCell = new Cell();
        newCell.edgeCount = 0;

        Edge edge1 = new Edge();
        Edge edge2 = new Edge();
        Edge edge3 = new Edge();
        Edge edge4 = new Edge();

        // 获得新节点
        State startState = new_StateNode();
        State endState = new_StateNode();

        // 构建边
        edge1.startState = startState;
        edge1.endState = left.edgeSet[0].startState;
        edge1.transSymbol = LexConstants.EPSILON;

        edge2.startState = startState;
        edge2.endState = right.edgeSet[0].startState;
        edge2.transSymbol = LexConstants.EPSILON;

        edge3.startState = left.edgeSet[left.edgeCount - 1].endState;
        edge3.endState = endState;
        edge3.transSymbol = LexConstants.EPSILON;

        edge4.startState = right.edgeSet[right.edgeCount - 1].endState;
        edge4.endState = endState;
        edge4.transSymbol = LexConstants.EPSILON;

        // 构建单元
        // 先将 left 和 right 的 edgeSet 复制到 newCell
        cell_EdgeSet_copy(newCell, left);
        cell_EdgeSet_copy(newCell, right);

        // 将新构建的四条边加入 edgeSet
        newCell.edgeSet[newCell.edgeCount++] = edge1;
        newCell.edgeSet[newCell.edgeCount++] = edge2;
        newCell.edgeSet[newCell.edgeCount++] = edge3;
        newCell.edgeSet[newCell.edgeCount++] = edge4;

        // 构建 newCell 的起始状态和结束状态
        newCell.startState = startState;
        newCell.endState = endState;

        return newCell;

    }

    // 处理 ab
    Cell do_Join(Cell left, Cell right) {
        // 将 left 的结束状态和 right 的开始状态合并，将 right 的边复制给 left，将 left 返回
        // 将 right 中所有以 startState 开头的边全部修改
        for (int i = 0; i < right.edgeCount; i++) {
            // FIXME 对入边和出边分开处理
            if (right.edgeSet[i].startState.stateName.equals(right.startState.stateName)) {
                right.edgeSet[i].startState = left.endState;
                STATE_NUM--;
            } else if (right.edgeSet[i].endState.stateName.equals(right.startState.stateName)) {
                right.edgeSet[i].endState = left.endState;
                STATE_NUM--;
            }
        }
        right.startState = left.endState;

        cell_EdgeSet_copy(left, right);
        // 将 left 的结束状态更新为 right 的结束状态
        left.endState = right.endState;

        return left;

    }

    // 处理 a*
    Cell do_Start(Cell cell) {
        Cell newCell = new Cell();
        newCell.edgeCount = 0;

        Edge edge1 = new Edge();
        Edge edge2 = new Edge();
        Edge edge3 = new Edge();
        Edge edge4 = new Edge();

        // 获得新节点
        State startState = new_StateNode();
        State endState = new_StateNode();

        // 构建边
        edge1.startState = startState;
        edge1.endState = endState;
        edge1.transSymbol = LexConstants.EPSILON;

        edge2.startState = cell.endState;
        edge2.endState = cell.startState;
        edge2.transSymbol = LexConstants.EPSILON;

        edge3.startState = startState;
        edge3.endState = cell.startState;
        edge3.transSymbol = LexConstants.EPSILON;

        edge4.startState = cell.endState;
        edge4.endState = endState;
        edge4.transSymbol = LexConstants.EPSILON;

        // 构建单元
        // 先将 cell 的 edgeSet 复制到 newCell
        cell_EdgeSet_copy(newCell, cell);

        // 将新构建的四条边加入 edgeSet
        newCell.edgeSet[newCell.edgeCount++] = edge1;
        newCell.edgeSet[newCell.edgeCount++] = edge2;
        newCell.edgeSet[newCell.edgeCount++] = edge3;
        newCell.edgeSet[newCell.edgeCount++] = edge4;

        // 构建 newCell 的起始状态和结束状态
        newCell.startState = startState;
        newCell.endState = endState;

        return newCell;

    }

    // 处理 a
    Cell do_Cell(char element) {
        Cell newCell = new Cell();
        newCell.edgeCount = 0;

        // 获得新节点
        State startState = new_StateNode();
        State endState = new_StateNode();

        Edge newEdge = new Edge();
        newEdge.startState = startState;
        newEdge.endState = endState;
        newEdge.transSymbol = element;

        // 构建单元
        newCell.edgeSet[newCell.edgeCount++] = newEdge;
        newCell.startState = newCell.edgeSet[0].startState;
        newCell.endState = newCell.edgeSet[0].endState;     // TODO edgeCount 此时为 1

        return newCell;

    }

    // 将一个单元的边的集合复制到另一个单元
    void cell_EdgeSet_copy(Cell destination, Cell source) {
        int d_count = destination.edgeCount;
        int s_count = source.edgeCount;

        for (int i = 0; i < s_count; i++) {
            destination.edgeSet[d_count + i] = source.edgeSet[i];
        }

        destination.edgeCount = d_count + s_count;

    }

    // 产生一个新的节点状态，便于管理
    State new_StateNode() {
        State newState = new State();
//        newState.stateName = String.valueOf((char)(STATE_NUM + 65));
        newState.stateName = String.valueOf(STATE_NUM);
        STATE_NUM++;
        return newState;
    }

    // 显示
    void display(Cell cell) {
        System.out.println("NFA 的边数：" + cell.edgeCount);
        System.out.println("NFA 的起始状态：" + cell.startState.stateName);
        System.out.println("NFA 的结束状态：" + cell.endState.stateName);

        for (int i = 0; i < cell.edgeCount; i++) {
            System.out.println("第 " + i + 1 + " 条边的起始状态：" + cell.edgeSet[i].startState.stateName +
                    "，结束状态：" + cell.edgeSet[i].endState.stateName +
                    "，转换符：" + cell.edgeSet[i].transSymbol);
        }

        System.out.println("结束");
    }


    public static Cell transform(String regular_expression) {

        NFATransformer transformer = new NFATransformer();
        // 接收输入
        transformer.input(regular_expression);
        // 添加“+”符号，方便转换成后缀表达式       // TODO 需要能够识别正则表达式元字符，例如：[^a-z]
        regular_expression = transformer.add_join_symbol(regular_expression);
        // 中缀转后缀        FIXME 方便计算机按照顺序识别正则表达式词法单元
        regular_expression = transformer.postfix(regular_expression);
        // 表达式转NFA
        Cell NFA_Cell = transformer.express_2_NFA(regular_expression);
        // 显示
        transformer.display(NFA_Cell);

        return NFA_Cell;


    }


}