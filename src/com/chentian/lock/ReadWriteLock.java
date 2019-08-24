package com.chentian.lock;

public class ReadWriteLock {

    private int readCount = 0;
    private int writeCount = 0;

    public synchronized void lockRead() throws InterruptedException{
        while (writeCount > 0){
            wait();
        }
        readCount++;
    }

    public synchronized void unLockRead() throws InterruptedException{
        readCount--;
        notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException{
        while (writeCount>0){
            wait();
        }
        writeCount++;

        while (readCount>0){
            wait();
        }
    }

    public synchronized void unLockWrite() throws InterruptedException{
        writeCount--;
        notifyAll();
    }

}
