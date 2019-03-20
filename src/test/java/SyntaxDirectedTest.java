import gian.compiler.practice.lexical.parser.LexExpression;
import gian.compiler.practice.lexical.parser.Token;
import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.syntactic.SyntacticLLParser;
import gian.compiler.practice.syntactic.SyntacticLRParser;
import gian.compiler.practice.syntactic.element.ItemCollection;
import gian.compiler.practice.syntactic.element.SyntaxSymbol;
import gian.compiler.practice.syntactic.element.SyntaxTree;
import lex.test.LexUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by gaojian on 2019/3/20.
 */
public class SyntaxDirectedTest {

    @Test
    public void testSyntaxTree(){
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("id", LexExpression.TokenType.ID));
        tokens.add(new Token("*", LexExpression.TokenType.OPERATOR));
        tokens.add(new Token("id", LexExpression.TokenType.ID));
        tokens.add(new Token("+", LexExpression.TokenType.OPERATOR));
        tokens.add(new Token("id", LexExpression.TokenType.ID));
        tokens.add(new Token(LexConstants.SYNTAX_END, LexExpression.TokenType.END));

        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        List<SyntaxSymbol> syntaxSymbols = SyntacticLLParser.parseSyntaxSymbol(syntaxs);

        System.out.println("-------------------------------LR ItemCollectionNode----------------------------------");
        Map<Integer, ItemCollection> allLRItemCollectionMap = SyntacticLRParser.getLRItemCollectionMap(syntaxSymbols);

        System.out.println("-------------------------------Create LR PredictMap----------------------------------");
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);
        Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictLRMap = SyntacticLRParser.predictLRMap(allLRItemCollectionMap.get(0), syntaxSymbols, syntaxFirstMap, syntaxFollowMap);

        System.out.println("------------------------------LRPredictMap----------------------------------");
        SyntaxTree syntaxTree = SyntacticLRParser.syntaxParseLR(allLRItemCollectionMap.get(0), tokens, predictLRMap);

        System.out.println("------------------------------SyntaxTree----------------------------------");
        LexUtils.outputUniversalTreeEchart(new LexUtils.UniversalTreeNode(syntaxTree.getSyntaxTreeRoot(), new LexUtils.UniversalTreeNode.UniversalTreeNodeMatch<SyntaxTree.SyntaxTreeNode>(){
            @Override
            public List<SyntaxTree.SyntaxTreeNode> getChildTreeNode(SyntaxTree.SyntaxTreeNode targetNode) {
                return targetNode.getSubProductNodeList();
            }
        }));

    }


}