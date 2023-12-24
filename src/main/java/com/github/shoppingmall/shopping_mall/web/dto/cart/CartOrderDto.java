package com.github.shoppingmall.shopping_mall.web.dto.cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {
    private int cartItemId;

    private List<CartOrderDto> cartOrderDtoList;
}
