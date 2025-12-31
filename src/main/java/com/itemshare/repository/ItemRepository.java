package com.itemshare.repository;

import com.itemshare.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByStatusOrderByCreatedAtDesc(Integer status, Pageable pageable);
    
    Page<Item> findByCategoryIdAndStatusOrderByCreatedAtDesc(Long categoryId, Integer status, Pageable pageable);
    
    Page<Item> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.status = 1 AND (i.title LIKE %?1% OR i.description LIKE %?1%)")
    Page<Item> findByKeyword(String keyword, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.status = 1 " +
           "AND (:keyword IS NULL OR i.title LIKE %:keyword% OR i.description LIKE %:keyword%) " +
           "AND (:categoryId IS NULL OR i.categoryId = :categoryId) " +
           "AND (:isFree IS NULL OR i.isFree = :isFree) " +
           "AND (:minPrice IS NULL OR i.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR i.price <= :maxPrice) " +
           "ORDER BY CASE WHEN :sortBy = 'createdAt' THEN i.createdAt END DESC, " +
           "CASE WHEN :sortBy = 'price' THEN i.price END ASC, " +
           "CASE WHEN :sortBy = 'viewCount' THEN i.viewCount END DESC")
    Page<Item> searchItems(@Param("keyword") String keyword, 
                          @Param("categoryId") Long categoryId,
                          @Param("isFree") Integer isFree,
                          @Param("minPrice") BigDecimal minPrice, 
                          @Param("maxPrice") BigDecimal maxPrice, 
                          @Param("sortBy") String sortBy, 
                          Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.categoryId IN ?1 AND i.status = 1 ORDER BY i.createdAt DESC")
    Page<Item> findByCategoryIds(List<Long> categoryIds, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.status = 1 ORDER BY i.viewCount DESC")
    List<Item> findPopularItems(Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.userId = ?1 AND i.status = 1 ORDER BY i.createdAt DESC")
    List<Item> findUserItems(Long userId, Pageable pageable);
}
