import gian.compiler.front.lexical.parser.LexExpression;
import gian.compiler.front.lexical.transform.LexConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/31.
 */
public class TestLexExpression {


    public static LexExpression.TokenType KEYWORD = new LexExpression.TokenType("keyword");
    public static LexExpression.TokenType TYPE = new LexExpression.TokenType("type");
    public static LexExpression.TokenType ID = new LexExpression.TokenType("id", true);
    public static LexExpression.TokenType NUMBER = new LexExpression.TokenType("number", true);
    public static LexExpression.TokenType DIGIT = new LexExpression.TokenType("digit", true);
    public static LexExpression.TokenType OPERATOR = new LexExpression.TokenType("operator");
    public static LexExpression.TokenType SEPARATOR = new LexExpression.TokenType("separator");
    public static LexExpression.TokenType PUNCTUATION = new LexExpression.TokenType("punctuation");
    public static LexExpression.TokenType SECTION = new LexExpression.TokenType("section");
    public static LexExpression.TokenType END = new LexExpression.TokenType(LexConstants.SYNTAX_END);

    public static List<LexExpression.Expression> expressions = new ArrayList<>();

    static {

        // 数据类型
        expressions.add(new LexExpression.Expression("int", TYPE, false));
        expressions.add(new LexExpression.Expression("float", TYPE, false));
        expressions.add(new LexExpression.Expression("string", TYPE, false));
        expressions.add(new LexExpression.Expression("boolean", TYPE, false));

        // 关键词
        expressions.add(new LexExpression.Expression("if", KEYWORD, false));
        expressions.add(new LexExpression.Expression("else", KEYWORD, false));
        expressions.add(new LexExpression.Expression("while", KEYWORD, false));
        expressions.add(new LexExpression.Expression("do", KEYWORD, false));
        expressions.add(new LexExpression.Expression("break", KEYWORD, false));
        expressions.add(new LexExpression.Expression("true", KEYWORD, false));
        expressions.add(new LexExpression.Expression("false", KEYWORD, false));

        expressions.add(new LexExpression.Expression("package", KEYWORD, false));
        expressions.add(new LexExpression.Expression("import", KEYWORD, false));
        expressions.add(new LexExpression.Expression("public", KEYWORD, false));
        expressions.add(new LexExpression.Expression("class", KEYWORD, false));
        expressions.add(new LexExpression.Expression("void", KEYWORD, false));

        // 标识符
        expressions.add(new LexExpression.Expression("[A-Za-z]\\w*", ID, false));

        // 数字
        expressions.add(new LexExpression.Expression("\\d+", DIGIT, false));
        expressions.add(new LexExpression.Expression("\\d+(\\.\\d+)?", NUMBER, false));

        // 操作符
        expressions.add(new LexExpression.Expression(">", OPERATOR, false));
        expressions.add(new LexExpression.Expression("<", OPERATOR, false));
        expressions.add(new LexExpression.Expression("=", OPERATOR, false));
        expressions.add(new LexExpression.Expression(">=", OPERATOR, false));
        expressions.add(new LexExpression.Expression(">=", OPERATOR, false));
        expressions.add(new LexExpression.Expression("==", OPERATOR, false));
        expressions.add(new LexExpression.Expression("!=", OPERATOR, false));
        expressions.add(new LexExpression.Expression("&&", OPERATOR, false));
        expressions.add(new LexExpression.Expression("\\|\\|", OPERATOR, false));
        expressions.add(new LexExpression.Expression("\\+", OPERATOR, false));
        expressions.add(new LexExpression.Expression("-", OPERATOR, false));
        expressions.add(new LexExpression.Expression("\\*", OPERATOR, false));
        expressions.add(new LexExpression.Expression("/", OPERATOR, false));
        expressions.add(new LexExpression.Expression("\\+\\+", OPERATOR, false));
        expressions.add(new LexExpression.Expression("--", OPERATOR, false));

        // 界限符
        expressions.add(new LexExpression.Expression("\\{", SEPARATOR, false));
        expressions.add(new LexExpression.Expression("\\}", SEPARATOR, false));
        expressions.add(new LexExpression.Expression("\\(", SEPARATOR, false));
        expressions.add(new LexExpression.Expression("\\)", SEPARATOR, false));
        expressions.add(new LexExpression.Expression("\\[", SEPARATOR, false));
        expressions.add(new LexExpression.Expression("\\]", SEPARATOR, false));

        // 标点符号
        expressions.add(new LexExpression.Expression(",", PUNCTUATION, false));
        expressions.add(new LexExpression.Expression(";", PUNCTUATION, false));

        // 空格符
        expressions.add(new LexExpression.Expression("\\s+", SECTION, true));

        // 结束符
        expressions.add(new LexExpression.Expression(LexConstants.SYNTAX_END, END, true));

    }

}