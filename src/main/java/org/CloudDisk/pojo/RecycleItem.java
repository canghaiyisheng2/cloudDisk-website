package org.CloudDisk.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "recyclebin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RecycleItemPK.class)
public class RecycleItem {
    @Id
    @Column(name = "type")
    String type;
    @Id
    @Column(name = "id")
    int no;
    @Column(name = "name")
    String name;
    @Column(name = "uploaddate")
    Date date;
    @Column(name = "usr")
    String usr;
    @Column(name = "fsize")
    String size;
    @Column(name = "path")
    int path;
    @Column(name = "fmsg")
    String msg;
    @Column(name = "token")
    String token;

    public RecycleItem(upFile file){
        this.type = "file";
        this.no = file.getNo();
        this.date = file.getDate();
        this.name = file.getName();
        this.path = file.getPath();
        this.msg = file.getMsg();
        this.token = file.getToken();
        this.size = file.getSize();
        this.usr = file.getUsr();
    }

    public RecycleItem(Dir dir){
        this.type = "dir";
        this.no = dir.getNo();
        this.date = dir.getDate();
        this.name = dir.getName();
        this.path = dir.getPath();
        this.usr = dir.getUsr();
    }
}
