package gian.compiler.practice.lexical.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tingyun on 2018/7/20.
 */
public class LexExpression {

    public static List<Expression> expressions = new ArrayList<>();

    static {
        // 关键词
        expressions.add(new Expression("if", ExpressionType.KEYWORD));
        expressions.add(new Expression("while", ExpressionType.KEYWORD));
        expressions.add(new Expression("do", ExpressionType.KEYWORD));
        expressions.add(new Expression("break", ExpressionType.KEYWORD));
        expressions.add(new Expression("true", ExpressionType.KEYWORD));
        expressions.add(new Expression("false", ExpressionType.KEYWORD));

        // 标识符
        expressions.add(new Expression("[A-Za-z]\\w*", ExpressionType.ID));

        // 数字
        expressions.add(new Expression("\\d+(\\.\\d+)?", ExpressionType.NUMBER));

        // 操作符
        expressions.add(new Expression(">", ExpressionType.OPERATOR));
        expressions.add(new Expression("<", ExpressionType.OPERATOR));
        expressions.add(new Expression("=", ExpressionType.OPERATOR));
        expressions.add(new Expression(">=", ExpressionType.OPERATOR));
        expressions.add(new Expression(">=", ExpressionType.OPERATOR));
        expressions.add(new Expression("==", ExpressionType.OPERATOR));
        expressions.add(new Expression("!=", ExpressionType.OPERATOR));
        expressions.add(new Expression("&&", ExpressionType.OPERATOR));
        expressions.add(new Expression("||", ExpressionType.OPERATOR));

        // 分隔符
        expressions.add(new Expression("{", ExpressionType.SEPARATOR));
        expressions.add(new Expression("}", ExpressionType.SEPARATOR));
        expressions.add(new Expression("(", ExpressionType.SEPARATOR));
        expressions.add(new Expression(")", ExpressionType.SEPARATOR));
        expressions.add(new Expression("[", ExpressionType.SEPARATOR));
        expressions.add(new Expression("]", ExpressionType.SEPARATOR));

        // 标点符号
        expressions.add(new Expression(",", ExpressionType.PUNCTUATION));
        expressions.add(new Expression(";", ExpressionType.PUNCTUATION));

        // 分段符
        expressions.add(new Expression("\\s", ExpressionType.SECTION));

    }

    public static enum ExpressionType{
        ID, KEYWORD, NUMBER, OPERATOR, SEPARATOR, PUNCTUATION, SECTION
    }

    // 词法单元匹配的正则表达式
    public static class Expression {

        private String expression;
        private ExpressionType type;

        public Expression() {

        }

        public Expression(String expression, ExpressionType type) {
            this.expression = expression;
            this.type = type;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public ExpressionType getType() {
            return type;
        }

        public void setType(ExpressionType type) {
            this.type = type;
        }
    }
}
