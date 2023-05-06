package com.company;

/**
 * Created by Intellij IDEA.
 *
 * @author Stock9
 * @create 2023/5/5 10:33
 **/
public class ServerRunnable implements Runnable {
    public void start() {
    }

    public void join() {
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println("当前线程：" + Thread.currentThread().getName() + "执行了run方法");

    }
}
