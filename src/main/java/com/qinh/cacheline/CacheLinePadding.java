package com.qinh.cacheline;

import org.junit.Test;
//import sun.misc.Contended;

/**
 * 缓存行对齐测试
 * 缓存行：
 * 缓存行越大，局部性空间效率越高，但读取时间慢。缓存行越小，局部性空间效率越低，但读取时间快。
 * 取一个折中值，目前多用：64字节（Inter CPU）
 *
 * @author Qh
 * @version 1.0
 * @date 2021/11/12 16:24
 */
public class CacheLinePadding {
    //@Contended  jdk8可以控制变量是否在同一个缓存行
    /**
     * 有一定概率 arr1[0]是一个缓存行的行尾，arr1[1]是一个缓存行的行头
     * 也有一定概率，arr1[0],arr1[1]是在同一个缓存行
     */
    private static volatile long[] arr1 = new long[2];
    private static volatile long[] arr2 = new long[16];

    @Test
    public void t1() throws InterruptedException {
        /*
        arr1：arr1下标为0，1的两个数挨着的
        第一颗CPU的线程只改arr1[0]的位置，第二颗CPU的线程只改arr1[1]的位置，但是线程1和2都分别把数据全读到缓存行里面的
        如果这两个数字要保持缓存一致性的话，效率会比较低
        arr2：将两个数据分别放在不同的缓存行里面
         */
        //
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 10000_0000L; i++) {
                arr1[0] = i;
            }
        });

        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 10000_0000L; i++) {
                arr1[1] = i;
            }
        });

        final long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - start) / 100_0000L);
    }
    
    @Test
    public void t2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 10000_0000L; i++) {
                arr2[0] = i;
            }
        });

        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 10000_0000L; i++) {
                arr2[8] = i;
            }
        });

        final long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - start) / 100_0000L);
    }
}
