package org.CloudDisk.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "dirinfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public Dir(RecycleItem item){
        this.type = item.getType();
        this.no = item.getNo();
        this.name = item.getName();
        this.date = item.getDate();
        this.usr = item.getUsr();
        this.path = item.getPath();
    }
}
