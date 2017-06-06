package com.wzq.redis.cluster;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Author: zhenqing.wang <wangzhenqing1008@163.com>
 * Date: 2016-08-10 16:51:12
 * Description: redis集群请求
 */
public class JedisClusterDemo {

    private JedisCluster jedisCluster;

    public void init() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("127.0.0.1",6379));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1",6380));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1",6381));
        jedisCluster = new JedisCluster(jedisClusterNodes);
    }

    public TreeSet<String> keys(){
        System.out.println("Start getting keys...");
        TreeSet<String> keys = new TreeSet<String>();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for(String k : clusterNodes.keySet()){
            System.out.println("Getting keys from: "+ k);
            if (k.split(":")[1].compareTo("6381") > 0){
                continue;
            }
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = jp.getResource();
            try {
                keys.addAll(connection.keys("ec*"));
                System.out.println(connection.get("ec:commodity:basic:9004"));
            } catch(Exception e){
                System.err.println("Getting keys error: "+e);
            } finally{
                System.out.println("Connection closed.");
                connection.close();
            }
        }
        System.out.println("Keys gotten!");
        return keys;
    }

    public static void main(String[] args) {
        JedisClusterDemo jedisClusterDemo = new JedisClusterDemo();
        jedisClusterDemo.init();
        TreeSet<String> set = jedisClusterDemo.keys();
        System.out.println(set.size());
    }
}
