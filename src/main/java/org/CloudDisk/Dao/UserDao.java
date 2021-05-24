package org.CloudDisk.Dao;

import org.CloudDisk.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface UserDao extends JpaRepository<User, String> {
    @Query(value = "select * from usrinfo where usrname=?",nativeQuery = true)
    public User findOneByName(String name);

    @Query(value = "select uuid, usrname from usrinfo",nativeQuery = true)
    public Map<String, String> findAllIdNameMap();

    @Modifying
    @Query(value = "update usrinfo set avatar=?2 where usrName=?1",nativeQuery = true)
    public void updateAvatar(String name, String avatar);

    @Modifying
    @Query(value = "update usrinfo set usrname=?2 where usrName=?1",nativeQuery = true)
    public void updateUserName(String oldName, String newName);
}

