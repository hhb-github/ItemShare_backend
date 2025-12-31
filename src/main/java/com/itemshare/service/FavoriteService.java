package com.itemshare.service;

import com.itemshare.entity.Favorite;
import com.itemshare.repository.FavoriteRepository;
import com.itemshare.entity.Item;
import com.itemshare.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private ItemService itemService;

    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public boolean existsByUserIdAndItemId(Long userId, Long itemId) {
        return favoriteRepository.existsByUserIdAndItemId(userId, itemId);
    }

    public void addFavorite(Long userId, Long itemId) {
        if (!existsByUserIdAndItemId(userId, itemId)) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setItemId(itemId);
            save(favorite);
            
            // 更新物品的收藏数
            itemService.updateFavoriteCount(itemId, 1);
        }
    }

    public void removeFavorite(Long userId, Long itemId) {
        if (existsByUserIdAndItemId(userId, itemId)) {
            favoriteRepository.deleteByUserIdAndItemId(userId, itemId);
            
            // 更新物品的收藏数
            itemService.updateFavoriteCount(itemId, -1);
        }
    }

    public List<Favorite> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Favorite> findUserFavorites(Long userId) {
        return favoriteRepository.findUserFavorites(userId);
    }
}
