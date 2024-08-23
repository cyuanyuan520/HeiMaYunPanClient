package com.itheima.service;

import com.itheima.exception.BussinessException;
import com.itheima.util.AgreementUtil;
import com.itheima.util.IOUtil;

import java.io.*;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.Scanner;

public class FileUpDownServiceImp implements FileUpDownService{
    private ResourceBundle bundle;
    private String downloadPath;
    public File current = new File("root");//保存现在浏览的文件夹
    private File downloadDir;//File类型的本地下载路径

    //初始化端口号 以及主界面
    @Override
    public void start() {
        try {
            //读取下载路径
            bundle = ResourceBundle.getBundle("yunpan");
            downloadPath = bundle.getString("DownloadPath");
            downloadDir = new File(downloadPath);//把下载路径封装成一个File对象

            //检测下载路径是否合法 不合法的话抛出异常
            if (!downloadDir.exists() && !downloadDir.mkdirs()){
                throw new BussinessException("由于某些未知原因初始化失败了!");
            } else if (downloadDir.isFile()) {
                throw new BussinessException("文件不能当成下载路径");
            }

            Scanner sc = new Scanner(System.in);

            //绘制主界面
            while (true){
                System.out.println("--------------------------------------------------------------------------");
                System.out.println("|                            欢迎进入黑马网盘                               |");
                System.out.println("|************************************************************************|");
                System.out.println("| 1) 浏览当前目录  2) 浏览子目录  3) 返回上一级目录  4) 下载文件  5) 上传文件       |");
                System.out.println("-------------------------------------------------------------------------");
                //输入选项
                String choice = sc.nextLine();
                switch (choice) {
                    case "1":
                        scanDirection(current);
                        break;
                    case "2":
                        System.out.println("请输入要浏览的子目录:");
                        String childPath = sc.nextLine();
                        scanDirection(new File(current, childPath));
                        break;
                    case "3":
                        if (current.getName().equals("root")){
                            System.out.println("[warn]已经回到根目录了");
                        } else {
                            scanDirection(current.getParentFile());
                        }
                        break;
                    case "4":
                        System.out.println("请输入要下载的文件名:");
                        String downloadFilename = sc.nextLine();
                        downloadFile(new File(current, downloadFilename));
                        break;
                    case "5":
                        System.out.println("请输入要上传文件的路径:");
                        String uploadFilename = sc.nextLine();
                        upLoadFile(new File(uploadFilename));//传到方法里的是本地的路径
                        break;
                    default:
                        System.out.println("其他功能还在开发中...");
                }

            }
        } catch (BussinessException e){
            e.printStackTrace();
        }
    }


    //浏览文件
    @Override
    public void scanDirection(File file) {
        try (Socket socket = new Socket("127.0.0.1", 8888);
             InputStream netIN =  socket.getInputStream();
             OutputStream netOut = socket.getOutputStream();
        ) {
            //System.out.println("[Notice]: 即将发送协议...]");//调试代码
            //发送协议 告诉服务器我要查询文件夹了:)
            String agreement = AgreementUtil.getAgreement("SCAN", file.toString(), null, null);
            AgreementUtil.sendAgreement(netOut, agreement);
            //System.out.println("[Notice]: 等待服务器响应...]");//调试代码
            String s = AgreementUtil.receiveAgreement(netIN);
            //准备接收消息
            String cotent;
            BufferedReader br = new BufferedReader(new InputStreamReader(netIN));
            if (AgreementUtil.getStatus(s).equals("FAILED")) {
                //服务端传来失败消息的处理方法
                System.out.println(AgreementUtil.getMessage(s));
            } else {
                //先更新current
                current = new File(AgreementUtil.getFilename(s));
                System.out.println("当前目录:" + current.toString());
                while ((cotent = br.readLine()) != null) {
                    System.out.println(cotent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //下载
    @Override
    public void downloadFile(File file) {
        try (Socket socket = new Socket("127.0.0.1", 8888);
             InputStream netIN =  socket.getInputStream();
             OutputStream netOut = socket.getOutputStream();
        ) {
            //生成并发送协议 告诉服务器我要下载文件
            String agreement = AgreementUtil.getAgreement("DOWNLOAD", file.toString(), null, null);
            AgreementUtil.sendAgreement(netOut, agreement);
            //接收服务器的回应协议
            String s = AgreementUtil.receiveAgreement(netIN);
            if (AgreementUtil.getStatus(s).equals("FAILED")){
                System.out.println(AgreementUtil.getMessage(s));
            } else {
                BufferedInputStream bis = new BufferedInputStream(netIN);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(downloadDir, file.getName())));
                int b;
                while ((b = bis.read()) != -1) {
                    bos.write(b);
                }
                bos.flush();
                bos.close();
                bis.close();
                System.out.println("[Notice]下载完成");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //
    @Override
    public void upLoadFile(File file) {
        try (Socket socket = new Socket("127.0.0.1", 8888);
             InputStream netIN =  socket.getInputStream();
             OutputStream netOut = socket.getOutputStream();
        ) {
            //参数是本地路径 向服务器发送协议时要转换成上传路径
            File upLoadPath = new File(current, file.getName());
            String s = AgreementUtil.getAgreement("UPLOAD", upLoadPath.toString(), null, null);
            AgreementUtil.sendAgreement(netOut, s);
            //等待服务器回应
            String s1 = AgreementUtil.receiveAgreement(netIN);
            if (AgreementUtil.getStatus(s1).equals("FAILED")){
                System.out.println(AgreementUtil.getMessage(s1));
            } else {
                InputStream fis = new FileInputStream(file);
                IOUtil.copy(fis, netOut);
                //上传完毕
                fis.close();
                System.out.println("[Notice]文件上传成功!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
