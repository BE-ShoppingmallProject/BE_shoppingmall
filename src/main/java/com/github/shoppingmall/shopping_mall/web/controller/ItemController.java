package com.github.shoppingmall.shopping_mall.web.controller;


import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @GetMapping("/items")
    public List<Item> allItems(){
        logger.info("/아이템 조회 요청");
        return itemService.itemList();
    }

    @GetMapping("/items/{item_id}")
    public Item itemdetail(int itemId){
        logger.info("/아이템 상세 조회");
        return itemService.itemDetailView(itemId);
    }

}
