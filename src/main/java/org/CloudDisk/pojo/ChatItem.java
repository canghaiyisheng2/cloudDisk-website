package org.CloudDisk.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatItem implements Serializable {
    String type;
    String sender;
    Date date;
    Object msg;
}
