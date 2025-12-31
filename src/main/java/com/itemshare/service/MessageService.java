package com.itemshare.service;

import com.itemshare.entity.Message;
import com.itemshare.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public Page<Message> findByReceiverIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable) {
        return messageRepository.findByReceiverIdOrderByCreatedAtDesc(receiverId, pageable);
    }

    public Page<Message> findBySenderIdOrderByCreatedAtDesc(Long senderId, Pageable pageable) {
        return messageRepository.findBySenderIdOrderByCreatedAtDesc(senderId, pageable);
    }

    public List<Message> findUnreadMessages(Long receiverId) {
        return messageRepository.findUnreadMessages(receiverId);
    }

    public Long countUnreadMessages(Long receiverId) {
        return messageRepository.countUnreadMessages(receiverId);
    }

    public List<Message> findByItemIdOrderByCreatedAtDesc(Long itemId) {
        return messageRepository.findByItemIdOrderByCreatedAtDesc(itemId);
    }

    public void markAsRead(Long messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setIsRead(1);
            messageRepository.save(message);
        });
    }

    public void markAllAsRead(Long receiverId) {
        List<Message> unreadMessages = findUnreadMessages(receiverId);
        for (Message message : unreadMessages) {
            message.setIsRead(1);
        }
        messageRepository.saveAll(unreadMessages);
    }
}
