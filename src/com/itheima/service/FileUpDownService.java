package com.itheima.service;

import java.io.File;

public interface FileUpDownService {
    //启动客户端
    void start();

    //浏览文件
    void scanDirection(File file);

    //下载
    void downloadFile(File file);

    //上传
    void upLoadFile(File file);
}
