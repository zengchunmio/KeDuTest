package utils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public class JedisUtil {
    public static JedisCluster jedisCluster = null;
    public static Integer maxTotal = 60000; // 最大连接数
    public static Integer maxIdle = 1000; // 最大空闲数
    public static Integer maxWaitMillis = 3000;// 超时时间

    public static void getRedisCluster(String clusterNodes, String password) {
        //分割出集群节点
        String[] cNodes = clusterNodes.split(",");
        Set<HostAndPort> nodes = new HashSet<>();
        for (String node : cNodes) {
            String[] ipAndPort = node.split(":");
            nodes.add(new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1])));
        }

        // 配置连接池
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        // 连接Redis集群
        jedisCluster = new JedisCluster(nodes, 3000, 3000, 5, password, jedisPoolConfig);
    }

    public String getValue(String key) {
        return jedisCluster.get(key);
    }

    public static void main(String[] args) {
        //Redis信息
        String nodes = "172.16.43.127:6379";
        String password = "123456";
        String key = "13764764202";

        //连接Redis
        JedisUtil jmeterRedisClusterUtil = new JedisUtil();
        try {
            jmeterRedisClusterUtil.getRedisCluster(nodes, password);
        } catch (Throwable e) {
            throw e;
        }

        //获取数据
//        String value = jmeterRedisClusterUtil.getValue(key);
//        System.out.println(value);

        String s = jedisCluster.get("SELLER_PHONE_CAPTCHA_LOGIN_15221888544");
        System.out.println(s);

    }
}
