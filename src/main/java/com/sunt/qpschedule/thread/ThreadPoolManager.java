package com.sunt.qpschedule.thread;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Yixing
 * @date 2020/11/19 16:13
 */
public class ThreadPoolManager {

    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 10, 5, TimeUnit.MINUTES, new LinkedBlockingDeque<>(5));

    public static ThreadPoolExecutor getInstance() {
        return threadPoolExecutor;
    }

    public void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }
}
