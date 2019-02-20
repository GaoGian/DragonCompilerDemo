package gian.compiler.practice.syntactic;

import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;

import java.util.*;

/**
 * Created by gaojian on 2019/1/25.
 */
public class SyntacticParser {

    /**
     * 解析简单文法：
     *      stmt → if expr then stmt else stmt
     *            | if stmt then stmt
     *            | begin stmtList end
     *  stmtList → stmt ; stmtList | stmt | ε
     * @param syntaxs
     * @return
     */
    public static List<SyntaxSymbol> parseSyntaxSymbol(List<String> syntaxs){
        // 解析成终结符/非终结符
        Map<String, List<List<String>>> syntaxMap = new HashMap<>();
        for(String syntax : syntaxs){
            String head = syntax.split("→")[0];
            String bodys = syntax.split("→")[1];

            String[] products = bodys.split("\\|");
            for(String body : products){
                String[] symbols = body.trim().split(" ");
                List<String> symbolList = new ArrayList<>();
                if(symbols != null && symbols.length >0){
                    symbolList.addAll(Arrays.asList(symbols));
                }else{
                    // 如果是空转换则加入空字符串
                    symbolList.add("");
                }

                if(syntaxMap.get(head.trim()) == null){
                    List<List<String>> bodyList = new ArrayList<>();
                    bodyList.add(Arrays.asList(symbols));
                    syntaxMap.put(head.trim(), bodyList);
                }else{
                    syntaxMap.get(head.trim()).add(Arrays.asList(symbols));
                }
            }

        }

        // 如果在syntaxMap中，则是非终结符号
        Map<String, SyntaxSymbol> exitSymbolMap = new HashMap<>();
        List<SyntaxSymbol> syntaxSymbolList = new ArrayList<>();
        for(String head : syntaxMap.keySet()){
            SyntaxSymbol headSymbol = getSymbol(head, syntaxMap, exitSymbolMap);
            syntaxSymbolList.add(headSymbol);
        }

        return syntaxSymbolList;
    }

    // 如果产生体中有非终结符并且未解析过，优先解析子非终结符
    public static SyntaxSymbol getSymbol(String head, Map<String, List<List<String>>> syntaxMap, Map<String, SyntaxSymbol> exitSymbolMap){
        List<List<SyntaxSymbol>> productList = new ArrayList<>();
        List<List<String>> productExpressionList = syntaxMap.get(head);
        for(List<String> symbols : productExpressionList){
            List<SyntaxSymbol> productSymbols = new ArrayList<>();
            for(String symbol : symbols) {
                if(exitSymbolMap.get(symbol) == null) {
                    SyntaxSymbol bodySymbol = null;
                    if (syntaxMap.keySet().contains(symbol)) {
                        // 说明是非终结符
                        bodySymbol = new SyntaxSymbol(symbol, true);
                    } else {
                        // 说明是终结符
                        bodySymbol = new SyntaxSymbol(symbol, false);
                    }
                    exitSymbolMap.put(symbol, bodySymbol);
                    productSymbols.add(bodySymbol);
                }else{
                    productSymbols.add(exitSymbolMap.get(symbol));
                }
            }
            productList.add(productSymbols);
        }

        SyntaxSymbol headSymbol = null;
        if(exitSymbolMap.get(head) == null){
            headSymbol = new SyntaxSymbol(head, true);
        }else{
            headSymbol = exitSymbolMap.get(head);
        }
        headSymbol.setBody(productList);

        return headSymbol;
    }

