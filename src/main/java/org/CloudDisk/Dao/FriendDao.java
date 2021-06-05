package org.CloudDisk.Dao;

import org.CloudDisk.pojo.FriendMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendDao extends JpaRepository<FriendMap, Integer> {

    @Query(value = "select count(*) from friendsinfo where uid1=?1 and uid2=?2", nativeQuery = true)
    public int count(String mainUid, String friendUid);

    @Modifying
    @Query(value = "delete from friendsinfo where uid1=?1 and uid2=?2", nativeQuery = true)
    public void deleteByUid(String mainUid, String friendUid);
}
