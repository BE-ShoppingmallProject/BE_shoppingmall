package com.github.shoppingmall.shopping_mall.web.dto.item;

import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemOptionEntity;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDto {
    private String itemName; // 상품 이름
    private String itemExplain; // 상품 설명
    private ItemStatus itemStatus; // 상품상태
    private Integer itemCount; // 상품 개수
    private Integer unitPrice; // 가격
//    private String optionContent; // 옵션 설명
//    private Integer additionalPrice; // 옵션 추가 가격

    public ItemDto(Item item, ItemOptionEntity itemOptionEntity){
        this.itemName = item.getItemName();
        this.itemExplain = item.getItemExplain();
        this.itemStatus = item.getItemStatus();
        this.itemCount = item.getItemCount();
        this.unitPrice = item.getUnitPrice();
//        this.optionContent = itemOptionEntity.getOptionContent();
//        this.additionalPrice = itemOptionEntity.getAdditionalPrice();
    }
}


