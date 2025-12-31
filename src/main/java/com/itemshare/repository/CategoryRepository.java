package com.itemshare.repository;

import com.itemshare.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentIdOrderBySortOrderAsc(Long parentId);
    
    List<Category> findByStatusOrderBySortOrderAsc(Byte status);
    
    @Query("SELECT c FROM Category c WHERE c.parentId = 0 AND c.status = 1 ORDER BY c.sortOrder ASC")
    List<Category> findRootCategories();
    
    @Query("SELECT c FROM Category c WHERE c.parentId = ?1 AND c.status = 1 ORDER BY c.sortOrder ASC")
    List<Category> findByParentIdAndStatus(Long parentId);
}
