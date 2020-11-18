package com.sunt.qpschedule;

import com.sunt.qpschedule.config.SuntApplicationContext;
import com.sunt.qpschedule.thread.QpTimerTask;

import java.util.Timer;

/**
 * @author Chen Yixing
 * @date 2020/11/17 14:32
 *
 * 方案设计：
 * 1 就一个定时器Timer，每隔一小时，轮询一下
 *      1.1 如果是指定的月初（既一号），那么查看下今天是否已经运行过（设置判断标志）
 *          1.1.1 运行过，那么啥也不做
 *          1.1.2 没运行过，那么执行相应的轮询任务
 *      1.2 如果非指定的月初，那么啥也不做
 *
 * 2 任务里面后期可能会要求每个表单独的一个线程来执行，以加快速度，因此预备一个线程池，看需要而定。
 *
 */
public class QpApplication {
    private static final long QP_DELAY = 0;
    private static final long QP_PERIOD = 1 * 60 * 60 * 1000;

    public static void main(String[] args) {
        Timer timer = new Timer();
        QpTimerTask qpTimerTask = new QpTimerTask();
        // 每隔指定时间轮询
        timer.schedule(qpTimerTask, QP_DELAY, QP_PERIOD);
    }
}
