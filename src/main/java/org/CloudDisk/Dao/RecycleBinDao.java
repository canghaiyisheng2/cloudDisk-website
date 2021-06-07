package org.CloudDisk.Dao;

import org.CloudDisk.pojo.RecycleItem;
import org.CloudDisk.pojo.RecycleItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecycleBinDao extends JpaRepository<RecycleItem, RecycleItemPK> {
    @Query(value = "select * from recyclebin where usr=?",nativeQuery = true)
    public List<RecycleItem> findAllByUser(String uid);
}
