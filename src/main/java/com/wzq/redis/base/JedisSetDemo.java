package com.wzq.redis.base;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Author: zhenqing.wang <wangzhenqing1008@163.com>
 * Date: 2016-06-21 10:36:39
 * Description: Set
 */
public class JedisSetDemo {
    private Jedis jedis;

    public void init() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public void add() {
        String key = "company";
        jedis.del(key);
        jedis.del("key");
        long num = jedis.sadd(key, "a", "a", "a");
        System.out.println(num);
        jedis.sadd(key, "b", "c", "d");
        Set<String> set = jedis.smembers(key);
        System.out.println(set);
        jedis.srem(key, "d");
        set = jedis.smembers(key);
        System.out.println(set);
        boolean flag = jedis.sismember(key, "a");
        System.out.println(flag);

        System.out.println(jedis.scard(key));

        jedis.smove(key, "key", "a");
        System.out.println(jedis.smembers("key"));

        String value = jedis.spop(key);
        System.out.println(value);

        value = jedis.srandmember(key);
        System.out.println(value);
    }

    public void other() {
        String key = "company";
        jedis.del(key);
        jedis.del("key");
        jedis.sadd(key, "a", "b", "c");
        jedis.sadd("key", "s", "x", "y", "a");
        Set<String> set = jedis.sinter(key, "key");
        System.out.println(set);

        jedis.sinterstore("res", key, "key");
        System.out.println(jedis.smembers("res"));

        set = jedis.sunion(key, "key");
        System.out.println(set);
        jedis.sunionstore("union", key, "key");
        System.out.println(jedis.smembers("union"));

        set = jedis.sdiff(key, "key");
        System.out.println(set);
        jedis.sdiffstore("diff", key, "key");
        System.out.println(jedis.smembers("diff"));
    }

    public static void main(String[] args) {
        JedisSetDemo jedisSetDemo = new JedisSetDemo();
        jedisSetDemo.init();
        jedisSetDemo.other();
    }
}
