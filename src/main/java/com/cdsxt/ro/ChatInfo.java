package com.cdsxt.ro;

import java.util.Date;

public class ChatInfo {

    /*
    * monggdb数据库文档设计：
{
"_id" : ObjectId("5a3b6c7e694fc50f800d8b22"),
"isRead" : false,
"msgContent" : "222",
"receiveUserId" : 33,
"receiveUserName" : "客服c",
"receiveUserType" : "backUser",
"sendDate" : NumberLong(1513843838587),
"sendUserId" : 2,
"sendUserName" : "曾小贤",
"sendUserType" : "frontUser"
}*/

    private Object id;
    private Boolean isRead;
    private String msgContent;
    private Integer receiveUserId;
    private String receiveUserName;
    private String receiveUserType;
    private Date sendDate;
    private Integer sendUserId;
    private String sendUserName;
    private String sendUserType;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
    public Boolean getIsRead() {
        return this.isRead;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Integer getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Integer receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getReceiveUserType() {
        return receiveUserType;
    }

    public void setReceiveUserType(String receiveUserType) {
        this.receiveUserType = receiveUserType;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Integer sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendUserType() {
        return sendUserType;
    }

    public void setSendUserType(String sendUserType) {
        this.sendUserType = sendUserType;
    }

    @Override
    public String toString() {
        return "ChatInfo{" +
                "id=" + id +
                ", isRead=" + isRead +
                ", msgContent='" + msgContent + '\'' +
                ", receiveUserId=" + receiveUserId +
                ", receiveUserName='" + receiveUserName + '\'' +
                ", receiveUserType='" + receiveUserType + '\'' +
                ", sendDate=" + sendDate +
                ", sendUserId=" + sendUserId +
                ", sendUserName='" + sendUserName + '\'' +
                ", sendUserType='" + sendUserType + '\'' +
                '}';
    }

}
