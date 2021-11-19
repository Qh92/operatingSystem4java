# 计算机底层知识（java）



## 目录：

![1636620441889](assets/1636620441889.png)



## 推荐书籍：

**不求甚解，观其大略**

![1636620794893](assets/1636620794893.png)

- 《编码：隐匿在计算机软硬件背后的语言》
- 《深入理解计算机系统》
- **语言：C JAVA  后续有精力学C二选一：《C程序设计语言》《C Primer Plus》**
- **数据结构与算法：--毕生的学习 leetCode**
- 	《Java数据结构与算法》《算法》
- 	*《算法导论》 //难《计算机程序设计艺术》//难*
- 操作系统：Linux内核源码解析 30天自制操作系统  **Linux内核设计与实现**
- 网络：机工《TCP/IP详解》卷一 翻译一般
- 编译原理：机工 龙书--《编译原理》《编程语言实现模式》
- 数据库：SQLite源码 Derby--JDK自带的数据库

![1636620543797](assets/1636620543797.png)





![1636621819104](assets/1636621819104.png)

适度打开

很多情况下保持黑箱即可，因为打开这个黑箱，你就会发现黑箱会变成黑洞，吞噬你所有的精力和时间，有可能使你偏离原来的方向，陷入到不必要的细节中无法自拔



## 硬件的基础知识

![1636622024224](assets/1636622024224.png)

### CPU的制作过程

Intel cpu的制作过程

https://haokan.baidu.com/v?vid=11928468945249380709&pd=bjh&fr=bjhauthor&type=video

CPU是如何制作的

https://www.sohu.com/a/255397866_468626

**生态**



### CPU的原理

计算机需要解决的最根本问题：如何代表数字

![1636622830973](assets/1636622830973.png)



![1636681377647](assets/1636681377647.png)





### 汇编语言（机器语言）的执行过程

**汇编语言的本质：机器语言的助记符 其实它就是机器语言**

![1636681707563](assets/1636681707563.png)



DMA：Direct Memory Access 直接内存访问

![1636682219919](assets/1636682219919.png)

```markdown
总线现在64bit的比较多

64位的寄存器可以当32位寄存器使用
64位的CPU指寄存器一次可以存储64位的数字，ALU与寄存器也有电路连接，它们之间电路一次也可以读取64位
```









### 量子计算机

![1636683504436](assets/1636683504436.png)

量子比特，同时表示0，1



### java相关硬件知识

cpu和内存是计算机的核心

### CPU的基本组成

![1636682219919](assets/1636682219919.png)

![1636685254556](assets/1636685254556.png)

**PC -> Program Counter 程序计数器（记录当前指令地址）**

**Registers -> 寄存器，暂时存储CPU计算需要用到的数据**
**ALU -> Arithmetic & Logic Unit 运算单元**

**CU -> Control Unit 控制单元**

**MMU -> Memory Management Unit 内存管理单元**

**cache -> 缓存（为什么需要缓存，因为CPU速度比内存速度快很多，每次去内存取数据就太慢了）**

![1636686528188](assets/1636686528188.png)

**context switch 线程上下文切换：将上一个正在运行的线程的寄存器和指令存储起来，把下一个线程需要的数据放过来**
**切换是需要消耗cpu，效率比较低（一个核心对应一个线程）**
**超线程：一个核心对应两个线程，一个运算单元对应多套寄存器和程序计数器**



![1636687022158](assets/1636687022158.png)

![1636687103928](assets/1636687103928.png)



一颗CPU对应多核，如下图，黄色框对应一颗CPU，里面是双核，每个核心有自己的L1、L2，两个封装在一起的核心共享一个L3，两颗CPU共享主内存

![1636687230605](assets/1636687230605.png)



**按 块读取数据**：一般来讲，从任何一个存储单元，不管是内存、L1、L2、L3、硬盘，读取数据都是按块来读取

**程序局部性原理**：读取了某个数据后，按程序来讲，它应该很快用到它相邻的数据，如果一个一个的读取数据，势必效率会很低，所以按块读取来提升效率

*硬盘读取数据是不是CPU读取数据到内存？*

```markdown
明显不是，CPU的64个寄存器能存放多少数据呢，DMA直接发送一个指令，这条指令告诉硬盘，把相应的数据读取到内存的某个位置，硬盘直接跟内存打交道
```



