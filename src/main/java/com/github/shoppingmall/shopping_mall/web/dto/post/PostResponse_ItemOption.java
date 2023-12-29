package com.github.shoppingmall.shopping_mall.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Schema(title = "판매 상품 페이지 응답 - 상품 옵션 DTO")
public class PostResponse_ItemOption {
    @Schema(description = "Option Id")
    private Integer optionId;
    @Schema(description = "Stock Item Id")
    private Integer stockId;
    @Schema(description = "옵션 설명")
    private String optionContent;
    @Schema(description = "옵션별 추가 가격")
    private Integer addPrice;
    @Schema(description = "재고 수량")
    private Integer quantity;
}
