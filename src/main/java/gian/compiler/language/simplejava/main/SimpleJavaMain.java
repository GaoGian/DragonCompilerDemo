package gian.compiler.language.simplejava.main;

import gian.compiler.front.syntactic.SyntacticLRParser;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.front.syntaxDirected.SyntaxDirectedParser;
import gian.compiler.language.simplejava.action.*;

import java.util.ArrayList;
import java.util.List;

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

    }

}
