package com.itemshare.entity;

import com.itemshare.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "follows")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Follow extends BaseEntity {

    @Column(name = "follower_id", nullable = false)
    private Long followerId;

    @Column(name = "following_id", nullable = false)
    private Long followingId;
}
