package com.github.shoppingmall.shopping_mall.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@Schema(title = "판매 상품 페이지 응답(판매자)DTO")
public class PostResponseBySeller {
    @Schema(description = "Post Id")
    private Integer postId;
    @Schema(description = "Item Id")
    private Integer itemId;
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
    @Schema(description = "재고량")
    private Integer quantity;
    @Schema(description = "상품 상태")
    private String itemStatus;
    @Schema(description = "판매시작 일자")
    private Timestamp startDate;
    @Schema(description = "판매종료 일자")
    private Timestamp endDate;
    @Schema(description = "생성 일자")
    private Timestamp createDate;
    @Schema(description = "수정 일자")
    private Timestamp updateDate;
    @Schema(description = "삭제 일자")
    private Timestamp deleteDate;
    @Schema(description = "썸네일 이미지 파일 경로")
    private String thumbNailImgPath;
}

