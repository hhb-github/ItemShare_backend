package com.itemshare.dto;

import lombok.Data;

@Data
public class ItemSearchDTO {

    private String keyword;
    private Long categoryId;
    private Long userId;
    private Integer conditionType;
    private Double minPrice;
    private Double maxPrice;
    private Integer isFree;
    private String location;
    private Integer status;
    
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortOrder;
}
