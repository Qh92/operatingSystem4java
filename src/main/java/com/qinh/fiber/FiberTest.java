package com.qinh.fiber;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;

import java.util.concurrent.CountDownLatch;

/**
 * Fiber:纤程测试
 *
 * @author Qh
 * @version 1.0
 * @date 2021/11/18 10:57
 */
public class FiberTest {
    
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        int size = 1000000;

        CountDownLatch latch = new CountDownLatch(size);

        for (int i = 0; i < size; i++) {
            /*new Thread(() -> {
                calc();
                latch.countDown();
            }).start();*/
            Fiber<Void> fiber = new Fiber<>((SuspendableRunnable) () -> {
                calc();
                latch.countDown();
            });
            fiber.start();
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
