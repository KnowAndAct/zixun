package com.example.toutiao.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.List;


@Service
public class JedisAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool = null;

    public static void print(int index, Object obj) {
        System.out.println(String.format("%d,%s", index, obj.toString()));
    }


    public static void mainx(String[] args) {
        Jedis jedis = new Jedis();
        jedis.flushAll();
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newhello");
        print(2, jedis.get("newhello"));
        jedis.setex("hello2", 15, "world");
        jedis.set("pv", "100");
        jedis.incr("pv");
        print(2, jedis.get("pv"));
        jedis.incrBy("pv", 5);
        print(2, jedis.get("pv"));

        //列表操作
        String listName = "listA";
        for (int i = 0; i < 10; ++i) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(3, jedis.lrange(listName, 0, 12));
        print(4, jedis.llen(listName));
        print(5, jedis.lpop(listName));
        print(6, jedis.llen(listName));
        print(7, jedis.lindex(listName, 3));
        print(8, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));
        print(9, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "bb"));
        print(10, jedis.lrange(listName, 0, 12));

        //map
        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "18618181818");

        print(11, jedis.hget(userKey, "name"));
        print(12, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        print(13, jedis.hgetAll(userKey));
        print(14, jedis.hkeys(userKey));
        print(15, jedis.hvals(userKey));
        print(16, jedis.hexists(userKey, "email"));
        print(17, jedis.hexists(userKey, "age"));
        jedis.hsetnx(userKey, "school", "swjtu");
        jedis.hsetnx(userKey, "name", "tk");
        print(18, jedis.hgetAll(userKey));

        //set
        String likeKeys1 = "newsLike1";
        String likeKeys2 = "newsLike2";
        for (int i = 0; i < 10; ++i) {
            jedis.sadd(likeKeys1, String.valueOf(i));
            jedis.sadd(likeKeys2, String.valueOf(i * 2));
        }
        print(19, jedis.smembers(likeKeys1));
        print(20, jedis.smembers(likeKeys2));
        print(21, jedis.sinter(likeKeys1, likeKeys2));
        print(22, jedis.sunion(likeKeys1, likeKeys2));
        print(23, jedis.sdiff(likeKeys1, likeKeys2));
        print(25, jedis.sismember(likeKeys1, "5"));
        jedis.srem(likeKeys1, "5");
        print(26, jedis.smembers(likeKeys1));
        print(27, jedis.scard(likeKeys1));
        jedis.smove(likeKeys2, likeKeys1, "14");
        print(28, jedis.scard(likeKeys1));
        print(29, jedis.smembers(likeKeys1));

        //sort set
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "jim");
        jedis.zadd(rankKey, 60, "Ben");
        jedis.zadd(rankKey, 90, "Lee");
        jedis.zadd(rankKey, 80, "Mei");
        jedis.zadd(rankKey, 75, "Lucy");
        print(30, jedis.zcard(rankKey));
        print(31, jedis.zcount(rankKey, 61, 100));
        print(32, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Lucy");
        print(33, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Luc");
        print(34, jedis.zcount(rankKey, 0, 100));
        print(35, jedis.zrange(rankKey, 1, 3));
        print(36, jedis.zrevrange(rankKey, 1, 3));

        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, 0, 100)) {
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }
        print(37, jedis.zrank(rankKey, "Ben"));
        print(38, jedis.zrevrank(rankKey, "Ben"));

        //jedis pool
        JedisPool pool = new JedisPool();
        for (int i = 0; i < 100; i++) {
            Jedis j = pool.getResource();
            j.get("a");
            System.out.println("POOL" + i);
            j.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379);

    }

    private Jedis getJedis() {
        return pool.getResource();
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if (value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
