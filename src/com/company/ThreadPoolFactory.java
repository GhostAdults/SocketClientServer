package com.company;

import java.util.concurrent.*;

/**
 * Created by Intellij IDEA.
 *
 * @author Stock9
 * @create 2023/5/5 11:35
 * 线程工厂类
 **/
public class ThreadPoolFactory {

    /**
     *
     * 声明一个可见的变量，用于防止多线程之间访问同一变量出现的竞态条件
     *
     */
    private volatile static ThreadPoolExecutor executor;

    final private static int CORE_POOL_SIZE = 5;

    final private static int MAXIMUM_POOL_SIZE = 10;

    /**
     * corePoolSize线程池的核心线程数
     * maximumPoolSize能容纳的最大线程数
     * keepAliveTime空闲线程存活时间
     * unit 存活的时间单位
     * workQueue 存放提交但未执行任务的队列
     * threadFactory 创建线程的工厂类
     * handler 等待队列满后的拒绝策略
     */
    public void excute() {
        ExecutorService threadPool = new ThreadPoolExecutor(2,5,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
    /**
     * 创建ExecutorService，单例模式（DCL）
     * @return
     */
    public static ExecutorService getExecutorService(){
        if (executor == null){
            synchronized (ThreadPoolFactory.class){
                if (executor == null){
                    executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 30, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(5),
                            new CustomThreadFactory("自定义线程"),
                            new ThreadPoolExecutor.AbortPolicy());
                }
            }
        }
        return executor;
    }
}
