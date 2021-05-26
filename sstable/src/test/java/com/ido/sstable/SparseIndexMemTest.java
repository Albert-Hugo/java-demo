package com.ido.sstable;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ido
 * @date 2020/9/1 14:43
 */
public class SparseIndexMemTest {

    @Test
    public void testSearch() throws IOException {
        SparseIndexMem indexMem = new SparseIndexMem(new SegmentFile("test.seg"));

        String k = "cPFSUbYv";
        //qcK43s0JxrbDCaVgdzX2

        System.out.println(indexMem.search(k).getVal());
        ;
    }


    public static void main(String[] args) {
        ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = reentrantLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = reentrantLock.writeLock();
        writeLock.lock();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        readLock.lock();
                        System.out.println("reading ");
                        Thread.sleep(2000);
                        System.out.println("read complete");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        readLock.unlock();
                    }
                }
            });
        }

        executorService.shutdown();
    }

    @Test
    public void testLock() throws InterruptedException {


    }
}
