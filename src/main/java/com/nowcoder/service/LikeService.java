
package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public long getLikeCount(int entityType , int entityId) {
        String likeKey = RedisKeyUtil.getIikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }

    public int getLikeStatus(int userId , int entityType , int entityId){
        String likeKey = RedisKeyUtil.getIikeKey(entityType, entityId);
        if(jedisAdapter.sisrember(likeKey , String.valueOf(userId))) {
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisIikeKey(entityType, entityId);
        return jedisAdapter.sisrember(disLikeKey , String.valueOf(userId)) ? -1 : 0;
    }

    public long like(int userId , int entityType , int entityId) {
        String likeKey = RedisKeyUtil.getIikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey , String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisIikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey , String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId , int entityType , int entityId) {
        String disLikeKey = RedisKeyUtil.getDisIikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey , String.valueOf(userId));

        String likeKey = RedisKeyUtil.getIikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey , String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    
}
