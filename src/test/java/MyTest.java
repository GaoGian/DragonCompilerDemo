import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by gaojian on 2019/1/24.
 */
public class MyTest {

    @Test
    public void test(){
        Scanner scan = new Scanner(System.in);
        System.out.println("input: " + scan.next());

    }

    @Test
    public void test1(){

        String content = "I am noob from runoob.com.";

        String pattern = ".*runoob.*";

        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("字符串中是否包含了 'runoob' 子字符串? " + isMatch);

    }

    @Test
    public void test2(){
        List<SyntaxSymbol> list1 = new ArrayList<>();
        list1.add(new SyntaxSymbol("if", true));
        list1.add(new SyntaxSymbol("expr", true));

        List<SyntaxSymbol> list2 = new ArrayList<>();
        list2.add(new SyntaxSymbol("if", true));
        list2.add(new SyntaxSymbol("expr", true));

        System.out.println(list1.equals(list2));

    }

}