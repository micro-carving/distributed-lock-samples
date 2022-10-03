package com.olinonee.framework.lock.entity;

import lombok.extern.slf4j.Slf4j;

/**
 * 死锁经典案例，以及死锁常用的2种解决方案
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-29
 */
@Slf4j
public class DeadLock {

    public static void main(String[] args) {
        // deadLockDemo();
        // deadLockHandleSchemeOne();
        deadLockHandleSchemeTwo();
    }

    /**
     * 创建死锁
     *
     * @param firstLock  第一把锁
     * @param secondLock 第二把锁
     * @return Thread类
     */
    private static Thread createDeadLock(String firstLock, String secondLock) {
        return new Thread(() -> {
            try {
                while (true) {
                    synchronized (firstLock) {
                        log.info("当前线程名称 [{}] 锁住了 [{}]!", Thread.currentThread().getName(), firstLock);
                        Thread.sleep(1000);
                        synchronized (secondLock) {
                            log.info("当前线程名称 [{}] 锁住了 [{}]!", Thread.currentThread().getName(), secondLock);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 死锁示例：
     * <pre>
     * 启动了两个线程，在每个线程中都要获得 LOCK_1 和 LOCK_2，其中
     * thread_1，先获取 LOCK_1，再获取 LOCK_2；
     * thread_2，先获取 LOCK_2，再获取 LOCK_1；
     * 这样，当 thread_1 获取到 LOCK_1 之后，就要去获取 LOCK_2，而 LOCK_2 则是先被 thread_2 获取了，因此 thread_1 就需要等待 thread_2 释放 LOCK_2 之后才能继续执行；
     * 但是 thread_2 在获取到 LOCK_2 之后，却是在等待 thread_1 释放 LOCK_1，因此这就形成了“循环等待条件”，从而形成了死锁。
     * </pre>
     */
    private static void deadLockDemo() {
        // 锁标志
        final String LOCK_1 = "LOCK1";
        final String LOCK_2 = "LOCK2";

        Thread thread_1 = createDeadLock(LOCK_1, LOCK_2);
        Thread thread_2 = createDeadLock(LOCK_2, LOCK_1);

        thread_1.start();
        thread_2.start();
    }

    /**
     * 死锁处理方案1：
     * <pre>
     * 想要解决这个死锁很简单，我们只需要让 thread_1 和 thread_2 获取 LOCK_1 和 LOCK_2 的顺序相同即可，即对多个资源加锁时，要保持加锁的顺序一致
     * </pre>
     */
    private static void deadLockHandleSchemeOne() {
        // 锁标志
        final String LOCK_1 = "LOCK1";
        final String LOCK_2 = "LOCK2";

        Thread thread_1 = createDeadLock(LOCK_1, LOCK_2);
        Thread thread_2 = createDeadLock(LOCK_1, LOCK_2);

        thread_1.start();
        thread_2.start();
    }

    /**
     * 死锁处理方案2：
     * <pre>
     * 除此之外，还有一种解决方法，那就是让 LOCK_1 和 LOCK_2 的值相同，这是为什么呢？因为字符串有一个常量池，如果不同的线程持有的锁是具有相同字符的字符串锁时，那么两个锁实际上就是同一个锁。
     * </pre>
     */
    private static void deadLockHandleSchemeTwo() {
        // 锁标志
        final String LOCK_1 = "LOCK";
        final String LOCK_2 = "LOCK";

        Thread thread_1 = createDeadLock(LOCK_1, LOCK_2);
        Thread thread_2 = createDeadLock(LOCK_2, LOCK_1);

        thread_1.start();
        thread_2.start();
    }
}
