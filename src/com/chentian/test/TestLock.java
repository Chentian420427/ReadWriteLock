package com.chentian.test;

import com.chentian.lock.ReadWriteLock;

import java.util.Random;

public class TestLock {

    public static void main(String[] args) {

        final LocalReadWriterTest lrw=new LocalReadWriterTest();
        for(int i=0;i<3;i++){//开三个线程进行读取
            new Thread(){
                public void run(){
                    try {
                        lrw.readData();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        for(int i=0;i<3;i++){//开三个线程进行写入
            Thread t=new Thread(){
                public void run(){
                    try {
                        lrw.writeData(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
		/*在 writer 释放写入锁定时，reader 和 writer 都处于等待状态，在这时要确定是授予读取锁定还是授予写入锁定。
		  Writer 优先比较普遍，因为预期写入所需的时间较短并且不那么频繁。Reader 优先不太普遍，因为如果 reader
		     正如预期的那样频繁和持久，那么它将导致对于写入操作来说较长的时延。公平或者“按次序”实现也是有可能的。 */
            t.setPriority(7);//让"写"有优先的能力,因为如果读的时间太多或频繁的话，写就会一直延时等待，这样不利于及时性
            t.start();
        }

    }



    static class LocalReadWriterTest{
        ReadWriteLock readWriteLock = new ReadWriteLock();
        private Integer data;
        public void readData() throws InterruptedException{
            readWriteLock.lockRead();//在读的操作中，可以有多个线程同时进行
            System.out.println(Thread.currentThread().getName()+" :开始读取....");
            Thread.sleep(1000);
            try{
                System.out.println(Thread.currentThread().getName()+" :"+data);
                System.out.println(Thread.currentThread().getName()+" :读取完毕....");
            }finally{
                readWriteLock.unLockRead();
            }
        }
        public void writeData(int data) throws InterruptedException{
            readWriteLock.lockWrite();//在写的操作中，只能有一个线程进行，即独立完成整个写入的过程
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName()+" :开始写入....");
            try{
                this.data=data;
                System.out.println(Thread.currentThread().getName()+" 写入的值:"+data);
                System.out.println(Thread.currentThread().getName()+" :写入完毕....");
            }finally{
                readWriteLock.unLockWrite();
            }
        }
    }
}




