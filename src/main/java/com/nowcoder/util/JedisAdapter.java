package com.nowcoder.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class JedisAdapter implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;
    public static void print(int index , Object obj) {
        System.out.println(String.format("%d , %d", index, obj.toString()));
    }

    public static void main(String[] args){
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        jedis.flushDB();

        //get set
        jedis.set("hello" , "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello" , "newhello");
        print(1, jedis.get("newhello"));
        jedis.setex("hello2" , 15 , "world");

        jedis.set("pv", "100");
        jedis.incr("pv");
        jedis.incrBy("pv" , 5);
        print(2 ,jedis.get("pv"));
        jedis.decrBy("pv" , 2);
        print(2 ,jedis.get("pv"));

        String listName ="list";
        jedis.del(listName);
        for (int i=0 ; i < 10 ; ++i) {

            jedis.lpush(listName, "a" + String.valueOf(i));

        }
        print(4 , jedis.lrange(listName,0 ,12));
        print(4 , jedis.lrange(listName,0 ,3));
        print(4 , jedis.llen(listName));
        print(6 , jedis.lpop(listName));
        print(4 , jedis.llen(listName));
        print(8, jedis.lrange(listName, 2, 6));
        print(9, jedis.lindex(listName, 3));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "bb"));
        print(11, jedis.lrange(listName, 0 ,12));

        //hash
        String userKey = "userxx";
        jedis.hset(userKey , "name" , "Jim");
        jedis.hset(userKey , "age" , "12");
        jedis.hset(userKey , "phone" , "18618181818");
        jedis.hget(userKey , "name");
        jedis.hgetAll(userKey);
        jedis.hdel(userKey , "phone");
        jedis.hsetnx(userKey , "school", "zju");
        jedis.hsetnx(userKey , "name", "jim");
        //如果存在，则不写入

        //set
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike1";
        for (int i = 0 ; i < 10 ; ++i ) {
            jedis.sadd(likeKey1 , String.valueOf(i));
            jedis.sadd(likeKey2 , String.valueOf(i*i));
        }
        print(20, jedis.smembers(likeKey1));
        print(20, jedis.smembers(likeKey2));
        print(20, jedis.sunion(likeKey1 , likeKey2));
        print(20, jedis.sdiff(likeKey1 , likeKey2));
        print(20, jedis.sinter(likeKey1 , likeKey2));
        print(20, jedis.sismember(likeKey1 , "12"));
        print(20, jedis.sismember(likeKey2 , "16"));
        jedis.srem(likeKey1 , "5");
        jedis.smove(likeKey2 , likeKey1 , "25");
        jedis.srandmember("commentLike1" , 3);//可以用在抽奖里面

        //优先队列
        String rankKey = "rankKey";
        jedis.zadd(rankKey , 15 , "jim");
        jedis.zadd(rankKey , 60 , "Ben");

        JedisPool pool = new JedisPool();


        User user = new User();
        user.setName("xx");
        user.setPassword("ppp");
        user.setHeadUrl("a.png");
        user.setSalt("salt");
        user.setId(1);
        print(46, JSONObject.toJSONString(user));
        jedis.set("user1" , JSONObject.toJSONString(user));

      String value = jedis.get("user1");
        User user2 = JSON.parseObject(value , User.class);
        print(47 , user2);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    public long sadd(String key , String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.sadd(key, value);
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }finally {
            if ( jedis!= null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key , String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }finally {
            if ( jedis!= null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }finally {
            if ( jedis!= null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sisrember(String key , String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }finally {
            if ( jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
}
