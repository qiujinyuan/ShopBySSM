package com.cdsxt.mangodb.impl;

import com.cdsxt.mangodb.ChatInfoDao;
import com.cdsxt.ro.ChatInfo;
import com.cdsxt.util.BeanDocumentUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ChatInfoDaoImpl implements ChatInfoDao {

    @Autowired
    private MongoClient mongoClient;

/*    static {
        mongoClient = new MongoClient();
    }*/

    // 只需要操作 chat_info 集合即可
    private MongoCollection<Document> getCollection(String collectionName) {
        MongoDatabase database = mongoClient.getDatabase("test");
        return database.getCollection(collectionName);
    }

    @Override
    public List<ChatInfo> queryChatInfoTwoUser(Integer sendUserId, Integer receiveUserId) {
        String str = "{$or:[" +
                "{sendUserId:" + sendUserId + ",receiveUserId:" + receiveUserId + "}," +
                "{sendUserId:" + receiveUserId + ",receiveUserId:" + sendUserId + "}" +
                "]}";
        return this.query(str);
    }

    @Override
    public List<ChatInfo> queryChatInfoNoRead(Integer userId) {
        String str = "{" +
                "isRead:false," +
                "receiveUserId:" + userId +
                "}";
        return this.query(str);
    }

    @Override
    public Integer countChatInfoNoRead(Integer userId) {
        return this.queryChatInfoNoRead(userId).size();
    }

    @Override
    public List<ChatInfo> queryChatInfoTwoUserNoRead(Integer sendUserId, Integer receiveUserId) {
        String str = "{\n" +
                "\tsendUserId:" + sendUserId + ",\n" +
                "\treceiveUserId:" + receiveUserId + ",\n" +
                "\tisRead:false\n" +
                "}";
        return this.query(str);
    }

    private List<ChatInfo> query(String str) {
        MongoCollection<Document> chatInfo = this.getCollection("chat_info");
        Document filter = Document.parse(str);
        FindIterable<Document> result = chatInfo.find(filter);
        List<ChatInfo> chatInfoList = new ArrayList<>();
        for (Document document : result) {
            ChatInfo c1 = BeanDocumentUtils.toBean(document, ChatInfo.class);
            chatInfoList.add(c1);
        }
        return chatInfoList;
    }

    @Override
    public Integer countChatInfoTwoUserNoRead(Integer sendUserId, Integer receiveUserId) {
        return this.queryChatInfoTwoUserNoRead(sendUserId, receiveUserId).size();
    }

    @Override
    public void saveChatInfo(ChatInfo chatInfo) {
        MongoCollection<Document> chatInfoColl = this.getCollection("chat_info");
        Document doc = BeanDocumentUtils.toDocument(chatInfo);
        chatInfoColl.insertOne(doc);
    }

    @Override
    public void updateReadChatInfoTwoUser(Integer sendUserId, Integer receiveUserId) {
        MongoCollection<Document> chatInfoColl = this.getCollection("chat_info");
        String str1 = "{\n" +
                "\tsendUserId:" + sendUserId + ",\n" +
                "\treceiveUserId:" + receiveUserId + ",\n" +
                "\tisRead:false\t\n" +
                "}";
        Document filter = Document.parse(str1);
        chatInfoColl.updateMany(filter, new Document("$set", new Document("isRead", true)));
    }
}
