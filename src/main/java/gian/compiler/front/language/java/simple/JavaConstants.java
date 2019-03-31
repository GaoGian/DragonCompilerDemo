package gian.compiler.front.language.java.simple;

import gian.compiler.front.language.java.simple.bean.VariableType;

/**
 * Created by Gian on 2019/3/27.
 */
public class JavaConstants {

    public static String PACKAGE_NAME = "packageName";
    public static String CLAZZ_MAP = "clazzMap";
    public static String IMPORT_LIST = "importList";
    public static String IMPORT_MAP = "importMap";
    public static String MODIFIER = "modifier";
    public static String EXTEND_INFO = "extendInfo";
    public static String CLAZZ_NAME = "extendClazzName";
    public static String FIELD_LIST = "fieldList";
    public static String FIELD_NAME = "fieldName";
    // 变量基本类型
    public static String VARIABLE_BASE_TYPE = "variableBaseType";
    // 变量类型
    public static String VARIABLE_TYPE = "variableType";
    // 方法返回类型
    public static String METHOD_RETURN_TYPE = "methodReturnType";
    public static String CONSTRUCTOR_LIST = "constructorList";
    public static String METHOD_LIST = "methodList";
    public static String PARAM_LIST = "paramList";
    public static String CODE = "code";
    public static String ENV = "env";
    // 中间代码行号
    public static String CODE_LABEL = "label";
    // 引用的code位置信息
    public static String REF_CODE_INDEX = "refCodeIndex";
    // 传递的参数变量名称
    public static String CALL_PARAM_LIST = "callParamList";

    public static String IMPORT_CLAZZ_ALL_NAME = "importClazzAllName";
    public static String VARIABLE_INIT_INFO = "variableInitInfo";
    // 当前解析类名称
    public static String CURRENT_CLAZZ_NAME = "currentClazzName";
    // 当前作用域
    public static String CURRENT_ENV = "currentEnv";
    // 类实例作用域，用于 this.xxx 查找变量
    public static String CLASS_ENV = "classEnv";
    // 类静态作用域，用于查找静态变量
    public static String CLASS_STATIC_ENG = "classStaticEnv";


    // 数据类型
    public static String VARIABLE_TYPE_INT = "int";
    public static String VARIABLE_TYPE_LONG = "long";
    public static String VARIABLE_TYPE_SHORT = "short";
    public static String VARIABLE_TYPE_FLOAT = "float";
    public static String VARIABLE_TYPE_DOUBLE = "double";
    public static String VARIABLE_TYPE_CHAR = "char";
    public static String VARIABLE_TYPE_BYTE = "byte";
    public static String VARIABLE_TYPE_BOOLEAN = "boolean";
    public static String VARIABLE_TYPE_VOID = "void";
    public static String VARIABLE_TYPE_CLAZZ = "clazz";     // 说明是class类型

    // 关键词
    public static String JAVA_KEYWORD_PACKAGE = "package";
    public static String JAVA_KEYWORD_IMPORT = "import";
    public static String JAVA_KEYWORD_CLASS = "class";
    public static String JAVA_KEYWORD_EXTENDS = "extends";
    public static String JAVA_KEYWORD_SUPER = "super";
    public static String JAVA_KEYWORD_THIS = "this";
    public static String JAVA_KEYWORD_NEW = "new";
    public static String JAVA_KEYWORD_TRUE = "true";
    public static String JAVA_KEYWORD_FALSE = "false";
    public static String JAVA_KEYWORD_PUBLIC = "public";
    public static String JAVA_KEYWORD_PROTECTED = "protected";
    public static String JAVA_KEYWORD_PRIVATE = "private";
    public static String JAVA_KEYWORD_STATIC = "static";
    public static String JAVA_KEYWORD_IF = "if";
    public static String JAVA_KEYWORD_ELSE = "else";
    public static String JAVA_KEYWORD_FOR = "for";
    public static String JAVA_KEYWORD_DO = "do";
    public static String JAVA_KEYWORD_WHILE = "while";
    public static String JAVA_KEYWORD_SWITCH = "switch";
    public static String JAVA_KEYWORD_RETURN = "return";
    public static String JAVA_KEYWORD_BREAK = "break";
    public static String JAVA_KEYWORD_CONTINUE = "continue";
    public static String JAVA_KEYWORD_CASE = "case";
    public static String JAVA_KEYWORD_DEFAULT = "default";

    // SimpleJava数据类型
    public static VariableType INT = new VariableType(JavaConstants.VARIABLE_TYPE_INT, null, false);
    public static VariableType LONG = new VariableType(JavaConstants.VARIABLE_TYPE_LONG, null, false);
    public static VariableType SHORT = new VariableType(JavaConstants.VARIABLE_TYPE_SHORT, null, false);
    public static VariableType FLOAT = new VariableType(JavaConstants.VARIABLE_TYPE_FLOAT, null, false);
    public static VariableType DOUBLE = new VariableType(JavaConstants.VARIABLE_TYPE_DOUBLE, null, false);
    public static VariableType CHAR = new VariableType(JavaConstants.VARIABLE_TYPE_CHAR, null, false);
    public static VariableType BYTE = new VariableType(JavaConstants.VARIABLE_TYPE_BYTE, null, false);
    public static VariableType BOOLEAN = new VariableType(JavaConstants.VARIABLE_TYPE_BOOLEAN, null, false);
    public static VariableType CLAZZ = new VariableType(JavaConstants.VARIABLE_TYPE_CLAZZ, null, false);
    public static VariableType VOID = new VariableType(JavaConstants.VARIABLE_TYPE_VOID, true);

}
