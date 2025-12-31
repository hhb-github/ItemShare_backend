package com.itemshare.service;

import com.itemshare.entity.Follow;
import com.itemshare.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    public Follow save(Follow follow) {
        return followRepository.save(follow);
    }

    public boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    public void addFollow(Long followerId, Long followingId) {
        if (!existsByFollowerIdAndFollowingId(followerId, followingId)) {
            Follow follow = new Follow();
            follow.setFollowerId(followerId);
            follow.setFollowingId(followingId);
            save(follow);
        }
    }

    public void removeFollow(Long followerId, Long followingId) {
        if (existsByFollowerIdAndFollowingId(followerId, followingId)) {
            followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
        }
    }

    public List<Follow> findByFollowerIdOrderByCreatedAtDesc(Long followerId) {
        return followRepository.findByFollowerIdOrderByCreatedAtDesc(followerId);
    }

    public List<Follow> findByFollowingIdOrderByCreatedAtDesc(Long followingId) {
        return followRepository.findByFollowingIdOrderByCreatedAtDesc(followingId);
    }

    public Long countFollowers(Long followingId) {
        return followRepository.countFollowers(followingId);
    }

    public Long countFollowing(Long followerId) {
        return followRepository.countFollowing(followerId);
    }
}
