package com.itheima.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//工具类 用来快捷上传或下载文件..
public class IOUtil {

    //快速使用IO流传输文件 下载/上传都可以用
    public static void copy(InputStream in, OutputStream out) throws IOException {
        int len;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf)) != -1){
            out.write(buf, 0, len);
        }
    }

}
