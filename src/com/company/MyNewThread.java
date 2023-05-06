package com.company;

/**
 * Created by Intellij IDEA.
 *
 * @author Stock9
 * @create 2023/5/5 11:11
 **/
public class MyNewThread extends Thread {
    @Override
    public void run() {
        System.out.println("当前线程：" + Thread.currentThread().getName() + "执行中-调用了run方法");
    }
}
