package com.itheima.main;

import com.itheima.service.FileUpDownService;
import com.itheima.service.FileUpDownServiceImp;

public class YunPanApp {
    public static void main(String[] args) {
        //创建客户端类
        FileUpDownService ClientService = new FileUpDownServiceImp();

        //启动!!(并初始化)
        ClientService.start();
    }
}
