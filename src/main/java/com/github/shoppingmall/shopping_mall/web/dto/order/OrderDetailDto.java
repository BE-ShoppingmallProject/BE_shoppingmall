package com.github.shoppingmall.shopping_mall.web.dto.order;


import com.github.shoppingmall.shopping_mall.repository.Order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetailDto {
    private Integer itemId;
    private Integer OptionId;
    private Integer quantity;
    private Integer itemCnt;
    private Integer itemPrice;
}
