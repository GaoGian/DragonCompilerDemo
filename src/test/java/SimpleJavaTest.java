import gian.compiler.front.lexical.parser.LexExpression;
import gian.compiler.front.lexical.parser.LexicalParser;
import gian.compiler.front.lexical.parser.Token;
import gian.compiler.front.syntactic.SyntacticLLParser;
import gian.compiler.front.syntactic.SyntacticLRParser;
import gian.compiler.front.syntactic.element.ItemCollection;
import gian.compiler.front.syntactic.element.SyntaxSymbol;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.front.syntaxDirected.SyntaxDirectedParser;
import gian.compiler.language.simplejava.main.SimpleJavaMain;
import gian.compiler.utils.ParseChartUtils;
import gian.compiler.utils.ParseUtils;
import lex.test.LexUtils;
import org.junit.Test;

import java.util.*;

/**
 * Created by gaojian on 2019/3/28.
 */
public class SimpleJavaTest {

    // 匹配语法分析树
    public static ParseChartUtils.UniversalTreeNode.UniversalTreeNodeMatcher getSimpleJavaDirectTreeMatcher(Map<Integer, SyntaxDirectedListener> syntaxDirectActionMap){
        return new ParseChartUtils.UniversalTreeNode.UniversalTreeNodeMatcher<SyntaxTree.SyntaxTreeNode>(){
            @Override
            public List<ParseChartUtils.UniversalTreeNode> getChildTreeNode(SyntaxTree.SyntaxTreeNode targetNode) {
                SyntaxDirectedListener syntaxDirectedListener = syntaxDirectActionMap.get(targetNode.getNumber());
                List<ParseChartUtils.UniversalTreeNode> matchSubTreeNodeList = new ArrayList<>();
                if(syntaxDirectedListener != null){
                    SyntaxTree.SyntaxTreeNode tempNode = new SyntaxTree.SyntaxTreeNode();
                    tempNode.setSyntaxSymbol(new SyntaxSymbol(syntaxDirectedListener.getClass().getSimpleName(), true));
                    matchSubTreeNodeList.add(new ParseChartUtils.UniversalTreeNode(tempNode, this, true));
                }
                for(SyntaxTree.SyntaxTreeNode childNode : targetNode.getSubProductNodeList()){
                    matchSubTreeNodeList.add(new ParseChartUtils.UniversalTreeNode(childNode, this, true));
                }
                return matchSubTreeNodeList;
            }
        };
    }

    @Test
    public void testSimpleJava(){
        // 读取词法文件
        List<LexExpression.Expression> expressions = ParseUtils.readExpressionFile(ParseUtils.getFile("SimpleJavaLexical.txt", true));
        // 解析目标语言文件生成词法单元数据
        List<Token> tokens = LexicalParser.parser(ParseUtils.getFile("SimpleJavaProgram.txt", true), expressions);
        System.out.println("-------------------------------SimpleJavaProgram Tokens----------------------------------");
        ParseChartUtils.outputToken(tokens);

        // 读取文法文件
        List<String> syntaxs = ParseUtils.getFile("SimpleJavaSyntax.txt", true);
        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        System.out.println("-------------------------------LR ItemCollectionNode----------------------------------");
        Map<Integer, ItemCollection> allLRItemCollectionMap = SyntacticLRParser.getLRItemCollectionMap(syntaxSymbols);
        // 显示LR0自动机
        ParseChartUtils.outputSyntaxEchart(allLRItemCollectionMap.get(0), 3600, -600, true);

        System.out.println("-------------------------------Create LR PredictMap----------------------------------");
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);
        Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictLRMap = SyntacticLRParser.predictLRMap(allLRItemCollectionMap.get(0), syntaxSymbols, syntaxFirstMap, syntaxFollowMap);
        // 显示SLR分析表
        ParseChartUtils.outputLRPredictMap(predictLRMap);

        System.out.println("------------------------------LRPredictMap----------------------------------");
        SyntaxTree syntaxTree = SyntacticLRParser.syntaxParseLR(allLRItemCollectionMap.get(0), tokens, predictLRMap);
        ParseChartUtils.outputUniversalTreeEchart(new ParseChartUtils.UniversalTreeNode(syntaxTree.getSyntaxTreeRoot(), SyntaxDirectedTest.getDirectTreeMatcher(), true), 300, 500);

    }

    @Test
    public void testSimpleJava1(){
        SyntaxTree syntaxTree = SyntacticLRParser.syntaxParseLR("SimpleJavaLexical.txt", "SimpleJavaSyntax.txt", "SimpleJavaProgram.txt", true);
        List<SyntaxDirectedListener> simpleJavaDirectListeners = SimpleJavaMain.getAllJavaListener();
        Map<Integer, SyntaxDirectedListener> syntaxDirectActionMap = SyntaxDirectedParser.matchSyntaxTreeNodeDirectAction(syntaxTree.getSyntaxTreeRoot(), 0, simpleJavaDirectListeners, new HashMap<>());

        ParseChartUtils.outputUniversalTreeEchart(new ParseChartUtils.UniversalTreeNode(syntaxTree.getSyntaxTreeRoot(), getSimpleJavaDirectTreeMatcher(syntaxDirectActionMap), true), 300, 500);

    }

}