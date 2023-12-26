package com.github.shoppingmall.shopping_mall.web.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SellerItemRequestDto {
    private Integer userId;
    private Integer categoryId;
    private Integer unitPrice;
    private Integer quantity;
    private String itemName;
    private String itemExplain;
    private String startDate;
    private String endDate;

}