package com.itemshare.entity;

import com.itemshare.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "favorites")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Favorite extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @PrePersist
    public void prePersist() {
        // 防止重复收藏
    }
}