![1636687703652](assets/1636687703652.png)



### 缓存行对齐

如下图：下面有两个核，第一个核要读取x数据，x，y相互挨着，位于同一块（同一个缓存行）

计算单元要读取x，先去L1缓存去读取，如果没有则去L2缓存读取，依次类推，最后去内存里面读取。数据返回路径，找到x所在的块（缓存行），将数据缓存到L3、L2、L1

一个核要读取x，会把y也读取进去。另外一个核要读取y，会把x也读取进去。在这种情况下，如果两个核的数据要保持一致性。实现这种情况，内部的原理是**缓存一致性协议**

![1636698706807](assets/1636698706807.png)



![1636704759500](assets/1636704759500.png)

Inter CPU取的64字节



```markdown
缓存行：
缓存行越大，局部性空间效率越高，但读取时间慢。缓存行越小，局部性空间效率越低，但读取时间快。
取一个折中值，目前多用：64字节（Inter CPU）
```



```java
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
```

缓存行对齐：对于有些特别敏感的数字，会存在线程高竞争的访问，为了保证不发生伪共享，可以使用缓存行对齐的编程方式

JDK7中，很多采用long padding提高效率

JDK8，加入了@Contended注解（自己做实验），需要加上 -XX:-RestrictContended 才能生效



disruptor框架，缓存行框架

![1636708127154](assets/1636708127154.png)





### CPU的乱序执行

![image-20211115224046081](assets\image-20211115224046081.png)



#### CPU层面如何禁止重排序？内存屏障

inter:lfence:读屏障，sfence:写屏障 ，mfence:读写屏障

当然也可以使用总线锁来解决



![image-20211115225459507](assets\image-20211115225459507.png)

原语比lock指令效率要高

CPU层面是原语、Lock指令



![image-20211115230136105](assets\image-20211115230136105.png)

![image-20211115230201745](assets\image-20211115230201745.png)



#### JVM规范

![1637053971947](assets/1637053971947.png)

#### volatile在JVM层面的实现，在CPU层面还是Lock指令

![1637054123689](assets/1637054123689.png)



volatile修饰引用类型时，读写引用类型也会加屏障么？（官方未明确说明，自己做实现）

对CPU层面来说，只要两个指令没有依赖关系就会发生指令重排序

对于JVM来说，重排序必须遵守happens-before原则



![1637054556222](assets/1637054556222.png)



![1637054840855](assets/1637054840855.png)



#### 总结：禁止乱序

##### CPU层面：

	intel -> 原语（lfence:读屏障，sfence:写屏障 ，mfence:读写屏障）或者锁总线

##### JVM层面：

	8个happens-before原则 4个内存屏障（StoreStore LoadLoad StoreLoad LoadStore）

##### as-if-serial：

	不管硬件什么顺序，单线程执行结果不会改变，看上去像是serial



### 合并写（不重要）

write combing buffer 一般4字节  由于ALU速度太快，所以在写入L1的同时，写入一个WC Buffer ，满了之后，再直接更新到L2

![1637055719097](assets/1637055719097.png)



### NUMA

UMA：多个CPU共享同一个内存

![1637056417475](assets/1637056417475.png)

NUMA：每一组插槽有一颗CPU和相邻的内存，每颗CPU使用相邻的内存

ZGC--NUMA aware

分配内存会优先分配该线程所在CPU的最近内存

![image-20211116225941429](assets\image-20211116225941429.png)



### 启动过程（不重要）

通电->bios uefi 工作 -> 自检 -> 到硬盘固定位置加载bootloader  -> 读取可配置信息 -> CMOS

![image-20211116231604709](assets\image-20211116231604709.png)



## OS

### 内核分类

微内核：弹性部署 5G loT

宏内核：PC phone

外核：科研 实验室中 为应用定制操作系统





![image-20211116234810196](assets\image-20211116234810196.png)



![image-20211116234911375](assets\image-20211116234911375.png)



![image-20211116234940734](assets\image-20211116234940734.png)





### 

### 内核态和用户态

![1637119227495](assets/1637119227495.png)

**CPU分不同的指令级别**

**linux 内核跑在ring 0级，用户程序跑在ring 3级，对于系统的关键访问，需要经过kernel的同意，保证系统的健壮性**

