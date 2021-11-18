package com.qinh.fiber;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;

import java.util.concurrent.*;

/**
 * Fiber:纤程测试
 *
 * @author Qh
 * @version 1.0
 * @date 2021/11/18 10:57
 */
public class FiberImproveTest {
    
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        int size = 1000000;

        CountDownLatch latch = new CountDownLatch(size);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 5, TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());

        Runnable[] runnables = new Runnable[10];

        for (int i = 0; i < 10; i++) {
            runnables[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    Fiber<Void> fiber = new Fiber<>((SuspendableRunnable) () -> {
                        calc();
                        latch.countDown();
                    });
                    fiber.start();
                }
            });
        }
        for (Runnable r : runnables){
            threadPoolExecutor.execute(r);
        }


        latch.await();

        System.out.println("执行完耗时: " + (System.currentTimeMillis() - start));
    }

    private static void calc(){
        int result = 0;
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 200; j++) {
                result += j;
            }
        }
    }
}
