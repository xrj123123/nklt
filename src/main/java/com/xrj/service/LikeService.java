package com.xrj.service;

import com.xrj.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 点赞
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        stringRedisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                boolean isMember = operations.opsForSet().isMember(entityLikeKey, String.valueOf(userId));
                // 先执行查询，然后开启事务
                operations.multi();
                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, String.valueOf(userId));
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, String.valueOf(userId));
                    operations.opsForValue().increment(userLikeKey);
                }
                // 提交事务
                return operations.exec();
            }
        });
    }

    // 查询某实体点赞的数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return stringRedisTemplate.opsForSet().size(entityLikeKey);
    }

    // 查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return stringRedisTemplate.opsForSet().isMember(entityLikeKey, String.valueOf(userId)) ? 1 : 0;
    }

    // 查询某个用户获得的赞
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        String s = stringRedisTemplate.opsForValue().get(String.valueOf(userLikeKey));
        return s == null ? 0 : Integer.parseInt(s);
    }
}
