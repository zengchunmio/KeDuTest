package utils;

import java.io.*;

/**
 * Created by Nickwong on 31/07/2018.
 * 根据1-8楼的建议，优化了代码
 */
public class ReadTxt {
    /**
     * 读入TXT文件
     */
    public static String readFile() {
//        String pathname = "src/test/resources/token.txt";
        String pathname = ClassLoader.getSystemClassLoader().getResource("token.txt").getPath();
        String line = new String();
        // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
        try (FileReader reader = new FileReader(pathname); BufferedReader br = new BufferedReader(reader)) {
            // 建立一个对象，它把文件内容转成计算机能读懂的语言
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "没有token";
    }

    /**
     * 写入TXT文件
     */
    public static void writeFile(String token) {
        try {
            String pathname = ClassLoader.getSystemClassLoader().getResource("token.txt").getPath();
//            File writeName = new File("src/test/resources/token.txt""src/test/resources/token.txt"); // 相对路径，如果没有则要建立一个新的output.txt文件
            File writeName = new File(pathname); // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(token);
                out.flush(); // 把缓存区内容压入文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


