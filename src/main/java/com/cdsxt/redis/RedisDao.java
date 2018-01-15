package com.cdsxt.redis;

public interface RedisDao {

    /**
     * 在 cart 中根据 key 取值
     *
     * @param key
     * @return
     */
    String getByKeyInCart(String key);

    void setCartStrToRedis(String username, String cartStr);
}
