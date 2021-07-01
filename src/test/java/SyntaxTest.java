import gian.compiler.front.lexical.parser.LexicalParser;
import gian.compiler.front.lexical.parser.Token;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.SyntacticLRParser;
import gian.compiler.front.syntactic.SyntacticLLParser;
import gian.compiler.front.syntactic.element.*;
import utils.ParseChartUtils;
import gian.compiler.utils.ParseUtils;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Gian on 2019/2/19.
 */
public class SyntaxTest {

    @Test
    public void test(){
        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("stmt → if expr then stmt else stmt | if stmt then stmt | begin stmtList end");
        syntaxs.add("stmtList → stmt ; stmtList | stmt | ");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

    }

    @Test
    public void testEliminateLeftRecursion(){

        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T + F | F ");
        syntaxs.add("F → ( E ) | id ");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        // 消除前
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("----------------------------------------------------------------------");

        SyntacticLLParser.eliminateLeftRecursion(syntaxSymbols);

        // 消除后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }
    }

    @Test
    public void testMergeCommonFactor(){

        List<String> syntaxs = new ArrayList<>();

//        syntaxs.add("stmt → if expr then stmt else stmt | if expr then stmt");
        syntaxs.add("T → a * F | a * F - F");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        // 提取前
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("----------------------------------------------------------------------");

        SyntacticLLParser.mergeCommonFactor(syntaxSymbols);

        // 提取后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }
    }

    @Test
    public void testMergeCommonFactor2(){

        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("T → T * F | T * F - F ");
        syntaxs.add("F → id ");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        // 消除前
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("-----------------------------左递归-------------------------------------");

        SyntacticLLParser.eliminateLeftRecursion(syntaxSymbols);

        // 消除后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("-----------------------------公因式-------------------------------------");

        // TODO 不能正确处理提取公因式后的文法
        SyntacticLLParser.mergeCommonFactor(syntaxSymbols);

        // 提取公因式
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

    }

    @Test
    public void testSyntaxFirstAndFolloew(){

        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        // 消除前
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("----------------------------------------------------------------------");

        SyntacticLLParser.eliminateLeftRecursion(syntaxSymbols);

        // 提取后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("-------------------------------FIRST-----------------------------------");
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);

        for(SyntaxSymbol symbol : syntaxSymbols) {
            System.out.println(symbol.getSymbol() + " -------- " + SyntacticLLParser.getSyntaxFirst(symbol, syntaxFirstMap));
        }

