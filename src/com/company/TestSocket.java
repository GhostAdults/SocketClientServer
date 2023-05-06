package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by Intellij IDEA.
 *
 * @author Stock9
 * @create 2023/4/28 15:23
 **/
public class TestSocket {

    public static void getHost() throws IOException, UnknownHostException {
        InetAddress host = InetAddress.getLocalHost();
        String ip = host.getHostAddress();
        System.out.println("本机ip地址：" + ip);

        Process p = Runtime.getRuntime().exec("ping " + ip);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            if (line.length() != 0)
                System.out.println(line);
            sb.append(line + "\r\n");
        }
        //System.out.println("本次指令返回的消息是：");
        //String s2 = new String(sb.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        //System.out.pripu(s2);
    }

    public static void client() {
        try {
            InetAddress host = InetAddress.getLocalHost();
            String ip = host.getHostAddress();
            //连接到本机的8888端口
            Socket s = new Socket(ip, 8888);
            if (s.isConnected()) {
                System.out.println("连接成功：" + s);
            }
            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            System.out.println("输入发送消息:");
            //使用Scanner读取控制台的输入，并发送到服务端
            Scanner sc = new Scanner(System.in);
            // 打开输出流
            OutputStream os = s.getOutputStream();
            //把输出流封装在DataOutputStream中
            DataOutputStream dos = new DataOutputStream(os);
            MyNewThread t1 = new MyNewThread() {
                @Override
                public void run() {

                    while (true) {
                        //接受服务端输入流的数据
                        try {
                            //读取输入流 看是否有数据被推送过来
                            String msg = dis.readUTF();
                            System.out.println("服务端：" + msg);
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                }
            };

            MyNewThread t2 = new MyNewThread() {
                @Override
                public void run() {
                    while (true) {
                        // 发送到服务端
                        try {
                            String str = sc.next();
                            // 使用输出流writeUTF给服务器发送字符串
                            dos.writeUTF(str);
                        } catch (IOException e) {
                            // TODO 自动生成的 catch 块
                            e.printStackTrace();
                        }
                    }
                }
            };
            t1.start();
            t2.start();
            t1.join();
            sc.close();
            dos.close();
            os.close();
            s.close();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

