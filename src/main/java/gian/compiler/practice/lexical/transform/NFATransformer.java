package gian.compiler.practice.lexical.transform;

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
            if (LexUtils.isLetter(check)) {
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
        // 先将 left 和 right 的 edgeMap 复制到 newCell
        cell_EdgeSet_copy(newCell, left);
        cell_EdgeSet_copy(newCell, right);

        // 将新构建的四条边加入 edgeMap
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
        // 先将 cell 的 edgeMap 复制到 newCell
        cell_EdgeSet_copy(newCell, cell);

        // 将新构建的四条边加入 edgeMap
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
    static void displayNfa(Cell cell) {
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

    public static Cell regExp2Nfa(String regular_expression) {

        NFATransformer transformer = new NFATransformer();
        // 接收输入
        transformer.input(regular_expression);
        // 中缀转后缀
        regular_expression = LexUtils.RNP(regular_expression);
        // 表达式转NFA
        Cell NFA_Cell = transformer.express_2_NFA(regular_expression);
        // 显示
        displayNfa(NFA_Cell);

        return NFA_Cell;

    }

}