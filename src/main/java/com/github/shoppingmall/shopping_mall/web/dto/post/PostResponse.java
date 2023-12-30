package com.github.shoppingmall.shopping_mall.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Data
@Getter
@Setter
@Schema(title = "판매 상품 페이지 응답DTO")
public class PostResponse {
    @Schema(description = "Post Id")
    private Integer postId;
    @Schema(description = "Item Id")
    private Integer itemId;
    @Schema(description = "Seller User Id")
    private Integer sellerUserId;
    @Schema(description = "제목")
    private String title;
    @Schema(description = "내용")
    private String content;
    @Schema(description = "View Count")
    private Integer viewCnt;
    @Schema(description = "상품명")
    private String itemName;
    @Schema(description = "상품 설명")
    private String itemExplain;
    @Schema(description = "단가")
    private Integer unitPrice;
    @Schema(description = "생성 일자")
    private Timestamp createDate;
    @Schema(description = "수정 일자")
    private Timestamp updateDate;
    @Schema(description = "상품 옵션 내용")
    private List<PostResponse_ItemOption> itemOptionList;
    @Schema(description = "썸네일 이미지 파일 경로")
    private String thumbNailImgPath;
    @Schema(description = "다른 이미지 파일 경우")
    private List<String> otherImgPathList;
}