package com.ithiema.service;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public interface FileUpDownService {
    // 启动客户端
    void start();

    // 浏览服务端数据操作
    void scanDirection(File file);

    // 下载业务操作
    void downloadFile(File file);

    // 上传业务操作
    void uploadFile(File file);
}
