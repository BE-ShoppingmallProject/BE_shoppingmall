package com.github.shoppingmall.shopping_mall.web.dto.seller_item;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemCreationDto {
    private ItemDto item;
    private List<ItemOptionDto> itemOptions;
    private List<StockItemDto> stockItems;
}
