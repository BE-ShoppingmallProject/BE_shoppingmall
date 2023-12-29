package com.github.shoppingmall.shopping_mall.web.dto.post;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Data
@Getter
@Setter
public class PostResponse {
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
    private List<PostResponse_ItemOption> itemOptionList;
    private String thumbNailImgPath;
    private List<String> otherImgPathList;
}