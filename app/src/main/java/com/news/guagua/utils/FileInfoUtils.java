package com.news.guagua.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title TAFileInfoUtil
 * @Package com.ta.util.filecache
 * @Description TAFileInfoUtil是一个字符串的操作类
 */
public class FileInfoUtils {
    /**
     * 返回自定文件或文件夹的大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) throws Exception {// 取得文件大小
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        } else {
            f.createNewFile();
            System.out.println("文件不存在");
        }
        return s;
    }

    // 递归
    public static long getFileSize(File f) throws Exception// 取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#0.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static long getlist(File f) {// 递归求取目录文件个数
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getlist(flist[i]);
                size--;
            }
        }
        return size;
    }

    private static List<String> items = null;//存放名称
    private static List<String> paths = null;//存放路径
    private static String rootPath = "/";

    /**
     * 获取指定文件夹 里面的文件（name = 返回文件名 path = 返回路径）
     *
     * @param filePath
     * @param isNameOrPath
     * @return
     */
    public static List<String> getFileDir(String filePath, String isNameOrPath) {
        try {
//            　　　　　　this.tv.setText("当前路径:" + filePath);// 设置当前所在路径
            items = new ArrayList<String>();
            paths = new ArrayList<String>();
            File f = new File(filePath);
            File[] files = f.listFiles();// 列出所有文件
            // 如果不是根目录,则列出返回根目录和上一目录选项
            if (!filePath.equals(rootPath)) {
                items.add("返回根目录");
                paths.add(rootPath);
                items.add("返回上一层目录");
                paths.add(f.getParent());
            }
            // 将所有文件存入list中
            if (files != null) {
                int count = files.length;// 文件个数
                for (int i = 0; i < count; i++) {
                    File file = files[i];
                    items.add(file.getName());
                    paths.add(file.getPath());
                }
            }
            if (isNameOrPath.equals("name")) {
                return items;
            } else if (isNameOrPath.equals("path")) {
                return paths;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file
     *            要删除的根目录
     */
    public static void DeleteFile(File file) {
        if (!file.exists()) {
//            mHandler.sendEmptyMessage(0);
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

//    public static String getLoadPath() {
//        StringBuffer loadPath = new StringBuffer();
//        loadPath.append(AppUtils.GetSDPath()+"/shoubei/");
//        File f = new File(loadPath.toString());
//        if (!f.exists()) {
//            f.mkdirs();
//        }
//        loadPath.append("/load/");
//        File file = new File(loadPath.toString());
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        return loadPath.toString();
//    }


    public final  static String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qimeng/";
    public static String getUserCachePath() {
        StringBuffer imgPath = new StringBuffer();
        imgPath.append(file_path);
        File f = new File(imgPath.toString());
        if (!f.exists()) {
            f.mkdirs();
        }
        imgPath.append("/image/");
        File file = new File(imgPath.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return imgPath.toString();
    }

    public static String getLoadPath() {
        StringBuffer loadPath = new StringBuffer();
        loadPath.append(file_path);
        File f = new File(loadPath.toString());
        if (!f.exists()) {
            f.mkdirs();
        }
        loadPath.append("/load/");
        File file = new File(loadPath.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return loadPath.toString();
    }


//    public static void saveFile(String str) {
//        String filePath = null;
//        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//        if (hasSDCard) { // SD卡根目录的hello.text
//            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "eDian/hello.txt";
//        } else  // 系统下载缓存根目录的hello.text
//            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "eDian/hello.txt";
//
//        try {
//            File file = new File(filePath);
//            if (!file.exists()) {
//                File dir = new File(file.getParent());
//                dir.mkdirs();
//                file.createNewFile();
//            }
//            FileOutputStream outStream = new FileOutputStream(file);
//            outStream.write(str.getBytes());
//            outStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


}

