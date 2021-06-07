package org.CloudDisk.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueueMsg implements Serializable {
    String type;
    String date;
    Object body;
}
