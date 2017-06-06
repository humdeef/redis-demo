package com.wzq.redis.base;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Author: zhenqing.wang <wangzhenqing1008@163.com>
 * Date: 2016-06-20 19:52:22
 * Description: List
 */
public class JedisListDemo {

    private Jedis jedis;

    public void init() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public void push() {
        String key = "company";
        jedis.del(key);
        long len = jedis.lpush(key, "baidu", "tencent", "taobao"); //lpush and rpush
        System.out.println(len);
        List<String> list = jedis.lrange(key, 0, -1);
        System.out.println(list);

        len = jedis.lpush(key, "cc");
        System.out.println(len);

        len = jedis.rpush(key, "a", "b", "c");
        System.out.println(len);
        list = jedis.lrange(key, 0, -1);
        System.out.println(list);

        len = jedis.lpushx(key, "key");
        System.out.println(len);
        len = jedis.rpushx(key, "key");
        System.out.println(len);

        len = jedis.lpushx("key", "key");
        System.out.println(len);

        len = jedis.rpushx("key", "key");
        System.out.println(len);
    }

    public void pop() {
        String key = "company";
        System.out.println(jedis.lrange(key, 0, -1));
        String top = jedis.lpop(key);
        System.out.println(top);
        top = jedis.rpop(key);
        System.out.println(top);

        List<String> list = jedis.blpop(1000, key);
        System.out.println(list);
        jedis.rpush(key, "blpop");
    }

    public void other() {
        String key = "company";
        long num = jedis.lrem(key, 0, "key");
        System.out.println(num);
        jedis.lset(key, 0, "black"); //"OK"
        String value = jedis.lindex(key, 0);
        System.out.println(value);
        List<String> list = jedis.lrange(key, 0, -1);
        System.out.println(list);
        jedis.ltrim(key, 0, -2);
        list = jedis.lrange(key, 0, -1);
        System.out.println(list);

        jedis.linsert(key, BinaryClient.LIST_POSITION.BEFORE, "baidu", "didi");
        list = jedis.lrange(key, 0, -1);
        System.out.println(list);

        jedis.linsert(key, BinaryClient.LIST_POSITION.AFTER, "baidu", "didi");
        list = jedis.lrange(key, 0, -1);
        System.out.println(list);

        jedis.rpoplpush(key, "key");
        System.out.println(jedis.lrange(key, 0, -1));
        System.out.println(jedis.lrange("key", 0, -1));

    }

    public static void main(String[] args) {
        JedisListDemo jedisListDemo = new JedisListDemo();
        jedisListDemo.init();
        jedisListDemo.push();
        jedisListDemo.other();
    }
}
