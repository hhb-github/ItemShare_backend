package com.itemshare.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemCreateDTO {

    @NotBlank(message = "物品标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @NotNull(message = "物品状态不能为空")
    private Integer conditionType;

    private String price;
    private String originalPrice;
    private Integer isFree;
    private String contactMethod;
    private String location;
    private String tags;
}
