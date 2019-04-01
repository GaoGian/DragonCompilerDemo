package gian.compiler.utils;

import gian.compiler.front.lexical.parser.LexExpression;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxSymbol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaojian on 2019/2/28.
 */
public class ParseUtils {

    // 读取文件内容
    public static List<String> getFile(String path, boolean isClassPath){
        List<String> fileContent = new ArrayList<>();
        File file = null;
        if(isClassPath) {
            ClassLoader classLoader = ParseUtils.class.getClassLoader();
            URL url = classLoader.getResource(path);
            System.out.println(url.getFile());
            file = new File(url.getFile());
        }else{
            file = new File(path);
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String line = null;
            while ((line = br.readLine()) != null) {//使用readLine方法，一次读一行
                if(!line.trim().equals("") && !line.startsWith("#")) {
                    fileContent.add(line);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return fileContent;
    }

    /**
     * 读取词法文件，生成词法规则
     * 词法规则格式：expressName → regexStr type isRegex isEmpty
     * regexStr：匹配正则表达式
     * type：类型名称
     * isRegex：是否根据字面量匹配
     * isEmpty：是否需要识别，为空的话就跳过
     */
    public static List<LexExpression.Expression> readExpressionFile(List<String> lexicalContent){
        // 解析成终结符/非终结符
        List<LexExpression.Expression> lexExpressionList = new ArrayList<>();
        for(String lexical : lexicalContent) {
            lexical = lexical.replaceAll("\\s+", " ");
            String lexicalHead = lexical.split("→")[0].trim();
            String lexicalBody = lexical.split("→")[1].trim();
            String[] lexElements = lexicalBody.split(" ");
            String regex = lexElements[0];
            String type = lexElements[1];
            Boolean isRexgexToken = Boolean.valueOf(lexElements[2]);
            Boolean isEmpty = Boolean.valueOf(lexElements[3]);

            LexExpression.Expression lexExpression = new LexExpression.Expression(regex, new LexExpression.TokenType(type, isRexgexToken), isEmpty);
            lexExpressionList.add(lexExpression);

        }

        return lexExpressionList;
    }

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
        Map<String, List<List<String>>> syntaxMap = new LinkedHashMap<>();
        for(String syntax : syntaxs){
            syntax = syntax.replaceAll("\\s+", " ");
            String head = syntax.split("→")[0].trim();
            String bodys = syntax.split("→")[1].trim();

            String[] products = bodys.split("\\|");
            for(String body : products){
                String[] symbols = body.trim().split(" ");
                List<String> symbolList = new ArrayList<>();
                for(String symbol : symbols){
                    symbolList.add(symbol.replace(LexConstants.MONTANT_UNICODE, LexConstants.MONTANT_STRING));
                }

                if(syntaxMap.get(head.trim()) == null){
                    List<List<String>> bodyList = new ArrayList<>();
                    bodyList.add(symbolList);
                    syntaxMap.put(head.trim(), bodyList);
                }else{
                    syntaxMap.get(head.trim()).add(symbolList);
                }
            }

        }

        // 如果在syntaxMap中，则是非终结符号
        Map<String, SyntaxSymbol> exitSymbolMap = new LinkedHashMap<>();
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
                        bodySymbol = new SyntaxSymbol(symbol, false);
                    } else {
                        // 说明是终结符
                        if(symbol.startsWith(LexConstants.REGEX_TOKEN_TAG_START)){
                            // 说明匹配的是正则表达式词法单元
                            bodySymbol = new SyntaxSymbol(symbol.substring(1, symbol.length()-1), true, true);
                        }else{
                            // 说明可以直接根据字面量匹配
                            bodySymbol = new SyntaxSymbol(symbol, true);
                        }
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
            headSymbol = new SyntaxSymbol(head, false);
        }else{
            headSymbol = exitSymbolMap.get(head);
        }
        headSymbol.setBody(productList);

        return headSymbol;
    }

}