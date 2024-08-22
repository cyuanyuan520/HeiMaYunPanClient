package com.ithiema.util;

import java.io.*;

/*
    协议工具类
    协议格式 : "Type=SCAN,FileName=root,Status=OK,Message=信息"
 */
public class AgreementUtil {
    // 解析协议 : 文件类型
    public static String getType(String agreement) {
        String[] strings = agreement.split(",");
        return strings[0].split("=")[1];
    }

    // 解析协议 : 文件名字
    public static String getFileName(String agreement) {
        String[] strings = agreement.split(",");
        return strings[1].split("=")[1];
    }

    // 解析协议 : 文件状态
    public static String getStatus(String agreement) {
        String[] strings = agreement.split(",");
        return strings[2].split("=")[1];
    }

    // 解析协议 : 状态信息
    public static String getMessage(String agreement) {
        String[] strings = agreement.split(",");
        return strings[3].split("=")[1];
    }

    // 封装协议 , 并返回
    public static String getAgreement(String type, String fileName, String status, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Type").append("=").append(type).append(",");
        sb.append("FileName").append("=").append(fileName).append(",");
        sb.append("Status").append("=").append(status).append(",");
        sb.append("Message").append("=").append(message);
        return sb.toString();
    }

    // 发送协议
    public static void sendAgreement(OutputStream netOut, String agreement) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(netOut));
        bw.write(agreement);
        bw.newLine();
        bw.flush();
    }

    // 接收协议
    public static String receiveAgreement(InputStream netIn) throws IOException {
        //读取第一行数据
        BufferedReader br = new BufferedReader(new InputStreamReader(netIn));
        String agreementContent = br.readLine();
        return agreementContent;
    }
}
