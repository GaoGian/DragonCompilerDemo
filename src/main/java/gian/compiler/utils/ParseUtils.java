package gian.compiler.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
                if(!line.equals("")) {
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

}