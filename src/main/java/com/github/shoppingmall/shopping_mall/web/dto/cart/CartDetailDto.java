package com.github.shoppingmall.shopping_mall.web.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartDetailDto {
    private Integer cartItemId; // 장바구니 상품아이디

    private String itemName; // 상품이름

    private Integer unitPrice; // 상품 가격

    private Integer quantity; // 상품 수량

    private String imgUrl; // 상품 이미지 경로
}
