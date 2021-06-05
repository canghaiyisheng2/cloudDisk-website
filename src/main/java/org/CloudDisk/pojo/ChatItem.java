package org.CloudDisk.pojo;

import java.io.Serializable;

public class ChatItem implements Serializable {
    String type;
    String sender;
    String date;
    Object msg;

    public ChatItem() {
    }

    public ChatItem(String type, String sender, String date, Object msg) {
        this.type = type;
        this.sender = sender;
        this.date = date;
        this.msg = msg;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }

    public Object getMsg() {
        return msg;
    }
}
