package com.github.shoppingmall.shopping_mall.web.dto.post;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PostResponse_ItemOption {
    private Integer optionId;
    private String optionContent;
    private Integer addPrice;
}
