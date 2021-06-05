package org.CloudDisk.pojo;

import javax.persistence.*;

@Entity
@Table(name = "requestinfo")
public class FriendRequestMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "uid1")
    String mainUid;
    @Column(name = "uid2")
    String friendUid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
