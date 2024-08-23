package com.itheima.util;

import java.io.*;

/*
    协议工具类
    协议格式 : "Type=SCAN,FileName=root,Status=OK,Message=信息"
 */
public class AgreementUtil {

    //解析协议的类型
    public static String getType(String agreement) {
        String[] split = agreement.split(",");
        return split[0].split("=")[1];
    }

    //解析协议:文件名
    public static String getFilename(String agreement) {
        String[] split = agreement.split(",");
        return split[1].split("=")[1];
    }

    //解析协议:状态
    public static String getStatus(String agreement) {
        String[] split = agreement.split(",");
        return split[2].split("=")[1];
    }


    //解析协议:消息
    public static String getMessage(String agreement) {
        String[] split = agreement.split(",");
        return split[3].split("=")[1];
    }

    //封装协议
    public static String getAgreement(String type, String fileName, String status, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Type").append("=").append(type).append(",");
        sb.append("FileName").append("=").append(fileName).append(",");
        sb.append("Status").append("=").append(status).append(",");
        sb.append("Message").append("=").append(message);
        return sb.toString();
    }

    //发送协议
    public static void sendAgreement(OutputStream netOut, String Agreement) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(netOut));
        bw.write(Agreement);
        bw.newLine();
        bw.flush();
    }

    //接收协议
    public static String receiveAgreement(InputStream netIn) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(netIn));
        String s = br.readLine();
        return s;
    }
}
