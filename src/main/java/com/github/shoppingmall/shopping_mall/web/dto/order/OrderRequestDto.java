package com.github.shoppingmall.shopping_mall.web.dto.order;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private OrderDto orderDto;
    private List<OrderDetailDto> orderDetails;
}
