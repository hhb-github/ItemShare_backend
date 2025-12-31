package com.itemshare.repository;

import com.itemshare.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByReceiverIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable);
    
    Page<Message> findBySenderIdOrderByCreatedAtDesc(Long senderId, Pageable pageable);
    
    @Query("SELECT m FROM Message m WHERE m.receiverId = ?1 AND m.isRead = 0 ORDER BY m.createdAt DESC")
    List<Message> findUnreadMessages(Long receiverId);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiverId = ?1 AND m.isRead = 0")
    Long countUnreadMessages(Long receiverId);
    
    @Query("SELECT m FROM Message m WHERE m.itemId = ?1 ORDER BY m.createdAt DESC")
    List<Message> findByItemIdOrderByCreatedAtDesc(Long itemId);
}
