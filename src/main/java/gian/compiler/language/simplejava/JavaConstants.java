package gian.compiler.language.simplejava;

/**
 * Created by Gian on 2019/3/27.
 */
public class JavaConstants {

    public final static String PACKAGE_NAME = "packageName";
    public final static String CLAZZ_MAP = "clazzMap";
    public final static String IMPORT_LIST = "importList";
    public final static String IMPORT_MAP = "importMap";
    public final static String MODIFIER = "modifier";
    public final static String EXTEND_INFO = "extendInfo";
    public final static String CLAZZ_NAME = "extendClazzName";
    public final static String FIELD_LIST = "fieldList";
    public final static String FIELD_NAME = "fieldName";
    // 表达式变量
    public final static String VARIABLE = "variable";
    // 变量基本类型
    public final static String VARIABLE_BASE_TYPE = "variableBaseType";
    // 变量类型
    public final static String VARIABLE_TYPE = "variableType";
    // 方法返回类型
    public final static String METHOD_RETURN_TYPE = "methodReturnType";
    public final static String CONSTRUCTOR_LIST = "constructorList";
    public final static String METHOD_LIST = "methodList";
    public final static String PARAM_LIST = "paramList";
    public final static String CODE = "code";
    public final static String ENV = "env";
    // 引用的code位置信息
    public final static String REF_CODE_INDEX = "refCodeIndex";
    // 传递的参数变量名称
    public final static String CALL_PARAM_LIST = "callParamList";

    public final static String IMPORT_CLAZZ_ALL_NAME = "importClazzAllName";
    public final static String VARIABLE_INIT_INFO = "variableInitInfo";
    // 当前解析类名称
    public final static String CURRENT_CLAZZ_NAME = "currentClazzName";
    // 当前作用域
    public final static String CURRENT_ENV = "currentEnv";
    // 类实例作用域，用于 this.xxx 查找变量
    public final static String CLASS_ENV = "classEnv";
    // 类静态作用域，用于查找静态变量
    public final static String CLASS_STATIC_ENG = "classStaticEnv";

    //-------------------------------- 中间码 ---------------------------------//
    // 临时存储变量
    public final static String CODE_TEMP_STR = "t";
    // 中间代码行号
    public final static String CODE_LABEL = "label";
    // 数组标识
    public final static String ARRAY_TAG = "[]";


    // 数据类型
    public final static String VARIABLE_TYPE_INT = "int";
    public final static String VARIABLE_TYPE_LONG = "long";
    public final static String VARIABLE_TYPE_SHORT = "short";
    public final static String VARIABLE_TYPE_FLOAT = "float";
    public final static String VARIABLE_TYPE_DOUBLE = "double";
    public final static String VARIABLE_TYPE_CHAR = "char";
    public final static String VARIABLE_TYPE_BYTE = "byte";
    public final static String VARIABLE_TYPE_BOOLEAN = "boolean";
    public final static String VARIABLE_TYPE_VOID = "void";

    public final static String VARIABLE_TYPE_CLAZZ = "Identifier";     // 说明是class类型
    public final static String VARIABLE_TYPE_DIGIT = "Number";
    public final static String VARIABLE_TYPE_NUMBER = "Digit";
    public final static String VARIABLE_TYPE_STRING = "String";

    // 关键词
    public final static String JAVA_KEYWORD_PACKAGE = "package";
    public final static String JAVA_KEYWORD_IMPORT = "import";
    public final static String JAVA_KEYWORD_CLASS = "class";
    public final static String JAVA_KEYWORD_EXTENDS = "extends";
    public final static String JAVA_KEYWORD_SUPER = "super";
    public final static String JAVA_KEYWORD_THIS = "this";
    public final static String JAVA_KEYWORD_NEW = "new";
    public final static String JAVA_KEYWORD_TRUE = "true";
    public final static String JAVA_KEYWORD_FALSE = "false";
    public final static String JAVA_KEYWORD_PUBLIC = "public";
    public final static String JAVA_KEYWORD_PROTECTED = "protected";
    public final static String JAVA_KEYWORD_PRIVATE = "private";
    public final static String JAVA_KEYWORD_STATIC = "static";
    public final static String JAVA_KEYWORD_IF = "if";
    public final static String JAVA_KEYWORD_ELSE = "else";
    public final static String JAVA_KEYWORD_FOR = "for";
    public final static String JAVA_KEYWORD_DO = "do";
    public final static String JAVA_KEYWORD_WHILE = "while";
    public final static String JAVA_KEYWORD_SWITCH = "switch";
    public final static String JAVA_KEYWORD_RETURN = "return";
    public final static String JAVA_KEYWORD_BREAK = "break";
    public final static String JAVA_KEYWORD_CONTINUE = "continue";
    public final static String JAVA_KEYWORD_CASE = "case";
    public final static String JAVA_KEYWORD_DEFAULT = "default";

    // 运算符
    public final static String JAVA_OPERATOR_ASSIGN = "=";
    public final static String JAVA_OPERATOR_ADD = "+";
    public final static String JAVA_OPERATOR_REDUCE = "-";
    public final static String JAVA_OPERATOR_MULIT = "*";
    public final static String JAVA_OPERATOR_DIVIDE = "/";
    public final static String JAVA_OPERATOR_INC = "++";
    public final static String JAVA_OPERATOR_DEC = "--";
    public final static String JAVA_OPERATOR_EQ = "==";
    public final static String JAVA_OPERATOR_NE = "!=";
    public final static String JAVA_OPERATOR_GT = ">";
    public final static String JAVA_OPERATOR_LT = "<";
    public final static String JAVA_OPERATOR_GE = ">=";
    public final static String JAVA_OPERATOR_LE = "<=";
    public final static String JAVA_OPERATOR_OR = "||";
    public final static String JAVA_OPERATOR_JOIN = " &&";


}
