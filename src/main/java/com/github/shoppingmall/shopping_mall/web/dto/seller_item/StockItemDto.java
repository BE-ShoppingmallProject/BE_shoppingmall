package com.github.shoppingmall.shopping_mall.web.dto.seller_item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.sql.In;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockItemDto {
    private Integer stockId;
    private String itemStatus;
    private Integer quantity;
    private String startDate;
    private String endDate;
}
