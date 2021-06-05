package org.CloudDisk.pojo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "dirinfo")

public class Dir {
    @Transient
    String type = "dir";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dno")
    int no;
    @Column(name = "dname")
    String name;
    @Column(name = "ddate")
    Date date;
    @Column(name = "dusr")
    String usr; //uid
    @Column(name = "path")
    int path;

    public void setNo(int no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public int getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getUsr() {
        return usr;
    }

    public int getPath() {
        return path;
    }



}
