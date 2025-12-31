package com.itemshare.entity;

import com.itemshare.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "system_configs")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SystemConfig extends BaseEntity {

    @NotBlank(message = "配置键不能为空")
    @Size(max = 100, message = "配置键长度不能超过100个字符")
    @Column(name = "config_key", unique = true, nullable = false, length = 100)
    private String configKey;

    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;

    @Size(max = 255, message = "配置描述长度不能超过255个字符")
    @Column(name = "description", length = 255)
    private String description;
}
