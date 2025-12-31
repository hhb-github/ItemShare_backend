package com.itemshare.service;

import com.itemshare.entity.Item;
import com.itemshare.repository.ItemRepository;
import com.itemshare.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private ImageService imageService;

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public Optional<Item> findById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        // 加载与Item关联的Image数据
        if (item.isPresent()) {
            item.get().setImages(imageService.findByItemId(item.get().getId()));
        }
        return item;
    }

    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Page<Item> findByStatusOrderByCreatedAtDesc(Integer status, Pageable pageable) {
        return itemRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
    }

    public Page<Item> findByCategoryIdAndStatusOrderByCreatedAtDesc(Long categoryId, Integer status, Pageable pageable) {
        return itemRepository.findByCategoryIdAndStatusOrderByCreatedAtDesc(categoryId, status, pageable);
    }

    public Page<Item> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable) {
        return itemRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Page<Item> findByKeyword(String keyword, Pageable pageable) {
        return itemRepository.findByKeyword(keyword, pageable);
    }

    public Page<Item> searchItems(String keyword, Long categoryId, Integer isFree, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, Pageable pageable) {
        return itemRepository.searchItems(keyword, categoryId, isFree, minPrice, maxPrice, sortBy, pageable);
    }

    public Page<Item> findByCategoryIds(List<Long> categoryIds, Pageable pageable) {
        return itemRepository.findByCategoryIds(categoryIds, pageable);
    }

    public List<Item> findPopularItems(Pageable pageable) {
        return itemRepository.findPopularItems(pageable);
    }

    public List<Item> findUserItems(Long userId, Pageable pageable) {
        return itemRepository.findUserItems(userId, pageable);
    }

    public void updateViewCount(Long itemId) {
        itemRepository.findById(itemId).ifPresent(item -> {
            item.setViewCount(item.getViewCount() + 1);
            itemRepository.save(item);
        });
    }

    public void updateFavoriteCount(Long itemId, Integer change) {
        itemRepository.findById(itemId).ifPresent(item -> {
            item.setFavoriteCount(item.getFavoriteCount() + change);
            itemRepository.save(item);
        });
    }

    public void updateItemStatus(Long itemId, Integer status) {
        itemRepository.findById(itemId).ifPresent(item -> {
            item.setStatus(status);
            itemRepository.save(item);
        });
    }

    public void deleteItem(Long itemId) {
        itemRepository.findById(itemId).ifPresent(item -> {
            item.setStatus(0); // 设置为删除状态
            itemRepository.save(item);
        });
    }
}
