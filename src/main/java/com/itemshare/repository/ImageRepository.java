package com.itemshare.repository;

import com.itemshare.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByItemIdOrderBySortOrderAsc(Long itemId);
    
    List<Image> findByItemIdAndIsMainTrue(Long itemId);
    
    @Query("SELECT i FROM Image i WHERE i.itemId IN ?1 ORDER BY i.sortOrder ASC")
    List<Image> findByItemIdsOrderBySortOrder(List<Long> itemIds);

    void deleteByItemId(Long itemId);
}