**内核执行的操作 -> 200多个系统调用函数 sendfile read write pthread fork**

**JVM ->站在OS的角度，就是一个普通程序**

intel CPU分为4个级别：ring 0 - 3

linux：只用了两个级别，ring 0 ，ring 3，在内核态时可以访问ring 0级的数据，应用程序只能访问ring 3级的数据

![1637118298373](assets/1637118298373.png)



内核空间：内存内核访问的空间

用户空间：应用程序访问的空间



## 进程 线程 纤程 中断

### 面试高频：进程和线程有什么区别？

答案：进程就是一个程序运行起来的状态，线程是一个进程中的不同的执行路径。

专业回答：进程是OS分配资源的基本单位，线程是执行调度的基本单位。分配资源最重要的是：分配独立的内存空间，线程调度执行（线程共享进程的内存空间，没有自己独立的内存空间）



![1637139787181](assets/1637139787181.png)



### Fiber：纤程

纤程：用户态的线程，线程中的线程，切换和调度不需要进过OS

优势：1、占有资源很少 OS级别的线程占用1M Fiber 4k 2、切换比较简单 3、启动很多个10w+

目前2020 3 22 支持内置纤程的语言：Kotlin Scala Go Python(lib) ...Java?

目前Java中对纤程的支持：没有内置

![1637140152923](assets/1637140152923.png)

JVM（Hotspot）级别的线程和操作系统的线程是一一对应的

多个纤程对应一个JVM级别的线程，纤程是跑在用户空间的



测试代码：

```java
public class FiberTest {
    
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        int size = 10000;

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
```

目前是10000个纤程，如果想提升效率，可以分为10份，10个操作系统级别的线程，每个线程创建1000个纤程

纤程的应用场景：1、很短的计算任务，不需要和内核打交道，2、并发量比较高的时候



linux中的进程有一个内核数据结构（PCB）

![1637218863308](assets/1637218863308.png)



![1637219161610](assets/1637219161610.png)



### 进程的管理

#### 进程创建和启动

![1637219278757](assets/1637219278757.png)



#### 僵尸进程 孤儿进程

![1637219386074](assets/1637219386074.png)



zombie.c

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <assert.h>
#include <sys/types.h>

int main() {
    pid_t pid = fork();
    if(0 == pid){
        printf("child id is %d\n",getpid());
        printf("parent id is %d\n",getppid());
    }else{
        while(1){}
    }
}
```



![1637219633698](assets/1637219633698.png)



orphan.c

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <assert.h>
#include <sys/types.h>

int main() {
    pid_t pid = fork();
    if(0 == pid){
        printf("parent id is %d\n",getppid());
        sleep(10);
        printf("parent id is %d\n",getppid());
    }else{
        printf("parent id is %d\n",getpid());
        sleep(5);
        exit(0);
    }
}
```



### 进程调度

![1637221295355](assets/1637221295355.png)





![1637221308479](assets/1637221308479.png)



linux2.6 采用CFS Completely Fair Scheduler

按优先级分配时间片的比例，记录每个进程的执行时间，如果有一个进程执行时间不到它应该分配的比例，优先执行（按时间补偿）

![1637221529748](assets/1637221529748.png)



#### 进程调度基本概念

![1637222250197](assets/1637222250197.png)

#### Linux默认的调度策略

实时进程（c语言创建），优先级分高低 -> FIFO ，优先级一样的->RR（Round Robin）

普通进程 -> CFS

![1637222551281](assets/1637222551281.png)



### 中断

硬件跟操作系统内核打交道的一种机制

操作系统级别的中断

摁下一个键盘按键后，会将当前电信号交给中断控制器，中断控制器会告诉CPU有一个键盘信号来了，CPU会在某个固定位置找到执行程序，这个执行程序会通知kernel，根据这个中断信号，在中断处理程序中找到对应的程序，

![1637285766836](assets/1637285766836.png)

中断分为：硬中断，软中断

0x80软中断：软件产生的中断，用户空间的程序要调用操作系统内核空间所提供的函数，必须要软中断来向操作系统申请

系统调用号：比如（不一定准确） write-4号，exit-1号....操作系统对外提供了一系列函数（大约200多个），每个函数对应一个编号

bx cx dx si di 是寄存器的名称

