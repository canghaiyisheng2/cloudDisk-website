package org.CloudDisk.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "friendsinfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "uid1")
    String mainUid;

    @Column(name = "uid2")
    String friendUid;

    public FriendMap reverse(){
        FriendMap friendMap = new FriendMap();
        friendMap.setFriendUid(this.mainUid);
        friendMap.setMainUid(this.friendUid);
        return friendMap;
    }
}
