package com.github.shoppingmall.shopping_mall.web.dto.post;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.sql.In;

import java.sql.Timestamp;

@Data
@Getter
@Setter
public class PostResponseByNormal {
    private Integer postId;
    private Integer itemId;
    private Integer sellerUserId;
    private String title;
    private String content;
    private Integer viewCnt;
    private String itemName;
    private String itemExplain;
    private Integer unitPrice;
    private Timestamp createDate;
    private Timestamp updateDate;
    private String thumbNailImgPath;
}
