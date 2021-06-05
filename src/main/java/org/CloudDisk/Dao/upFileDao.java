package org.CloudDisk.Dao;

import org.CloudDisk.pojo.upFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface upFileDao extends JpaRepository<upFile,Integer> {

    @Query(value = "select * from fileinfo where path=?",nativeQuery = true)
    public List<upFile> findByPath(int path);
}