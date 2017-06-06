package com.wzq.redis.transactions;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Author: zhenqing.wang <wangzhenqing1008@163.com>
 * Date: 2016-06-21 12:26:28
 * Description: Transactions
 */
public class JedisTransactionsDemo {

    private Jedis jedis;

    public void init() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public void transactions() {
        long start = System.currentTimeMillis();
        Transaction tx = jedis.multi();
        for (int i = 0; i < 1000; i++) {
            tx.set("tx" + i, String.valueOf(i));
        }
        List<Object> list = tx.exec();
        System.out.println(list);
        long end = System.currentTimeMillis();
        System.out.println("Transaction SET: " + ((end - start) / 1000.0) + " seconds");
        jedis.disconnect();
    }

    public static void main(String[] args) {
        JedisTransactionsDemo demo = new JedisTransactionsDemo();
        demo.init();
        demo.transactions();
    }
}
