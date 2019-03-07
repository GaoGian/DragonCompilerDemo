package gian.compiler.practice.lexical.transform;

/**
 * Created by Gian on 2019/1/27.
 */
public class LexConstants {

    public static final char EPSILON = 'ε';
    public static final char EOF = '\0';

    public static final String TRAN_UNABLE = "Φ";

    public static final String EOF_STR = "\\0";
    public static final String EPSILON_STR = "ε";
    public static final String METE_LIST = "∑";

    public static final String APPEND = "ο";

    // 正则表达式常用位置符
    public static final char UNITE = '|';
    public static final char START = '*';
    public static final char ONE_MORE = '+';
    public static final char ONE_LESS = '?';
    public static final String UNITE_STR = "|";
    public static final String START_STR = "*";
    public static final String ONE_MORE_STR = "+";
    public static final String ONE_LESS_STR = "?";

    public static final String SYNTAX_EMPTY = "ε";
    public static final String SYNTAX_END = "$";

    // 终结符：正则表达式词法单元标志
    public static final String REGEX_TOKEN_TAG_START = "◀";
    public static final String REGEX_TOKEN_TAG_END = "▶";

    // 增广文法起始符号
    public static final String AUGMENT_SYNTAX_TAG = "^";
    public static final String AUGMENT_SYNTAX_INDEX_TAG = "·";

    // LR
    public static final String SYNTAX_LR_ACTION = "ACTION";
    public static final String SYNTAX_LR_GOTO = "GOTO";

    public static final String SYNTAX_LR_ACTION_TYPE = "ACTION_TYPE";
    public static final String SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION = "NEXT_ITEMCOLLECTION";

    public static final String SYNTAX_LR_ACTION_SHIFT = "ACTION_SHIFT";
    public static final String SYNTAX_LR_ACTION_REDUCE = "ACTION_REDUCE";
    public static final String SYNTAX_LR_ACTION_ACCEPT = "ACTION_ACCEPT";
    public static final String SYNTAX_LR_ACTION_ERROR = "ACTION_ERROR";

    public static final String SYNTAX_LR_ACTION_GOTO = "ACTION_GOTO";


}
