package org.CloudDisk.Dao;

import org.CloudDisk.pojo.Dir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DirDao extends JpaRepository<Dir, Integer> {

    @Query(value = "select * from dirinfo where path=?", nativeQuery = true)
    public List<Dir> findByPath(int path);

}
