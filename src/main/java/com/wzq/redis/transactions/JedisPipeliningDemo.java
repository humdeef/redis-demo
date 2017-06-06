package com.wzq.redis.transactions;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.List;

/**
 * Author: zhenqing.wang <wangzhenqing1008@163.com>
 * Date: 2016-06-21 12:31:07
 * Description: Pipelining
 */
public class JedisPipeliningDemo {

    private Jedis jedis;

    public void init() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public void pipelining(){
        long start = System.currentTimeMillis();
        Pipeline pipeline = jedis.pipelined();
        for (int i = 0; i < 1000; i++) {
            pipeline.set("tx" + i, String.valueOf(i));
        }
        List<Object> list = pipeline.syncAndReturnAll();
        System.out.println(list);
        long end = System.currentTimeMillis();
        System.out.println("Transaction SET: " + ((end - start) / 1000.0) + " seconds");
        jedis.disconnect();
    }

    public static void main(String[] args) {
        JedisPipeliningDemo demo = new JedisPipeliningDemo();
        demo.init();
        demo.pipelining();
    }
}
