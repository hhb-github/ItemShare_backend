package com.itemshare.entity;

import com.itemshare.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "operation_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OperationLog extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "操作类型不能为空")
    @Column(name = "operation", nullable = false, length = 100)
    private String operation;

    @NotBlank(message = "目标类型不能为空")
    @Column(name = "target_type", nullable = false, length = 50)
    private String targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
}
