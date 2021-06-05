package org.CloudDisk.pojo;

import javax.persistence.*;

@Entity
@Table(name = "friendsinfo")
public class FriendMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "uid1")
    String mainUid;

    @Column(name = "uid2")
    String friendUid;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setMainUid(String mainUid) {
        this.mainUid = mainUid;
    }

    public void setFriendUid(String friendUid) {
        this.friendUid = friendUid;
    }

    public String getMainUid() {
        return mainUid;
    }

    public String getFriendUid() {
        return friendUid;
    }

    public FriendMap reverse(){
        FriendMap friendMap = new FriendMap();
        friendMap.setFriendUid(this.mainUid);
        friendMap.setMainUid(this.friendUid);
        return friendMap;
    }
}
