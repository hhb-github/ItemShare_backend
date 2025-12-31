package com.itemshare.controller;

import com.itemshare.entity.Favorite;
import com.itemshare.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> addFavorite(@RequestParam Long userId, @RequestParam Long itemId) {
        favoriteService.addFavorite(userId, itemId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestParam Long userId, @RequestParam Long itemId) {
        favoriteService.removeFavorite(userId, itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFavorite(@RequestParam Long userId, @RequestParam Long itemId) {
        boolean exists = favoriteService.existsByUserIdAndItemId(userId, itemId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favorite>> getUserFavorites(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteService.findUserFavorites(userId);
        return ResponseEntity.ok(favorites);
    }
}
