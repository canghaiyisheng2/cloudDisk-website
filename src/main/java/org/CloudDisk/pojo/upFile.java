package org.CloudDisk.pojo;

import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "fileinfo")
public class upFile implements Serializable {

    @Transient
    String type = "file";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fno")
    int no;
    @Column(name = "fname")
    String name;
    @Column(name = "fdate")
    Date date;
    @Column(name = "fmsg")
    String msg;
    @Column(name = "fusr")
    String usr; //uid
    @Column(name = "path")
    int path;
    @Column(name = "token")
    String token;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFilePath(){
        String suffix = "." + FilenameUtils.getExtension(name);
        return "/upload/" + usr + "/" + no + suffix;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