例子：在java程序中写了一个read()，它是调用了c库的read()，调用了内核空间的read函数，system_call（系统调用处理程序）会找到一张表，这张表就是函数调用的表（说明在上），发现是调用read()函数，然后从对应的寄存器中将参数读取出来，最后处理程序

![1637225772692](assets/1637225772692.png)



高并发的核心处理：分而治之

#### 从汇编角度理解软中断

搭建汇编环境 yum install nasm

```assembly
;hello.asm
;write(int fd,const void *buffer,size_t nbytes)
;fd 文件描述符 file descriptor linux:一切皆文件

section data
	msg db "Hello", 0xA ;0xA 换行
	len equ $ - msg 

section .text
global _start
_start:

	mov edx, len
	mov ecx, msg
	mov ebx, 1 ;文件描述符1 std_out
	mov eax, 4 ;write函数系统调用号4
	int 0x80
	
	mov ebx, 0
	mov eax, 1 ;exit函数系统调用号
	int 0x80
```



![1637287536257](assets/1637287536257.png)

编译：nasm -f elf -g -F stabs hello.asm -o hello.o

编译成 elf文件格式（linux可执行的文件格式）编译成.o文件

链接：ld -m elf_i386 -o hello hello.o

![1637288169082](assets/1637288169082.png)



一个程序的执行过程，要么处于用户态，要么处于内核态

eg.网络编程io，客户端要从服务器读取数据（read），阻塞/非阻塞指：用户空间的read一定会到内核空间执行read，在内核空间执行read的时候，用户空间能不能继续干活。用户空间能继续干活就是非阻塞，不能继续干活就是阻塞



![1637291573032](assets/1637291573032.png)



## 内存管理



### 内存管理的发展历程

DOS 时代 -> 同一时间只能有一个进程运行（有一些特殊算法可以支持多进程）

windows9x -> 多个进程装入内存 1.内存不足 2.互相打扰

为了解决这两个问题，诞生了现在的内存管理系统：虚拟地址 分页装入 软硬件结合寻址

#### 1.分页（解决内存不够用）

内存中分成固定大小的页框（4k），把程序（硬盘上）分成4k大小的块，用到哪块加载那块，加载的过程中，如果内存已满，会把最不常用的一块放到swap分区（交换分区），把最新的一块加载进来，这个就是著名的LRU算法

​	a.LRU算法，leetcode的146题，头条要求手撕

​	b.LRU Least Recently Used 最不常用

​	c.hash表保证查找操作O(1) + 链表保证排序操作和新增操作O(1)

​	d.双向链表（保证左边的指针指向右边块）

运行qq.exe，是把qq所有的都加载进内存么，不是的，只是加载了部分块进内存

![1637304245579](assets/1637304245579.png)



解决相互打扰问题：虚拟地址空间

#### 2.虚拟内存（解决相互打扰问题）

​	1.Dos windows31 ...可以相互打扰

​	2.为了保证互不影响，让进程工作在虚拟空间，程序中用到的空间地址不再是直接的物理地址，而是虚拟地址，这样，A进程永远不可能访问到B进程的空间

​	3.虚拟空间多大呢？就是寻址空间，比如 64位操作系统，2^64 （byte），32位 ，2^32 （byte），比物理空间大得多

​	4.站在虚拟角度，进程是独享整个系统 + CPU

​	5.内存映射：偏移量 + 段的基地址 = 线性地址（虚拟空间）

​	6.线性地址通过OS + MMU（硬件 Memory Management Unit） -> 映射到真正的物理地址

![1637307900939](assets/1637307900939.png)



![1637308874934](assets/1637308874934.png)



![1637309040690](assets/1637309040690.png)





![1637309579625](assets/1637309579625.png)



#### 3.缺页中断（缺页异常）

​	需要用到的数据内存中没有，产生缺页异常（中断），由内核处理并加载	



### ZGC

算法叫做：Colored Pointer 

GC信息记录在指针上，不是记录在头部，immediate memory use

42位指针，寻址空间4T  JDK13 ->扩展为16T，目前为止最大16T 2^44（地址总线48位 - ZGC4位颜色）



![1637310351992](assets/1637310351992.png)



### CPU如何区分一个 立即数 和 一条指令

总线内部分为：数据总线、地址总线、控制总线，通过哪条总线来的区分是 立即数和指令

地址总线目前：48位

颜色指针本质上包含了地址映射的概念



