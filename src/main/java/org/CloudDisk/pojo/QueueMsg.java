package org.CloudDisk.pojo;

import java.io.Serializable;

public class QueueMsg implements Serializable {
    String type;
    String date;
    Object body;

    public QueueMsg() {
    }

    public QueueMsg(String type, String date, Object body) {
        this.type = type;
        this.date = date;
        this.body = body;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public Object getBody() {
        return body;
    }
}
