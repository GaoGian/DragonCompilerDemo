package gian.compiler.language.simplejava.main;

import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.SyntacticLRParser;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.front.syntaxDirected.SyntaxDirectedParser;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.action.*;
import gian.compiler.language.simplejava.ast.AstNode;
import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.bean.Clazz;
import gian.compiler.language.simplejava.bean.ClazzConstructor;
import gian.compiler.language.simplejava.bean.ClazzField;
import gian.compiler.language.simplejava.bean.ClazzMethod;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Gian on 2019/4/7.
 */
public class SimpleJavaMain {

    public static void main(String[] args){
        changeOutput(false);

        // 解析目标程序分析语法树
        SyntaxTree syntaxTree = SyntacticLRParser.syntaxParseLR("SimpleJavaLexical.txt", "SimpleJavaSyntax.txt", "SimpleJavaProgram.txt", true);

        // 加载语义动作监听器
        List<SyntaxDirectedListener> simpleJavaDirectListeners = getAllJavaListener();

        // 匹配监听器，生成AST
        SyntaxDirectedParser.syntaxDirectedParser(syntaxTree, simpleJavaDirectListeners);

        // TODO 获取抽象语法树
        Map<String, Clazz> clazzMap = (Map<String, Clazz>) syntaxTree.getSyntaxTreeRoot().getSynProperty(JavaConstants.CLAZZ_MAP);
        for(String className : clazzMap.keySet()){
            Clazz clazz = clazzMap.get(className);
            ExecuteClazzAstDirect(clazz);
        }

    }

    // 根据抽象语法树执行语义动作，生成中间码
    public static void ExecuteClazzAstDirect(Clazz clazz){
        String packageName = clazz.getPackageName();
        List<String> importList = clazz.getImportList();
        Map<String, String> importMap = clazz.getImportMap();
        String clazzPermission = clazz.getPermission();
        String clazzName = clazz.getClazzName();
        String superType = clazz.getExtendInfo();

        List<ClazzField> fieldList = clazz.getFieldList();
        List<ClazzConstructor> constructorList = clazz.getConstructorList();
        List<ClazzMethod> methodList = clazz.getMethodList();

        // 输出类信息
        System.out.println("package " + packageName);
        for(String importStr : importList){
            System.out.println("import " + importStr);
        }
        System.out.println(clazzPermission + " class " + clazzName + (superType != null ? " extends " + superType : "") + "{");

        for(ClazzField clazzField  : fieldList){
            System.out.println(clazzField.getPermission() + " " + clazzField.getDeclType().toString() + " " + clazzField.getName());
        }

        for(ClazzConstructor constructor : constructorList){
            System.out.println(constructor.getPermission() + " " + constructor.getConstructorName() + "(" + constructor.getParamList().toString() + ")");
            // 初始化构造方法时需要初始化实例变量
            // TODO 实例变量初始化需要放在super方法之后，需要将该结构在生成时挂靠到构造节点上
            for(ClazzField clazzField : fieldList){
                Stmt clazzFieldCode = clazzField.getCode();
                if(clazzFieldCode != null){
                    clazzFieldCode.gen();
                }
            }

            Stmt constructorCode = constructor.getCode();
            if(constructorCode != null) {
                constructorCode.gen();
            }
        }

        // FIXME 需要区分初始化声明assign和变量assign
        // FIXME 函数执行前需要将方法参数引入到局部变量中

        for(ClazzMethod method : methodList){
            System.out.println(method.getPermission() + " " + method.getReturnType().toString() + " " + method.getMethodName() + "(" + method.getParamList().toString() + ")");

            Stmt methodCode = method.getCode();
            if(methodCode != null){
                methodCode.gen();
            }
        }

        System.out.println("}");

    }

    public static List<SyntaxDirectedListener> getAllJavaListener(){
        List<SyntaxDirectedListener> simpleJavaDirectListeners = new ArrayList<>();
        simpleJavaDirectListeners.addAll(JavaLanguageDirectAction.getAllListener());
        simpleJavaDirectListeners.addAll(PackageDeclarationDirectAction.getAllListener());
        simpleJavaDirectListeners.addAll(ImportDeclarationAction.getAllListener());
        simpleJavaDirectListeners.addAll(ClassDeclarationAction.getAllListener());
        simpleJavaDirectListeners.addAll(ClassBodyAction.getAllListener());
        simpleJavaDirectListeners.addAll(ClassBodyDeclarationAction.getAllListener());
        simpleJavaDirectListeners.addAll(FieldDeclarationAction.getAllListener());
        simpleJavaDirectListeners.addAll(TypeDeclarationAction.getAllListener());
        simpleJavaDirectListeners.addAll(ModifierDeclarationAction.getAllListener());
        simpleJavaDirectListeners.addAll(VariableInitializerAction.getAllListener());
        simpleJavaDirectListeners.addAll(ConstructorBodyAction.getAllListener());
        simpleJavaDirectListeners.addAll(ConstructorDeclarationAction.getAllListener());
        simpleJavaDirectListeners.addAll(MethodDeclarationAction.getAllListener());
        simpleJavaDirectListeners.addAll(FormalParametersAction.getAllListener());
        simpleJavaDirectListeners.addAll(BlockAction.getAllListener());
        simpleJavaDirectListeners.addAll(LocalVariableDeclarationStatementAction.getAllListener());
        simpleJavaDirectListeners.addAll(StatementAction.getAllListener());
        simpleJavaDirectListeners.addAll(ParExpressionAction.getAllListener());
        simpleJavaDirectListeners.addAll(ExpressionAction.getAllListener());
        simpleJavaDirectListeners.addAll(TermAction.getAllListener());
        simpleJavaDirectListeners.addAll(FactorAction.getAllListener());

        return simpleJavaDirectListeners;
    }

    public static void changeOutput(boolean change){
        if(change) {
            PrintStream print = null;
            try {
                print = new PrintStream("D:\\SimpleJavaMainInfo.log");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.setOut(print);
        }
    }

}
