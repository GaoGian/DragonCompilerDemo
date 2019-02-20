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
        List<List<String>> bodyList = syntaxMap.get(head);
        List<List<SyntaxSymbol>> bodySymbolList = new ArrayList<>();
        for(List<String> symbols : bodyList){
            List<SyntaxSymbol> bodySymbols = new ArrayList<>();
            for(String symbol : symbols) {
                // FIXME 解决自循环依赖
                if(exitSymbolMap.get(symbol) == null) {
                    SyntaxSymbol bodySymbol = null;
                    if (syntaxMap.keySet().contains(symbol)) {
                        // 说明是非终结符
                        bodySymbol = getSymbol(symbol, syntaxMap, exitSymbolMap);
                    } else {
                        // 说明是终结符
                        bodySymbol = new SyntaxSymbol(symbol, false);
                    }
                    exitSymbolMap.put(symbol, bodySymbol);
                    bodySymbols.add(bodySymbol);
                }else{
                    bodySymbols.add(exitSymbolMap.get(symbol));
                }
            }
            bodySymbolList.add(bodySymbols);
        }

        SyntaxSymbol headSymbol = new SyntaxSymbol(head, true, bodySymbolList);
        return headSymbol;
    }

}