package org.CloudDisk.pojo;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "fileinfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(name = "fsize")
    String size;
    @Column(name = "path")
    int path;
    @Column(name = "token")
    String token;

    public upFile(RecycleItem item){
        this.no = item.getNo();
        this.name = item.getName();
        this.date = item.getDate();
        this.msg = item.getMsg();
        this.usr = item.getUsr();
        this.size = item.getSize();
        this.path = item.getPath();
        this.token = item.getToken();
    }

    public String getFilePath(){
        String suffix = "." + FilenameUtils.getExtension(name);
        return "/upload/" + usr + "/" + no + suffix;
    }

}
