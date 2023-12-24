package com.github.shoppingmall.shopping_mall.repository.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemOption implements Serializable {
    private Integer optionId;
    private Integer itemId;

}
