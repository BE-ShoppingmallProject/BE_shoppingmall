package com.github.shoppingmall.shopping_mall.web.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemDto {
    @NotNull(message = "상품 아이디는 필수 입력값입니다.")
    private int itemId;

    @Min(value = 1, message = "최소 1개 이상 담아주세요.")
    private int count;

}
