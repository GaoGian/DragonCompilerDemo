package gian.compiler.language.simplejava.main;

import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.SyntacticLRParser;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.front.syntaxDirected.SyntaxDirectedParser;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.action.*;
import gian.compiler.language.simplejava.bean.Clazz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Gian on 2019/4/7.
 */
public class SimpleJavaMain {

    public static void main(String[] args){
        // 解析目标程序分析语法树
        SyntaxTree syntaxTree = SyntacticLRParser.syntaxParseLR("SimpleJavaLexical.txt", "SimpleJavaSyntax.txt", "SimpleJavaMath.txt", true);

        // 加载语义动作监听器
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

        // 生成语义式语法树
        SyntaxTree annotionSyntaxTree = SyntaxDirectedParser.syntaxDirectedParser(syntaxTree, simpleJavaDirectListeners);

        // TODO 获取抽象语法树
        Map<String, Clazz> clazzMap = (Map<String, Clazz>) annotionSyntaxTree.getSyntaxTreeRoot().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CLAZZ_MAP);
        for(String className : clazzMap.keySet()){
            Clazz clazz = clazzMap.get(className);
            ExecuteClazzAstDirect(clazz);
        }

    }

    // TODO 根据抽象语法树执行语义动作，生成中间码
    public static void ExecuteClazzAstDirect(Clazz clazz){

    }

}