        System.out.println("-------------------------------FOLLOW----------------------------------");

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> followMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);

        for(SyntaxSymbol syntaxSymbol : syntaxSymbols){
            System.out.println(syntaxSymbol.getSymbol() + " -------- " + SyntacticLLParser.getSyntaxFollow(syntaxSymbol, followMap));
        }

    }

    @Test
    public void testSyntaxPredict(){

        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        // TODO 不适用二义性文法
//        syntaxs.add("S → i E t S S' | a ");
//        syntaxs.add("S' → e S | ε ");
//        syntaxs.add("E → b ");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        SyntacticLLParser.eliminateLeftRecursion(syntaxSymbols);

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);

        System.out.println("-------------------------------预测分析表----------------------------------");

        Map<SyntaxSymbol, Map<String, Set<SyntaxProduct>>> syntaxPredictMap = SyntacticLLParser.syntaxPredictMap(syntaxFirstMap, syntaxFollowMap);

        ParseChartUtils.outputLL1SyntaxPredict(syntaxFirstMap, syntaxFollowMap, syntaxPredictMap);
    }

    @Test
    public void testSyntaxPredict2(){

        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("id", TestLexExpression.KEYWORD));
        tokens.add(new Token("+", TestLexExpression.OPERATOR));
        tokens.add(new Token("id", TestLexExpression.KEYWORD));
        tokens.add(new Token("*", TestLexExpression.OPERATOR));
        tokens.add(new Token("id", TestLexExpression.KEYWORD));
        tokens.add(new Token(LexConstants.SYNTAX_END, TestLexExpression.END));

        System.out.println("-------------------------------预测分析表----------------------------------");

        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        System.out.println("----------------------------消除左递归，提取公因式------------------------------");
        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);
        SyntacticLLParser.eliminateLeftRecursion(syntaxSymbols);
        // 提取后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);

        Map<SyntaxSymbol, Map<String, Set<SyntaxProduct>>> syntaxPredictMap = SyntacticLLParser.syntaxPredictMap(syntaxFirstMap, syntaxFollowMap);

        ParseChartUtils.outputLL1SyntaxPredict(syntaxFirstMap, syntaxFollowMap, syntaxPredictMap);

        System.out.println("-------------------------------LL(1)语法分析----------------------------------");

        SyntacticLLParser.syntaxParseByLL(tokens, syntaxSymbols.get(0), syntaxPredictMap);

    }

    @Test
    public void testSyntaxPredict3(){

        System.out.println("-------------------------------读取文法----------------------------------");
        // TODO 构造文法
        List<String> syntaxs = ParseUtils.getFile("syntaxContentFile.txt", true);
        // 消除前
        for(String line : syntaxs) {
            System.out.println(line);
        }

        System.out.println("----------------------------文法解析------------------------------");
        // TODO 修改终结符的识别方式，需要和词法规则联系起来
        // TODO 终结符有几类：关键字、符号、变量、值
        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);
        // 消除前
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("----------------------------消除左递归------------------------------");
        SyntacticLLParser.eliminateLeftRecursion(syntaxSymbols);
        // 提取后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("-----------------------------提取公因式-------------------------------------");
        SyntacticLLParser.mergeCommonFactor(syntaxSymbols);

        // 提取公因式
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("-------------------------------预测分析表----------------------------------");
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);

        Map<SyntaxSymbol, Map<String, Set<SyntaxProduct>>> syntaxPredictMap = SyntacticLLParser.syntaxPredictMap(syntaxFirstMap, syntaxFollowMap);

        ParseChartUtils.outputLL1SyntaxPredict(syntaxFirstMap, syntaxFollowMap, syntaxPredictMap);

        System.out.println("------------------------------解析测试代码----------------------------------");
        System.out.println("------------------------------原样显示代码----------------------------------");
        // TODO 文法中的终结符需要和 LexExpression 元字符进行匹配，然后在判断输入符是否是对应的LexExpression的符号（验证正则表达式是否匹配，已经TokenType是否一致）
        // TODO 需要区分关键字和ID类别的终结符
        /** 一般情况下词法分析是在文法分析过程中进行的，根据语义返回识别的符号，这里由于分开处理，所以需要进行额外的关联映射 **/
        List<Token> tokens = LexicalParser.parser(ParseUtils.getFile("compilerCode.txt", true), TestLexExpression.expressions);
        int line = 0;
        for(Token token : tokens){
            if(token.getLine() > line){
                line = token.getLine();
                System.out.println("");
            }
            System.out.print(token.toString());
        }
        System.out.println("");
        System.out.println("------------------------------显示详细信息----------------------------------");
        line = 0;
        for(Token token : tokens){
            if(token.getLine() > line){
                line = token.getLine();
                System.out.println("");
            }
            System.out.print(token.getToken() + " ");
        }
        System.out.println("");

        System.out.println("-------------------------------LL(1)语法分析----------------------------------");
        SyntacticLLParser.syntaxParseByLL(tokens, syntaxSymbols.get(0), syntaxPredictMap);

    }

    @Test
    public void testSyntaxPredict4(){
        SyntacticLLParser.syntaxParseByLL("syntaxContentFile.txt", "compilerCode.txt", TestLexExpression.expressions, true);
    }

    @Test
    public void testSyntaxCLOSURE(){
        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        System.out.println("-------------------------------startItemCollection----------------------------------");
        ItemCollection startItemCollection = SyntacticLRParser.getStartItemCollection(syntaxSymbols, 0);
        for(Item item : startItemCollection.getItemList()){
            System.out.println(item.toString());
        }

        System.out.println("-------------------------------startItemCollection CLOSURE----------------------------------");
        startItemCollection = SyntacticLRParser.closure(startItemCollection, 1, SyntacticLRParser.getSymbolProductMap(SyntacticLRParser.getSyntaxProducts(syntaxSymbols)));
        for(Item item : startItemCollection.getItemList()){
               System.out.println(item.toString());
        }

    }

    @Test
    public void testSyntaxGOTO(){

        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        System.out.println("-------------------------------startItemCollection----------------------------------");
        ItemCollection startItemCollection = SyntacticLRParser.getStartItemCollection(syntaxSymbols, 0);
        for(Item item : startItemCollection.getItemList()){
            System.out.println(item.toString());
        }

        System.out.println("-------------------------------ItemCollection GOTO 'E'----------------------------------");
        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap = SyntacticLRParser.getSymbolProductMap(SyntacticLRParser.getSyntaxProducts(syntaxSymbols));
        ItemCollection tempItemCollection = SyntacticLRParser.moveItem(startItemCollection, new SyntaxSymbol("E", false), 1, symbolProductMap);
        for(Item item : tempItemCollection.getItemList()){
            System.out.println(item.toString());
        }
        System.out.println("-------------------------------ItemCollection GOTO '+'----------------------------------");
        tempItemCollection = SyntacticLRParser.moveItem(tempItemCollection, new SyntaxSymbol("+", true), 2, symbolProductMap);
        for(Item item : tempItemCollection.getItemList()){
            System.out.println(item.toString());
        }

    }

    @Test
    public void itemCollectionNodeTest(){

        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        System.out.println("-------------------------------startItemCollection----------------------------------");
        AtomicInteger itemCollectionNo = new AtomicInteger(0);
        ItemCollection startItemCollection = SyntacticLRParser.getStartItemCollection(syntaxSymbols, itemCollectionNo.getAndIncrement());
        for(Item item : startItemCollection.getItemList()){
            System.out.println(item.toString());
        }

        System.out.println("-------------------------------ItemCollectionNode----------------------------------");
        List<SyntaxProduct> syntaxProducts = SyntacticLRParser.getSyntaxProducts(syntaxSymbols);
        Set<SyntaxSymbol> allGotoSymtaxSymbol = SyntacticLRParser.getAllGotoSymtaxSymbol(syntaxProducts);
        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap = SyntacticLRParser.getSymbolProductMap(syntaxProducts);
        Map<ItemCollection, ItemCollection> allItemCollectionMap = new LinkedHashMap<>();
        SyntacticLRParser.getLR0ItemCollectionNodes(startItemCollection.getItemList().get(0).getSyntaxProduct(), startItemCollection, allGotoSymtaxSymbol, symbolProductMap, itemCollectionNo, allItemCollectionMap);

        // 显示LR0自动机
        System.out.println("LR(O) 项集数量：" + allItemCollectionMap.size());
        ParseChartUtils.outputSyntaxEchart(startItemCollection);


    }

    @Test
    public void testLR0parse(){
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("id", TestLexExpression.ID));
        tokens.add(new Token("*", TestLexExpression.OPERATOR));
        tokens.add(new Token("id", TestLexExpression.ID));
        tokens.add(new Token("+", TestLexExpression.OPERATOR));
        tokens.add(new Token("id", TestLexExpression.ID));
        tokens.add(new Token(LexConstants.SYNTAX_END, TestLexExpression.END));


        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        System.out.println("-------------------------------startItemCollection----------------------------------");
        AtomicInteger itemCollectionNo = new AtomicInteger(0);
        ItemCollection startItemCollection = SyntacticLRParser.getStartItemCollection(syntaxSymbols, itemCollectionNo.getAndIncrement());
        for(Item item : startItemCollection.getItemList()){
            System.out.println(item.toString());
        }

        System.out.println("-------------------------------ItemCollectionNode----------------------------------");
        List<SyntaxProduct> syntaxProducts = SyntacticLRParser.getSyntaxProducts(syntaxSymbols);
        Set<SyntaxSymbol> allGotoSymtaxSymbol = SyntacticLRParser.getAllGotoSymtaxSymbol(syntaxProducts);
        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap = SyntacticLRParser.getSymbolProductMap(syntaxProducts);
        Map<ItemCollection, ItemCollection> allItemCollectionMap = new LinkedHashMap<>();
        SyntacticLRParser.getLR0ItemCollectionNodes(startItemCollection.getItemList().get(0).getSyntaxProduct(), startItemCollection, allGotoSymtaxSymbol, symbolProductMap, itemCollectionNo, allItemCollectionMap);
        // 显示LR0自动机
        ParseChartUtils.outputSyntaxEchart(startItemCollection);

        System.out.println("-------------------------------LR0 parse----------------------------------");
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);

        SyntacticLRParser.syntaxParseLR0(startItemCollection, tokens, syntaxFirstMap, syntaxFollowMap);
    }

    @Test
    public void testLR0parse1(){
        // 读取文法文件
        List<String> syntaxs = ParseUtils.getFile("syntaxContentFile.txt", true);

        // 解析目标语言文件生成词法单元数据
        List<Token> tokens = LexicalParser.parser(ParseUtils.getFile("compilerCode.txt", true), TestLexExpression.expressions);

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        System.out.println("-------------------------------startItemCollection----------------------------------");
        AtomicInteger itemCollectionNo = new AtomicInteger(0);
        ItemCollection startItemCollection = SyntacticLRParser.getStartItemCollection(syntaxSymbols, itemCollectionNo.getAndIncrement());
        for(Item item : startItemCollection.getItemList()){
            System.out.println(item.toString());
        }

        System.out.println("-------------------------------ItemCollectionNode----------------------------------");
        List<SyntaxProduct> syntaxProducts = SyntacticLRParser.getSyntaxProducts(syntaxSymbols);
        Set<SyntaxSymbol> allGotoSymtaxSymbol = SyntacticLRParser.getAllGotoSymtaxSymbol(syntaxProducts);
        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap = SyntacticLRParser.getSymbolProductMap(syntaxProducts);
        Map<ItemCollection, ItemCollection> allItemCollectionMap = new LinkedHashMap<>();
        SyntacticLRParser.getLR0ItemCollectionNodes(startItemCollection.getItemList().get(0).getSyntaxProduct(), startItemCollection, allGotoSymtaxSymbol, symbolProductMap, itemCollectionNo, allItemCollectionMap);
        // 显示LR0自动机
        ParseChartUtils.outputSyntaxEchart(startItemCollection, 3600, 300);

        System.out.println("-------------------------------LR0 parse----------------------------------");
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);
        SyntacticLRParser.syntaxParseLR0(startItemCollection, tokens, syntaxFirstMap, syntaxFollowMap);


    }

    @Test
    public void testCreateSLRPredictMap(){
        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        System.out.println("-------------------------------startItemCollection----------------------------------");
        AtomicInteger itemCollectionNo = new AtomicInteger(0);
        ItemCollection startItemCollection = SyntacticLRParser.getStartItemCollection(syntaxSymbols, itemCollectionNo.getAndIncrement());
        for(Item item : startItemCollection.getItemList()){
            System.out.println(item.toString());
        }

        System.out.println("-------------------------------ItemCollectionNode----------------------------------");
        List<SyntaxProduct> syntaxProducts = SyntacticLRParser.getSyntaxProducts(syntaxSymbols);
        Set<SyntaxSymbol> allGotoSymtaxSymbol = SyntacticLRParser.getAllGotoSymtaxSymbol(syntaxProducts);
        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap = SyntacticLRParser.getSymbolProductMap(syntaxProducts);
        Map<ItemCollection, ItemCollection> allItemCollectionMap = new LinkedHashMap<>();
        SyntacticLRParser.getLR0ItemCollectionNodes(startItemCollection.getItemList().get(0).getSyntaxProduct(), startItemCollection, allGotoSymtaxSymbol, symbolProductMap, itemCollectionNo, allItemCollectionMap);
        // 显示LR0自动机
        ParseChartUtils.outputSyntaxEchart(startItemCollection, 300, 100);

        System.out.println("-------------------------------product number----------------------------------");
        for(SyntaxProduct syntaxProduct : syntaxProducts){
            System.out.println("prduct: " + syntaxProduct.getNumber() + ": " + syntaxProduct.toString());
        }

        System.out.println("-------------------------------SLRPredictMap----------------------------------");
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);
        Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictSLRMap = SyntacticLRParser.predictLRMap(startItemCollection, syntaxSymbols, syntaxFirstMap, syntaxFollowMap);
        // 显示SLR分析表
        ParseChartUtils.outputLRPredictMap(predictSLRMap);

    }

    @Test
    public void testSLRPredictMap(){
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("id", TestLexExpression.ID));
        tokens.add(new Token("+", TestLexExpression.OPERATOR));
        tokens.add(new Token("id", TestLexExpression.ID));
        tokens.add(new Token("*", TestLexExpression.OPERATOR));
        tokens.add(new Token("id", TestLexExpression.ID));
        tokens.add(new Token(LexConstants.SYNTAX_END, TestLexExpression.END));

        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        /**
         * 代码
         * a + b * c
         *
         * 文法
         * E → V + F
         * F → V * V | V
         * V → id
         *
         * 自顶向下语法分析
         * E -> V + F
         *   -> id + F
         *   -> id + V * V
         *   -> id + id * V
         *   -> id + id * id
         *
         */

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        System.out.println("-------------------------------startItemCollection----------------------------------");
        AtomicInteger itemCollectionNo = new AtomicInteger(0);
        ItemCollection startItemCollection = SyntacticLRParser.getStartItemCollection(syntaxSymbols, itemCollectionNo.getAndIncrement());
        for(Item item : startItemCollection.getItemList()){
            System.out.println(item.toString());
        }

        System.out.println("-------------------------------ItemCollectionNode----------------------------------");
        List<SyntaxProduct> syntaxProducts = SyntacticLRParser.getSyntaxProducts(syntaxSymbols);
        Set<SyntaxSymbol> allGotoSymtaxSymbol = SyntacticLRParser.getAllGotoSymtaxSymbol(syntaxProducts);
        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap = SyntacticLRParser.getSymbolProductMap(syntaxProducts);
        Map<ItemCollection, ItemCollection> allItemCollectionMap = new LinkedHashMap<>();
        SyntacticLRParser.getLR0ItemCollectionNodes(startItemCollection.getItemList().get(0).getSyntaxProduct(), startItemCollection, allGotoSymtaxSymbol, symbolProductMap, itemCollectionNo, allItemCollectionMap);
        // 显示LR0自动机
        ParseChartUtils.outputSyntaxEchart(startItemCollection, 300, 100);

        System.out.println("-------------------------------product number----------------------------------");
        for(SyntaxProduct syntaxProduct : syntaxProducts){
            System.out.println("prduct: " + syntaxProduct.getNumber() + ": " + syntaxProduct.toString());
        }

        System.out.println("-------------------------------Create SLRPredictMap----------------------------------");
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);
        Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictSLRMap = SyntacticLRParser.predictLRMap(startItemCollection, syntaxSymbols, syntaxFirstMap, syntaxFollowMap);
        // 显示SLR分析表
        ParseChartUtils.outputLRPredictMap(predictSLRMap);

        System.out.println("-------------------------------SLRPredictMap----------------------------------");
        SyntacticLRParser.syntaxParseLR(startItemCollection, tokens, predictSLRMap);

    }

    @Test
    public void testSLRPredictMap2(){
        // 读取文法文件
        List<String> syntaxs = ParseUtils.getFile("syntaxContentFile.txt", true);

        // 解析目标语言文件生成词法单元数据
        List<Token> tokens = LexicalParser.parser(ParseUtils.getFile("compilerCode.txt", true), TestLexExpression.expressions);

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        System.out.println("-------------------------------startItemCollection----------------------------------");
        AtomicInteger itemCollectionNo = new AtomicInteger(0);
        ItemCollection startItemCollection = SyntacticLRParser.getStartItemCollection(syntaxSymbols, itemCollectionNo.getAndIncrement());
        for(Item item : startItemCollection.getItemList()){
            System.out.println(item.toString());
        }

        System.out.println("-------------------------------ItemCollectionNode----------------------------------");
        List<SyntaxProduct> syntaxProducts = SyntacticLRParser.getSyntaxProducts(syntaxSymbols);
        Set<SyntaxSymbol> allGotoSymtaxSymbol = SyntacticLRParser.getAllGotoSymtaxSymbol(syntaxProducts);
        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap = SyntacticLRParser.getSymbolProductMap(syntaxProducts);
        Map<ItemCollection, ItemCollection> allItemCollectionMap = new LinkedHashMap<>();
        SyntacticLRParser.getLR0ItemCollectionNodes(startItemCollection.getItemList().get(0).getSyntaxProduct(), startItemCollection, allGotoSymtaxSymbol, symbolProductMap, itemCollectionNo, allItemCollectionMap);
        // 显示LR0自动机
        ParseChartUtils.outputSyntaxEchart(startItemCollection, 3600, 300);

        System.out.println("-------------------------------product number----------------------------------");
        for(SyntaxProduct syntaxProduct : syntaxProducts){
            System.out.println("prduct: " + syntaxProduct.getNumber() + ": " + syntaxProduct.toString());
        }

        System.out.println("-------------------------------Create SLRPredictMap----------------------------------");
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);
        Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictSLRMap = SyntacticLRParser.predictLRMap(startItemCollection, syntaxSymbols, syntaxFirstMap, syntaxFollowMap);
        // 显示SLR分析表
        ParseChartUtils.outputLRPredictMap(predictSLRMap);

        System.out.println("-------------------------------SLRPredictMap----------------------------------");
        SyntacticLRParser.syntaxParseLR(startItemCollection, tokens, predictSLRMap);
    }

    /**
     * 完整测试
     */
    @Test
    public void testSLRPredictMap3(){
        SyntacticLRParser.syntaxParseSLR("syntaxContentFile.txt", "compilerCode.txt", TestLexExpression.expressions, true);
    }

    @Test
    public void testLRItemCollection(){

        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("S → C C ");
        syntaxs.add("C → c C | d ");

        List<SyntaxSymbol> syntaxSymbols = ParseUtils.parseSyntaxSymbol(syntaxs);

        Map<Integer, ItemCollection> allLRItemCollectionMap = SyntacticLRParser.getLRItemCollectionMap(syntaxSymbols);

        System.out.println("-------------------------------ItemCollectionNode----------------------------------");
        // 显示LR0自动机
        ParseChartUtils.outputSyntaxEchart(allLRItemCollectionMap.get(0), 300, -200, true);

    }

    @Test
    public void testLRItemCollection1(){

        // 读取文法文件
//        List<String> syntaxs = ParseUtils.getFile("syntaxContentFile.txt", true);
//
//        // 解析目标语言文件生成词法单元数据
//        List<Token> tokens = LexicalParser.parser(ParseUtils.getFile("compilerCode.txt", true), TestLexExpression.expressions);


        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("id", TestLexExpression.ID));
        tokens.add(new Token("+", TestLexExpression.OPERATOR));
        tokens.add(new Token("id", TestLexExpression.ID));
        tokens.add(new Token("*", TestLexExpression.OPERATOR));
        tokens.add(new Token("id", TestLexExpression.ID));
        tokens.add(new Token(LexConstants.SYNTAX_END, TestLexExpression.END));

        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

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
        ParseChartUtils.outputUniversalTreeEchart(new ParseChartUtils.UniversalTreeNode(syntaxTree.getSyntaxTreeRoot(), SyntaxDirectedTest.getDirectTreeMatcher(), true), 100, 100);
    }

    /**
     * 完整测试
     */
    @Test
    public void testLRItemCollection2(){
        SyntacticLRParser.syntaxParseLR("syntaxContentFile.txt", "compilerCode.txt", TestLexExpression.expressions, true);
    }

}
