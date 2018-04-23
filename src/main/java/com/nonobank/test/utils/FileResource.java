package com.nonobank.test.utils;

import com.nonobank.test.commons.MockException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import java.io.*;

/**
 * Created by H.W. on 2018/4/12.
 */
public class FileResource {
    // private static final String absolute_path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final String separator = "/";

    public static String getOriginDirpath(String prePath, String appName, String interName) {
        return getDirpath(prePath, "", appName, interName);
    }

    public static String getDirpath(String prePath, String relaPath) {
        return prePath + separator + relaPath + ".json";
    }

    public static String getDirpath(String absolute_path, String env, String appName, String interName) {
        String temp = interName.replace(separator, "_");
        String dirpath = absolute_path + separator + env + separator + appName + separator + temp + ".json";
        return dirpath;
    }

    public static boolean createFile(String prePath, String env, String appName, String interName, String content) throws MockException {
        return createFile(getDirpath(prePath, env, appName, interName), content);
    }

    private static boolean createFile(String path, String content) throws MockException {
        content = FormatUtil.formatJson(content);

        boolean bool = false;
        FileSystemResource resource = new FileSystemResource(path);
        if (!resource.exists()) {
            try {
                bool = createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new MockException(path + "文件创建失败");
            }
        }
        if (bool) {
            resource = new FileSystemResource(path);
        }
        if (resource.isWritable()) {
            OutputStream os = null;
            BufferedWriter bw = null;
            try {
                os = resource.getOutputStream();
                bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.write(content);
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
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

                bool = true;
            }


        }
        return bool;

    }


    private static boolean createFile(String path) throws IOException {
        boolean bool = false;
        int pos = path.lastIndexOf(separator);
        String dir = path.substring(0, pos);
        File file = new File(dir);
        if (!file.exists()) {
            bool = file.mkdirs();
        }
        file = new File(path);
        if (!file.exists()) {
            bool = file.createNewFile();
        }
        if (file.isFile()) {
            return true;
        } else {

        }
        return bool;
    }


    public static String getResource(String path) throws MockException {
        FileSystemResource resource = new FileSystemResource(path);
        String result = null;
        if (!resource.exists()) {
            return null;
        }
        if (!resource.isReadable()) {
            throw new MockException("文件" + path + "不可读");
        }
        try {
            result = getStringFromInputStream(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new MockException("文件" + path + "读取失败");
        }
        return result;
    }

    private static String getStringFromInputStream(InputStream input) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }


    public static void copyFile(String originPath, String targetPath) throws MockException {
        //  String sitPath = getDirpath("sit",appName,interName);
        String result = getResource(originPath);

        createFile(targetPath, result);
        //createFile(sitPath,result);

    }

    public static void copyFile(String prePath, String env, String appName, String interName) throws MockException {
        String originPath = getDirpath(prePath, "", appName, interName);
        String targetPath = getDirpath(prePath, env, appName, interName);
        //  String sitPath = getDirpath("sit",appName,interName);
        String result = getResource(originPath);

        createFile(targetPath, result);
        //createFile(sitPath,result);

    }


}