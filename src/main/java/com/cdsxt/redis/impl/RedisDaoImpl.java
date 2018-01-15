package com.cdsxt.redis.impl;

import com.cdsxt.redis.RedisDao;
import com.cdsxt.util.RedisUtil;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Objects;

@Repository
public class RedisDaoImpl implements RedisDao {

    /**
     * 在 cart 中根据 key 取值
     *
     * @param key
     * @return
     */
    @Override
    public String getByKeyInCart(String key) {
        Jedis jedis = RedisUtil.getJedisFromSingle();
        if (Objects.nonNull(jedis)) {
            for (Map.Entry<String, String> entry : jedis.hgetAll("cart").entrySet()) {
                if (key.equals(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void setCartStrToRedis(String username, String cartStr) {
        Jedis jedis = RedisUtil.getJedisFromSingle();
        if (Objects.nonNull(jedis)) {
            jedis.hset("cart", username, cartStr);
        }
    }
}
