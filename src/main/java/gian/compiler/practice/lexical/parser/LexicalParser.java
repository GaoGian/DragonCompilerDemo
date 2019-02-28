package gian.compiler.practice.lexical.parser;

import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.lexical.transform.regex.LexAutomatonTransformer;
import gian.compiler.practice.lexical.transform.regex.LexSimplePattern;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by gaojian on 2019/1/24.
 */
public class LexicalParser {

    public static List<Token> parser(List<String> fileContent, List<LexExpression.Expression> lexExpression){
        // TODO 将正则表达式转换成状态转换图
        List<TranCell> tranCells = new ArrayList<>();
        for(LexExpression.Expression expression : lexExpression){
            tranCells.add(new TranCell((LexAutomatonTransformer.LexDFACell)LexAutomatonTransformer.getNFA2MinDFA(expression.getExpression()), expression));
        }

        // 匹配表达式，记录匹配起始位置、当前读取位置、行号
        // 1、顺序匹配，判断后续字符是否合理，匹配失败则进行回溯位置    TODO 穷举法不太方便，考虑使用first集、follow集作为向前看集合
        // 2、采用并行匹配方式，输出最长匹配序列，如果同时有多个命中方案，以词法优先顺序为主  TODO 暂时采用该方法
        // 3、合并状态转换图    TODO 稍微有点麻烦，暂时不采用

        Map<TranCell, Set<LexAutomatonTransformer.LexState>> originMatchTranCells = new LinkedHashMap<>();
        for(TranCell tranCell : tranCells){
            Set<LexAutomatonTransformer.LexState> tranState = new HashSet<>();
            tranState.add(tranCell.getLexCell().getStartState());
            originMatchTranCells.put(tranCell, tranState);
        }

        List<Token> parseResult = new ArrayList<>();

        int lineIndex = 0;
        for(String line : fileContent){
            // FIXME 尾部不全空格符，方便转换解析
            line += " ";
            // 上一次完成识别的字符位置
            int startIndex = 0;
            // 当前正在识别的字符位置
            int endIndex = startIndex;

            Map<TranCell, Set<LexAutomatonTransformer.LexState>> matchTranCells = new LinkedHashMap<>();
            matchTranCells.putAll(originMatchTranCells);
            List<TranCell> matchKeyList = new ArrayList<>();
            matchKeyList.addAll(originMatchTranCells.keySet());

            // 输入字符
            while(endIndex < line.length()){
                String input = String.valueOf(line.charAt(endIndex));
                Map<TranCell, Integer> accTranAbleCell = new LinkedHashMap<>();

                for(int i=0; i<matchKeyList.size(); i++){
                    TranCell matchCell = matchKeyList.get(i);
                    // 当前状态
                    Set<LexAutomatonTransformer.LexState> tranStates = matchTranCells.get(matchCell);
                    // 转换后的状态
                    Set<LexAutomatonTransformer.LexState> tranAbleStates = new HashSet<>();

                    // 逐个转换，返回转换集合
                    for(LexAutomatonTransformer.LexState tranState : tranStates){
                        Set<LexAutomatonTransformer.LexState> tranStateList = tranState.tranState(input);
                        tranAbleStates.addAll(tranStateList);
                    }

                    if(tranAbleStates.size() == 0){
                        // 说明无法继续转换，判断当前状态是否是接受状态，否则报错
                        LexAutomatonTransformer.LexDFACell lexCell = matchCell.getLexCell();
                        Set<LexAutomatonTransformer.LexAggState> accStateSet = lexCell.getAccStateSet();
                        // 判断上一次转换是否是接受态
                        for(LexAutomatonTransformer.LexState tranAbleState : tranStates){
                            if(!accStateSet.contains(tranAbleState)){
                                // 说明不能转换，去除该匹配单元
                            }else{
                                // 说明已经接受，去除该匹配单元
                                accTranAbleCell.put(matchCell, endIndex - startIndex);
                            }
                            matchKeyList.remove(i);
                            // 修正下个转换的位置
                            i--;
                        }
                    }else{
                        // 说明还能继续转换，更新下次的转换集合
                        matchTranCells.put(matchCell, tranAbleStates);
                    }

                }

                if(matchKeyList.size() == 0){
                    // 说明已经匹配结束，输出接受集合最大长度
                    TranCell maxMatchCell = null;
                    Integer maxMatchLength = null;
                    for(TranCell matchAbleCell : accTranAbleCell.keySet()){
                        Integer matchLength = accTranAbleCell.get(matchAbleCell);
                        if(maxMatchCell == null){
                            maxMatchCell = matchAbleCell;
                            maxMatchLength = matchLength;
                        }else{
                            if(matchLength > maxMatchLength){
                                maxMatchCell = matchAbleCell;
                                maxMatchLength = matchLength;
                            }
                            if(matchLength == maxMatchLength){
                                // 如果匹配长度一样，那么按照顺序匹配，TODO 也可以设置优先级
//                                    throw new RuntimeException("maxMatchLength error");
                            }
                        }
                    }

                    // 返回最长匹配字符
                    Integer accIndex = startIndex + maxMatchLength;
                    if(!maxMatchCell.getExpression().isEmpty()) {
//                            Token token = new Token(line.substring(startIndex, accIndex), maxMatchCell.getExpression().getType());
                        Token token = new Token(line.substring(startIndex, accIndex), maxMatchCell.getExpression().getType(), startIndex + 1, lineIndex + 1);
                        parseResult.add(token);
                    }

                    // 重置匹配集合
                    matchTranCells.clear();
                    matchTranCells.putAll(originMatchTranCells);
                    matchKeyList.clear();
                    matchKeyList.addAll(originMatchTranCells.keySet());

                    // 刷新读取位置
                    startIndex = accIndex;
                    endIndex = accIndex;

                    if(endIndex == line.length()) {
                        break;
                    }
                }else{
                    endIndex++;
                }
            }

            lineIndex++;
        }

        // 在末尾添加上结束符
        parseResult.add(new Token(LexConstants.SYNTAX_END, LexExpression.TokenType.END, 1, parseResult.get(parseResult.size()-1).getLine() + 1));

        return parseResult;
    }

    public static class TranCell{
        private LexAutomatonTransformer.LexDFACell lexCell;
        private LexExpression.Expression expression;

        public TranCell(LexAutomatonTransformer.LexDFACell lexCell, LexExpression.Expression expression) {
            this.lexCell = lexCell;
            this.expression = expression;
        }

        public LexAutomatonTransformer.LexDFACell getLexCell() {
            return lexCell;
        }

        public void setLexCell(LexAutomatonTransformer.LexDFACell lexCell) {
            this.lexCell = lexCell;
        }

        public LexExpression.Expression getExpression() {
            return expression;
        }

        public void setExpression(LexExpression.Expression expression) {
            this.expression = expression;
        }
    }

}