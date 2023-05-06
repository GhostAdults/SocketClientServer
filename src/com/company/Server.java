package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Created by Intellij IDEA.
 *
 * @author Stock9
 * @create 2023/4/28 16:08
 **/
public class Server {

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        //使用线程池创建两个线程
        ExecutorService service = ThreadPoolFactory.getExecutorService();

        //定义一个AtomicBoolean类型变量
        AtomicBoolean running = new AtomicBoolean(true);

        //服务端打开端口8888
        ServerSocket ss = new ServerSocket(8888);
        System.out.println("已启动监听端口号:8888");
        //在8888端口上监听，看是否有连接请求过来
        Socket s = ss.accept();
        //打开输入流
        InputStream is = s.getInputStream();
        //把输入流封装在DataInputStream
        DataInputStream dis = new DataInputStream(is);

        // 向客户端发送消息
        OutputStream os = s.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        //线程1：监听客户端消息
        service.execute(() -> {
            LOGGER.info("线程1: " + Thread.currentThread().getName());
            String msg = null;
            while (running.get()) {
                try {
                    if (s.isClosed()) {
                        LOGGER.warning("socket已关闭");
                    }
                    //使用readUTF读取字符串
                    msg = dis.readUTF();
                    System.out.println("有新的消息来自：" + s);
                    //打印出来
                    System.out.println("客户端：" + msg);
                } catch (IOException e) {
                    LOGGER.warning("Connection reset".equals(e.getMessage()) ? "错误: 网络连接意外中断" : "未知错误:" + e.getMessage());
                    // 终止程序
                    running.getAndSet(false);
                } finally {
                    try {
                        if (msg != null && running.get()) {
                            dos.writeUTF("√消息已发送");
                            dos.flush();
                            System.out.println("回复客户端：" + "已收到您的消息：");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

            }
            // 清理工作
            try {
                dis.close();
                dos.close();
                is.close();
                os.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // 线程2：向客户端发送消息
        service.execute(() -> {
            LOGGER.info("线程2: " + Thread.currentThread().getName());
            // 等待输入，输入 q 关闭程序
            System.out.println("输入 q 关闭程序");
            try (Scanner sc = new Scanner(System.in)) {
                while (running.get()) {  // 检查程序是否需要关闭
                    String str = sc.nextLine();
                    if ("q".equals(str)) {
                        // 关闭程序
                        running.getAndSet(false);
                        break;
                    }
                    dos.writeUTF(str);
                    dos.flush();
                }
            } catch (Exception e) {
                LOGGER.warning(e.getMessage());
            }
        });
        while (true) {
            if (!running.get()) {
                System.out.println("线程池关闭");
                // 关闭线程池
                service.shutdown();
                break;
            }
        }
    }
}
