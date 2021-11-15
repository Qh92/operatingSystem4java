package com.qinh;

import sun.misc.Contended;

/**
 * jdk8中@Contended测试 jdk8可以控制变量是否在同一个缓存行
 * 同一组的数据在同一缓存行
 *
 * -XX:-RestrictContended //-XX:+PrintFieldLayout
 *
 * 我们用2个线程来修改字段
 *
 * 测试1：线程0修改value1和value2；线程1修改value3和value4；他们都在同一组中。测试时间：7550997200
 *
 * 测试2：线程0修改value1和value3；线程1修改value2和value4；他们在不同组中。测试时间：22784751100
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-15 21:41
 */
public final class FalseSharing implements Runnable {
    public final static long ITERATIONS = 500L * 1000L * 1000L;
    private static volatile VolatileLong volatileLong;
    private String groupId;

    public FalseSharing(String groupId) {
        this.groupId = groupId;
    }

    public static void main(final String[] args) throws Exception {
        VolatileLong.class.toString();
        // Thread.sleep(10000);
//        System.out.println("starting....");
//
//        volatileLong = new VolatileLong();
//        final long start = System.nanoTime();
//        runTest();
//        System.out.println("duration = " + (System.nanoTime() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread t0 = new Thread(new FalseSharing("t0"));
        Thread t1 = new Thread(new FalseSharing("t1"));
        t0.start();
        t1.start();
        t0.join();
        t1.join();
    }

    @Override
    public void run() {
        long i = ITERATIONS + 1;
        if (groupId.equals("t0")) {
            while (0 != --i) {
                volatileLong.value1 = i;
                volatileLong.value2 = i;
            }
        } else if (groupId.equals("t1")) {
            while (0 != --i) {
                volatileLong.value3 = i;
                volatileLong.value4 = i;
            }
        }
    }

    //测试2：（基于以上代码修改下面的部分）
    /*@Override
    public void run() {
        long i = ITERATIONS + 1;
        if (groupId.equals("t0")) {
            while (0 != --i) {
                volatileLong.value1 = i;
                volatileLong.value3 = i;
            }
        } else if (groupId.equals("t1")) {
            while (0 != --i) {
                volatileLong.value2 = i;
                volatileLong.value4 = i;
            }
        }
    }*/

}

class VolatileLong {
    @Contended("group0")
    public volatile long value1 = 0L;
    @Contended("group0")
    public volatile long value2 = 0L;

    @Contended("group1")
    public volatile long value3 = 0L;
    @Contended("group1")
    public volatile long value4 = 0L;
}

/**
 * 查看对象布局
 * public static class ContendedTest4 {
 *     @Contended
 *     private Object contendedField1;
 *
 *     @Contended
 *     private Object contendedField2;
 *
 *     private Object plainField3;
 *     private Object plainField4;
 * }
 *
 * 注意前面的@12表示字段在类中的地址偏移
 * TestContended$ContendedTest4: field layout
 * @ 12 --- instance fields start ---
 * @ 12 "plainField3" Ljava.lang.Object;
 * @ 16 "plainField4" Ljava.lang.Object;
 * @148 "contendedField1" Ljava.lang.Object; (contended, group = 0)
 * @280 "contendedField2" Ljava.lang.Object; (contended, group = 0)
 * @416 --- instance fields end ---
 * @416 --- instance ends ---
 *
 * contendedField1和contendedField2的内存地址不连续，超过了64字节
 *
 *
 * public static class ContendedTest5 {
 *     @Contended("updater1")
 *     private Object contendedField1;
 *
 *     @Contended("updater1")
 *     private Object contendedField2;
 *
 *     @Contended("updater2")
 *     private Object contendedField3;
 *
 *     private Object plainField5;
 *     private Object plainField6;
 * }
 *
 * 注意contendedField1和contendedField2和其他字段之间有填充，但是它们之间是紧挨着的，类内偏移量为4 bytes，为一个对象的大小
 * TestContended$ContendedTest5: field layout
 * @ 12 --- instance fields start ---
 * @ 12 "plainField5" Ljava.lang.Object;
 * @ 16 "plainField6" Ljava.lang.Object;
 * @148 "contendedField1" Ljava.lang.Object; (contended, group = 12)
 * @152 "contendedField2" Ljava.lang.Object; (contended, group = 12)
 * @284 "contendedField3" Ljava.lang.Object; (contended, group = 15)
 * @416 --- instance fields end ---
 * @416 --- instance ends ---
 */