    /**
     * 消除左递归（P134）
     * @param originSyntaxSymbolList
     * @return
     */
    public static void eliminateLeftRecursion(List<SyntaxSymbol> originSyntaxSymbolList){
        // TODO 将原文法按照上下级关系排序

        for(int i=0; i<originSyntaxSymbolList.size(); i++){

            SyntaxSymbol currSyntaxSymbol = originSyntaxSymbolList.get(i);
            List<List<SyntaxSymbol>> currProductBodys = currSyntaxSymbol.getBody();

            // 消除对上级的左递归
            for(int j=0; j<i-1; j++){
                // 判断当前文法是否依赖上级（所有上级）
                SyntaxSymbol preSyntaxSymbol = originSyntaxSymbolList.get(j);
                for(int k=0; k<currProductBodys.size(); k++){
                    List<SyntaxSymbol> currProductBody = currProductBodys.get(k);
                    // 不处理ε产生体
                    // FIXME 确认空产生体是什么样的，是长度为0，还是symbol为""
                    if(currProductBody.size() > 0) {
                        // 判断产生体首位是否和上级相同
                        if (currProductBody.get(0).getSymbol().equals(preSyntaxSymbol.getSymbol())) {
                            // 清除该产生式，后面需要进行替换
                            currProductBodys.remove(k);
                            // 修正遍历位置
                            k--;

                            // 将产生体首位替换成所有上级的所有产生体  TODO 需要处理环和ε产生体
                            // 去掉依赖的上级头部
                            List<SyntaxSymbol> tempProductBody = currProductBody.subList(1, currProductBody.size());
                            List<List<SyntaxSymbol>> preProductBodys = preSyntaxSymbol.getBody();
                            for (int l = 0; l < preProductBodys.size(); l++) {
                                List<SyntaxSymbol> preProductBody = preProductBodys.get(l);
                                // TODO 暂时这样处理ε产生体
                                if (preProductBody.size() > 0) {
                                    List<SyntaxSymbol> newCurrProductBody = new ArrayList<>();
                                    // 将产生体首位替换成所有上级的所有产生体
                                    newCurrProductBody.addAll(preProductBody);
                                    newCurrProductBody.addAll(tempProductBody);

                                    // TODO 每次替换似乎都需要重新遍历一遍
                                    // 将替换的产生式加入到产生式列表
                                    currProductBodys.add(newCurrProductBody);
                                }
                            }
                        }
                    }
                }
            }

            // 消除对自己的左递归
            // 判断是否有左递归
            boolean isLeftRecursion = false;
            List<List<SyntaxSymbol>> leftRecursionList = new ArrayList<>();
            List<List<SyntaxSymbol>> unLeftRecursionList = new ArrayList<>();
            for(int m=0; m<currProductBodys.size(); m++) {
                List<SyntaxSymbol> currProductBody = currProductBodys.get(m);
                // 判断产生体是否左递归
                if(currProductBody.size() > 0) {
                    if (currProductBody.get(0).getSymbol().equals(currSyntaxSymbol.getSymbol())) {
                        leftRecursionList.add(currProductBody);
                        // 标记有左递归
                        isLeftRecursion = true;
                    } else {
                        unLeftRecursionList.add(currProductBody);
                    }
                }else{
                    unLeftRecursionList.add(currProductBody);
                }
            }
            // 消除左递归
            if(isLeftRecursion){
                // 生成消除左递归的文法
                SyntaxSymbol eliminateSyntaxSymbol = new SyntaxSymbol(currSyntaxSymbol.getSymbol() + "'", true);
                List<List<SyntaxSymbol>> eliminateProductBodys = new ArrayList<>();
                for(List<SyntaxSymbol> leftRecursionBody : leftRecursionList){
                    // 消除左递归首位
                    leftRecursionBody = leftRecursionBody.subList(1, leftRecursionBody.size());
                    // 加上消除左递归文法
                    leftRecursionBody.add(eliminateSyntaxSymbol);
                    eliminateProductBodys.add(leftRecursionBody);
                }
                // 加上空表达式
                List<SyntaxSymbol> emptyBody = new ArrayList<>();
                eliminateProductBodys.add(emptyBody);
                // 设置消除左递归文法的产生体
                eliminateSyntaxSymbol.setBody(eliminateProductBodys);

                // 将消除的左递归文法符号加入文法列表
                originSyntaxSymbolList.add(eliminateSyntaxSymbol);

                // 转化原来的左递归文法
                for(List<SyntaxSymbol> unLeftRecursionBody : unLeftRecursionList){
                    // 在原来没有左递归的产生体后面加上消除左递归的文法符号
                    unLeftRecursionBody.add(eliminateSyntaxSymbol);
                }
                // 替换原来的左递归产生体
                currSyntaxSymbol.setBody(unLeftRecursionList);
            }
        }

    }


}