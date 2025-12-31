package com.itemshare.repository;

import com.itemshare.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    @Query("SELECT f FROM Follow f WHERE f.followerId = ?1 ORDER BY f.createdAt DESC")
    List<Follow> findByFollowerIdOrderByCreatedAtDesc(Long followerId);
    
    @Query("SELECT f FROM Follow f WHERE f.followingId = ?1 ORDER BY f.createdAt DESC")
    List<Follow> findByFollowingIdOrderByCreatedAtDesc(Long followingId);
    
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followingId = ?1")
    Long countFollowers(Long followingId);
    
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followerId = ?1")
    Long countFollowing(Long followerId);
}
