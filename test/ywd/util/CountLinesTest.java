package ywd.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * 
 * <p>
 * 代码量统计类
 * </p>
 * <p>
 * 运行——计算本项目代码量， 包括：.java .xml .jsp 可传 类名 统计src下的文件夹代码量。
 * （传参不能统计src以外的文件夹，including other source folder like test）
 * </p>
 * 
 * Project: Zpos_2.0.0 Copyright: @杭州辰林信息技术有限公司
 * 
 * @version 1.0
 * @author Ye_WD 2012-10-16
 * 
 */
public class CountLinesTest {

    private CountLinesTest() {
    }

    /**
     * 通用方法
     * 
     * @throws Exception
     */
    public static void countClassDirLines(String className) throws Exception {

        // 项目文件夹
        String project = System.getProperty("user.dir");

        // 类文件夹
        String classPath = className.substring(0, className.lastIndexOf('.'));
        classPath = classPath.replace('.', '/');

        String path = project + "/src/" + classPath;

        System.out.println("路径：" + path);
        File f = new File(path);
        countLines(f);
    }

    /** 程序 */
    static long normalLines = 0;

    /** 注释 */
    static long commentLines = 0;

    /** 空白 */
    static long whiteLines = 0;

    public static void main(String[] args) throws Exception {
        // 获取当前java文件路径的方式
        // System.out.println(new File("").getCanonicalPath()); //获取标准的路径
        // 项目统计
        System.out.println(System.getProperty("user.dir"));
        countLines(new File(System.getProperty("user.dir")));
    }

    private static void countLines(File file) throws Exception {

        // File[] codeFiles= file.listFiles();
        File[] codeFiles = IOUtil.findByRegex(file, "" + ".*\\.java$||"
                + ".*\\.xml$||" + ".*\\.css$||" + ".*\\.js$||" + ".*\\.html$||" + ".*\\.jsp$");
        for (File child : codeFiles) {
            if (!child.getName().equals("amcharts.js")
                    && !child.getName().equals("jquery-2.1.3.js")
                    && !child.getName().equals("jquery-ui.css")
                    && !child.getName().equals("jquery-ui.js")) {
                parse(child);
            }
        }
        System.out.println("程序：" + normalLines);
        System.out.println("注释：" + commentLines);
        System.out.println("空白：" + whiteLines);
    }

    @SuppressWarnings("resource")
    private static void parse(File f) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(f));
        String line = "";
        boolean comment = false;
        while ((line = in.readLine()) != null) {
            line = line.trim(); //
            if (line.matches("^[\\s&&[^\\n]]*$")) {
                // System.out.println(line);
                whiteLines++;
            } else if (line.startsWith("<!--") && !line.endsWith("-->")) {
                commentLines++;
                comment = true;
            } else if ((line.startsWith("/*") && line.endsWith("*/"))
                    || (line.startsWith("<%--") && line.endsWith("--%>"))
                    || (line.startsWith("<!--") && line.endsWith("-->"))) {
                commentLines++;
            } else if ((line.startsWith("/*") && !line.endsWith("*/"))
                    || (line.startsWith("<%--") && !line.endsWith("--%>"))) {
                commentLines++;
                comment = true;
            } else if (true == comment) {
                commentLines++;
                if (line.endsWith("*/") || line.endsWith("-->")
                        || line.endsWith("--%>")) {
                    comment = false;
                }
            } else if (line.startsWith("//")) {
                commentLines++;
            } else {
                normalLines++;
            }
        }
    }
}
