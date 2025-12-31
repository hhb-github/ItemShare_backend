package com.itemshare.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageCreateDTO {

    @NotBlank(message = "接收者ID不能为空")
    private String receiverId;

    private String itemId;
    private String parentId;
    private String content;
    private Integer messageType;
}
