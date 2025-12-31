package com.itemshare.entity;

import com.itemshare.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "view_history")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ViewHistory extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
}
