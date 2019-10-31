package com.blj.util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis连接池 工具类
 *
 * @author BaiLiJun  on 2019/10/31
 */
public class RedisUtils {

    private static JedisPool pool;

    /**
     * 访问密码
     */
    private static final String AUTH = "123456";

    /**
     * 超时时长
     */
    private static final int TIMEOUT = 6000;

    /**
     * 设置最大线程数 一个线程就是一个jedis
     */
    private static final int MAX_TOTAL = 20;

    /**
     * 设置最大空闲数
     */
    private static final int MAX_IDLE = 2;

    /**
     * 设置检查项为true，表示从线程池拿出来的对象一定是检查可用
     */
    private static final boolean TEST_ON_BORROW = true;


    /**
     * 获取Jedis实例
     *
     * @param ip
     * @param port
     * @return pool
     */
    public static JedisPool getPool(String ip, int port) {
        if (pool == null) {
            //创建JedisPoolConfig 给config设置连接池参数，使用config创建JedisPool
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setTestOnBorrow(TEST_ON_BORROW);
            return new JedisPool(config, ip, port, TIMEOUT);
        }
        return pool;
    }
    //关闭pool对象
    public static void close() {
        if (pool != null) {
            pool.close();
        }
    }


}
