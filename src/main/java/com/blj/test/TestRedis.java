package com.blj.test;

import com.blj.util.RedisUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * Java中使用连接池操作Redis
 *
 * @author BaiLiJun  on 2019/10/31
 */
public class TestRedis {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 6379;

    /**
     * Jedis操作Map
     */
    @Test
    public void operateMapRedisDemo() {
        //得到连接池
        JedisPool pool = RedisUtils.getPool(IP, PORT);
        //获取jedis对象
        Jedis jedis = pool.getResource();
        Map<String, String> map = new HashMap<>();
        map.put("name", "jack");
        map.put("age", "23");
        map.put("address", "home");
        map.put("friend", "tom");
        map.put("class", "1班");
        //将map置入redis中
        jedis.hmset("info", map);

        //hmget第一个参数为map的键，后面的为map中对象的键，可添加多个
        List<String> hmget = jedis.hmget("info", "name", "age", "class");
        for (String s : hmget) {
            System.out.println("s = " + s);
        }
        //运行结果 jack 23 1班

        jedis.hdel("info", "address");
        System.out.println("删除address后为：" + jedis.hmget("info", "address"));
        //运行结果：删除address后为：[null]

        System.out.println("共" + jedis.hlen("info") + "个元素");
        //运行结果：共4个元素

        System.out.println("是否存在：" + jedis.exists("info"));
        //运行结果：是否存在:true

        System.out.println("所有的键" + jedis.hkeys("info"));
        //运行结果：所有的键[name, friend, class, age]

        System.out.println("所有的值" + jedis.hvals("info"));
        //运行结果：所有的值[23, jack, tom, 1班]

        Iterator<String> iterator = jedis.hkeys("info").iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            System.out.println("key = " + key + ", value = " + jedis.hmget("info", key));
        }
        //运行结果
        // key = name,      value = [jack]
        // key = friend,    value = [tom]
        //key = class,      value = [1班]
        //key = age,        value = [23]

    }


    /**
     * Jedis操作List
     */
    @Test
    public void operateListRedisDemo(){
        //得到连接池
        JedisPool pool = RedisUtils.getPool(IP, PORT);
        //获取jedis对象
        Jedis jedis = pool.getResource();
        //清空myList的值
        jedis.del("myList");
        //lpush为在list头部插入元素，rpush为在list尾部插入元素
        jedis.lpush("myList","1","2");
        jedis.lpush("myList","3","4");
        jedis.lpush("myList","5","6");
        jedis.lpush("myList","7","8");
        //其中0为起始位置，end=-1时为全部
        System.out.println("myList = " + jedis.lrange("myList",0,-1));
        //运行结果：myList = [8, 7, 6, 5, 4, 3, 2, 1]

        //清空myList的值
        jedis.del("myList");
        //lpush为在list头部插入元素，rpush为在list尾部插入元素
        jedis.rpush("myList","1","2");
        jedis.rpush("myList","3","4");
        jedis.rpush("myList","5","6");
        jedis.rpush("myList","7","8");
        //其中0为起始位置，end=-1时为全部
        System.out.println("myList = " + jedis.lrange("myList",0,-1));
        //运行结果：myList = [1, 2, 3, 4, 5, 6, 7, 8]

        //清空myList的值
        jedis.del("myList");
        //lpush为在list头部插入元素，rpush为在list尾部插入元素
        jedis.lpush("myList","12","27");
        jedis.rpush("myList","14","56");
        jedis.rpush("myList","43","22");
        jedis.sort("myList");
        //其中0为起始位置，end=-1时为全部
        System.out.println("myList = " + jedis.lrange("myList",0,-1));
        //运行结果：myList = [27, 12, 14, 56, 43, 22]
    }

    /**
     * Jedis操作Set
     */
    @Test
    public void operateSetRedisDemo(){
        //得到连接池
        JedisPool pool = RedisUtils.getPool(IP, PORT);
        //获取jedis对象
        Jedis jedis = pool.getResource();
        jedis.sadd("set","1");
        jedis.sadd("set","2");
        jedis.sadd("set","3");
        jedis.sadd("set","4");
        jedis.sadd("set","5");
        //显示所有的元素
        System.out.println(jedis.smembers("set"));
        //运行结果：[1, 2, 3, 4, 5]

        System.out.println("集合的元素个数 = " + jedis.scard("set"));
        //运行结果：集合的元素个数 = 5

        //移除set集合中的元素5
        jedis.srem("set","5");

        System.out.println("元素5是否在集合中："+jedis.sismember("set","5"));
        //运行结果：元素5是否在集合中：false

        System.out.println("显示所有元素："+jedis.smembers("set"));
        //运行结果：显示所有元素：[1, 2, 3, 4]

    }

    /**
     * Jedis操作Sorted Set有序集合
     */
    @Test
    public void operateSortedSetRedisDemo(){
        //得到连接池
        JedisPool pool = RedisUtils.getPool(IP, PORT);
        //获取jedis对象
        Jedis jedis = pool.getResource();
        //根据“第二个参数（score）”进行排序
        jedis.zadd("user",20,"tom");
        jedis.zadd("user",21,"jack");
        jedis.zadd("user",23,"lily");
        jedis.zadd("user",19,"king");
        Set<String> user = jedis.zrange("user", 0, -1);
        System.out.println("user = " + user.toString());
        //运行结果：user = [king, tom, jack, lily]

        System.out.println("查询当前set集合的元素个数:" + jedis.zcard("user"));
        //运行结果：查询当前set集合的元素个数:4

        System.out.println("users集合里面，score 值在min和max之间（包括）元素个数:"+jedis.zcount("user",10,20));
        //运行结果：users集合里面，score 值在min和max之间（包括）元素个数:2

        //移除user集合中1个或多个元素
        Long zrem = jedis.zrem("user", "tom", "jack");
        System.out.println("zrem = " + zrem);
        if(zrem>0){
            System.out.println("删除成功，当前集合的元素个数为：" + jedis.zcard("user"));
        }
        //运行结果：删除成功，当前集合的元素个数为：2

        Long i = jedis.zremrangeByScore("user", 10, 20);
        if(i>0){
            System.out.println("删除成功，当前集合的元素个数为：" + jedis.zcard("user"));
        }
        //运行结果：删除成功，当前集合的元素个数为：1

        //移除users当中第0到第1之间的元素（包含），也可以是负数：以 -1 表示最后一个成员， -2 表示倒数第二个成员
        Long num = jedis.zremrangeByRank("user", 0, 1);
        System.out.println("num = " + num);
        if(num>0){
            System.out.println("删除成功，当前集合的元素个数为：" + jedis.zcard("user"));
        }
        //运行结果：删除成功，当前集合的元素个数为：0


    }

}



