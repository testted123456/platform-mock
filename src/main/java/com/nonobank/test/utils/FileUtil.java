package com.nonobank.test.utils;

import org.springframework.core.io.FileSystemResource;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/25.
 */
public class FileUtil {

    public static final String SEPARATOR = System.getProperty("file.separator");


    public static boolean createFile(String fileName, String content) throws IOException {
        boolean flag = false;
        if (StringUtils.isEmpty(fileName)) {
            return flag;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            flag = createFileIfAbsent(fileName);
        }
        if (flag) {
            flag = writeFile(fileName, content);
        }
        return flag;
    }

    public static boolean copyFile(String destFileName, String originalFileName) {
        String content = null;
        boolean flag = false;
        try {
            content = getFileContent(originalFileName);
            if (!StringUtils.isEmpty(content)) {
                flag = createFile(destFileName, FormatUtil.formatJson(content));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return flag;
    }

    public static String getFileContent(String fileName) throws IOException {
        FileSystemResource resource = new FileSystemResource(fileName);
        if (resource.exists() && resource.isReadable()) {
            return getContent(resource.getInputStream());
        }
        return null;
    }

    private static boolean createDirIfAbsent(String dirPath) {
        File file = new File(dirPath);
        if (file.exists()) {
            return true;
        }
        return file.mkdirs();
    }

    private static String getDirPathFromFileFullName(String fileName) {
        int pos = fileName.lastIndexOf(SEPARATOR);
        String path = fileName.substring(0, pos);
        return path;
    }

    private static boolean createFileIfAbsent(String filename) {
        String dirPath = getDirPathFromFileFullName(filename);
        if (!createDirIfAbsent(dirPath)) {
            return false;
        }
        File file = new File(filename);
        if (file.exists()) {
            return true;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean writeFile(String filename, String content) throws IOException {
        FileSystemResource resource = new FileSystemResource(filename);
        if (resource.isWritable()) {
            OutputStream os = null;
            BufferedWriter bw = null;
            try {
                os = resource.getOutputStream();
                bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.write(content);
                bw.flush();
            } catch (IOException e) {
                throw e;
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    private static String getContent(InputStream ins) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

}
