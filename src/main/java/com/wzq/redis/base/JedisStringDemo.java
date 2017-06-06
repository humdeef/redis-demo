package com.wzq.redis.base;

import com.google.gson.Gson;
import com.wzq.redis.User;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Author: zhenqing.wang <wangzhenqing1008@163.com>
 * Date: 2016-06-20 15:43:27
 * Description: Jedis基本用法
 */
public class JedisStringDemo {

    private Jedis jedis;

    @Before
    public void init() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public void getAndSet() {
        //get and set
        String res = jedis.set("name", "wangzq"); //set值，成功返回ok
        System.out.println(res); //"OK"
        res = jedis.get("name"); //获取key为name的value值
        System.out.println(res); //"wangzq"

        res = jedis.getSet("name", "what's your name"); //将key赋为新值并返回旧值
        System.out.println(res); //"wangzq"
        res = jedis.get("name");
        System.out.println(res); //"what's your name"
    }

    public void mgetAndmset() {
        String res = jedis.mset("name", "wangzq", "age", "28", "job", "engineer"); //set多个key值
        System.out.println(res); //"OK"
        List<String> resList = jedis.mget("name", "age");
        System.out.println(resList); //"[wangzq, 28]"
    }

    public void incrAnddecr() {
        String res = jedis.set("current", "2"); //需要赋值为数字
        System.out.println(res); //"OK"
        long num = jedis.incr("current");
        System.out.println(num); //3
        System.out.println(jedis.get("current")); //3

        num = jedis.incrBy("current", 3);
        System.out.println(num); //6

        num = jedis.decr("current");
        System.out.println(num); //5
        num = jedis.decrBy("current", 3);
        System.out.println(num); //2

    }

    public void appendAndSubstr() {
        jedis.append("name", "zhenqing");
        System.out.println(jedis.get("name"));

        String res = jedis.substr("name", 0, 5);
        System.out.println(res);
    }

    public void other() {
        jedis.del("name");
        System.out.println(jedis.get("name"));
        jedis.setnx("name", "qingqing");
        System.out.println(jedis.get("name"));

        //设置超时时间
        jedis.setex("name", 1, "qq");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(jedis.get("name"));
    }

    //设置锁
    public boolean acquireLock(String lock, long expire) {
        long value = System.currentTimeMillis() + expire * 1000 + 1;
        long acquire = jedis.setnx(lock, String.valueOf(value));
        if (acquire == 1) { //可以设置，说明获得锁
            return true;
        }
        String currentValue = jedis.get(lock);
        if (currentValue != null && Long.valueOf(currentValue) < System.currentTimeMillis()) {
            //如果currentValue超过当前时间，说明被其他线程设置锁了，获取不到
            String oldValue = jedis.getSet(lock, String.valueOf(value));
            if (oldValue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }

    //释放锁
    public void releaseLock(String lock){
        long currentTime = System.currentTimeMillis();
        if (currentTime < Long.parseLong(jedis.get(lock))){
            jedis.del(lock);
        }
    }



//    public static void main(String[] args) {
//        JedisStringDemo jedisDemo = new JedisStringDemo();
//        jedisDemo.init();
//        jedisDemo.other();
//    }

    @Test
    public void testSetUser() {
//        JedisStringDemo jedisDemo = new JedisStringDemo();
//        jedisDemo.init();
        User user = new User();
        user.setAge(25);
        user.setId(1);
        user.setName("aa");
        Gson gson = new Gson();
        String json = gson.toJson(user);
        jedis.setnx("user1", json);
        System.out.println(jedis.get("user1"));
    }

    @Test
    public void setExpireTime(){
        JedisStringDemo jedisDemo = new JedisStringDemo();
        jedisDemo.init();
//        jedis.setex("user", 1, "qq");
        System.out.println(jedis.expire("user1",20));
    }

    @Test
    public void getUser(){
        JedisStringDemo jedisDemo = new JedisStringDemo();
        jedisDemo.init();
        Gson gson = new Gson();
        String json = jedis.get("user1");
        System.out.println("result:"+json);
        User user = gson.fromJson(json,User.class);
        System.out.println(user);
    }
}
