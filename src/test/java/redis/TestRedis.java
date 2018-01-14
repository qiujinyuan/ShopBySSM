package redis;

import org.testng.annotations.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class TestRedis {


    @Test
    public void testGet() {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.hset("cart", "zhangsan", "[{pid:'1', pname:'裤子'}]");
        jedis.hset("cart", "lisi", "[{pid:'2', pname:'衣服'}]");
        jedis.hset("cart", "wangwu", "[{pid:'3', pname:'大衣'}]");
        System.out.println(jedis.hgetAll("cart"));

        for (Map.Entry<String, String> entry : jedis.hgetAll("cart").entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
    }
}
