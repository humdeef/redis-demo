package com.wzq.redis.base;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * Author: zhenqing.wang <wangzhenqing1008@163.com>
 * Date: 2016-06-21 10:57:05
 * Description: Sorted Set
 */
public class JedisSortedSetDemo {

    private Jedis jedis;

    public void init() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public void add(){
        String key = "company";
        jedis.del(key);
        jedis.del("key");
        jedis.zadd(key, 1, "a");
        jedis.zadd(key, 10, "b");
        jedis.zadd(key, 5, "c");
        Set<String> set = jedis.zrange(key, 0, -1);
        System.out.println(set);
        jedis.zrem(key, "c");
        set = jedis.zrange(key, 0, -1);
        System.out.println(set);
        long len = jedis.zcard(key);
        System.out.println(len);

        jedis.zadd(key, 99, "e");
        jedis.zadd(key, 90, "d");
        long num = jedis.zcount(key, 90, 100);
        System.out.println(num);

        Double score = jedis.zscore(key, "d");
        System.out.println(score);
    }

    public void range(){
        String key = "company";
        jedis.del(key);
        jedis.zadd(key, 99, "a");
        jedis.zadd(key, 90, "b");
        jedis.zadd(key, 88, "c");
        jedis.zadd(key, 70, "d");
        jedis.zadd(key, 65, "e");
        jedis.zadd(key, 95, "f");

        Set<String> set = jedis.zrange(key, 0, -1);
        System.out.println(set);

        Set<Tuple> tupleSet = jedis.zrangeWithScores(key, 0, -1);
        System.out.println(tupleSet);

        tupleSet = jedis.zrevrangeWithScores(key, 0, -1);
        System.out.println(tupleSet);

        set = jedis.zrangeByScore(key, 90, 99);
        System.out.println(set);

        tupleSet = jedis.zrangeByScoreWithScores(key, 90, 99);
        System.out.println(tupleSet);

        set = jedis.zrevrangeByScore(key, 99, 90);
        System.out.println(set);

        tupleSet = jedis.zrevrangeByScoreWithScores(key, 99, 90);
        System.out.println(tupleSet);

    }

    public void other(){
        String key = "company";
        jedis.del(key);
        jedis.del("key");
        jedis.del("inter");
        jedis.del("union");
        jedis.zadd(key, 4000, "a");
        jedis.zadd(key, 5000, "b");
        jedis.zadd(key, 7000, "c");
        jedis.zadd(key, 8000, "d");
        Double score = jedis.zincrby(key, 2000, "a");
        System.out.println(score);

        long rank = jedis.zrank(key, "a");
        System.out.println(rank);

        rank = jedis.zrevrank(key, "a");
        System.out.println(rank);

        long num = jedis.zremrangeByRank(key, 0, 0);
        System.out.println(num);

        num = jedis.zremrangeByScore(key, 7000, 8000);
        System.out.println(num);

        jedis.zadd("key", 1000, "a");
        jedis.zadd("key", 2000, "b");
        jedis.zadd("key", 1000, "e");
        jedis.zadd("key", 1000, "f");

        System.out.println(jedis.zrange(key, 0, -1));
        System.out.println(jedis.zrange("key", 0, -1));

        jedis.zinterstore("inter", key, "key");
        System.out.println(jedis.zrangeWithScores("inter", 0, -1));

        jedis.zunionstore("union", key, "key");
        System.out.println(jedis.zrangeWithScores("union", 0, -1));
    }


    public static void main(String[] args) {
        JedisSortedSetDemo jedisSortedSetDemo = new JedisSortedSetDemo();
        jedisSortedSetDemo.init();
        jedisSortedSetDemo.other();
    }

}
