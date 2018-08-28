import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gaojian on 2018/8/28.
 * 一个完整的语法分析、词法分析例子
 */
public class CFG {

    private Map<String, List<String[]>> BNF;
    private Map<String, String> termRegex;
    private String start, text;
    private int i = 0;

    public CFG(Map<String, List<String[]>> productions, String start, Map<String, String> termRegex) {
        this.BNF = productions;
        this.termRegex = termRegex;
        this.start = start;
    }

    public boolean recognize(String text) {
        this.text = text;
        return match(start) && i == text.length();
    }

    private boolean match(String V) {
        if (V.isEmpty()) return true; //epsilon
        List<String[]> production = BNF.get(V);
        if (production == null) { //no production for this symbol, should be terminal symbol
            String re = termRegex.get(V); //get the regex for this terminal symbol
            if (re == null) throw new RuntimeException("invalid CFG.");
            re = "\\s*" + re + "\\s*";
            Pattern pattern = Pattern.compile(re);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find(i) && matcher.start() == i) {
                i = matcher.end();
                return true;
            }
            return false;
        }
        //try each production. if one matches, succeed, or recover the pointer and try next.
        int save = i;
        for (String[] p : production) {
            boolean flag = true;
            i = save;
            for (String T : p) {
                if (!match(T)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                System.out.println(V + "->" + String.join(" ", p));
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        //1) E -> TE'
        //2) E'-> +TE' | -TE' | e
        //3) T -> FT'
        //3) T'-> *FT' | /FT' | e
        //4) F -> int | (E)
        //specify the BNF table, should be left factored, left recursion removed
        Map<String, List<String[]>> BNF = new HashMap<String, List<String[]>>();
        List<String[]> pE = new ArrayList<String[]>();
        pE.add(new String[]{"T", "EA"});
        BNF.put("E", pE);
        List<String[]> pEA = new ArrayList<String[]>();
        pEA.add(new String[]{"+", "T", "EA"});
        pEA.add(new String[]{"-", "T", "EA"});
        pEA.add(new String[]{""});
        BNF.put("EA", pEA);
        List<String[]> pT = new ArrayList<String[]>();
        pT.add(new String[]{"F", "TA"});
        BNF.put("T", pT);
        List<String[]> pTA = new ArrayList<String[]>();
        pTA.add(new String[]{"*", "F", "TA"});
        pTA.add(new String[]{"/", "F", "TA"});
        pTA.add(new String[]{""});
        BNF.put("TA", pTA);
        List<String[]> pF = new ArrayList<String[]>();
        pF.add(new String[]{"INT"});
        pF.add(new String[]{"(", "E", ")"});
        BNF.put("F", pF);

        //specify terminal symbol regular expression table
        Map<String, String> termRegex = new HashMap<String, String>();
        termRegex.put("+", "\\+");
        termRegex.put("-", "\\-");
        termRegex.put("*", "\\*");
        termRegex.put("/", "/");
        termRegex.put("(", "\\(");
        termRegex.put(")", "\\)");
        termRegex.put("INT", "[0-9]");

        CFG cfg = new CFG(BNF, "E", termRegex);
        boolean b = cfg.recognize("(1 + 1)*2");
        System.out.println(b);
    }


}