package com.github.shoppingmall.shopping_mall.web.dto.post;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter
@Setter
public class PostResponseBySeller {
    private Integer postId;
    private Integer itemId;
    private String title;
    private String content;
    private Integer viewCnt;
    private String itemName;
    private String itemExplain;
    private Integer unitPrice;
    private Integer quantity;
    private String itemStatus;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp createDate;
    private Timestamp updateDate;
    private Timestamp deleteDate;
    private String thumbNailImgPath;
}

