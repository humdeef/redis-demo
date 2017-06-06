package com.wzq.redis.base;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: zhenqing.wang <wangzhenqing1008@163.com>
 * Date: 2016-06-21 11:59:22
 * Description: Hash
 */
public class JedisHashDemo {
    private Jedis jedis;

    public void init() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public void setAndget(){
        String key = "company";
        jedis.del(key);
        jedis.del("key");
        jedis.hset(key, "name", "wangzhenqing");

        String value = jedis.hget(key, "name");
        System.out.println(value);

        jedis.hsetnx(key, "name", "wangzq");
        value = jedis.hget(key, "name");
        System.out.println(value);

        jedis.hsetnx("key", "name", "wangzq");
        value = jedis.hget("key", "name");
        System.out.println(value);

        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "wangzq");
        map.put("class", "18");
        map.put("company", "google");
        jedis.hmset(key, map);

        List<String> list = jedis.hmget(key, "name", "class");
        System.out.println(list);

        Map<String, String> allMap = jedis.hgetAll(key);
        System.out.println(map);
    }

    public void other(){
        String key = "company";
        jedis.del(key);
        jedis.del("key");
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "wangzq");
        map.put("class", "18");
        map.put("company", "google");
        jedis.hmset(key, map);

        long len = jedis.hlen(key);
        System.out.println(len);

        Set<String> set = jedis.hkeys(key);
        System.out.println(set);

        List<String> valueList = jedis.hvals(key);
        System.out.println(valueList);

        boolean flag = jedis.hexists(key, "class");
        System.out.println(flag);

        long num = jedis.hincrBy(key, "class", 1000);
        System.out.println(num);

        jedis.hdel(key, "class");
        System.out.println(jedis.hget(key, "class"));

    }

    public static void main(String[] args) {
        JedisHashDemo jedisHashDemo = new JedisHashDemo();
        jedisHashDemo.init();
        jedisHashDemo.other();
    }
}
