package org.CloudDisk.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "requestinfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "uid1")
    String mainUid;
    @Column(name = "uid2")
    String friendUid;

}
