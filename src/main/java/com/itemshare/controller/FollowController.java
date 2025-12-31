package com.itemshare.controller;

import com.itemshare.entity.Follow;
import com.itemshare.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping
    public ResponseEntity<Void> addFollow(@RequestParam Long followerId, @RequestParam Long followingId) {
        followService.addFollow(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFollow(@RequestParam Long followerId, @RequestParam Long followingId) {
        followService.removeFollow(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFollow(@RequestParam Long followerId, @RequestParam Long followingId) {
        boolean exists = followService.existsByFollowerIdAndFollowingId(followerId, followingId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<Follow>> getFollowers(@PathVariable Long userId) {
        List<Follow> followers = followService.findByFollowingIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<Follow>> getFollowing(@PathVariable Long userId) {
        List<Follow> following = followService.findByFollowerIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/followers/count/{userId}")
    public ResponseEntity<Long> getFollowersCount(@PathVariable Long userId) {
        Long count = followService.countFollowers(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/following/count/{userId}")
    public ResponseEntity<Long> getFollowingCount(@PathVariable Long userId) {
        Long count = followService.countFollowing(userId);
        return ResponseEntity.ok(count);
    }
}
