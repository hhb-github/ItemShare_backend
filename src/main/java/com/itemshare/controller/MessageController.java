package com.itemshare.controller;

import com.itemshare.entity.Message;
import com.itemshare.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message savedMessage = messageService.save(message);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<Page<Message>> getMessagesByReceiver(@PathVariable Long receiverId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages = messageService.findByReceiverIdOrderByCreatedAtDesc(receiverId, pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<Page<Message>> getMessagesBySender(@PathVariable Long senderId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages = messageService.findBySenderIdOrderByCreatedAtDesc(senderId, pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/unread/{receiverId}")
    public ResponseEntity<List<Message>> getUnreadMessages(@PathVariable Long receiverId) {
        List<Message> messages = messageService.findUnreadMessages(receiverId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/unread/count/{receiverId}")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long receiverId) {
        Long count = messageService.countUnreadMessages(receiverId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<Message>> getMessagesByItem(@PathVariable Long itemId) {
        List<Message> messages = messageService.findByItemIdOrderByCreatedAtDesc(itemId);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all/{receiverId}")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long receiverId) {
        messageService.markAllAsRead(receiverId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        // 这里可以实现软删除，标记消息为已删除状态
        return ResponseEntity.ok().build();
    }
}
