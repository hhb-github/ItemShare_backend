package com.itemshare.controller;

import com.itemshare.entity.Item;
import com.itemshare.dto.ApiResponse;
import com.itemshare.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<ApiResponse<Item>> createItem(@RequestBody Item item) {
        try {
            Item savedItem = itemService.save(item);
            return ResponseEntity.ok(ApiResponse.success(savedItem, "物品创建成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("物品创建失败", 500));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Item>> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemService.findById(id);
        if (item.isPresent()) {
            // 增加浏览次数
            itemService.updateViewCount(id);
            return ResponseEntity.ok(ApiResponse.success(item.get(), "获取物品信息成功"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("物品不存在", 404));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Item>>> getItems(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = itemService.findByStatusOrderByCreatedAtDesc(1, pageable);
        return ResponseEntity.ok(ApiResponse.success(items, "获取物品列表成功"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<Item>>> getItemsByCategory(@PathVariable Long categoryId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = itemService.findByCategoryIdAndStatusOrderByCreatedAtDesc(categoryId, 1, pageable);
        return ResponseEntity.ok(ApiResponse.success(items, "获取分类物品成功"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<Item>>> getItemsByUser(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = itemService.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(items, "获取用户物品成功"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Item>>> searchItems(@RequestParam(required = false) String keyword,
                                                  @RequestParam(required = false) Long categoryId,
                                                  @RequestParam(required = false) Integer isFree,
                                                  @RequestParam(required = false) Double minPrice,
                                                  @RequestParam(required = false) Double maxPrice,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size,
                                                  @RequestParam(defaultValue = "createdAt") String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Item> items = itemService.searchItems(keyword, 
                                                       categoryId,
                                                       isFree,
                                                       minPrice != null ? BigDecimal.valueOf(minPrice) : null, 
                                                       maxPrice != null ? BigDecimal.valueOf(maxPrice) : null, 
                                                       sortBy, 
                                                       pageable);
            return ResponseEntity.ok(ApiResponse.success(items, "搜索物品成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("搜索物品失败", 500));
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<Item>>> getPopularItems(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Item> items = itemService.findPopularItems(pageable);
        return ResponseEntity.ok(ApiResponse.success(items, "获取热门物品成功"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Item>> updateItem(@PathVariable Long id, @RequestBody Item item) {
        Optional<Item> existingItem = itemService.findById(id);
        if (existingItem.isPresent()) {
            item.setId(id);
            Item updatedItem = itemService.save(item);
            return ResponseEntity.ok(ApiResponse.success(updatedItem, "物品更新成功"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("物品不存在", 404));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateItemStatus(@PathVariable Long id, @RequestParam Integer status) {
        itemService.updateItemStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(null, "物品状态更新成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success(null, "物品删除成功"));
    }
}
