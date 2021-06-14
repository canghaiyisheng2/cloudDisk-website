package org.CloudDisk.Dao;

import org.CloudDisk.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<User, String> {
    @Query(value = "select * from usrinfo where usrname=?",nativeQuery = true)
    public User findOneByName(String name);

    @Query(value = "select dirno from usrinfo where uuid=?",nativeQuery = true)
    public int getUserPathByUid(String uid);

    @Query(value = "select * from usrinfo,friendsinfo where uid1=? and uuid=uid2", nativeQuery = true)
    public List<User> findFriendsByUid(String uid);

    @Query(value = "select uuid, usrName, auth from usrinfo", nativeQuery = true)
    public List<User> findallUsers();

    @Query(value = "select uuid, usrName, auth from usrinfo where uuid=?", nativeQuery = true)
    public User findOneById(String uid);


    @Modifying
    @Query(value = "update usrinfo set dirno=?2 where uuid=?1", nativeQuery = true)
    public void updateDirNoById(String id, int dirNo);

    @Modifying
    @Query(value = "update usrinfo set avatar=?2 where usrName=?1",nativeQuery = true)
    public void updateAvatar(String name, String avatar);

    @Modifying
    @Query(value = "update usrinfo set usrname=?2 where uuid=?1",nativeQuery = true)
    public void updateUserName(String uid, String newName);

    @Modifying
    @Query(value = "update usrinfo set pwd=?2 where uuid=?1",nativeQuery = true)
    public void updatePassword(String uid, String newPassword);

    @Modifying
    @Query(value = "delete from usrinfo where uuid=?1 ", nativeQuery = true)
    public void deleteByUid(String uid);

    @Modifying
    @Query(value = "update usrinfo set auth = 'admin' where uuid=?1",nativeQuery = true)
    public void riseAuth(String uid);


}

