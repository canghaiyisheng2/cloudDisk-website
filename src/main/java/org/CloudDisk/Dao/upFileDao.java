package org.CloudDisk.Dao;

import org.CloudDisk.pojo.upFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface upFileDao extends JpaRepository<upFile,Integer> {
    @Query(value = "select f from upFile f where f.usr=?1")
    public Page<upFile> findAll(String uid, Pageable pageable);
}