package ywd.util;

// package tarena.io;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * <p>
 * IO宸ュ叿椤�
 * </p>
 * <p>
 * IO宸ュ叿
 * </p>
 * 
 * Project: Zpos_2.0.0 Copyright: @鏉窞杈版灄淇℃伅鎶�鏈湁闄愬叕鍙�
 * 
 * @version 1.0
 * @author Ye_WD 2012-10-16
 * 
 */
public class IOUtil {

    private IOUtil() {

    }

    public static File[] findByFilter(File dir, FileFilter filter) {
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return new File[0]; //
        }
        ArrayList<File> list = new ArrayList<File>();
        for (File f : files) {
            if (f.isFile()) {
                if (filter.accept(f)) { //
                    list.add(f);
                }
            } else {
                File[] arr = findByFilter(f, filter);
                Collections.addAll(list, arr);
            }
        }
        return list.toArray(new File[0]);
    }

    public static File[] findByRegex(File dir, String pattern) {
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return new File[0]; // 锟斤拷锟截匡拷锟斤拷锟斤拷锟绞久伙拷锟斤拷募锟�
        }
        ArrayList<File> list = new ArrayList<File>();
        for (File f : files) {
            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    list.add(f); // 匹锟斤拷锟斤拷募锟斤拷锟斤拷爰拷锟�
                }
            } else {
                // 锟捷癸拷锟斤拷目录f锟斤拷锟斤拷,锟斤拷锟斤拷目录锟揭碉拷锟斤拷锟斤拷锟斤拷锟侥硷拷锟斤拷锟诫集锟斤拷
                File[] arr = findByRegex(f, pattern);
                Collections.addAll(list, arr);
            }
        }
        return list.toArray(new File[0]);
    }

    public static boolean dirDelete(File dir) {
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return dir.delete(); // 锟斤拷目录直锟斤拷删
        }
        for (File f : files) {
            if (f.isFile()) {
                if (!f.delete()) { // 某锟斤拷锟侥硷拷删锟斤拷锟斤拷
                    return false;
                }
            } else {
                // 锟捷癸拷删锟斤拷目录f
                if (!dirDelete(f)) { // 某锟斤拷目录删锟斤拷锟斤拷
                    return false;
                }
            }
        }
        // dir锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷删锟斤拷锟缴撅拷锟絛ir
        return dir.delete();
    }

    public static long dirLength(File dir) {
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return 0;
        }
        long size = 0;
        for (File f : files) {
            if (f.isFile()) {
                size += f.length();
            } else {
                // 锟斤拷目录f锟侥达拷小
                size += dirLength(f); // 锟捷癸拷
            }
        }
        return size;
    }

    public static void main(String[] args) {
        // long size =
        // IOUtil.dirLength(new File("D:/spring"));
        // System.out.println(size);

        // boolean b =
        // IOUtil.dirDelete(new File("D:/ce"));
        // System.out.println(b);

        // File[] arr = IOUtil.findByRegex(
        // new File("c:/windows"),
        // ".*\\.[bB][mM][pP]$"); // .*\.[bB][mM][pP]$ // Bmp BMP bMp
        // for (File f : arr) {
        // System.out.println(f.getName());
        // }

        /**
         * 
         class SizeFilter implements FileFilter { public boolean accept(File
         * f) { if (f.length() >= 1024 * 1024) { return true; } else { return
         * false; } } } File[] arr = IOUtil.findByFilter(new File("c:/windows"),
         * new SizeFilter()); for (File f : arr) {
         * System.out.println(f.getName() + " - " + f.length()); }
         */
        System.out.println(getSrcPath());
    }

    // 椤圭洰鏂囦欢澶�
    public static String getSrcPath() {
        String src = System.getProperty("user.dir");
        src += "/src";
        return src;
    }

    // 绫绘枃浠跺す
    public static String getThisClassPath(String className) {
        String src = getSrcPath();
        String classPath = className.substring(0, className.lastIndexOf('.'));
        classPath = classPath.replace('.', '/');
        String path = src + "/" + classPath;
        return path;
    }
}
