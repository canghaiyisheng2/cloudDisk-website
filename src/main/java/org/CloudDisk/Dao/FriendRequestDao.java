package org.CloudDisk.Dao;

import org.CloudDisk.pojo.FriendMap;
import org.CloudDisk.pojo.FriendRequestMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendRequestDao extends JpaRepository<FriendRequestMap,Integer> {

    @Query(value = "select count(*) from requestinfo where uid1=?1 and uid2=?2", nativeQuery = true)
    public int count(String mainUid, String friendUid);

    @Modifying
    @Query(value = "delete from requestinfo where uid1=?1 and uid2=?2",nativeQuery = true)
    public void delete(String mainUid, String friendUid);
}
