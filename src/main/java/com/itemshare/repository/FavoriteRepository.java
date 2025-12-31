package com.itemshare.repository;

import com.itemshare.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserIdAndItemId(Long userId, Long itemId);
    
    void deleteByUserIdAndItemId(Long userId, Long itemId);
    
    @Query("SELECT f FROM Favorite f WHERE f.userId = ?1 ORDER BY f.createdAt DESC")
    List<Favorite> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT f FROM Favorite f WHERE f.userId = ?1 AND f.itemId IN (SELECT i.id FROM Item i WHERE i.status = 1) ORDER BY f.createdAt DESC")
    List<Favorite> findUserFavorites(Long userId);
}
