import org.junit.Test;

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

}