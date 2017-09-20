package com.android.router.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuzhao on 2017/9/20.
 */

public class ThreadPoolUtils {
    private static int COREPOOLSIZE = 3;// 线程池中活跃的线程数
    private static int MAXIMUMPOOLSIZE = 10;// 线程池中最大容量
    private static int KEEPALIVETIME = 2;// 超出活跃线程数量的线程销毁前等待任务的时间；
    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            COREPOOLSIZE, MAXIMUMPOOLSIZE, KEEPALIVETIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());// 使用线程池替代单纯 的Thread。


    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }

}
