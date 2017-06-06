package com.id.java;

import java.util.Random;

/**
 * Created by trli on 2017/6/6.
 */
public class IdWorker {

    private final long workerId;
    private final static long twepoch = 1288834974657L;
    private long sequence = 0L;
    private final static long workerIdBits = 10L;
    public final static long maxWorkerId = -1L ^ -1L << workerIdBits;
    private final static long sequenceBits = 10L;
    private final static long workerIdShift = sequenceBits;
    private final static long timestampLeftShift = sequenceBits + workerIdBits;
    public final static long sequenceMask = -1L ^ -1L << sequenceBits;
    private long lastTimestamp = -1L;

    public IdWorker(final long workerId) {
//        System.out.println(this.maxWorkerId);
//        System.out.println();
        if (workerId > this.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format(
                    "worker Id can't be greater than %d or less than 0",
                    this.maxWorkerId));
        }
        this.workerId = workerId;
    }
    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & this.sequenceMask;
            if (this.sequence == 0) {
                System.out.println("###########" + sequenceMask);
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            try {
                throw new Exception(
                        String.format(
                                "Clock moved backwards. Refusing to generate id for %d milliseconds",
                                this.lastTimestamp - timestamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.lastTimestamp = timestamp;
        long nextId = ((timestamp - twepoch << timestampLeftShift))
                | (this.workerId << this.workerIdShift) | (this.sequence);
        System.out.println("timestamp:" + timestamp + ",timestampLeftShift:"
                + timestampLeftShift + ",nextId:" + nextId + ",workerId:"
                + workerId + ",sequence:" + sequence);
        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }



    public static void main(String[] args){
       ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

        for(int i=0;i<1000;i++) {
            singleThreadExecutor.execute(new Runnable() {
                public void run() {
//                    IdWorker idWorker = new IdWorker(new Random().nextInt(1024));
                    IdWorker idWorker = new IdWorker(1);
                    try {
                        Thread.sleep(1);
                        System.out.println(idWorker.nextId());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

}
