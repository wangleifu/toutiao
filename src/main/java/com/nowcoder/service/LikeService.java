package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 点赞、点踩 业务逻辑
 *
 * @author wangleifu
 * @create 2018-12-20 13:09
 */
@Service
public class LikeService {
    @Autowired
    private JedisAdapter jedisAdapter;

    /**
     * 获取用户喜欢的状态
     * @param userId     用户标识id
     * @param entityId   实体标识id
     * @param entityId 类型id
     * @return 如果喜欢返回1， 不喜欢返回-1， 否则返回0
     */
    public int getStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        return jedisAdapter.sismember(dislikeKey, String.valueOf(userId)) ? -1 : 0;
    }

    public long like(int userId, int entityType, int entityId) {
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdapter.srem(dislikeKey, String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    public long dislike(int userId, int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        String dislikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
        jedisAdapter.sadd(dislikeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

}
