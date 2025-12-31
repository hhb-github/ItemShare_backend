package com.itemshare.entity;

import com.itemshare.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "items")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Item extends BaseEntity {

    @NotBlank(message = "物品标题不能为空")
    @Size(max = 200, message = "物品标题长度不能超过200个字符")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "分类ID不能为空")
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @NotNull(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "物品状态不能为空")
    @Column(name = "condition_type", nullable = false)
    private Integer conditionType;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice = BigDecimal.ZERO;

    @Column(name = "is_free")
    private Integer isFree = 0;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "favorite_count")
    private Integer favoriteCount = 0;

    @Column(name = "contact_method", length = 100)
    private String contactMethod;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "tags", length = 500)
    private String tags;

    @Column(name = "reviewed_at")
    private java.time.LocalDateTime reviewedAt;

    @Column(name = "reviewed_by")
    private Long reviewedBy;
    
    @Transient
    private java.util.List<Image> images;
}
