package redis;

import com.cdsxt.mangodb.impl.ChatInfoDaoImpl;
import com.cdsxt.redis.RedisDao;
import com.cdsxt.redis.impl.RedisDaoImpl;
import com.cdsxt.ro.ChatInfo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.testng.annotations.Test;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Map;

public class TestRedis {


    // @Test
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

    // @Test
    public void testGeyByKey() {
        RedisDao rd = new RedisDaoImpl();
        System.out.println(rd.getByKeyInCart("zen"));
    }


    // @Test
    public void testMongoDb() {
        MongoClient mc = new MongoClient("localhost", 27017);
        MongoDatabase database = mc.getDatabase("test");
        MongoCollection<Document> chatInfo = database.getCollection("chat_info");
        for (Document doc : chatInfo.find()) {
            if ((doc.get("sendDate").getClass()).equals(Double.class)) {
                System.out.println(new Date(doc.getDouble("sendDate").longValue()));
            } else {
                System.out.println(new Date(doc.getLong("sendDate")));
            }
        }
        // System.out.println(chatInfo.count());
    }

    @Test
    public void test1() {
        ChatInfoDaoImpl ci = new ChatInfoDaoImpl();
        // System.out.println(ci.countChatInfoNoRead(2));
        // System.out.println(ci.countChatInfoTwoUserNoRead(11,1));
        //
        // ChatInfo chatInfo = new ChatInfo();
        // chatInfo.setMsgContent("测试消息");
        // chatInfo.setRead(false);
        // chatInfo.setSendUserId(11);
        // chatInfo.setReceiveUserId(2);
        // ci.saveChatInfo(chatInfo);
        //11 1
        ci.updateReadChatInfoTwoUser(11,1);
    }
}
